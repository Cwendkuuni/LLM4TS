package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.invokeConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNullOuterClass() {
        assertNull(new ConstructorInstantiator(null).newInstance(String.class));
    }

    @Test
    public void testNewInstanceWithNullClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        assertNull(instantiator.newInstance(null));
    }

    @Test
    public void testNewInstanceWithParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        String result = (String) instantiator.newInstance(String.class);
        assertInstanceOf(String.class, result);
    }

    @Test
    public void testNewInstanceWithParametrizedClassNoArgs() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Class<Foo> clazz = Foo.class;
        Foo result = instantiator.newInstance(clazz);
        assertInstanceOf(Foo.class, result);
        assertNull(getInternalState(result, "outerInstance"));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithParametrizedClassWrongTypeOuterClass() {
        Object outerInstance = new String();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Class<Foo> clazz = Foo.class;
        instantiator.newInstance(clazz);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithParametrizedClassNoVisibleConstructor() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Class<HiddenFoo> clazz = HiddenFoo.class;
        instantiator.newInstance(clazz);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithParametrizedClassThrowingException() {
        Object outerInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Class<ThrowingFoo> clazz = ThrowingFoo.class;
        instantiator.newInstance(clazz);
    }

    @Test
    public void testWithOuterClass() {
        Object outerInstance = new Object();
        Constructor<Foo> fooConstructor = Foo.class.getDeclaredConstructor(outerInstance.getClass());
        fooConstructor.setAccessible(true);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Foo result = instantiator.withOuterClass(Foo.class);
        assertInstanceOf(Foo.class, result);
        assertNotNull(getInternalState(result, "outerInstance"));
        assertEquals(outerInstance, getInternalState(result, "outerInstance"));
    }

    @Test(expected = InstantiationException.class)
    public void testParamsException() {
        Class<Bar> clazz = Bar.class;
        throw new AssertionError("Expected InstantiationException", () -> new ConstructorInstantiator(new Object()).paramsException(clazz, new RuntimeException("Error")));
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor() {
        throw new AssertionError("Expected InstantiationException", () -> new ConstructorInstantiator(new Object()).noArgConstructor(String.class.getDeclaredConstructor()));
    }

    private static class Foo {
        private Object outerInstance;

        private Foo(Object outerInstance) {
            this.outerInstance = outerInstance;
        }
    }

    private static class HiddenFoo {
        private HiddenFoo() {
        }

        private HiddenFoo(Object outerInstance) {
            this();
            this.outerInstance = outerInstance;
        }

        private Object outerInstance;
    }

    private static class ThrowingFoo {
        private ThrowingFoo() throws Exception {
            throw new Exception("Exception in constructor");
        }

        private String name;

        public ThrowingFoo(String name) {
            this();
            this.name = name;
        }
    }
}