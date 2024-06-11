import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.equals(obj2);
        assertTrue(result);
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String result = obj.toString();
        assertNotNull(result);
    }

    @Test
    public void testEqualsNullObject() {
        IFXObject obj1 = new IFXObject();
        boolean result = obj1.equals(null);
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        boolean result = obj1.equals(obj2);
        assertFalse(result);
    }

    @Test
    public void testToStringArrayObject() {
        IFXObject obj = new IFXObject();
        Object[] array = new Object[1];
        array[0] = new IFXObject();
        obj.setArray(array);
        String result = obj.toString();
        assertNotNull(result);
    }

    @Test
    public void testToStringException() {
        IFXObject obj = new IFXObject();
        obj.setException(true);
        String result = obj.toString();
        assertEquals("Exception!", result.substring(0, 10));
    }
}