package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("*TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("Test"));
        assertFalse(reflectionSearch.hasAnnotation("*Test"));

        assertTrue(reflectionSearch.hasAnnotation("Retention"));
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Retention"));
        assertTrue(reflectionSearch.hasAnnotation("*Retention"));
        assertTrue(reflectionSearch.hasAnnotation("*java.lang.annotation.Retention"));
    }

    @Test
    public void testMethodsThatHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("testMethodsThatHave", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatDontHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("testHasAnnotation", methods.get(0).getName());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("annotation");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("annotation");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("testMethodsThatHave", methods.get(0).getName());
        assertEquals("testHasAnnotation", methods.get(1).getName());
    }
}