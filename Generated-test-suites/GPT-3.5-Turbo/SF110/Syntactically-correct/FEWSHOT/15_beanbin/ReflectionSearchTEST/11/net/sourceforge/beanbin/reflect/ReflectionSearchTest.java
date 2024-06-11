
package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        assertTrue(reflectionSearch.hasAnnotation("org.junit.Test"));
        assertFalse(reflectionSearch.hasAnnotation("org.junit.Before"));
    }
    
    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        reflectionSearch.methodsThatHave("test");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(3, methods.size());
        
        for(Method method : methods) {
            assertTrue(method.getName().contains("test"));
        }
    }
    
    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        reflectionSearch.methodsThatDontHave("test");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testAnd() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        reflectionSearch.methodsThatHave("test").and().methodsThatHave("assertFalse");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(1, methods.size());
        
        Method method = methods.get(0);
        
        assertTrue(method.getName().contains("test"));
        assertTrue(method.getName().contains("assertFalse"));
    }
    
    @Test
    public void testOr() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        reflectionSearch.methodsThatHave("test").or().methodsThatHave("assertNotNull");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(4, methods.size());
    }

    @Test
    public void testGetMethods() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertNotNull(methods);
    }
}
