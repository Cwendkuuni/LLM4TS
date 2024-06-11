import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply("Test"));
        assertTrue(Predicates.alwaysTrue().apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply("Test"));
        assertFalse(Predicates.alwaysFalse().apply(null));
    }

    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply("Test"));
    }

    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
        assertTrue(Predicates.notNull().apply("Test"));
    }

    @Test
    public void testNot() {
        Predicate<String> isUpperCase = s -> Character.isUpperCase(s.charAt(0));
        Predicate<String> notUpperCase = Predicates.not(isUpperCase);
        assertTrue(notUpperCase.apply("test"));
        assertFalse(notUpperCase.apply("Test"));
    }

    // Continue writing test cases for the remaining methods


}