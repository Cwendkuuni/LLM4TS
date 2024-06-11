package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNullOuterClassInstance() {
        Class<String> stringClass = String.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(stringClass));
        assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = mock(Object.class);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).newInstance(null));
        assertEquals("Unable to create mock instance of null.\nPlease ensure the provided class is not null.", exception.getMessage());
    }

    @Test
    public void testNewInstanceWithParameterlessConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        String instance = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test
    public void testNewInstanceWithOuterClassInstance() {
        Object outerClassInstance = mock(Object.class);
        Class<Integer> integerClass = Integer.class;
        Integer instance = new ConstructorInstantiator(outerClassInstance).newInstance(integerClass);
        assertInstanceOf(Integer.class, instance);
    }

    @Test
    public void testNewInstanceWithConstructor() {
        Object outerClassInstance = spy(new Object() {});
        Class<MyClass> myClass = MyClass.class;
        Constructor<MyClass> constructor = myClass.getDeclaredConstructor(Object.class);
        MyClass instance = new ConstructorInstantiator(outerClassInstance).newInstance(myClass);
        assertInstanceOf(MyClass.class, instance);
        constructor.setAccessible(true);
        assertInstanceOf(outerClassInstance.getClass(), constructor.newInstance(instance).getClass().getDeclaredFields()[0].get(instance));
    }

    @Test
    public void testNewInstanceWithoutParameterlessConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<MyClassWithConstructor> myClass = MyClassWithConstructor.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).newInstance(myClass));
        assertEquals("Unable to create mock instance of 'MyClassWithConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstanceWithThrowingConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<MyClassWithThrowingConstructor> myClass = MyClassWithThrowingConstructor.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).newInstance(myClass));
        assertEquals("Unable to create mock instance of 'MyClassWithThrowingConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testParamsException() {
        Object outerClassInstance = mock(Object.class);
        Class<MyClassWithTwoConstructors> myClass = MyClassWithTwoConstructors.class;
        InstantiationException exception = new ConstructorInstantiator(outerClassInstance).paramsException(myClass, new RuntimeException("Unexpected error"));
        assertEquals("Unable to create mock instance of 'MyClassWithTwoConstructors'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        String instance = ConstructorInstantiator.noArgConstructor(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test
    public void testNoArgConstructorNullClass() {
        InstantiationException exception = assertThrows(InstantiationException.class, () -> ConstructorInstantiator.noArgConstructor(null));
        assertEquals("Unable to create mock instance of null.\nPlease ensure the provided class is not null.", exception.getMessage());
    }

    private static class MyClass {
        MyClass(Object outerClassInstance) {}
    }

    private static class MyClassWithConstructor {
        MyClassWithConstructor(Object outerClassInstance) {}

        private final Object outerClassInstance;

        Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private static class MyClassWithTwoConstructors {
        MyClassWithTwoConstructors() {}

        MyClassWithTwoConstructors(Object outerClassInstance) {}
    }

    private static class MyClassWithThrowingConstructor {
        MyClassWithThrowingConstructor() throws Exception {}
    }
}