package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(org.mockito.internal.util.testing.SilentTest.class) // SilentTest to suppress warnings
public class ConstructorInstantiatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ClassWithNoArgConstructor_ReturnsInstance() {
        Class<T> cls = (Class<T>) mock(Class.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        T instance = instantiator.newInstance(cls);
        assertInstanceOf(cls, instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ClassWithArgConstructor_ThrowsException() {
        Class<Object> cls = Object.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(cls);
            assertFalse(true);
        } catch (InstantiationException e) {
            //expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ClassWithOuterClassInstance_ReturnsInstance() {
        Object outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = instantiator.newInstance(cls);
        assertInstanceOf(cls, instance);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_ClassWithOuterClassInstanceAndNoSupportedConstructor_ThrowsException() {
        Object outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(cls); // this will throw an exception as there is no supported constructor
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ClassWithSupportedConstructor_ReturnsInstance() {
        Object outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);
        Constructor<T> constructor = mock(Constructor.class);
        when(cls.getDeclaredConstructor(outerClassInstance.getClass())).thenReturn(constructor);
        when(constructor.newInstance(outerClassInstance)).thenReturn(mock(T.class));

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = instantiator.newInstance(cls);
        assertInstanceOf(cls, instance);
    }

    @Test
    public void testParamsException_ClassWithNoArgConstructor_ReturnsMessageAndCause() {
        Class<Object> cls = Object.class;
        InstantiationException e = ConstructorInstantiator.paramsException(cls, new RuntimeException("test"));
        String expectedMessage = "Unable to create mock instance of '" + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
        String actualMessage = e.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals("test", e.getCause());
    }

    @Test
    public void testParamsException_ClassWithArgConstructor_ReturnsMessageAndCause() {
        Class<Object> cls = Object.class;
        InstantiationException e = ConstructorInstantiator.paramsException(cls, new InstantiationException("test"));
        String expectedMessage = "Unable to create mock instance of '" + cls.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.";
        String actualMessage = e.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertSame(e, e.getCause());
    }
}