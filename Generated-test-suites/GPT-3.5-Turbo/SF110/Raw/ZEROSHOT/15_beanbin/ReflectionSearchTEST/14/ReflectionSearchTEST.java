import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NotAnAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("AnotherAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        reflectionSearch.methodsThatHave("method1");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("method2");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(1, methods.size());
        assertEquals("method1", methods.get(0).getName());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        reflectionSearch.methodsThatDontHave("method1");
        reflectionSearch.or();
        reflectionSearch.methodsThatDontHave("method2");
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(1, methods.size());
        assertEquals("method3", methods.get(0).getName());
    }
    
    @Test
    public void testAnd() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        ReflectionSearch result = reflectionSearch.and();
        
        assertSame(reflectionSearch, result);
        assertEquals(Conditional.AND, reflectionSearch.query.getNextConditional());
    }
    
    @Test
    public void testOr() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        ReflectionSearch result = reflectionSearch.or();
        
        assertSame(reflectionSearch, result);
        assertEquals(Conditional.OR, reflectionSearch.query.getNextConditional());
    }
    
    private static class MyClass {
        @MyAnnotation
        void method1() {}
        
        @MyAnnotation
        void method2() {}
        
        void method3() {}
    }
}