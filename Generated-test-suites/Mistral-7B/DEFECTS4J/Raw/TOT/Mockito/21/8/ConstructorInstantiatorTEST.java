package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.exceptions.base.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParamelessClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Integer intInstance = constructorInstantiator.newInstance(Integer.class);
        Assert.assertNotNull(intInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullOuterClassInstanceAndNonParamelessClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(String.class);
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndParamelessClass() {
        String outerClassInstance = "test outer class";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Integer intInstance = constructorInstantiator.newInstance(Integer.class);
        Assert.assertNotNull(intInstance);
        Assert.assertEquals(intInstance.getClass().getSimpleName(), "Integer");
        Assert.assertNotSame(intInstance, outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndNonParamelessClassNoConstructor() {
        String outerClassInstance = "test outer class";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(String[].class);
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndSingleParameterClass() {
        String outerClassInstance = "test outer class";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String stringInstance = constructorInstantiator.newInstance(String.class, "test string");
        Assert.assertNotNull(stringInstance);
        Assert.assertEquals(stringInstance.getClass().getSimpleName(), "String");
        Assert.assertNotSame(stringInstance, outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithValidOuterClassInstanceAndNonSupportedParameterType() {
        String outerClassInstance = "test outer class";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(CustomClass.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterClassInstanceAndThrowsException() {
        String outerClassInstance = "test outer class";
        ConstructorInstantiator constructorInstantiator = spy(ConstructorInstantiator.class);
        doThrow(new RuntimeException("Test Exception")).when(constructorInstantiator)
                .newInstance(String.class);
        try {
            ConstructorInstantiator constructor = new ConstructorInstantiator(outerClassInstance);
            constructor.newInstance(String.class);
            fail();
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndTwoParametersClass() {
        String outerClassInstance = "test outer class";
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            String string1 = "test string 1";
            String string2 = "test string 2";
            Object[] constructorArgs = {outerClassInstance, string1};
            Class<?> cls = Class.forName("java.util.HashMap");
            Constructor<Map> constructor = cls.getDeclaredConstructor(Object[].class);
            Map<String, String> mapInstance = constructor.newInstance(constructorArgs);
            Assert.assertNotNull(mapInstance);
            Assert.assertEquals(mapInstance.getClass().getSimpleName(), "HashMap");
            Assert.assertNotSame(mapInstance, outerClassInstance);
        } catch (Exception e) {
            throw new InstantiationException("Unable to create mock instance of 'HashMap'.\nPlease ensure that the outer instance has correct type and that the target class has a constructor that accepts an array of Objects as parameter.", e);
        }
    }

    private static class CustomClass {
        // Custom class for testing non-supported parameter type
    }
}