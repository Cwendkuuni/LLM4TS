import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.util.FieldAccessUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Object targetObject;

    @Before
    public void setUp() {
        injectionStrategy = new PropertyAndSetterInjection();
        Class<?> targetClass = Mockito.mock(SomeTargetClass.class);
        targetObject = new SomeTargetClass();
    }

    @Test
    public void testSuccessfulMockInjection() {
        Field injectMocksField = targetObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.annotatedWith(Mockito.MockAnnotation.class);

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(Dependency1.class));
        mockCandidates.add(Mockito.mock(Dependency2.class));

        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates);
        assertTrue("Injection should have occurred", injectionOccurred);

        Dependency1 dependency1 = (Dependency1) FieldAccessUtils.getFieldValue(targetObject, "injectMocksField");
        assertFalse("Dependency should not be null", dependency1 == null);

        Dependency2 dependency2 = (Dependency2) FieldAccessUtils.getFieldValue(targetObject, "fieldToBeInjected");
        assertFalse("Dependency should not be null", dependency2 == null);
    }

    @Test(expected = IllegalStateException.class)
    public void testMixingFinalAndNonFinalFieldsDuringMockInjection() {
        Class<?> targetClass = Mockito.mock(SomeTargetClassWithFinalAndNonFinalFields.class);
        Object targetObject = new SomeTargetClassWithFinalAndNonFinalFields();

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(Dependency1.class));
        mockCandidates.add(Mockito.mock(Dependency2.class));

        Field injectMocksField = targetClass.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.annotatedWith(Mockito.MockAnnotation.class);

        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates);
        assertTrue("Injection should have occurred", injectionOccurred);

        Field finalField = targetClass.getDeclaredField("finalField");
        finalField.setAccessible(true);

        // This should fail as final fields are not considered for injection
        injectionStrategy.processInjection(finalField, targetObject, mockCandidates);
    }

    @Test
    public void testNoMockCandidatesFound() {
        Class<?> targetClass = Mockito.mock(SomeTargetClassNoCandidates.class);
        Object targetObject = new SomeTargetClassNoCandidates();

        Set<Object> mockCandidates = new HashSet<>();

        Field injectMocksField = targetClass.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.annotatedWith(Mockito.MockAnnotation.class);

        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates);
        assertFalse("Injection should not have occurred", injectionOccurred);
    }

    @Test
    public void testNoMockCandidatesFoundWithCustomFiltering() {
        Class<?> targetClass = Mockito.mock(SomeTargetClassCustomFilter.class);
        Object targetObject = new SomeTargetClassCustomFilter();

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(Dependency1.class));

        injectionStrategy.setMockCandidateFilter(new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
                return null;
            }

            @Override
            public Object thenInject() {
                return null;
            }
        });

        Field injectMocksField = targetClass.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.annotatedWith(Mockito.MockAnnotation.class);

        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates);
        assertFalse("Injection should not have occurred", injectionOccurred);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInjectMocksField() {
        Set<Object> mockCandidates = new HashSet<>();

        injectionStrategy.setMockCandidateFilter(new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
                return null;
            }

            @Override
            public Object thenInject() {
                return null;
            }
        });

        injectionStrategy.processInjection(null, null, mockCandidates);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInjectMocksFieldOwner() {
        Set<Object> mockCandidates = new HashSet<>();

        injectionStrategy.setMockCandidateFilter(new MockCandidateFilter() {
            @Override
            public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
                return null;
            }

            @Override
            public Object thenInject() {
                return null;
            }
        });

        injectionStrategy.processInjection(new Field(), null, mockCandidates);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMockCandidates() {
        injectionStrategy.processInjection(new Field(null, "injectMocksField"), new Object(), null);
    }
}

class SomeTargetClass {
    @InjectMocks
    private Dependency1 injectMocksField;
    private Dependency2 fieldToBeInjected;
}

class SomeTargetClassWithFinalAndNonFinalFields {
    @InjectMocks
    private Dependency1 injectMocksField;
    public final Dependency2 finalField;
    private Dependency3 fieldToBeInjected;
}

class SomeTargetClassNoCandidates {
    @InjectMocks
    private Dependency1 injectMocksField;
}

class SomeTargetClassCustomFilter {
    @InjectMocks
    private Dependency1 injectMocksField;
}

interface Dependency1 {}
interface Dependency2 {}