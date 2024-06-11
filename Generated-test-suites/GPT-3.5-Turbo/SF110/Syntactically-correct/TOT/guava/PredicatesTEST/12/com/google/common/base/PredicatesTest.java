import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        // Test with any type of input
        assertTrue(Predicates.alwaysTrue().apply("test"));
        assertTrue(Predicates.alwaysTrue().apply(123));
        assertTrue(Predicates.alwaysTrue().apply(null));
    }
    
    @Test
    public void testAlwaysFalse() {
        // Test with any type of input
        assertFalse(Predicates.alwaysFalse().apply("test"));
        assertFalse(Predicates.alwaysFalse().apply(123));
        assertFalse(Predicates.alwaysFalse().apply(null));
    }
    
    @Test
    public void testIsNull() {
        // Test with null input
        assertTrue(Predicates.isNull().apply(null));
        
        // Test with non-null input
        assertFalse(Predicates.isNull().apply("test"));
        assertFalse(Predicates.isNull().apply(123));
    }
    
    @Test
    public void testNotNull() {
        // Test with null input
        assertFalse(Predicates.notNull().apply(null));
        
        // Test with non-null input
        assertTrue(Predicates.notNull().apply("test"));
        assertTrue(Predicates.notNull().apply(123));
    }
    
    @Test
    public void testNot() {
        Predicate<String> isNull = Predicates.isNull();
        Predicate<String> notNull = Predicates.notNull();
        
        // Test with null input
        assertFalse(Predicates.not(isNull).apply(null));
        assertTrue(Predicates.not(notNull).apply(null));
        
        // Test with non-null input
        assertTrue(Predicates.not(isNull).apply("test"));
        assertFalse(Predicates.not(notNull).apply("test"));
    }
    
    // Other test cases for other methods in the class...
    
}