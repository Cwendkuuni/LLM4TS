import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals_True() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_False() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        
        assertNotNull(obj.stringify(obj, level));
    }
    
    @Test
    public void testDoCheckEquals_True() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testDoCheckEquals_False() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 0;
        
        assertNotNull(obj.tab(level));
    }
}