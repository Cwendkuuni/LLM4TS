import org.junit.Test;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields_SuccessfulInjection() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        field1.setAccessible(true);
        injectMocksFields.add(field1);

        field2.setAccessible(true);
        injectMocksFields.add(field2);

        mocks.add(mock1);
        mocks.add(mock2);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        assertNotNull(testClassInstance.getField1());
        assertNotNull(testClassInstance.getField2());

        assertSame(mock1, testClassInstance.getField1());
        assertSame(mock2, testClassInstance.getField2());

        verify(mock1, times(1)).setPropertySetter(any());
        verify(mock2, times(1)).setPropertySetter(any());
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldInitializationFailure() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        field1.setAccessible(true);
        injectMocksFields.add(field1);

        field2.setAccessible(true);
        injectMocksFields.add(field2);

        mocks.add(mock1);
        mocks.add(mock2);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        doThrow(MockitoException.class).when(fieldInitializer).initialize();

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        verify(reporter, times(1)).cannotInitializeForInjectMocksAnnotation(field1.getName(), MockitoException.class);
        verify(reporter, never()).cannotInitializeForInjectMocksAnnotation(field2.getName(), MockitoException.class);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksFieldInjectionFailure() {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        field1.setAccessible(true);
        injectMocksFields.add(field1);

        field2.setAccessible(true);
        injectMocksFields.add(field2);

        mocks.add(mock1);
        mocks.add(mock2);

        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        doThrow(MockitoException.class).when(mock1).setPropertySetter(any());

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        assertNotNull(testClassInstance.getField1());
        assertNotNull(testClassInstance.getField2());

        assertSame(mock1, testClassInstance.getField1());
        assertSame(mock2, testClassInstance.getField2());

        verify(mock1, times(1)).setPropertySetter(any());
        verify(mock2, never()).setPropertySetter(any());
    }
}