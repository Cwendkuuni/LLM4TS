package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Array;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.equals(obj2));
        
        obj2 = obj1;
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}\n";
        assertEquals(expected, obj1.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj1 = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}\n";
        assertEquals(expected, obj1.stringify(obj1, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj1 = new IFXObject();
        String expected = "";
        assertEquals(expected, obj1.tab(0));
        
        expected = "  ";
        assertEquals(expected, obj1.tab(1));
        
        expected = "    ";
        assertEquals(expected, obj1.tab(2));
    }
}