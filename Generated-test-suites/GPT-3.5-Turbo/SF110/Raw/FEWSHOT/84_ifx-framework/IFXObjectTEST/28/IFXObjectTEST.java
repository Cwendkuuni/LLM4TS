
package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertEquals(obj1, obj2);
        
        // Test with different object types
        Object obj3 = new Object();
        
        assertFalse(obj1.equals(obj3));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        // Test expected string representation
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
            "  }\n";
            
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        
        // Test with an object without any methods starting with "get"
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
            "  }\n";
            
        String result = obj.stringify(obj, level);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        
        // Test when obj1 is null and obj2 is not null
        assertFalse(obj1.doCheckEquals(null, obj2));
        
        // Test when obj1 is not null and obj2 is null
        assertFalse(obj1.doCheckEquals(obj1, null));
        
        // Test when obj1 and obj2 have different class names
        assertFalse(obj1.doCheckEquals(obj1, new IFXObjectExtension()));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        
        // Test when level is 0
        String expected = "";
        
        assertEquals(expected, obj.tab(0));
        
        // Test when level is 1
        expected = "  ";
        
        assertEquals(expected, obj.tab(1));
        
        // Test when level is 2
        expected += "  ";
        
        assertEquals(expected, obj.tab(2));
    }
    
    // Helper class for testing
    private class IFXObjectExtension extends IFXObject {
        
    }
}
