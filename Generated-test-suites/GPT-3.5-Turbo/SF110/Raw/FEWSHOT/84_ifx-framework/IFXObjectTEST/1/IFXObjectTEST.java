package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // test with same objects
        assertTrue(obj1.equals(obj2));
        
        // test with different class
        Object obj3 = new Object();
        assertFalse(obj1.equals(obj3));
    }
    
    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        
        // test with empty object
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj1.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj1 = new IFXObject();
        
        // test with empty object
        String expected = "{}";
        assertEquals(expected, obj1.stringify(obj1, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // test with same objects
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        
        // test with different class
        Object obj3 = new Object();
        assertFalse(obj1.doCheckEquals(obj1, obj3));
    }
    
    @Test
    public void testTab() {
        IFXObject obj1 = new IFXObject();
        
        // test with level 0
        String expected = "";
        assertEquals(expected, obj1.tab(0));
        
        // test with level 1
        expected = "  ";
        assertEquals(expected, obj1.tab(1));
        
        // test with level 2
        expected = "    ";
        assertEquals(expected, obj1.tab(2));
    }
}