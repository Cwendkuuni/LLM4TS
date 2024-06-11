import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertEquals(Predicates.alwaysTrue().apply(null), true);
        assertEquals(Predicates.alwaysTrue().apply("test"), true);
    }

    @Test
    public void testAlwaysFalse() {
        assertEquals(Predicates.alwaysFalse().apply(null), false);
        assertEquals(Predicates.alwaysFalse().apply("test"), false);
    }

    @Test
    public void testIsNull() {
        assertEquals(Predicates.isNull().apply(null), true);
        assertEquals(Predicates.isNull().apply("test"), false);
    }

    @Test
    public void testNotNull() {
        assertEquals(Predicates.notNull().apply(null), false);
        assertEquals(Predicates.notNull().apply("test"), true);
    }

    // Add more test methods for other methods in Predicates class

}