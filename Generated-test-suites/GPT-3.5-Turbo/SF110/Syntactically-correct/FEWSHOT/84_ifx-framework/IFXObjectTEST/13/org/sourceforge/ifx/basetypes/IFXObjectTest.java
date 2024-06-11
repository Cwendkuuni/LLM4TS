package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;

import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj1));
        assertTrue(obj2.equals(obj2));
        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));
    }

    @Test
    public void testEqualsDifferentClass() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsNull() {
        IFXObject obj1 = new IFXObject();
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n}", obj.toString());
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n}", obj.stringify(obj, 0));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        assertEquals("", obj.tab(0));
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
    }
}