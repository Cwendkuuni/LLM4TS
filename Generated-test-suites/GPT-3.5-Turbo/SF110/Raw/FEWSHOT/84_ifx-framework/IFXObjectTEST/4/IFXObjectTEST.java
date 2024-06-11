package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
        
        obj1 = null;
        assertFalse(obj2.equals(obj1));
        
        obj1 = new IFXObject();
        assertFalse(obj1.equals(""));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}\n";
        assertEquals(expected, obj.toString());
    }
}