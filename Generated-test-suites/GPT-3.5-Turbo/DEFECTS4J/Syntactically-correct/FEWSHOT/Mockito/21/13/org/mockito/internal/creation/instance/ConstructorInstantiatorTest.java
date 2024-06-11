package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiatorWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> cls = TestClass.class;
        TestClass newInstance = constructorInstantiator.newInstance(cls);
        assertNotNull(newInstance);
        assertTrue(newInstance instanceof TestClass);
    }

    @Test
    public void testConstructorInstantiatorNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<?> cls = TestClass.class;
        TestClass newInstance = constructorInstantiator.newInstance(cls);
        assertNotNull(newInstance);
        assertTrue(newInstance instanceof TestClass);
    }

    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorWithOuterClassException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> cls = TestClassWithNoParamConstructor.class;
        constructorInstantiator.newInstance(cls);
    }

    @Test(expected = InstantationException.class)
    public void testConstructorInstantiatorNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<?> cls = TestClassWithNoParamConstructor.class;
        constructorInstantiator.newInstance(cls);
    }
    
    static class TestClass {
        public TestClass() {}
        public TestClass(Object outerClassInstance) {}
    }

    static class TestClassWithNoParamConstructor {
        public TestClassWithNoParamConstructor(Object outerClassInstance) {}
    }
}