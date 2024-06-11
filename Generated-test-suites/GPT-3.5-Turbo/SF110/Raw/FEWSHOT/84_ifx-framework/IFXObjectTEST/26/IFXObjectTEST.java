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
        
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));
        
        Object obj3 = new Object();
        assertFalse(obj1.equals(obj3));
        
    }
    
    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" + 
                          "  }\n";
        assertEquals(expected, obj1.toString());
        
    }
    
    @Test
    public void testStringify() {
        IFXObject obj1 = new IFXObject();
        int level = 0;
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" + 
                          "  }\n";
        assertEquals(expected, obj1.stringify(obj1, level));
        
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj1 = new IFXObject();
        int level = 0;
        String expected = "";
        assertEquals(expected, obj1.tab(level));
        
    }

}