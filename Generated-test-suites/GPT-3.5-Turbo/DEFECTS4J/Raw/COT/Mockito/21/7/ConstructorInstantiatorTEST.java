
package org.mockito.internal.creation.instance;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object instance = constructorInstantiator.newInstance(ClassWithNoArgConstructor.class);
        assertNotNull(instance);
        assertTrue(instance instanceof ClassWithNoArgConstructor);
    }

    @Test
    public void testWithOuterClass() {
        OuterClass outerClassInstance = new OuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = constructorInstantiator.newInstance(ClassWithOuterClassConstructor.class);
        assertNotNull(instance);
        assertTrue(instance instanceof ClassWithOuterClassConstructor);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_Exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(ClassWithOuterClassConstructor.class);
    }
    
    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_Exception() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(ClassWithParamConstructor.class);
    }

    public static class ClassWithNoArgConstructor {
        public ClassWithNoArgConstructor() {
        }
    }

    public static class ClassWithOuterClassConstructor {
        public ClassWithOuterClassConstructor(OuterClass outerClass) {
        }
    }

    public static class ClassWithParamConstructor {
        public ClassWithParamConstructor(String param) {
        }
    }

    public static class OuterClass {
    }
}
