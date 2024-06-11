import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply(new Object()));
    }

    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply(new Object()));
    }

    @Test
    public void testNotNull() {
        assertTrue(Predicates.notNull().apply(new Object()));
        assertFalse(Predicates.notNull().apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Object> notAlwaysTrue = Predicates.not(alwaysTruePredicate);
        assertFalse(notAlwaysTrue.apply(new Object()));
    }

    @Test
    public void testAnd() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
        Predicate<Object> andPredicate = Predicates.and(alwaysTruePredicate, alwaysFalsePredicate);
        assertFalse(andPredicate.apply(new Object()));
    }

    @Test
    public void testOr() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
        Predicate<Object> orPredicate = Predicates.or(alwaysTruePredicate, alwaysFalsePredicate);
        assertTrue(orPredicate.apply(new Object()));
    }

    @Test
    public void testEqualTo() {
        Predicate<Object> equalToPredicate = Predicates.equalTo("test");
        assertTrue(equalToPredicate.apply("test"));
        assertFalse(equalToPredicate.apply("different"));
    }

    // Test edge cases and exception handling scenarios if necessary

}