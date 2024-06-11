import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.base.Predicates;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(new Object()));
        assertFalse(Predicates.alwaysTrue().apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply(new Object()));
        assertTrue(Predicates.alwaysFalse().apply(null));
    }

    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply(new Object()));
    }
    
    // Additional test cases for other methods can be added here
    
}