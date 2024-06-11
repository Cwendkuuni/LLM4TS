import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
    }
    
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasWildcard("wild*"));
        assertFalse(reflectionSearch.hasWildcard("noWild"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = reflectionSearch.methodsThatHave("method");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("method");
        assertNotNull(result);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
    }
}