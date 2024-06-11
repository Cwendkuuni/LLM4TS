package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class<String> stringClass = String.class;
        assertNull(ConstructorInstantiator.getInstance().newInstance(stringClass));
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = mock(Object.class);
        assertNull(new ConstructorInstantiator(outerClassInstance).newInstance(null));
    }

    @Test
    public void testNewInstanceWithClassHavingNoArgConstructor() {
        Class<String> stringClass = String.class;
        String instance = new ConstructorInstantiator(null).newInstance(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassHavingNoArgConstructorButThrowsException() {
        Class<String> stringClass = new Class<String>() {
            @Override
            protected Constructor<String>[] getDeclaredConstructors() {
                return new Constructor[0];
            }

            @Override
            public Constructor<String> getDeclaredConstructor(Class<?>... paramTypes) {
                throw new InstantiationException("Error");
            }
        };
        new ConstructorInstantiator(null).newInstance(stringClass);
    }

    @Test
    public void testNewInstanceWithClassHavingParametrizedConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<Foo> fooClass = Foo.class;

        Foo instance = new ConstructorInstantiator(outerClassInstance).newInstance(fooClass);
        assertInstanceOf(Foo.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassHavingParametrizedConstructorButNoConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<Foo> fooClass = Foo.class;

        try {
            new ConstructorInstantiator(outerClassInstance).withOuterClass(fooClass);
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Error", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassHavingParametrizedConstructorButThrowsException() {
        Object outerClassInstance = mock(Object.class);
        Class<Foo> fooClass = Foo.class;

        try {
            Constructor<Foo> constructor = fooClass.getDeclaredConstructor(Object.class);
            throw new InstantiationException("Error", new InstantiationException("Error"));
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Error", e);
        } catch (InstantiationException e) {
            throw e;
        }
    }

    private static class Foo {
        private final Object outer;

        public Foo(Object outer) {
            this.outer = outer;
        }
    }
}