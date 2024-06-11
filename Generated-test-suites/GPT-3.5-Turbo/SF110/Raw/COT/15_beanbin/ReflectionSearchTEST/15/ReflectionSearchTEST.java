
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertTrue(reflectionSearch.hasAnnotation("Deprecated"));
        assertTrue(reflectionSearch.hasAnnotation("*"));
    }
    
    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        ReflectionSearch result = reflectionSearch.methodsThatHave("public");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("private");
        assertNotNull(result);
    }
    
    @Test
    public void testAnd() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        ReflectionSearch result = reflectionSearch.methodsThatHave("public").and();
        assertNotNull(result);
    }
    
    @Test
    public void testOr() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        ReflectionSearch result = reflectionSearch.methodsThatHave("public").or();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMethods() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
    }
}
