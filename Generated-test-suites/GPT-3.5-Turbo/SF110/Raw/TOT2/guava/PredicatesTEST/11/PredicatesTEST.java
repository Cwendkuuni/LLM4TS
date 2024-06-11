import org.junit.Test;
import static org.junit.Assert.*;
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
        assertFalse(Predicates.isNull().apply(new Object()));
    }

    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
        assertTrue(Predicates.notNull().apply(new Object()));
    }

    @Test
    public void testNot() {
        assertFalse(Predicates.not(Predicates.isNull()).apply(null));
        assertTrue(Predicates.not(Predicates.isNull()).apply(new Object()));
    }

    // Additional test cases for other methods can be added here

}