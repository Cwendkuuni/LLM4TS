import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Predicates;

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
    }

    @Test
    public void testNotNull() {
        assertTrue(Predicates.notNull().apply(new Object()));
    }

    @Test
    public void testNot() {
        assertFalse(Predicates.not(Predicates.isNull()).apply(null));
    }

    @Test
    public void testAndIterable() {
        assertFalse(Predicates.and(new ObjectPredicate.ALWAYS_TRUE(), new ObjectPredicate.ALWAYS_FALSE()).apply(new Object()));
    }

    @Test
    public void testAndArray() {
        assertFalse(Predicates.and(new ObjectPredicate.ALWAYS_TRUE(), new ObjectPredicate.ALWAYS_FALSE()).apply(new Object()));
    }

    @Test
    public void testAndFirstSecond() {
        assertFalse(Predicates.and(new ObjectPredicate.ALWAYS_TRUE(), new ObjectPredicate.ALWAYS_FALSE()).apply(new Object()));
    }

    @Test
    public void testOrIterable() {
        assertTrue(Predicates.or(new ObjectPredicate.ALWAYS_TRUE(), new ObjectPredicate.ALWAYS_FALSE()).apply(new Object()));
    }

    @Test
    public void testOrArray() {
        assertTrue(Predicates.or(new ObjectPredicate.ALWAYS_TRUE(), new ObjectPredicate.ALWAYS_FALSE()).apply(new Object()));
    }

    @Test
    public void testOrFirstSecond() {
        assertTrue(Predicates.or(new ObjectPredicate.ALWAYS_TRUE(), new ObjectPredicate.ALWAYS_FALSE()).apply(new Object()));
    }

    // More tests for other methods can be added here

}