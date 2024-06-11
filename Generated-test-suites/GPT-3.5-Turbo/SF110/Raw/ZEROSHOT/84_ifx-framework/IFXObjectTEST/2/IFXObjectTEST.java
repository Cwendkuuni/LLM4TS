package org.sourceforge.ifx.basetypes;

import org.junit.Test;

import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj1));
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals("string"));
        assertFalse(obj1.equals(obj2));
        assertTrue(obj1.equals(obj2));
        obj1 = null;
        obj2 = null;
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        String str = obj.stringify(obj, 0);
        assertNotNull(str);
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertFalse(obj1.doCheckEquals(null, obj1));
        assertFalse(obj1.doCheckEquals("string", obj1));
        assertFalse(obj1.doCheckEquals(obj1, null));
        assertFalse(obj1.doCheckEquals(obj1, "string"));
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        obj1 = null;
        obj2 = null;
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        String tab = obj.tab(3);
        assertNotNull(tab);
        assertEquals("      ", tab);
    }
}