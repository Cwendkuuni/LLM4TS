import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import net.sourceforge.beanbin.BeanBinException;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        
        // Edge case: term with wildcard
        assertFalse(reflectionSearch.hasAnnotation("*Test"));
        
        // Error scenario: null input
        try {
            reflectionSearch.hasAnnotation(null);
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("length");
        Method[] methods = reflectionSearch.getMethods().toArray(new Method[0]);
        assertEquals(1, methods.length);
        
        // Edge case: term not found
        reflectionSearch.methodsThatHave("xyz");
        methods = reflectionSearch.getMethods().toArray(new Method[0]);
        assertEquals(0, methods.length);
        
        // Error scenario: null term
        try {
            reflectionSearch.methodsThatHave(null);
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Typical use case
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("xyz");
        Method[] methods = reflectionSearch.getMethods().toArray(new Method[0]);
        assertTrue(methods.length > 0);
        
        // Edge case: term found
        reflectionSearch.methodsThatDontHave("length");
        methods = reflectionSearch.getMethods().toArray(new Method[0]);
        assertEquals(0, methods.length);
        
        // Error scenario: null term
        try {
            reflectionSearch.methodsThatDontHave(null);
            fail("Expected BeanBinException to be thrown");
        } catch (BeanBinException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    // Additional test cases for and(), or(), getMethods() and intersection()/union() methods can be added
    
}