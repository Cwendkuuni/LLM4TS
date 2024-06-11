package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {
    
    private Class clazz;
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        clazz = getClass();
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
        assertTrue(reflectionSearch.hasAnnotation("Annotation2"));
        assertFalse(reflectionSearch.hasAnnotation("Annotation3"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("method1");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("method1", methods.get(0).getName());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("method1");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("method2", methods.get(0).getName());
        assertEquals("method3", methods.get(1).getName());
    }
    
    @Test
    public void testAnd() throws BeanBinException {
        reflectionSearch.methodsThatHave("method1").and().methodsThatHave("method2");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("method1").and().methodsThatHave("method3");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("method3", methods.get(0).getName());
    }
    
    @Test
    public void testOr() throws BeanBinException {
        reflectionSearch.methodsThatHave("method1").or().methodsThatHave("method2");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("method1", methods.get(0).getName());
        assertEquals("method2", methods.get(1).getName());
        
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("method1").or().methodsThatHave("method3");
        methods = reflectionSearch.getMethods();
        assertEquals(3, methods.size());
        assertEquals("method1", methods.get(0).getName());
        assertEquals("method2", methods.get(1).getName());
        assertEquals("method3", methods.get(2).getName());
    }
}