package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance() {
        Class<String> stringClass = String.class;
        assertNull(new ConstructorInstantiator(null).newInstance(stringClass));
    }

    @Test
    public void testNewInstance_NullClass() {
        Object outerClassInstance = mock(Object.class);
        assertNull(new ConstructorInstantiator(outerClassInstance).newInstance(null));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithoutNoArgConstructor() {
        Class<MyClassWithParams> myClassWithParams = MyClassWithParams.class;
        new ConstructorInstantiator(null).newInstance(myClassWithParams);
    }

    @Test
    public void testNewInstance_ClassWithNoArgConstructor() {
        Class<String> stringClass = String.class;
        String instance = new ConstructorInstantiator(null).newInstance(stringClass);
        assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassWithoutConstructor() {
        Class<MyClassWithNoConstructor> myClassWithNoConstructor = MyClassWithNoConstructor.class;
        new ConstructorInstantiator(null).withOuterClass(myClassWithNoConstructor);
    }

    @Test
    public void testWithOuterClass_ClassWithConstructor() {
        Class<MyClassWithConstructor> myClassWithConstructor = MyClassWithConstructor.class;
        Object outerClassInstance = mock(Object.class);
        MyClassWithConstructor instance = new ConstructorInstantiator(outerClassInstance).withOuterClass(myClassWithConstructor);
        assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassWithException() {
        Class<MyClassWithException> myClassWithException = MyClassWithException.class;
        Object outerClassInstance = mock(Object.class);
        new ConstructorInstantiator(outerClassInstance).withOuterClass(myClassWithException);
    }

    @Test
    public void testParamsException() {
        Class<MyClassWithParams> myClassWithParams = MyClassWithParams.class;
        Exception e = new Exception("Test Exception");
        InstantiationException expectedException = new ConstructorInstantiator().paramsException(myClassWithParams, e);
        assertEquals("Unable to create mock instance of 'MyClassWithParams'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", expectedException.getMessage());
        assertEquals("Test Exception", expectedException.getCause().getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        String instance = ConstructorInstantiator.noArgConstructor(stringClass);
        assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_ClassWithException() {
        Class<MyClassWithException> myClassWithException = MyClassWithException.class;
        ConstructorInstantiator.noArgConstructor(myClassWithException);
    }
}

class MyClassWithParams {
    MyClassWithParams(int i) {}
}

class MyClassWithNoConstructor {}

class MyClassWithConstructor {}

class MyClassWithException extends Exception {
    MyClassWithException() {}
}