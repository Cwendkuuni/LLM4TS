package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(SomeClass.class);
    }
    
    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("SomeAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("AnotherAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch.methodsThatHave("someTerm");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.contains(SomeClass.class.getMethod("someMethod")));
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("someTerm");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.contains(SomeClass.class.getMethod("someOtherMethod")));
        assertTrue(methods.contains(SomeClass.class.getMethod("yetAnotherMethod")));
    }
    
    @Test
    public void testAnd() {
        reflectionSearch.methodsThatHave("someTerm").and().methodsThatHave("anotherTerm");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.contains(SomeClass.class.getMethod("bothMethods")));
    }
    
    @Test
    public void testOr() {
        reflectionSearch.methodsThatHave("someTerm").or().methodsThatHave("anotherTerm");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(3, methods.size());
        assertTrue(methods.contains(SomeClass.class.getMethod("someMethod")));
        assertTrue(methods.contains(SomeClass.class.getMethod("someOtherMethod")));
        assertTrue(methods.contains(SomeClass.class.getMethod("bothMethods")));
    }
    
    private static class SomeAnnotation {
    }
    
    @SomeAnnotation
    private static class SomeClass {
        
        @SomeAnnotation
        public void someMethod() {}
        
        public void someOtherMethod() {}
        
        @SomeAnnotation
        public void yetAnotherMethod() {}
        
        public void bothMethods() {}
    }
}