package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance() {
        Class<String> stringClass = String.class;
        assertNull(ConstructorInstantiator.getInstance().newInstance(stringClass));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithNoArgConstructorFailure() {
        Class<NoArgConstructorException> noArgConstructorExceptionClass = NoArgConstructorException.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(noArgConstructorExceptionClass);
    }

    @Test
    public void testNewInstance_ClassWithArgConstructor() {
        Class<WithOuterClass> withOuterClass = WithOuterClass.class;
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        WithOuterClass instance = instantiator.newInstance(withOuterClass);
        assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithNoSupportedConstructor() {
        Class<UnsupportedConstructor> unsupportedConstructor = UnsupportedConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(unsupportedConstructor);
    }

    @Test
    public void testWithOuterClass() {
        Class<WithOuterClass> withOuterClass = WithOuterClass.class;
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        WithOuterClass instance = instantiator.withOuterClass(withOuterClass);
        assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassWithNoConstructor() {
        Class<NoConstructor> noConstructor = NoConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.withOuterClass(noConstructor);
    }

    @Test
    public void testParamsException() {
        Class<WithTwoArguments> withTwoArguments = WithTwoArguments.class;
        InstantiationException e = ConstructorInstantiator.paramsException(withTwoArguments, new Exception("Test exception"));
        String expectedMessage = "Unable to create mock instance of 'WithTwoArguments'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
        String actualMessage = e.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor() {
        Class<NoArgConstructorException> noArgConstructorExceptionClass = NoArgConstructorException.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.noArgConstructor(noArgConstructorExceptionClass);
    }

    private static class NoArgConstructorException {
    }

    private static class WithOuterClass {
    }

    private static class UnsupportedConstructor {
        UnsupportedConstructor(int i) {}
    }

    private static class NoConstructor {
        NoConstructor() {}

        private NoConstructor(int i) {}
    }

    private static class WithTwoArguments {
        WithTwoArguments(int i, String s) {}
    }
}