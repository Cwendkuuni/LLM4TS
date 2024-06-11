import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        assertEquals(true, new IFXObject().equals(new IFXObject()));
    }
    
    @Test
    public void testToString() {
        assertNotNull(new IFXObject().toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject object = new IFXObject();
        assertEquals("", object.stringify(object, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject object = new IFXObject();
        assertEquals(true, object.doCheckEquals(object, object));
    }
    
    @Test
    public void testTab() {
        IFXObject object = new IFXObject();
        assertEquals("", object.tab(0));
    }
}