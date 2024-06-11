package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import static org.junit.Assert.*;
import static org.mockito.internal.util.reflection.Whitebox.*;;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        Class mockClass = MockitoTestUtils.mockClass();
        outerClassInstance = new Object() {};
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNewInstanceWithNullOuterClass() {
        Class<Object> cls = Object.class;
        try {
            constructorInstantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals(noArgConstructor(cls.getClass()).getMessage(), e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithValidOuterClassAndParams() {
        Class<MyClass> cls = MyClass.class;
        MyClass expected = new MyClass();
        try {
            MyClass actual = constructorInstantiator.newInstance(cls);
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithInvalidOuterClassAndParams() {
        Class<MyOtherClass> cls = MyOtherClass.class;
        try {
            constructorInstantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals(paramsException(cls.getClass(), new Exception("Test Exception")).getMessage(), e.getMessage());
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullClass() {
        try {
            constructorInstantiator.newInstance(null);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unexpected exception: null", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor() {
        Class<Object> cls = Object.class;
        Object actual = constructorInstantiator.noArgConstructor(cls);
        assertNotNull(actual);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorWithException() {
        Class<MyClass> cls = MyClass.class;
        try {
            constructorInstantiator.noArgConstructor(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals(noArgConstructor(cls.getClass()).getMessage(), e.getMessage());
        }
    }

    @Test
    public void testParamsException() {
        Class<MyClass> cls = MyClass.class;
        Class<String> stringClass = String.class;
        Exception exception = new Exception("Test Exception");
        InstantiationException expected = constructorInstantiator.paramsException(cls, exception);
        InstantiationException actual = constructorInstantiator.paramsException(stringClass, exception);
        assertEquals(expected.getMessage(), actual.getMessage());
    }
}

class MyClass {}
class MyOtherClass {}