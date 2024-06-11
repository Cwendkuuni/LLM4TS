
package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;

    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
    }

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
    }

    @Test
    public void testHasAnnotation_withWildcard() {
        assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
        assertTrue(reflectionSearch.hasAnnotation("My*"));
        assertFalse(reflectionSearch.hasAnnotation("OtherAnnotation"));
    }

    @Test
    public void testHasAnnotation_exact() {
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("OtherAnnotation"));
    }

    @Test
    public void testHasAnnotation_empty() {
        assertFalse(reflectionSearch.hasAnnotation(""));
    }

    @Test
    public void testHasAnnotation_invalidAnnotationName() {
        assertFalse(reflectionSearch.hasAnnotation("?InvalidAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("testMethod");
        assertEquals(1, reflectionSearch.getMethods().size());
        assertEquals("testMethod", reflectionSearch.getMethods().get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("testMethod");
        assertEquals(0, reflectionSearch.getMethods().size());
    }
}
