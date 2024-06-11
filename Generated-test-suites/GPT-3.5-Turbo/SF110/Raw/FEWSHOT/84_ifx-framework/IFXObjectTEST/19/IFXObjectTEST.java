package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertEquals(false, obj1.equals(null));
        assertEquals(true, obj1.equals(obj1));
        assertEquals(true, obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertTrue(result);
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.stringify(obj, level));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 2;
        String expected = "    ";
        assertEquals(expected, obj.tab(level));
    }
}