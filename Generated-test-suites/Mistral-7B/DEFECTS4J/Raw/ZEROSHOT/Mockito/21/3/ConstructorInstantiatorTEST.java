package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = null;
        try {
            String instance = constructorInstantiator.newInstance(stringClass);
            assertEquals("", instance);
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndNoSuchConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(stringClass));
        assertTrue(exception.getMessage().contains(stringClass.getSimpleName()));
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterLessConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNull(instance);
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndParameterlessConstructor() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNotNull(instance);
        assertEquals("", instance);
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndNoSuchConstructor() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(String[].class));
        assertTrue(exception.getMessage().contains(String[].class.getSimpleName()));
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndConstructorWithSingleArgument() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<Add> addClass = Add.class;
        Constructor<Add> constructor = addClass.getDeclaredConstructor(Integer.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(addClass));
        assertTrue(exception.getMessage().contains(addClass.getSimpleName()));
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndConstructorWithCorrectArguments() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<Add> addClass = Add.class;
        Constructor<Add> constructor = addClass.getDeclaredConstructor(Integer.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Add instance = constructorInstantiator.newInstance(addClass);
        assertEquals(0, instance.getSum(), 0.0);
        Mockito.verify(outerClassInstance).toString();
    }

    @Test
    public void testWithOuterClassWithNullOuterClassInstanceAndNoSuchConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.withOuterClass(stringClass));
        assertTrue(exception.getMessage().contains(stringClass.getSimpleName()));
    }

    @Test
    public void testWithOuterClassWithNullOuterClassInstanceAndParameterlessConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertNull(constructorInstantiator.withOuterClass(stringClass));
    }

    @Test
    public void testWithOuterClassWithOuterClassInstanceAndParameterlessConstructor() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<String> stringClass = String.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = constructorInstantiator.withOuterClass(stringClass);
        assertNotNull(instance);
        assertEquals("", instance);
    }

    @Test
    public void testWithOuterClassWithOuterClassInstanceAndNoSuchConstructor() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<Add> addClass = Add.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.withOuterClass(addClass));
        assertTrue(exception.getMessage().contains(addClass.getSimpleName()));
    }

    @Test
    public void testWithOuterClassWithOuterClassInstanceAndConstructorWithSingleArgument() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<Add> addClass = Add.class;
        Constructor<Add> constructor = addClass.getDeclaredConstructor(Integer.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.withOuterClass(addClass));
        assertTrue(exception.getMessage().contains(addClass.getSimpleName()));
    }

    @Test
    public void testWithOuterClassWithOuterClassInstanceAndConstructorWithCorrectArguments() {
        Object outerClassInstance = Mockito.mock(Object.class);
        Class<Add> addClass = Add.class;
        Constructor<Add> constructor = addClass.getDeclaredConstructor(Integer.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Add instance = constructorInstantiator.withOuterClass(addClass);
        assertEquals(0, instance.getSum(), 0.0);
        Mockito.verify(outerClassInstance).toString();
    }

    @Test
    public void testParamsExceptionWithNullClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<?> nullClass = null;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.paramsException(nullClass, new RuntimeException()));
        assertTrue(exception.getMessage().contains("Please ensure that the outer instance has correct type and that the target class has parameter-less constructor."));
    }

    @Test
    public void testParamsExceptionWithNonRuntimeException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<Add> addClass = Add.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.paramsException(addClass, new Exception()));
        assertTrue(exception.getMessage().contains(addClass.getSimpleName()));
    }

    @Test
    public void testNoArgConstructorWithNullClass() {
        ConstructorInstantiator constructorInstantiator = null;
        Class<String> stringClass = String.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.noArgConstructor(stringClass));
        assertTrue(exception.getMessage().contains(stringClass.getSimpleName()));
    }

    @Test
    public void testNoArgConstructorWithException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<String> stringClass = String.class;
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> constructorInstantiator.noArgConstructor(String[].class));
        UndeclaredThrowableException cause = (UndeclaredThrowableException) exception.getCause();
        InstantiationException innerException = (InstantiationException) cause.getException();
        assertTrue(innerException.getMessage().contains(String[].class.getSimpleName()));
    }
}