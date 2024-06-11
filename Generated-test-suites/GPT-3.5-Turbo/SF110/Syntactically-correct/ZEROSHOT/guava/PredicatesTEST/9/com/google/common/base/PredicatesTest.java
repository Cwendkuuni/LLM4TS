import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(null));
        assertFalse(Predicates.alwaysTrue().apply("test"));
        assertFalse(Predicates.alwaysTrue().apply(123));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply(null));
        assertFalse(Predicates.alwaysFalse().apply("test"));
        assertFalse(Predicates.alwaysFalse().apply(123));
    }

    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply("test"));
        assertFalse(Predicates.isNull().apply(123));
    }

    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
        assertTrue(Predicates.notNull().apply("test"));
        assertTrue(Predicates.notNull().apply(123));
    }

    // Add more test methods for other methods in the Predicates class

}