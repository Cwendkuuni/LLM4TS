package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

    private Class clazz;

    @Before
    public void setup() {
        clazz = ReflectionSearch.class;
    }

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        assertTrue(reflectionSearch.hasAnnotation("java.lang.Deprecated"));
        assertTrue(reflectionSearch.hasAnnotation("java.lang.SuppressWarnings"));
        assertFalse(reflectionSearch.hasAnnotation("javax.persistence.Entity"));
        assertFalse(reflectionSearch.hasAnnotation("javax.persistence.Table"));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatHave("getMethods").getMethods();
        assertEquals(1, methods.size());
        assertEquals("getMethods", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("getDeclaredMethods").getMethods();
        assertEquals(1, methods.size());
        assertEquals("getDeclaredMethods", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("getQuery").getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatDontHave("getMethods").getMethods();
        assertFalse(methods.isEmpty());
        for (Method method : methods) {
            assertFalse(method.getName().equals("getMethods"));
        }

        methods = reflectionSearch.methodsThatDontHave("getDeclaredMethods").getMethods();
        assertFalse(methods.isEmpty());
        for (Method method : methods) {
            assertFalse(method.getName().equals("getDeclaredMethods"));
        }

        methods = reflectionSearch.methodsThatDontHave("getQuery").getMethods();
        assertEquals(2, methods.size());
    }

    @Test
    public void testAndOr() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatHave("getMethods").and().methodsThatHave("getQuery")
                .getMethods();
        assertEquals(1, methods.size());
        assertEquals("getMethods", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("getMethods").or().methodsThatHave("getQuery").getMethods();
        assertEquals(3, methods.size());
    }
}