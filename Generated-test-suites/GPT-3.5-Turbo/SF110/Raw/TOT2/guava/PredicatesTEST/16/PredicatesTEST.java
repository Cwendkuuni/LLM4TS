import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        assertTrue(isNullPredicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();
        assertTrue(notNullPredicate.apply(new Object()));
    }

    // Add more test cases for other methods

}