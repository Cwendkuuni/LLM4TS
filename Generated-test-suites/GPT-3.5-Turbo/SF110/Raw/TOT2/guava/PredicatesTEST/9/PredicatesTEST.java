import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        // Test when input is any object
        assertTrue(Predicates.alwaysTrue().apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        // Test when input is any object
        assertFalse(Predicates.alwaysFalse().apply(new Object()));
    }

    @Test
    public void testIsNull() {
        // Test when input is null
        assertTrue(Predicates.isNull().apply(null));
        // Test when input is not null
        assertFalse(Predicates.isNull().apply(new Object()));
    }

    // Add test methods for other public methods in the Predicates class

}