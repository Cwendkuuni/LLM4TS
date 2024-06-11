
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstanceWithNullOuterClass() {
        // Arrange
        Class<?> cls = Object.class;

        // Act
        Object result = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }

    @Test
    public void testNewInstanceWithNonNullOuterClass() {
        // Arrange
        Object outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> cls = Object.class;

        // Act
        Object result = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(result);
        assertEquals(cls, result.getClass());
    }

    @Test
    public void testWithOuterClass() throws Exception {
        // Arrange
        Object outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> cls = Object.class;
        Constructor<Object> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
        Object expected = new Object();

        // Mocking
        Constructor<Object> mockedConstructor = mock(Constructor.class);
        when(mockedConstructor.newInstance(outerClassInstance)).thenReturn(expected);

        // Act
        Object result = constructorInstantiator.withOuterClass(cls);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);

        // Verify
        verify(cls, times(1)).getDeclaredConstructor(outerClassInstance.getClass());
        verify(mockedConstructor, times(1)).newInstance(outerClassInstance);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() throws Exception {
        // Arrange
        Object outerClassInstance = new Object();
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> cls = Object.class;
        Constructor<Object> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());

        // Mocking
        Constructor<Object> mockedConstructor = mock(Constructor.class);
        when(mockedConstructor.newInstance(outerClassInstance)).thenThrow(Exception.class);

        // Act
        constructorInstantiator.withOuterClass(cls);
    }

    @Test
    public void testParamsException() {
        // Arrange
        Class<?> cls = Object.class;
        Exception e = new Exception("test exception");

        // Act
        InstantationException result = constructorInstantiator.paramsException(cls, e);

        // Assert
        assertNotNull(result);
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", result.getMessage());
        assertEquals(e, result.getCause());
    }

    @Test
    public void testNoArgConstructor() throws Exception {
        // Arrange
        Class<?> cls = Object.class;
        Object expected = new Object();

        // Mocking
        when(cls.newInstance()).thenReturn(expected);

        // Act
        Object result = constructorInstantiator.noArgConstructor(cls);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);

        // Verify
        verify(cls, times(1)).newInstance();
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() throws Exception {
        // Arrange
        Class<?> cls = Object.class;

        // Mocking
        when(cls.newInstance()).thenThrow(Exception.class);

        // Act
        constructorInstantiator.noArgConstructor(cls);
    }
}
