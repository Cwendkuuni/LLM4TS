package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationHandler;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @SuppressWarnings("unchecked")
    private static <T> T createInstance(Class<T> cls, Object outerClassInstance) {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        return constructorInstantiator.newInstance(cls);
    }

    @Test
    public void testNoArgConstructor() {
        Class<Foo> fooClass = Foo.class;
        Foo expectedFoo = mock(Foo.class, new Strictness(Strictness.STRICT_STUBS));
        Foo foo = createInstance(fooClass, null);
        assertEquals(expectedFoo, foo);
    }

    @Test
    public void testConstructorWithArgument() {
        Class<Bar> barClass = Bar.class;
        Object outerClassInstance = createMock(Baz.class);
        Bar expectedBar = mock(Bar.class, new Strictness(Strictness.STRICT_STUBS));
        when(expectedBar.doSomething(any(String.class))).thenReturn(42);

        Bar bar = createInstance(barClass, outerClassInstance);
        assertEquals(expectedBar, bar);
        assertEquals(42, bar.doSomething("test"));
    }

    @Test(expected = InstantiationException.class)
    public void testThrowsExceptionWhenNoArgConstructor() {
        Class<Baz> bazClass = Baz.class;
        createInstance(bazClass, null);
    }

    @Test(expected = InstantiationException.class)
    public void testThrowsExceptionWhenConstructorWithArgument() {
        Class<Quux> quuxClass = Quux.class;
        Object outerClassInstance = createMock(Foo.class);
        createInstance(quuxClass, outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testThrowsExceptionWhenInvalidOuterClassType() {
        Class<Quux> quuxClass = Quux.class;
        Object outerClassInstance = createMock(Baz.class);
        createInstance(quuxClass, outerClassInstance);
    }

    @Test
    public void testParamsExceptionIsThrownWhenConstructorNotFound() {
        Class<Quux> quuxClass = Quux.class;
        Object outerClassInstance = createMock(Quux.class);
        try {
            createInstance(quuxClass, outerClassInstance);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    static class Foo {}
    static class Bar {
        public int doSomething(String s) {
            return 42;
        }
    }
    static class Baz {}
    static class Quux {
        private Bar bar;

        Quux(Bar bar) {
            this.bar = bar;
        }
    }
}