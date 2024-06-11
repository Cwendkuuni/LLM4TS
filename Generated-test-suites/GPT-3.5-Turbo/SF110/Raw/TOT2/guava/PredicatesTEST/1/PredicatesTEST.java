import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(10));
        assertTrue(predicate.apply(0));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<String> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply("test"));
        assertFalse(predicate.apply(null));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
        assertFalse(predicate.apply("test"));
    }
    
    // More test cases for other methods should be implemented here
    
}