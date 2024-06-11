
import org.junit.*;
import static org.junit.Assert.*;

public class IFXObjectTest {
    
    IFXObject obj = new IFXObject();
    
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n"
            + "}";
            
        assertEquals(expected, obj.toString());
    }
    
}
