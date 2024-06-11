package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        // Create test objects
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test equal objects
        assertEquals(true, obj1.equals(obj1));
        
        // Test different objects
        assertEquals(false, obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        // Create test objects
        IFXObject obj = new IFXObject();
        
        // Test toString method
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n}\n", obj.toString());
    }
    
    @Test
    public void testStringify() {
        // Create test object
        IFXObject obj = new IFXObject();
        
        // Test stringify method
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n}\n", obj.stringify(obj, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        // Create test objects
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test doCheckEquals method
        assertEquals(true, obj1.doCheckEquals(obj1, obj1));
        assertEquals(false, obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        // Create test object
        IFXObject obj = new IFXObject();
        
        // Test tab method
        assertEquals("", obj.tab(0));
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
    }
}