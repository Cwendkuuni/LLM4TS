import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testStringify() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.stringify(obj, 0));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject thisObj = new IFXObject();
        IFXObject thatObj = new IFXObject();
        
        assertTrue(thisObj.doCheckEquals(thisObj, thatObj));
    }

    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.tab(2));
    }
}