package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {
    private static class TestClass {
        @SuppressWarnings("unused")
        private int privateField;
        
        @SuppressWarnings("unused")
        public static int publicStaticField;
        
        public int publicField;
        
        public TestClass() {}
        
        private void privateMethod() {}
        
        public static void publicStaticMethod() {}
        
        public void publicMethod() {}
    }
    
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(TestClass.class);
    }
    
    @Test
    public void testHasAnnotation() {
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertTrue(reflectionSearch.hasAnnotation("SuppressWarnings"));
    }
    
    @Test
    public void testMethodsThatHave() throws Exception {
        List<Method> expectedMethods = Arrays.asList(
            TestClass.class.getDeclaredMethod("privateMethod"),
            TestClass.class.getDeclaredMethod("publicStaticMethod"),
            TestClass.class.getDeclaredMethod("publicMethod")
        );
        
        List<Method> methods = reflectionSearch.methodsThatHave("Method").getMethods();
        assertEquals(expectedMethods, methods);
    }
    
    @Test
    public void testMethodsThatDontHave() throws Exception {
        List<Method> expectedMethods = Arrays.asList(
            TestClass.class.getDeclaredMethod("privateMethod"),
            TestClass.class.getDeclaredMethod("publicStaticMethod"),
            TestClass.class.getDeclaredMethod("publicMethod")
        );
        
        List<Method> methods = reflectionSearch.methodsThatDontHave("Field").getMethods();
        assertEquals(expectedMethods, methods);
    }
    
    @Test
    public void testAnd() throws Exception {
        List<Method> expectedMethods = Arrays.asList(
            TestClass.class.getDeclaredMethod("publicMethod")
        );
        
        List<Method> methods = reflectionSearch
            .methodsThatHave("Method")
            .and()
            .methodsThatDontHave("Field")
            .getMethods();
        
        assertEquals(expectedMethods, methods);
    }
    
    @Test
    public void testOr() throws Exception {
        List<Method> expectedMethods = Arrays.asList(
            TestClass.class.getDeclaredMethod("privateMethod"),
            TestClass.class.getDeclaredMethod("publicStaticMethod"),
            TestClass.class.getDeclaredMethod("publicMethod")
        );
        
        List<Method> methods = reflectionSearch
            .methodsThatHave("Method")
            .or()
            .methodsThatDontHave("Method")
            .getMethods();
        
        assertEquals(expectedMethods, methods);
    }
    
    @Test
    public void testGetMethods() throws Exception {
        List<Method> expectedMethods = Arrays.asList(
            TestClass.class.getDeclaredMethod("privateMethod"),
            TestClass.class.getDeclaredMethod("publicStaticMethod"),
            TestClass.class.getDeclaredMethod("publicMethod")
        );
        
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(expectedMethods, methods);
        
        // Test caching
        List<Method> cachedMethods = reflectionSearch.getMethods();
        assertEquals(methods, cachedMethods);
    }
    
    @Test
    public void testIntersect() throws Exception {
        List<Method> master = new ArrayList<Method>(
            Arrays.asList(TestClass.class.getDeclaredMethods())
        );
        List<Method> toAdd = new ArrayList<Method>(
            Arrays.asList(TestClass.class.getDeclaredMethod("privateMethod"))
        );

        reflectionSearch.intersect(master, toAdd);
        assertEquals(1, master.size());
        assertFalse(master.contains(TestClass.class.getDeclaredMethod("privateMethod")));
    }
    
    @Test
    public void testUnion() throws Exception {
        List<Method> master = new ArrayList<Method>(
            Arrays.asList(TestClass.class.getDeclaredMethods())
        );
        List<Method> toAdd = new ArrayList<Method>(
            Arrays.asList(TestClass.class.getDeclaredMethod("privateMethod"))
        );

        reflectionSearch.union(master, toAdd);
        assertEquals(4, master.size());
        assertTrue(master.contains(TestClass.class.getDeclaredMethod("privateMethod")));
    }
}