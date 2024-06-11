package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationHandler;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    private Object outerClassInstance;

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_WhenCalledWithNullOuterClassInstance_ThenReturnNewInstanceWithoutOuterClass() {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        T instance = constructorInstantiator.newInstance(cls);

        verify(cls).newInstance();
        assertNull(instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_WhenCalledWithNotNullOuterClassInstance_ThenReturnNewInstanceWithOuterClass() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        T instance = constructorInstantiator.newInstance(cls);

        Constructor<T> constructor = mock(Constructor.class);
        doAnswer(invocationOnMock -> constructor.newInstance(outerClassInstance)).when(cls).getDeclaredConstructor(outerClassInstance.getClass());

        verify(cls).getDeclaredConstructor(outerClassInstance.getClass());
        verify(constructor).newInstance(outerClassInstance);

        assertNotNull(instance);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_WhenCalledWithClassWithoutNoArgConstructor_ThenThrowException() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = mock(Class.class);

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_WhenCalledWithClassWithExceptionInNoArgConstructor_ThenThrowException() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = mock(Class.class);
        Constructor<T> constructor = mock(Constructor.class);
        doThrow(new InstantiationException("Test exception")).when(constructor).newInstance();
        doAnswer(invocationOnMock -> constructor).when(cls).getDeclaredConstructor();

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testWithOuterClass_WhenCalledWithClassWithConstructor_ThenReturnNewInstanceWithOuterClass() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = mock(Class.class);

        Constructor<T> constructor = mock(Constructor.class);
        doAnswer(invocationOnMock -> constructor.newInstance(outerClassInstance)).when(cls).getDeclaredConstructor(outerClassInstance.getClass());

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = constructorInstantiator.withOuterClass(cls);

        verify(cls).getDeclaredConstructor(outerClassInstance.getClass());
        verify(constructor).newInstance(outerClassInstance);

        assertNotNull(instance);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_WhenCalledWithClassWithExceptionInConstructor_ThenThrowException() {
        outerClassInstance = mock(Object.class);
        Class<T> cls = mock(Class.class);

        Constructor<T> constructor = mock(Constructor.class);
        doAnswer(invocationOnMock -> {
            throw new InstantiationException("Test exception");
        }).when(constructor).newInstance(outerClassInstance);
        doAnswer(invocationOnMock -> constructor).when(cls).getDeclaredConstructor(outerClassInstance.getClass());

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.withOuterClass(cls);
    }
}