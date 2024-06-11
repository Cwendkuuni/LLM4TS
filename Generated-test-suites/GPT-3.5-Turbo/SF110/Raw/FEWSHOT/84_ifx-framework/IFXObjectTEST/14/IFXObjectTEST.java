package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.reflect.Method;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));

        IFXObject obj3 = null;
        assertFalse(obj1.equals(obj3));

        Object obj4 = new Object();
        assertFalse(obj1.equals(obj4));

        IFXObject obj5 = new IFXObject();
        assertFalse(obj1.equals(obj5));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "    }\n";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj2));

        IFXObject obj3 = null;
        assertFalse(obj1.doCheckEquals(obj1, obj3));

        Object obj4 = new Object();
        assertFalse(obj1.doCheckEquals(obj1, obj4));
    }

    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
        assertEquals("", obj.tab(0));
    }
}