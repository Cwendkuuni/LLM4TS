package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Retention"));
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Documented"));
        assertFalse(reflectionSearch.hasAnnotation("java.lang.annotation.Target"));

        assertFalse(reflectionSearch.hasAnnotation("*annotation.Retention"));
        assertTrue(reflectionSearch.hasAnnotation("java.*.annotation.Documented"));
        assertTrue(reflectionSearch.hasAnnotation("*.annotation.Target"));

        assertFalse(reflectionSearch.hasAnnotation(null));
    }

    @Test
    public void testMethodsThatHave() {
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.methodsThatHave("equals");

        // assertion statements

    }

    @Test
    public void testMethodsThatDontHave() {
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatDontHave("toString");
        reflectionSearch.methodsThatDontHave("equals");

        // assertion statements

    }

    @Test
    public void testAnd() {
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("toString").and().methodsThatDontHave("equals");

        // assertion statements

    }

    @Test
    public void testOr() {
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("toString").or().methodsThatDontHave("equals");

        // assertion statements

    }

    @Test
    public void testGetMethods() {
        Class clazz = DummyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("hashCode");

        // assertion statements

    }

    private class DummyClass {
        @Override
        public String toString() {
            return "DummyClass";
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DummyClass;
        }

        public void dummyMethod() {
            // method implementation
        }
    }
}