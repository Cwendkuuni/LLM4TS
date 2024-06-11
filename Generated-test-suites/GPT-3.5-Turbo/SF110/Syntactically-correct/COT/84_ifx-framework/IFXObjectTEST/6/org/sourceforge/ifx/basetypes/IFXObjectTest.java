import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    @Test
    public void testEquals_selfObject_returnsTrue() {
        IFXObject obj = new IFXObject();
        assertTrue(obj.equals(obj));
    }
    
    @Test
    public void testEquals_nullObject_returnsFalse() {
        IFXObject obj = new IFXObject();
        assertFalse(obj.equals(null));
    }
    
    @Test
    public void testEquals_differentClassObject_returnsFalse() {
        IFXObject obj = new IFXObject();
        assertFalse(obj.equals("string"));
    }
    
    @Test
    public void testToString_returnsStringRepresentation() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
        assertTrue(obj.toString().contains("IFXObject"));
    }
}