import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply(null));
    }

    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply(""));
    }

    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
        assertTrue(Predicates.notNull().apply(""));
    }

    @Test
    public void testNot() {
        Predicate<Object> truePredicate = Predicates.alwaysTrue();
        Predicate<Object> falsePredicate = Predicates.alwaysFalse();

        assertFalse(Predicates.not(truePredicate).apply(null));
        assertTrue(Predicates.not(falsePredicate).apply(null));
    }

    // TODO: Add tests for other methods in the Predicates class

}