package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setup() {
        outerClassInstance = new Object() {};
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNewInstance_NullOuterClass_ReturnsInstanceFromNoArgConstructor() {
        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_NotNullOuterClass_CallsConstructorWithOuterClassArgument() {
        Class<TestClass> testClass = TestClass.class;
        TestClass instance = constructorInstantiator.newInstance(testClass);
        assertNotNull(instance);

        TestClass expectedInstance = new TestClass(outerClassInstance);
        assertEquals(expectedInstance, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NullOuterClass_ThrowsExceptionForClassWithoutNoArgConstructor() {
        Class<TestClassWithNoNoArgConstructor> testClassWithNoNoArgConstructor = TestClassWithNoNoArgConstructor.class;
        constructorInstantiator.newInstance(testClassWithNoNoArgConstructor);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NotNullOuterClass_ThrowsExceptionForClassWithoutConstructor() {
        Class<TestClassWithoutConstructor> testClassWithoutConstructor = TestClassWithoutConstructor.class;
        constructorInstantiator.newInstance(testClassWithoutConstructor);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NotNullOuterClass_ThrowsExceptionForConstructorWithIncorrectType() {
        Class<TestClassWithIncorrectConstructorType> testClassWithIncorrectConstructorType = TestClassWithIncorrectConstructorType.class;
        constructorInstantiator.newInstance(testClassWithIncorrectConstructorType);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NotNullOuterClass_ThrowsExceptionForConstructorWithThrowable() {
        Class<TestClassWithThrowableConstructor> testClassWithThrowableConstructor = TestClassWithThrowableConstructor.class;
        constructorInstantiator.newInstance(testClassWithThrowableConstructor);
    }

    private static class TestClass {
        TestClass(Object outer) {
        }
    }

    private static class TestClassWithNoNoArgConstructor {
        // class with no no-arg constructor
    }

    private static class TestClassWithoutConstructor {
        // class without any constructor
    }

    private static class TestClassWithIncorrectConstructorType {
        TestClassWithIncorrectConstructorType(String s) {
        }
    }

    private static class TestClassWithThrowableConstructor {
        TestClassWithThrowableConstructor() throws Exception {
            throw new Exception();
        }
    }
}

class InstantiationException extends RuntimeException {
    InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}