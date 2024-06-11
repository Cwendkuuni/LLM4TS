package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setup() {
        reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Test"));
        assertTrue(reflectionSearch.hasAnnotation("org.junit.Test"));
        assertFalse(reflectionSearch.hasAnnotation("NotAnAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
        assertEquals(2, methods.size());
        assertEquals("testHasAnnotation", methods.get(0).getName());
        assertEquals("testMethodsThatHave", methods.get(1).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testAnd() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("test").and()
                .methodsThatHave("Annotation").getMethods();
        assertEquals(1, methods.size());
        assertEquals("testHasAnnotation", methods.get(0).getName());
    }

    @Test
    public void testOr() throws BeanBinException {
        List<Method> methods = reflectionSearch.methodsThatHave("test").or()
                .methodsThatDontHave("NotAnAnnotation").getMethods();
        assertEquals(2, methods.size());
        assertEquals("testHasAnnotation", methods.get(0).getName());
        assertEquals("testMethodsThatHave", methods.get(1).getName());
    }

    // Add more test cases to comprehensively test all methods in the ReflectionSearch class

}