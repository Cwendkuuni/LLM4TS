package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));
        
        obj1 = null;
        assertFalse(obj2.equals(obj1));
        
        obj2 = null;
        assertFalse(obj1.equals(obj2));
        
        assertFalse(obj1.equals("Not an IFXObject"));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\n}" ;
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "\n}" ;
        assertEquals(expected, obj.stringify(obj, level));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj = new IFXObject();
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        assertTrue(obj.doCheckEquals(obj, obj));
        assertFalse(obj.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();

        String expected = "";
        assertEquals(expected, obj.tab(0));
    }
}