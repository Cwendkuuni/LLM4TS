import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        // Test case 1, when the Annotation name equals the term
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        
        // Test case 2, when the Annotation class name equals the term
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("TestClass$TestAnnotation"));
        
        // Test case 3, when the Annotation name contains the term as a substring
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("nnot"));
        
        // Test case 4, when the Annotation class name contains the term as a substring
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("tAnno"));
        
        // Test case 5, when the Annotation name matches the term with a wildcard
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("Test*"));
        
        // Test case 6, when the Annotation class name matches the term with a wildcard
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("T*tAnot"));
        
        // Test case 7, when the Annotation name does not match the term
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertFalse(reflectionSearch.hasAnnotation("DummyAnnotation"));
        
        // Test case 8, when the Annotation class name does not match the term
        reflectionSearch = new ReflectionSearch(TestClass.class);
        assertFalse(reflectionSearch.hasAnnotation("TestClass$DummyAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(2, methods.size());
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("testMethod1")));
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("testMethod2")));
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatDontHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(1, methods.size());
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("dummyMethod")));
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("dummy");
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(1, methods.size());
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("testMethod2")));
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("dummy");
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(3, methods.size());
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("testMethod1")));
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("testMethod2")));
        assertTrue(methods.contains(TestClass.class.getDeclaredMethod("dummyMethod")));
    }
    
    private static class TestClass {
        
        @TestAnnotation
        public void testMethod1() {
            
        }
        
        @TestAnnotation
        public void testMethod2() {
            
        }
        
        public void dummyMethod() {
            
        }
    }
}