package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test same object
        assertTrue(obj1.equals(obj1));
        
        // Test different objects
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n"
                        + "  \n"
                        + "}";
                        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n"
                        + "  \n"
                        + "}";
                        
        assertEquals(expected, obj.stringify(obj, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test same object
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        
        // Test different objects
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        
        String expected = "    ";
        
        assertEquals(expected, obj.tab(2));
    }
}