import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoConfigurationException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.collections.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private List<Field> fields;
    private Class<?> testClass;

    @Before
    public void setup() {
        testClass = TestClass.class;
        injectionStrategy = new PropertyAndSetterInjection();
        fields = orderedInstanceFieldsFrom(testClass);
    }

    @Test
    public void testProcessInjectionHierarchy() {
        Object instance = mock(testClass);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(DependentClass1.class));
        mockCandidates.add(mock(DependentClass2.class));

        assertTrue(injectionStrategy.processInjection(testClass.getDeclaredField("injectMocksField"), instance, mockCandidates));

        for (Field field : fields) {
            assertFalse(field.isAnnotationPresent(InjectMocks.class));
            Object injected = field.get(instance);
            assertTrue(injected instanceof DependentClass1 || injected instanceof DependentClass2);
        }
    }

    @Test(expected = MockitoConfigurationException.class)
    public void testProcessInjectionNoMockFound() {
        Object instance = mock(testClass);
        Set<Object> mockCandidates = new HashSet<>();

        assertTrue(injectionStrategy.processInjection(testClass.getDeclaredField("injectMocksField"), instance, mockCandidates));

        for (Field field : fields) {
            if (field.getType().isAssignableFrom(NonExistingClass.class)) {
                Object injected = field.get(instance);
                assertFalse(injected instanceof NonExistingClass);
            }
        }

        // This should fail since there is no mock candidate for NonExistingClass
        injectionStrategy.processInjection(testClass.getDeclaredField("injectMocksField"), instance, mockCandidates);
    }

    @Test
    public void testProcessInjectionNoSetter() {
        Object instance = mock(testClass);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mock(DependentClass1.class));

        Field field = testClass.getDeclaredField("fieldWithoutSetter");
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, instance);
        assertTrue(report.wasInitialized());
        assertTrue(field.get(instance) instanceof DependentClass1);
    }

    @Test(expected = MockitoConfigurationException.class)
    public void testProcessInjectionFailedInitialization() {
        Object instance = mock(testClass);
        Set<Object> mockCandidates = new HashSet<>();

        Field field = testClass.getDeclaredField("fieldWithFailedInitialization");
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, instance);
        assertFalse(report.wasInitialized());

        // This should fail since the initialization failed
        injectionStrategy.processInjection(testClass.getDeclaredField("injectMocksField"), instance, mockCandidates);
    }

    private static class TestClass {
        @InjectMocks
        private DependentClass1 dependent1;
        @InjectMocks
        private DependentClass2 dependent2;
        private DependentClass3 fieldWithoutSetter;
        private NonExistingClass fieldWithFailedInitialization;

        // Getters and setters omitted for brevity
    }

    private static class DependentClass1 {}
    private static class DependentClass2 {}
    private static class DependentClass3 {}
    private static class NonExistingClass {}

    private static List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        // ... same as in the original class
    }
}