
package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation1"));
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation2"));
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation3"));
    }

    @Test
    public void testHasWildcardAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        assertTrue(reflectionSearch.hasAnnotation("*TestAnnotation1"));
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation*"));
        assertTrue(reflectionSearch.hasAnnotation("*TestAnnotation*"));
    }

    @Test
    public void testDoesNotHaveWildcardAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        assertFalse(reflectionSearch.hasAnnotation("*TestAnnotation4"));
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation*4"));
        assertFalse(reflectionSearch.hasAnnotation("*TestAnnotation*4"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("methodName1");
        assertNotNull(result);
        
        List<Method> methods = result.getMethods();
        assertNotNull(methods);
        
        // Assert method names
        assertEquals(1, methods.size());
        assertEquals("methodName1", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("methodName1");
        assertNotNull(result);
        
        List<Method> methods = result.getMethods();
        assertNotNull(methods);
        
        // Assert method names
        assertNotEquals("methodName1", methods.get(0).getName());
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("methodName1").and().methodsThatHave("methodName2");
        assertNotNull(result);
        
        List<Method> methods = result.getMethods();
        assertNotNull(methods);
        
        // Assert method names
        assertEquals(2, methods.size());
        assertEquals("methodName1", methods.get(0).getName());
        assertEquals("methodName2", methods.get(1).getName());
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("methodName1").or().methodsThatHave("methodName2");
        assertNotNull(result);
        
        List<Method> methods = result.getMethods();
        assertNotNull(methods);
        
        // Assert method names
        assertEquals(2, methods.size());
        assertEquals("methodName1", methods.get(0).getName());
        assertEquals("methodName2", methods.get(1).getName());
    }

}
