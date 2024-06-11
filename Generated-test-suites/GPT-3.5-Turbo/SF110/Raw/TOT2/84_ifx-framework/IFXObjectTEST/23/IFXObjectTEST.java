
import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
    }

    @Test
    public void testEqualsWithNullObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToStringWithException() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() {
                throw new RuntimeException("Exception in toString");
            }
        };
        try {
            obj.toString();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}
