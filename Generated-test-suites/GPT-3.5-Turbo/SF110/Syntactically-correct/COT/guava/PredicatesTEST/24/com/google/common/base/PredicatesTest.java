import static org.junit.Assert.*;
import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(10));
        assertTrue(predicate.apply(-5));
        assertTrue(predicate.apply(0));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(10));
        assertFalse(predicate.apply(-5));
        assertFalse(predicate.apply(0));
    }

    @Test
    public void testIsNull() {
        Predicate<String> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
        assertFalse(predicate.apply("hello"));
    }

    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertFalse(predicate.apply(null));
        assertTrue(predicate.apply("hello"));
    }

    @Test
    public void testNot() {
        Predicate<Integer> predicate = Predicates.not(new Predicate<Integer>() {
            public boolean apply(Integer i) {
                return i % 2 == 0;
            }
        });
        assertTrue(predicate.apply(1));
        assertFalse(predicate.apply(2));
    }

    // More test cases for other public methods...

}