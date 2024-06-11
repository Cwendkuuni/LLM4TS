import static org.junit.Assert.*;
import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        // Typical use case
        assertTrue(Predicates.alwaysTrue().apply("test"));
        // Edge case
        assertFalse(Predicates.alwaysTrue().apply(null));
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
        assertTrue(Predicates.notNull().apply(null));
    }
    
    // Add test cases for other methods as well

}