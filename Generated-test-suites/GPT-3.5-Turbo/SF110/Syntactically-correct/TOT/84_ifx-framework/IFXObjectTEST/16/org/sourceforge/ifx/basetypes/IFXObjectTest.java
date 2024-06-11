
import static org.junit.Assert.*;

import org.junit.Test;

public class IFXObjectTest {

    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    IFXObject obj3 = null;

    @Test
    public void testEquals() {
        // Typical use case
        assertTrue(obj1.equals(obj2));

        // Edge case: null object
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testToString() {
        // Typical use case
        assertNotNull(obj1.toString());

        // Error scenario: null object
        assertNull(obj3.toString());
    }
}

