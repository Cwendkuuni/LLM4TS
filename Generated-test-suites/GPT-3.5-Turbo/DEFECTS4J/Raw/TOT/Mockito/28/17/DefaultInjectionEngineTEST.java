import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {
    private DefaultInjectionEngine defaultInjectionEngine;

    @Before
    public void setUp() {
        defaultInjectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields_validFields_mockInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);

        Object testClassInstance = new Object();

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).set(testClassInstance, mockObject);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_mockitoException_reported() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);

        Object testClassInstance = new Object();

        Reporter reporter = mock(Reporter.class);
        doThrow(new MockitoException("Cannot initialize for InjectMocks Annotation"))
                .when(reporter).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(reporter, times(1)).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
    }

    @Test
    public void testInjectMocksOnFields_superTypeField_mockInjected() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);

        Object testClassInstance = new SubClass();

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).set(testClassInstance, mockObject);
    }

    @Test
    public void testInjectMocksOnFields_noMockCandidate_noInjection() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = mock(Field.class);
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();

        Object testClassInstance = new SubClass();

        // Act
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(field, never()).setAccessible(true);
        verify(field, never()).set(any(), any());
    }

    private static class SubClass extends DefaultInjectionEngine {
        private Object field;
    }
}