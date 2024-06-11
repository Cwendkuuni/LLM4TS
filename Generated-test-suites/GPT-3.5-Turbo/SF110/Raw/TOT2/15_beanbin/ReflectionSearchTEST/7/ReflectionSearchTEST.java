import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;
    
    @Test
    public void testHasAnnotation() {
        reflectionSearch = new ReflectionSearch(String.class);
        
        assertFalse(reflectionSearch.hasAnnotation("Test"));
        assertTrue(reflectionSearch.hasAnnotation("Override"));
    }
    
    @Test
    public void testHasWildcard() {
        reflectionSearch = new ReflectionSearch(String.class);
        
        assertFalse(reflectionSearch.hasWildcard("Test"));
        assertTrue(reflectionSearch.hasWildcard("T*st"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        reflectionSearch = new ReflectionSearch(String.class);
        
        reflectionSearch.methodsThatHave("toString");
        assertEquals(1, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        reflectionSearch = new ReflectionSearch(String.class);
        
        reflectionSearch.methodsThatDontHave("toString");
        assertEquals(0, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        reflectionSearch = new ReflectionSearch(String.class);
        
        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("hashCode");
        
        assertEquals(1, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testIntersect() {
        reflectionSearch = new ReflectionSearch(String.class);
        
        Method method1 = String.class.getDeclaredMethod("toString");
        Method method2 = String.class.getDeclaredMethod("hashCode");
        
        reflectionSearch.intersect(List.of(method1), List.of(method2));
        assertEquals(0, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testUnion() {
        reflectionSearch = new ReflectionSearch(String.class);
        
        Method method1 = String.class.getDeclaredMethod("toString");
        Method method2 = String.class.getDeclaredMethod("hashCode");
        
        reflectionSearch.union(List.of(method1), List.of(method2));
        assertEquals(2, reflectionSearch.getMethods().size());
    }
}