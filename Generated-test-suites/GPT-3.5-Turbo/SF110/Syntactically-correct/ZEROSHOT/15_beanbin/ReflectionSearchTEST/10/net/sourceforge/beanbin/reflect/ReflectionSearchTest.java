package net.sourceforge.beanbin.reflect;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        // Test case 1
        assertFalse(reflectionSearch.hasAnnotation("test"));
        
        // Test case 2
        ReflectionSearch reflectionSearch2 = new ReflectionSearch(ReflectionSearch.class);
        assertTrue(reflectionSearch2.hasAnnotation("SuppressWarnings"));
        
        // Test case 3
        ReflectionSearch reflectionSearch3 = new ReflectionSearch(ReflectionSearch.class);
        assertFalse(reflectionSearch3.hasAnnotation("NotNull"));
    }
    
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        // Test case 1
        assertTrue(reflectionSearch.hasWildcard("test*"));
        
        // Test case 2
        assertFalse(reflectionSearch.hasWildcard("test"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        // Test case 1
        ReflectionSearch result = reflectionSearch.methodsThatHave("test");
        assertNotNull(result);
        
        // Test case 2
        ReflectionSearch result2 = reflectionSearch.methodsThatHave("setup");
        assertNotNull(result2);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        // Test case 1
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("test");
        assertNotNull(result);
        
        // Test case 2
        ReflectionSearch result2 = reflectionSearch.methodsThatDontHave("setup");
        assertNotNull(result2);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        
        // Test case 1
        List<Method> methods1 = reflectionSearch.getMethods();
        assertNotNull(methods1);
        
        // Test case 2
        ReflectionSearch reflectionSearch2 = new ReflectionSearch(ReflectionSearch.class);
        List<Method> methods2 = reflectionSearch2.getMethods();
        assertNotNull(methods2);
    }
    
    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        // Test case 1
        reflectionSearch.intersect(master, toadd);
        assertTrue(master.isEmpty());
        
        // Test case 2
        Method method1 = null;
        Method method2 = null;
        Method method3 = null;
        Method method4 = null;
        
        try {
            method1 = ReflectionSearch.class.getMethod("hasAnnotation", String.class);
            method2 = ReflectionSearch.class.getMethod("hasWildcard", String.class);
            method3 = ReflectionSearch.class.getMethod("methodsThatHave", String.class);
            method4 = ReflectionSearch.class.getMethod("methodsThatDontHave", String.class);
        } 
        catch (NoSuchMethodException e) {
            // do nothing
        }
        
        master.add(method1);
        master.add(method2);
        master.add(method3);
        
        toadd.add(method1);
        toadd.add(method4);
        
        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size());
        assertEquals(method1, master.get(0));
    }
    
    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        // Test case 1
        reflectionSearch.union(master, toadd);
        assertTrue(master.isEmpty());
        
        // Test case 2
        Method method1 = null;
        Method method2 = null;
        Method method3 = null;
        Method method4 = null;
        
        try {
            method1 = ReflectionSearch.class.getMethod("hasAnnotation", String.class);
            method2 = ReflectionSearch.class.getMethod("hasWildcard", String.class);
            method3 = ReflectionSearch.class.getMethod("methodsThatHave", String.class);
            method4 = ReflectionSearch.class.getMethod("methodsThatDontHave", String.class);
        } 
        catch (NoSuchMethodException e) {
            // do nothing
        }
        
        master.add(method1);
        master.add(method2);
        master.add(method3);
        
        toadd.add(method1);
        toadd.add(method4);
        
        reflectionSearch.union(master, toadd);
        assertEquals(4, master.size());
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
        assertTrue(master.contains(method3));
        assertTrue(master.contains(method4));
    }
}