import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PredicatesTest {

    private List<String> stringList;

    @Before
    public void setUp() {
        stringList = new ArrayList<>(Arrays.asList("apple", "banana", null, "orange", "grape"));
    }

    @Test
    public void testAlwaysTrue() {
        // Test typical use case
        assertEquals(true, Predicates.alwaysTrue().apply("test"));

        // Test edge case
        assertEquals(true, Predicates.alwaysTrue().apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        // Test typical use case
        assertEquals(false, Predicates.alwaysFalse().apply("test"));

        // Test edge case
        assertEquals(false, Predicates.alwaysFalse().apply(null));
    }

    @Test
    public void testIsNull() {
        // Test typical use case
        assertEquals(false, Predicates.isNull().apply("test"));

        // Test edge cases
        assertEquals(true, Predicates.isNull().apply(null));
        assertEquals(false, Predicates.isNull().apply(123));
    }

    @Test
    public void testNotNull() {
        // Test typical use case
        assertEquals(true, Predicates.notNull().apply("test"));

        // Test edge cases
        assertEquals(false, Predicates.notNull().apply(null));
        assertEquals(true, Predicates.notNull().apply(123));
    }

    @Test
    public void testNot() {
        // Test typical use case
        Predicate<String> predicate = Predicates.alwaysTrue();
        Predicate<String> notPredicate = Predicates.not(predicate);
        assertEquals(false, notPredicate.apply("test"));

        // Test edge cases
        predicate = Predicates.alwaysFalse();
        notPredicate = Predicates.not(predicate);
        assertEquals(true, notPredicate.apply("test"));

        predicate = Predicates.notNull();
        notPredicate = Predicates.not(predicate);
        assertEquals(false, notPredicate.apply(null));
    }

    // Test other methods...

}