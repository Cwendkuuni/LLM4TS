package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.query.SearchType;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        Class<?> clazz = TestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Test"));
        assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.Test"));
        assertFalse(reflectionSearch.hasAnnotation("Ignore"));
        assertFalse(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.Ignore"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("foo");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("foo", methods.get(0).getName());
        
        reflectionSearch.methodsThatHave("bar");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("foo", methods.get(0).getName());
        assertEquals("bar", methods.get(1).getName());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("foo");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        reflectionSearch.methodsThatDontHave("baz");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
        
        reflectionSearch.methodsThatHave("bar").methodsThatDontHave("foo");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("bar", methods.get(0).getName());
    }
    
    @Test
    public void testAnd() throws BeanBinException {
        reflectionSearch.methodsThatHave("foo").and().methodsThatHave("bar");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("foo", methods.get(0).getName());
    }
    
    @Test
    public void testOr() throws BeanBinException {
        reflectionSearch.methodsThatHave("foo").or().methodsThatHave("baz");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.get(0).getName().equals("foo") || methods.get(0).getName().equals("baz"));
        assertTrue(methods.get(1).getName().equals("foo") || methods.get(1).getName().equals("baz"));
    }
    
    private static class TestClass {
        @Test
        public void foo() {}
        
        @Test
        public void bar() {}
        
        @Ignore
        public void baz() {}
    }
}