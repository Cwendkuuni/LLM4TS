package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.experimental.annotations.Nullable;
import org.mockito.internal.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ConstructorInstantiatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstance_Success() throws Exception {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class));
        T result = instantIator.newInstance(cls);
        // assert result is not null
        verify(cls).newInstance();
        // assert outerClassInstance is set correctly
        ConstructorInstantiator constructorInstantiator = (ConstructorInstantiator) getInternalState(instantIator, "outerClassInstance");
        assert (constructorInstantiator.outerClassInstance != null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_NullOuterClassInstance() throws Exception {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        instantIator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_NoArgConstructor_Failure() throws Exception {
        Class<T> cls = mock(Class.class);
        when(cls.newInstance()).thenThrow(new InstantiationException("Exception message"));
        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class));
        instantIator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_NoConstructor() throws Exception {
        Class<T> cls = mock(Class.class);
        when(cls.getDeclaredConstructor(MockitoMocker.class)).thenReturn(null);
        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class));
        instantIator.newInstance(cls);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_ConstructorThrowsException() throws Exception {
        Class<T> cls = mock(Class.class);
        Constructor<T> constructor = mock(Constructor.class);
        when(cls.getDeclaredConstructor(MockitoMocker.class)).thenReturn(constructor);
        when(constructor.newInstance(mock(Object.class))).thenThrow(new InstantiationException("Exception message"));
        ConstructorInstantiator instantIator = new ConstructorInstantiator(mock(Object.class));
        instantIator.newInstance(cls);
    }

    @Test
    public void testParamsException() {
        InstantiationException exception = ConstructorInstantiator.paramsException(String.class, new InstantiationException("Test error"));
        String expectedMessage = "Unable to create mock instance of '" + String.class.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testNoArgConstructor() throws Exception {
        Class<?> cls = mock(Class.class);
        Object result = ConstructorInstantiator.noArgConstructor(cls);
        // assert result is not null
        verify(cls).newInstance();
        assertNotNull(result);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_Failure() throws Exception {
        Class<?> cls = mock(Class.class);
        when(cls.newInstance()).thenThrow(new InstantiationException("Exception message"));
        ConstructorInstantiator.noArgConstructor(cls);
    }

    private static class MockitoMocker {
    }

}