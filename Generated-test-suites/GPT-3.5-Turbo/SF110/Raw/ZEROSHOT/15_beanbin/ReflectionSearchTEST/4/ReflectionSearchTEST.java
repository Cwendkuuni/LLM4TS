import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;
import net.sourceforge.beanbin.BeanBinException;
import java.util.List;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("Deprecated"));
        assertTrue(reflectionSearch.hasAnnotation("*Target"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
        
        assertEquals(3, methods.size());
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testHasAnnotation")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave")));
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
        
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("test").and().methodsThatHave("Annotation");
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(1, methods.size());
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testHasAnnotation")));
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        reflectionSearch.methodsThatHave("test").or().methodsThatHave("DontHave");
        List<Method> methods = reflectionSearch.getMethods();
        
        assertEquals(2, methods.size());
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave")));
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        List<Method> methods = reflectionSearch.getMethods();
        
        assertNotNull(methods);
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testHasAnnotation")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatDontHave")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testAnd")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testOr")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testGetMethods")));
    }
    
    // Additional tests can be added here
    
}