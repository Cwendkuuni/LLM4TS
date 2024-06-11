import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        // Typical use case
        assertTrue(Predicates.alwaysTrue().apply("test"));
        
        // Edge case
        assertTrue(Predicates.alwaysTrue().apply(null));
    }
    
    @Test
    public void testAlwaysFalse() {
        // Typical use case
        assertFalse(Predicates.alwaysFalse().apply("test"));
        
        // Edge case
        assertFalse(Predicates.alwaysFalse().apply(null));
    }
    
    @Test
    public void testIsNull() {
        // Typical use case
        assertTrue(Predicates.isNull().apply(null));
        
        // Edge case
        assertFalse(Predicates.isNull().apply("test"));
    }
    
    @Test
    public void testNotNull() {
        // Typical use case
        assertTrue(Predicates.notNull().apply("test"));
        
        // Edge case
        assertFalse(Predicates.notNull().apply(null));
    }
    
    @Test
    public void testNot() {
        // Typical use case
        assertFalse(Predicates.not(Predicates.isNull()).apply(null));
        
        // Edge case
        assertTrue(Predicates.not(Predicates.isNull()).apply("test"));
    }
    
    // Additional test cases for other methods...
    
}