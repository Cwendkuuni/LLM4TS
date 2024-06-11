import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation_MethodWithAnnotation_ReturnsTrue() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        boolean result = reflectionSearch.hasAnnotation("MyAnnotation");
        
        assertTrue(result);
    }
    
    @Test
    public void testHasAnnotation_MethodWithoutAnnotation_ReturnsFalse() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        boolean result = reflectionSearch.hasAnnotation("OtherAnnotation");
        
        assertFalse(result);
    }
    
    @Test
    public void testHasAnnotation_MethodWithWildcardAnnotation_ReturnsTrue() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        boolean result = reflectionSearch.hasAnnotation("My*");
        
        assertTrue(result);
    }
    
    @Test
    public void testHasAnnotation_ClassWithAnnotation_ReturnsTrue() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        boolean result = reflectionSearch.hasAnnotation("MyClassAnnotation");
        
        assertTrue(result);
    }
    
    @Test
    public void testHasAnnotation_ClassWithoutAnnotation_ReturnsFalse() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        boolean result = reflectionSearch.hasAnnotation("OtherClassAnnotation");
        
        assertFalse(result);
    }
    
    @Test
    public void testHasAnnotation_ClassWithWildcardAnnotation_ReturnsTrue() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        boolean result = reflectionSearch.hasAnnotation("My*");
        
        assertTrue(result);
    }
    
    @Test
    public void testMethodsThatHave_MethodsExist_ReturnsMethods() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        List<Method> methods = reflectionSearch.methodsThatHave("doSomething").getMethods();
        
        assertEquals(1, methods.size());
        assertTrue(methods.get(0).getName().equals("doSomething"));
    }
    
    @Test
    public void testMethodsThatHave_MethodsDoNotExist_ReturnsEmptyList() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        List<Method> methods = reflectionSearch.methodsThatHave("nonExistingMethod").getMethods();
        
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testMethodsThatDontHave_MethodsExist_ReturnsEmptyList() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        List<Method> methods = reflectionSearch.methodsThatDontHave("doSomething").getMethods();
        
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testMethodsThatDontHave_MethodsDoNotExist_ReturnsMethods() throws BeanBinException {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        List<Method> methods = reflectionSearch.methodsThatDontHave("nonExistingMethod").getMethods();
        
        assertEquals(1, methods.size());
    }
    
    @Test
    public void testAnd_Called_ReturnsSameInstance() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        ReflectionSearch result = reflectionSearch.and();
        
        assertSame(reflectionSearch, result);
    }
    
    @Test
    public void testOr_Called_ReturnsSameInstance() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        
        ReflectionSearch result = reflectionSearch.or();
        
        assertSame(reflectionSearch, result);
    }

}