package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance() {
        Class<Object> objectClass = Object.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(objectClass);
        assertNull(result);
    }

    @Test
    public void testNewInstance_ParameterlessClass() {
        Class<Object> objectClass = Object.class;
        Object outerClassInstance = mock(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.newInstance(objectClass);
        assertInstanceOf(Object.class, result);
    }

    @Test
    public void testNewInstance_ClassWithNoArgConstructor() {
        Class<MyClass> myClass = MyClass.class;
        Object outerClassInstance = mock(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.newInstance(myClass);
        assertInstanceOf(MyClass.class, result);
    }

    @Test
    public void testNewInstance_ClassWithArgConstructor() {
        Class<MyClassWithArgConstructor> myClass = MyClassWithArgConstructor.class;
        Object outerClassInstance = mock(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            Object result = instantiator.newInstance(myClass);
            assertInstanceOf(MyClassWithArgConstructor.class, result);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testWithOuterClass() {
        Class<MyClassWithOuter> myClass = MyClassWithOuter.class;
        Object outerClassInstance = mock(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.withOuterClass(myClass);
        assertInstanceOf(MyClassWithOuter.class, result);
    }

    @Test
    public void testNoArgConstructor() {
        Class<Object> objectClass = Object.class;
        Object result = ConstructorInstantiator.noArgConstructor(objectClass);
        assertInstanceOf(Object.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testParamsException() {
        Class<MyClassWithArgConstructor> myClass = MyClassWithArgConstructor.class;
        InstantiationException exception = ConstructorInstantiator.paramsException(myClass, new RuntimeException());
        assertInstanceOf(InstantiationException.class, exception);
        assertInstanceOf(MyClassWithArgConstructor.class, exception.getTargetType());
        assertEquals("Unable to create mock instance of 'MyClassWithArgConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }
}

class MyClass {}

class MyClassWithArgConstructor {
    MyClassWithArgConstructor(int arg) {}
}

class MyClassWithOuter {
    MyClassWithOuter(String outer) {}
}