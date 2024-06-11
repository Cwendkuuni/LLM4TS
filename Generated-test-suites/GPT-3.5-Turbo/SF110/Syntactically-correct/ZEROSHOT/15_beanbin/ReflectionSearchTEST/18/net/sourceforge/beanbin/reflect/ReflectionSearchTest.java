import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }
    
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        assertTrue(reflectionSearch.hasWildcard("te*"));
        assertFalse(reflectionSearch.hasWildcard("test"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("testMethod1")));
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("testMethod2")));
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("otherMethod")));
    }
    
    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        List<Method> methods = reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("other").getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("testMethod1")));
    }
    
    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        List<Method> methods = reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("other").getMethods();
        assertEquals(3, methods.size());
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("testMethod1")));
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("testMethod2")));
        assertTrue(methods.contains(ExampleClass.class.getDeclaredMethod("otherMethod")));
    }
    
    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        List<Method> master = reflectionSearch.getMethods();
        List<Method> toadd = reflectionSearch.methodsThatHave("test").getMethods();
        
        reflectionSearch.intersect(master, toadd);
        
        assertEquals(2, master.size());
        assertTrue(master.contains(ExampleClass.class.getDeclaredMethod("testMethod1")));
        assertTrue(master.contains(ExampleClass.class.getDeclaredMethod("testMethod2")));
    }
    
    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        
        List<Method> master = reflectionSearch.getMethods();
        List<Method> toadd = reflectionSearch.methodsThatDontHave("test").getMethods();
        
        reflectionSearch.union(master, toadd);
        
        assertEquals(2, master.size());
        assertTrue(master.contains(ExampleClass.class.getDeclaredMethod("otherMethod")));
        assertTrue(master.contains(ExampleClass.class.getDeclaredMethod("nonExistentMethod")));
    }
}

class ExampleClass {
    @TestAnnotation
    public void testMethod1() {
    }
    
    @TestAnnotation
    public void testMethod2() {
    }
    
    public void otherMethod() {
    }
    
    public void nonExistentMethod() {
    }
}