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

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields_successfulInjection() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        DefaultInjectionEngine engine = new DefaultInjectionEngine();

        Field field1 = getFieldFromClass("field1", testClassInstance.getClass());
        Field field2 = getFieldFromClass("field2", testClassInstance.getClass());
        Field field3 = getFieldFromClass("field3", testClassInstance.getClass());

        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);

        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock objects are injected successfully into the fields
        verify(field1, times(1)).set(testClassInstance, any(Object.class));
        verify(field2, times(1)).set(testClassInstance, any(Object.class));
        verify(field3, times(1)).set(testClassInstance, any(Object.class));
    }

    @Test
    public void testInjectMocksOnFields_nullTestClassInstance() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = null;

        DefaultInjectionEngine engine = new DefaultInjectionEngine();

        Field field1 = getFieldFromClass("field1", testClassInstance.getClass());
        
        injectMocksFields.add(field1);

        mocks.add(mock(Object.class));

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the injector handles the null testClassInstance gracefully without throwing NullPointerException
        verify(field1, never()).set(testClassInstance, any(Object.class));
    }

    @Test
    public void testInjectMocksOnFields_initializationFailure() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        DefaultInjectionEngine engine = new DefaultInjectionEngine();

        Field field1 = getFieldFromClass("field1", testClassInstance.getClass());

        injectMocksFields.add(field1);

        mocks.add(mock(Object.class));

        doThrow(new MockitoException("Mock initialization failed")).when(field1).initialize();

        // Act
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the Reporter is called with the correct error message
        verifyStatic(Reporter.class, times(1));
        Reporter.cannotInitializeForInjectMocksAnnotation(eq("field1"), any(MockitoException.class));
    }

    private Field getFieldFromClass(String fieldName, Class<?> clazz) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}