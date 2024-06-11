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
        assertTrue(reflectionSearch.hasAnnotation("Test"));
        assertFalse(reflectionSearch.hasAnnotation("SomeAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("testMethodsThatHave", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatDontHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("testHasAnnotation", methods.get(0).getName());
        assertEquals("testMethodsThatDontHave", methods.get(1).getName());
    }

    // Additional test cases for ReflectionSearch methods...
}