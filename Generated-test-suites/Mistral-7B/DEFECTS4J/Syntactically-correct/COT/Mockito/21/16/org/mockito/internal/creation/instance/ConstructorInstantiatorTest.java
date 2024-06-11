package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Assert.assertNotNull(constructorInstantiator);
    }

    @Test
    public void testNewInstance_ParameterlessClass() {
        Class<DummyClass> dummyClass = DummyClass.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        DummyClass instance = constructorInstantiator.newInstance(dummyClass);
        Assert.assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithNullOuterClass_Parameterless() {
        Class<DummyClass> dummyClass = DummyClass.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(dummyClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithNullOuterClass_WithParameters() {
        Class<DummyClassWithParam> dummyClass = DummyClassWithParam.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(dummyClass);
    }

    @Test
    public void testNewInstance_ClassWithOuterClass_Parameterless() {
        Object outerClassInstance = new Object();
        Class<DummyClass> dummyClass = DummyClass.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        DummyClass instance = constructorInstantiator.newInstance(dummyClass);
        Assert.assertNotNull(instance);
        Assert.assertEquals(outerClassInstance, instance.getOuterClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithOuterClass_WithInvalidOuterClassType() {
        Object outerClassInstance = new Object();
        Class<DummyClassWithParam> dummyClass = DummyClassWithParam.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(dummyClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithOuterClass_WrongNumberOfConstructors() {
        Object outerClassInstance = new Object();
        Class<DummyClassWithTwoConstructors> dummyClass = DummyClassWithTwoConstructors.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(dummyClass);
    }

    @Test(expected = InvocationTargetException.class)
    public void testWithOuterClass_ClassWithException() throws Exception {
        Object outerClassInstance = new Object();
        Class<DummyClassWithException> dummyClass = DummyClassWithException.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(dummyClass);
    }

    @Test
    public void testParamsException_ParameterlessClass() {
        Class<DummyClass> dummyClass = DummyClass.class;
        try {
            new ConstructorInstantiator(null).newInstance(dummyClass);
        } catch (Exception e) {
            InstantiationException exception = assertInstanceOf(InstantiationException.class, e);
            String msg = exception.getMessage();
            assertTrue(msg.contains("Please ensure it has parameter-less constructor."));
        }
    }

    @Test
    public void testParamsException_ClassWithParameters() {
        Class<DummyClassWithParam> dummyClass = DummyClassWithParam.class;
        try {
            new ConstructorInstantiator(null).newInstance(dummyClass);
        } catch (Exception e) {
            InstantiationException exception = assertInstanceOf(InstantiationException.class, e);
            String msg = exception.getMessage();
            assertTrue(msg.contains("Please ensure that the outer instance has correct type and that the target class has parameter-less constructor."));
        }
    }

    private static class DummyClass {
        DummyClass() {
        }

        Object getOuterClass() {
            return null;
        }
    }

    private static class DummyClassWithParam {
        DummyClassWithParam(Object outerClass) {
        }

        Object getOuterClass() {
            return outerClass;
        }
    }

    private static class DummyClassWithTwoConstructors {
        DummyClassWithTwoConstructors() {
        }

        DummyClassWithTwoConstructors(int i) {
        }
    }

    private static class DummyClassWithException {
        DummyClassWithException() throws Exception {
            throw new Exception("Test Exception");
        }
    }
}