import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidate;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TypeInformation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Class<?> targetClass;
    private Object targetInstance;
    private Set<MockCandidate> mockCandidates;

    @Before
    public void setup() {
        targetClass = new ClassWithMocksToInject();
        targetInstance = Mockito.mock(targetClass);
        mockCandidates = createMockCandidates();
        initMocks(this);
        injectionStrategy = new PropertyAndSetterInjection();
    }

    @Test
    public void testInjectMocks_usingSetters() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        FieldInitializationReport report = new FieldInitializer(targetInstance, injectMocksField).initialize();

        // When
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);

        // Then
        verify(targetInstance).setMockedField(Mockito.eq(mockCandidates.iterator().next()));
        verifyNoMoreInteractions(targetInstance);
    }

    @Test
    public void testInjectMocks_usingFields() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        Modifier originalModifiers = injectMocksField.getModifiers();
        injectMocksField.setAccessible(true);

        // When
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);

        // Then
        MockCandidate mockCandidate = mockCandidates.iterator().next();
        assertTrue(injectMocksField.get(targetInstance) instanceof Mockito$$Mock$$Mockito$$1);
        assertTrue(injectMocksField.get(targetInstance) == mockCandidate.getMock());
        verifyNoMoreInteractions(targetInstance);

        // Clean up
        injectMocksField.setAccessible(false);
        injectMocksField.setModifiers(originalModifiers);
    }

    @Test
    public void testInjectMocks_noCandidatesFound() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        Object targetInstance = Mockito.mock(targetClass);

        // When
        injectionStrategy.processInjection(injectMocksField, targetInstance, Collections.emptySet());

        // Then
        verifyNoMoreInteractions(targetInstance);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocks_mockInitializationThrowsException() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        Object targetInstance = Mockito.mock(targetClass);
        Set<MockCandidate> mockCandidates = createMockCandidates();
        mockCandidates.remove(mockCandidates.iterator().next());

        // When
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);

        // Then - should throw an exception
    }

    @Test(expected = InvocationTargetException.class)
    public void testInjectMocks_fieldInitializationThrowsException() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        Object targetInstance = Mockito.mock(targetClass);
        Set<MockCandidate> mockCandidates = createMockCandidates();

        // When
        try {
            new FieldInitializer(targetInstance, injectMocksField).initialize();
        } catch (MockitoException e) {
            throw e.getCause();
        }

        // Then - should throw an exception
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);
    }

    @Test
    public void testInjectMocks_multipleCandidatesFound_UsingType() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        Object targetInstance = Mockito.mock(targetClass);
        Set<MockCandidate> mockCandidates = createMockCandidates();

        // When
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);

        // Then
        verify(targetInstance).setMockedField(mockCandidates.iterator().next());
        verifyNoMoreInteractions(targetInstance);

        // Clean up
        clearMocks(targetInstance);

        // Given
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);

        // Then
        MockCandidate mockCandidate = mockCandidates.iterator().next();
        assertTrue(injectMocksField.get(targetInstance) instanceof Mockito$$Mock$$Mockito$$1);
        assertTrue(injectMocksField.get(targetInstance) == mockCandidate.getMock());
        verifyNoMoreInteractions(targetInstance);
    }

    @Test
    public void testInjectMocks_multipleCandidatesFound_UsingName() {
        // Given
        Field injectMocksField = getDeclaredField(targetClass, "injectMocksField");
        Object targetInstance = Mockito.mock(targetClass);
        Set<MockCandidate> mockCandidates = createMockCandidates();
        mockCandidates.remove(mockCandidates.iterator().next());

        // When
        injectionStrategy.processInjection(injectMocksField, targetInstance, mockCandidates);

        // Then - should not inject since more than one candidate found
        verifyNoMoreInteractions(targetInstance);

        // Clean up
        clearMocks(targetInstance);

        // Given
        Field injectMocksField2 = getDeclaredField(targetClass, "injectMocksField2");
        injectionStrategy.processInjection(injectMocksField2, targetInstance, mockCandidates);

        // Then
        MockCandidate mockCandidate = mockCandidates.iterator().next();
        assertTrue(injectMocksField2.get(targetInstance) instanceof Mockito$$Mock$$Mockito$$1);
        assertTrue(injectMocksField2.get(targetInstance) == mockCandidate.getMock());
        verifyNoMoreInteractions(targetInstance);
    }

    private Set<MockCandidate> createMockCandidates() {
        TypeInformation targetClassTypeInformation = new TypeInformation(targetClass);
        Set<MockCandidate> mockCandidates = newMockSafeHashSet();
        mockCandidates.add(new MockCandidate(targetClassTypeInformation, new TypeInformation(Mockito.class), "mock1"));
        mockCandidates.add(new MockCandidate(targetClassTypeInformation, new TypeInformation(Mockito.class), "mock2"));
        return mockCandidates;
    }

    private Field getDeclaredField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearMocks(Object object) {
        Mockito.reset(object);
    }
}

class ClassWithMocksToInject {
    @InjectMocks
    private FieldToMock fieldToMock;
}

class FieldToMock {
    private MockedField mockedField;

    public void setMockedField(MockedField mockedField) {
        this.mockedField = mockedField;
    }

    public MockedField getMockedField() {
        return mockedField;
    }
}

class MockedField {
}