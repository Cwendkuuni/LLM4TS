package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.ClassCastException;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.creation.instance.ConstructorInstantiator.noArgConstructor;
import static org.mockito.internal.creation.instance.ConstructorInstantiator.paramsException;
import static org.mockito.internal.creation.instance.ConstructorInstantiator.withOuterClass;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void init() {
        outerClassInstance = new Object() {};
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNullOuterClassInstance() {
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(String.class));
        assertEquals("Unable to create mock instance of 'java.lang.String'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNullOuterClassInstanceParamClass() {
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(null));
        assertNull(exception);
    }

    @Test
    public void testNullClass() {
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(null));
        assertNull(exception);
    }

    @Test
    public void testNoArgConstructor() {
        Object instance = constructorInstantiator.newInstance(String.class);
        assertInstanceOf(String.class, instance);
    }

    @Test
    public void testWithOuterClass() {
        Class innerClass = new InnerClass();
        Class<InnerClass> innerClassType = (Class<InnerClass>) innerClass.getClass();

        InnerClass innerInstance = constructorInstantiator.newInstance(innerClassType);
        assertInstanceOf(InnerClass.class, innerInstance);
        assertEquals(outerClassInstance, innerInstance.outerClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNoSuchConstructor() {
        Class<NoSuchConstructorClass> noSuchConstructorClass = NoSuchConstructorClass.class;
        constructorInstantiator.withOuterClass(noSuchConstructorClass);
    }

    @Test(expected = ClassCastException.class)
    public void testOuterClassNotMatching() {
        Object outerClassInstanceDifferentType = new Object() {};
        ConstructorInstantiator constructorInstantiatorDifferentType = new ConstructorInstantiator(outerClassInstanceDifferentType);

        Class<InnerClass> innerClassType = (Class<InnerClass>) new InnerClass().getClass();
        constructorInstantiatorDifferentType.newInstance(innerClassType);
    }

    private static class InnerClass {
        Object outerClass;

        InnerClass() {
            this.outerClass = outerClassInstance;
        }
    }

    private static class NoSuchConstructorClass {
        NoSuchConstructorClass() {}

        private static NoSuchConstructorClass innerClass() {
            return new NoSuchConstructorClass();
        }
    }
}