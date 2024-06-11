package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));

        obj1 = null;
        obj2 = new IFXObject();
        assertFalse(obj2.equals(obj1));
        
        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.equals("test"));
        
        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.equals(null));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.equals(new Object()));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  }";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  }";
        assertEquals(expected, obj.stringify(obj, 0));
        
        obj = new IFXObject();
        expected = "Exception! java.lang.NullPointerException";
        assertEquals(expected, obj.stringify(null, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj2));

        obj1 = null;
        obj2 = new IFXObject();
        assertFalse(obj2.doCheckEquals(obj1, obj2));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj2));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, "test"));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, null));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, new Object()));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(null, null));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(null, obj2));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, null));

        obj1 = new IFXObject();
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(null, "test"));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 3;
        String expected = "      ";
        assertEquals(expected, obj.tab(level));
        
        level = 0;
        expected = "";
        assertEquals(expected, obj.tab(level));
    }
}