import org.junit.*;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        
        // Typical use cases
        assertTrue(alwaysTrue.apply(10));
        assertTrue(alwaysTrue.apply(0));
        
        // Edge cases
        assertTrue(alwaysTrue.apply(Integer.MIN_VALUE));
        assertTrue(alwaysTrue.apply(Integer.MAX_VALUE));
        
        // Error scenarios
        assertTrue(alwaysTrue.apply(null));
    }
    
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        
        // Typical use cases
        assertFalse(alwaysFalse.apply(10));
        assertFalse(alwaysFalse.apply(0));
        
        // Edge cases
        assertFalse(alwaysFalse.apply(Integer.MIN_VALUE));
        assertFalse(alwaysFalse.apply(Integer.MAX_VALUE));
        
        // Error scenarios
        assertFalse(alwaysFalse.apply(null));
    }
    
    // Repeat the above tests for the remaining methods in Predicates class
    
    // Test cases for other methods...

}