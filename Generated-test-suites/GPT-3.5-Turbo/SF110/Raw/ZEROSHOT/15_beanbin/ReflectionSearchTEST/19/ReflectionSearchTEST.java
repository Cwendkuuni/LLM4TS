package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    private Class clazz;
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        clazz = TestClass.class;
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
        
        reflectionSearch.methodsThatHave("method2");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals("method1", methods.get(0).getName());
        assertEquals("method2", methods.get(1).getName());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("method1");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("method2", methods.get(0).getName());
        
        reflectionSearch.methodsThatDontHave("method2");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testAnd() {
        reflectionSearch.methodsThatHave("method1").and().methodsThatHave("method2");
        assertEquals(2, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testOr() {
        reflectionSearch.methodsThatHave("method1").or().methodsThatHave("method3");
        assertEquals(2, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        reflectionSearch.methodsThatHave("method1");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("method1", methods.get(0).getName());
    }
    
    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        Method method1 = null;
        Method method2 = null;
        try {
            method1 = clazz.getMethod("method1");
            method2 = clazz.getMethod("method2");
        } catch (NoSuchMethodException e) {
            fail("Failed to retrieve methods");
        }
        
        master.add(method1);
        master.add(method2);
        
        toadd.add(method1);
        
        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size());
        assertEquals(method1, master.get(0));
    }
    
    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        
        Method method1 = null;
        Method method2 = null;
        try {
            method1 = clazz.getMethod("method1");
            method2 = clazz.getMethod("method2");
        } catch (NoSuchMethodException e) {
            fail("Failed to retrieve methods");
        }
        
        master.add(method1);
        
        toadd.add(method2);
        
        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
    }
}