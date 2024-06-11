import static org.junit.Assert.*;
import org.junit.Test;

public class PredicatesTest {
    
    @Test
    public void testAlwaysTrue() {
        Predicate<String> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply("test"));
        assertFalse(predicate.apply(null));
    }
    
    @Test
    public void testAlwaysFalse() {
        Predicate<String> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply("test"));
        assertFalse(predicate.apply(null));
    }
    
    @Test
    public void testIsNull() {
        Predicate<String> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
        assertFalse(predicate.apply("test"));
    }
    
    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertTrue(predicate.apply("test"));
        assertFalse(predicate.apply(null));
    }
    
    // Add test cases for other methods in Predicates class
    
}