package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.invokeConstructor;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance() {
        Class<Object> cls = Object.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(cls));
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_ParameterlessConstructor() {
        Object outerClassInstance = new Object();
        Class<Foo> cls = Foo.class;

        Foo instance = new ConstructorInstantiator(outerClassInstance).newInstance(cls);
        assertInstanceOf(Foo.class, instance);
    }

    @Test
    public void testNewInstance_ConstructorWithOneParameter() {
        Object outerClassInstance = new Object();
        Class<Bar> cls = Bar.class;

        Bar instance = new ConstructorInstantiator(outerClassInstance).newInstance(cls);
        assertInstanceOf(Bar.class, instance);
        assertSame(outerClassInstance, getInternalState(instance, "outerClassInstance"));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithNoPublicConstructor() {
        Object outerClassInstance = new Object();
        Class<Baz> cls = Baz.class;

        new ConstructorInstantiator(outerClassInstance).newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithExceptionInConstructor() {
        Object outerClassInstance = new Object();
        Class<Quux> cls = Quux.class;

        new ConstructorInstantiator(outerClassInstance).newInstance(cls);
    }

    private static class Foo {}
    private static class Bar {
        public Bar(Object outerClassInstance) {}
    }
    private static class Baz {
        private Baz(Object outerClassInstance) {}

        private static Constructor<Baz> constructor;
        static {
            try {
                constructor = Baz.class.getDeclaredConstructor(Object.class);
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static class Quux {
        private Quux() throws InstantiationException {
            throw new InstantiationException("Unable to create mock instance of 'Quux'.\nPlease ensure it has parameter-less constructor.");
        }
    }

    private static void setOuterClassInstance(Object instance, Object outerClassInstance) {
        setInternalState(instance, "outerClassInstance", outerClassInstance);
    }

    private static <T> T invokeConstructor(Class<T> cls, Object... args) {
        try {
            return (T) invokeConstructor(cls, args);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static InstantiationException assertThrows(Class<? extends InstantiationException> exceptionType, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception: " + exceptionType);
        } catch (InstantiationException e) {
            if (exceptionType.isInstance(e)) {
                return e;
            }
            throw new RuntimeException("Unexpected exception: ", e);
        }
    }
}