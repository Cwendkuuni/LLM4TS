import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<String> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply("Test"));
        assertFalse(alwaysTruePredicate.apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(10));
        assertFalse(alwaysFalsePredicate.apply(null));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertTrue(isNullPredicate.apply(null));
        assertFalse(isNullPredicate.apply("Test"));
    }

    // Add more test cases for other public methods here

    @Test
    public void testAndEdgeCases() {
        Predicate<Integer> isPositive = i -> i > 0;
        Predicate<Integer> isEven = i -> i % 2 == 0;

        Predicate<Integer> combinedPredicate = Predicates.and(isPositive, isEven);
        assertFalse(combinedPredicate.apply(3));
    }

    @Test
    public void testInstanceOfExceptionHandling() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
        assertTrue(instanceOfPredicate.apply("Test"));
        assertFalse(instanceOfPredicate.apply(10)); // Should handle ClassCastException
    }

    // Add more test cases for edge cases and exception handling scenarios

}