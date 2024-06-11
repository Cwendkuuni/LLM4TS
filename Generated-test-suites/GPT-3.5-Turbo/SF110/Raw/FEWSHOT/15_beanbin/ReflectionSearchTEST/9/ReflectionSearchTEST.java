package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {
    private Class clazz;
    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        clazz = MyClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }

    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NotAnAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("myMethod");
        // Assert expected results
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("myMethod");
        // Assert expected results
    }

    @Test
    public void testAnd() {
        reflectionSearch.and();
        // Assert expected behavior
    }

    @Test
    public void testOr() {
        reflectionSearch.or();
        // Assert expected behavior
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        // Assert expected results
    }
}

class MyClass {
    @MyAnnotation
    public void myMethod() {
        // Method implementation
    }
}