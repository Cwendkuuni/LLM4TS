import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertTrue(isNull.apply(null));
        assertFalse(isNull.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertTrue(notNull.apply(new Object()));
        assertFalse(notNull.apply(null));
    }

    // Additional tests for edge cases and exceptions

    @Test
    public void testAndWithEmptyList() {
        Predicate<Object> and = Predicates.and(Collections.emptyList());
        assertFalse(and.apply(new Object()));
    }

    @Test
    public void testAndWithNullPredicate() {
        Predicate<Object> and = Predicates.and((Predicate<Object>) null);
        assertFalse(and.apply(new Object()));
    }

    @Test
    public void testComposeWithNullPredicate() {
        Predicate<Object> predicate = obj -> obj == null;
        Predicate<Object> compose = Predicates.compose(null, new Object());
        assertFalse(compose.apply(new Object()));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<CharSequence> containsPatternFromString = Predicates.containsPattern("abc");
        assertTrue(containsPatternFromString.apply("abc"));
        assertFalse(containsPatternFromString.apply("def"));
    }

}