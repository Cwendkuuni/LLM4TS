package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationHandler;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    private Object outerClassInstance;

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ParameterlessConstructor() {
        Class<T> cls = (Class<T>) mock(Class.class);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        T result = instantiator.newInstance(cls);

        assertInstanceOf(cls, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ConstructorWithNoArguments_Success() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        T result = instantiator.newInstance(cls);

        assertInstanceOf(cls, result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_ConstructorWithNoArguments_Failure() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);

        doThrow(new InstantiationException("Test Error")).when(cls).newInstance();

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_ConstructorWithOneArgument_Success() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);

        Constructor<T> constructor = mock(Constructor.class);
        when(constructor.newInstance(outerClassInstance)).thenReturn(mock(T.class));
        when(cls.getDeclaredConstructor(outerClassInstance.getClass())).thenReturn(constructor);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        T result = instantiator.newInstance(cls);

        assertInstanceOf(cls, result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_ConstructorWithOneArgument_Failure() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = (Class<T>) mock(Class.class);

        Constructor<T> constructor = mock(Constructor.class);
        when(constructor.newInstance(outerClassInstance)).thenThrow(new InstantiationException("Test Error"));
        when(cls.getDeclaredConstructor(outerClassInstance.getClass())).thenReturn(constructor);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testParamsException() {
        Class<T> cls = (Class<T>) mock(Class.class);
        Exception e = mock(Exception.class);

        InstantiationException expectedException = ConstructorInstantiator.paramsException(cls, e);

        String expectedMessage = "Unable to create mock instance of '"
                + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";

        assertEquals(expectedMessage, expectedException.getMessage());
        assertEquals(e, expectedException.getCause());
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNoArgConstructor() {
        Class<T> cls = (Class<T>) mock(Class.class);

        T result = ConstructorInstantiator.noArgConstructor(cls);

        assertInstanceOf(cls, result);
    }

    @Test
    public void testNullOuterClassInstance() {
        Class<Object> cls = Object.class;

        InstantiationException expectedException = assertThrows(InstantiationException.class, () -> ConstructorInstantiator.noArgConstructor(cls));

        String expectedMessage = "Unable to create mock instance of '"
                + cls.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.";

        assertEquals(expectedMessage, expectedException.getMessage());
    }
}