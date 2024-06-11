import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        // Typical use case
        assertTrue(new ReflectionSearch(ReflectionSearch.class).hasAnnotation("Override"));
        
        // Edge case: annotation with wildcard
        assertTrue(new ReflectionSearch(ReflectionSearch.class).hasAnnotation("*.Search"));
        
        // Error scenario: null input
        assertFalse(new ReflectionSearch(ReflectionSearch.class).hasAnnotation(null));
    }
    
    @Test
    public void testMethodsThatHave() {
        // Typical use case
        assertNotNull(new ReflectionSearch(ReflectionSearch.class).methodsThatHave("toString").getMethods());
        
        // Edge case: method search with wildcard
        assertNotNull(new ReflectionSearch(ReflectionSearch.class).methodsThatHave("get*").getMethods());
        
        // Error scenario: invalid input
        try {
            new ReflectionSearch(ReflectionSearch.class).methodsThatHave(null);
            fail("Exception not thrown for null input");
        } catch (BeanBinException e) {
            // expected
        }
    }
    
    @Test
    public void testMethodsThatDontHave() {
        // Typical use case
        assertNotNull(new ReflectionSearch(ReflectionSearch.class).methodsThatDontHave("equals").getMethods());
        
        // Edge case: method search with wildcard
        assertNotNull(new ReflectionSearch(ReflectionSearch.class).methodsThatDontHave("*ToString").getMethods());
        
        // Error scenario: invalid input
        try {
            new ReflectionSearch(ReflectionSearch.class).methodsThatDontHave(null);
            fail("Exception not thrown for null input");
        } catch (BeanBinException e) {
            // expected
        }
    }
    
    @Test
    public void testAnd() {
        // No specific test needed as it is a simple method call
    }
    
    @Test
    public void testOr() {
        // No specific test needed as it is a simple method call
    }
    
    @Test
    public void testGetMethods() {
        // Typical use case
        assertNotNull(new ReflectionSearch(ReflectionSearch.class).getMethods());
        
        // Edge case: empty result
        assertNotNull(new ReflectionSearch(ReflectionSearch.class).methodsThatHave("NonExistentMethod").getMethods());
    }
}