import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.equals(obj1));
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(new Integer(5)));
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.toString());
        assertTrue(obj.toString().contains("IFXObject"));
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        
        assertEquals(obj.stringify(obj, 0), obj.toString());
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.tab(0));
        assertEquals("", obj.tab(0));
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
    }
}