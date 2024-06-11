package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InstantiationStrategy;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        MyClass myClass = instantiator.newInstance(MyClass.class);
        assertEquals(MyClass.class, myClass.getClass());
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndClassWithConstructor() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        ClassWithConstructor classWithConstructor = Mockito.spy(ClassWithConstructor.class);
        try {
            Object outerClassInstance = instantiator.newInstance(classWithConstructor.getClass());
            assertNull(outerClassInstance);
            assertInstanceOf(InstantiationException.class, instantiator.newInstance(ClassWithConstructor.class));
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'ClassWithConstructor'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = mock(SomeClass.class, InstantiationStrategy.MOCKITO_LENIENT);
        Instantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        MyClass myClass = instantiator.newInstance(MyClass.class);
        assertEquals(MyClass.class, myClass.getClass());
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndClassWithConstructor() {
        Object outerClassInstance = mock(SomeClass.class, InstantiationStrategy.MOCKITO_LENIENT);
        Instantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        ClassWithConstructor classWithConstructor = Mockito.spy(ClassWithConstructor.class);
        MyClass myClass = instantiator.newInstance(ClassWithConstructor.class);
        assertEquals(outerClassInstance, myClass.outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndClassWithException() {
        Object outerClassInstance = mock(SomeClass.class, InstantiationStrategy.MOCKITO_LENIENT);
        Instantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        ClassWithException classWithException = Mockito.spy(ClassWithException.class);
        instantiator.newInstance(ClassWithException.class);
    }

    @Test
    public void testParamsException() {
        try {
            ConstructorInstantiator.paramsException(MyClassWithTwoArgs.class, new InstantiationException("Test Exception"));
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'MyClassWithTwoArgs'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorException() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(MyClassWithNoArgConstructor.class);
    }

    static class SomeClass {
    }

    static class MyClass implements Instantiatable {
    }

    static class MyClassWithTwoArgs {
        public MyClassWithTwoArgs(Object arg1, Object arg2) {
        }
    }

    static class ClassWithConstructor {
        private final Object outerClassInstance;

        public ClassWithConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
    }

    static class MyClassWithNoArgConstructor {
        public MyClassWithNoArgConstructor() {
            throw new RuntimeException("Test Exception");
        }
    }

    static class ClassWithException {
        private final Object outerClassInstance;

        public ClassWithException(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
            throw new RuntimeException("Test Exception");
        }
    }
}