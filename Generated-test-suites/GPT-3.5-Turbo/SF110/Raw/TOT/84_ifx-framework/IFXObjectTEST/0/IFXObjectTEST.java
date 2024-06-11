package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        // Test typical use case - objects are equal
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
        
        // Test typical use case - objects are not equal
        assertFalse(obj1.equals(null));
        
        // Test edge case - objects are null
        obj1 = null;
        obj2 = null;
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        // Test typical use case
        IFXObject obj = new IFXObject();
        String expectedOutput = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        assertEquals(expectedOutput, obj.toString());
        
        // Test edge case - object is null
        obj = null;
        assertNull(obj.toString());
    }
    
    @Test
    public void testStringify() {
        // Test typical use case
        IFXObject obj = new IFXObject();
        int level = 0;
        String expectedOutput = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        assertEquals(expectedOutput, obj.stringify(obj, level));
        
        // Test edge case - object is null
        obj = null;
        level = 0;
        assertNull(obj.stringify(obj, level));
    }
    
    @Test
    public void testDoCheckEquals() {
        // Test typical use case - objects are equal
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        
        // Test typical use case - objects are not equal
        obj1 = new IFXObject();
        obj2 = null;
        assertFalse(obj1.doCheckEquals(obj1, obj2));
        
        // Test edge case - objects are null
        obj1 = null;
        obj2 = null;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        // Test typical use case
        IFXObject obj = new IFXObject();
        int level = 3;
        String expectedOutput = "      ";
        assertEquals(expectedOutput, obj.tab(level));
        
        // Test edge case - level is 0
        level = 0;
        expectedOutput = "";
        assertEquals(expectedOutput, obj.tab(level));
    }
}