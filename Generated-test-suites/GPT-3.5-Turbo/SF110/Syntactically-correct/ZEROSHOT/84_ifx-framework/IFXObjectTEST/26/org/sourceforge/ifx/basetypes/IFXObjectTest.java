package org.sourceforge.ifx.basetypes;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test with same object
        assertTrue(obj1.equals(obj1));
        
        // Test with null object
        assertFalse(obj1.equals(null));
        
        // Test with different class object
        assertFalse(obj1.equals(new String()));
        
        // Test with equal objects
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        // Test toString method
        assertNotNull(obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        
        // Test stringify method
        assertNotNull(obj.stringify(obj, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test with same object
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        
        // Test with null objects
        assertFalse(obj1.doCheckEquals(null, null));
        assertFalse(obj1.doCheckEquals(obj1, null));
        assertFalse(obj1.doCheckEquals(null, obj1));
        
        // Test with different class objects
        assertFalse(obj1.doCheckEquals(obj1, new String()));
        
        // Test with equal objects
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        
        // Test tab method
        assertNotNull(obj.tab(0));
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
    }
}