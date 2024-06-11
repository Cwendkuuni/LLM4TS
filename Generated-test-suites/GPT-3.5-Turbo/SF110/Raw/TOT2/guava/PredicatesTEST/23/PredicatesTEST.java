import static org.junit.Assert.*;
import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
        assertFalse(predicate.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> predicate = Predicates.notNull();
        assertFalse(predicate.apply(null));
        assertTrue(predicate.apply(new Object()));
    }

    // Add more test cases for other methods in Predicates class

}