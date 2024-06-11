package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_ParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(String.class);

        assertNull(result);
    }

    @Test
    public <T> void testNewInstance_NullOuterClassInstance_ConstructorWithParameters() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<T> cls = MyClassWithConstructor.class;

        try {
            T result = instantiator.newInstance(cls);
            assertNull(result);
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_ParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.newInstance(String.class);

        assertNotNull(result);
    }

    @Test
    public <T> void testNewInstance_NotNullOuterClassInstance_ConstructorWithParameters() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<T> cls = MyClassWithConstructor.class;

        try {
            T result = instantiator.newInstance(cls);
            assertNotNull(result);
            assertEquals(outerClassInstance, result);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_NoParameterlessConstructor_ExceptionThrown() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<T> cls = MyClassWithoutNoArgConstructor.class;

        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_OuterClassInstanceNull_ConstructorWithParameters_ExceptionThrown() {
        Object outerClassInstance = null;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<T> cls = MyClassWithConstructor.class;

        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNewInstance_ConstructorThrowsException_ExceptionThrown() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<T> cls = MyClassWithThrowingConstructor.class;

        try {
            instantiator.newInstance(cls);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MyClassWithConstructor {
        private final Object outerClassInstance;

        public MyClassWithConstructor(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
    }

    private static class MyClassWithConstructor<T> {
        private final Object outerClassInstance;

        public MyClassWithConstructor(Object outerClassInstance, T arg) {
            this.outerClassInstance = outerClassInstance;
        }
    }

    private static class MyClassWithoutNoArgConstructor {
        private MyClassWithoutNoArgConstructor() {
        }
    }

    private static class MyClassWithThrowingConstructor {
        public MyClassWithThrowingConstructor() throws Exception {
            throw new InstantiationException("Exception occurred");
        }
    }
}