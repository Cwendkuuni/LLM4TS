import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        // Typical use case
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
        
        // Edge case
        IFXObject obj3 = null;
        assertFalse(obj1.equals(obj3));
    }
    
    @Test
    public void testToString() {
        // Typical use case
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.toString());
        
        // Edge case
        IFXObject[] array = {new IFXObject(), new IFXObject()};
        obj = new IFXObject();
        obj.setArray(array);
        expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  array = [\n    [0]:org.sourceforge.ifx.basetypes.IFXObject = {}\n  ,\n    [1]:org.sourceforge.ifx.basetypes.IFXObject = {}\n  ]\n}";
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testDoCheckEquals() {
        // Typical use case
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        
        // Edge case
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = null;
        assertFalse(obj3.doCheckEquals(obj3, obj4));
    }
    
    @Test
    public void testTab() {
        // Typical use case
        IFXObject obj = new IFXObject();
        assertEquals("    ", obj.tab(2));
    }
}