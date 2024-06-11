package org.sourceforge.ifx.basetypes;

import java.lang.reflect.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IFXObjectTest {

    private IFXObject obj1;
    private IFXObject obj2;

    @Before
    public void setUp() {
        obj1 = new IFXObject();
        obj2 = new IFXObject();
    }

    @Test
    public void testEquals() {
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                          "}";
        assertEquals(expected, obj1.toString());
    }

    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                          "}";
        assertEquals(expected, obj.stringify(obj, level));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject thisObj = new IFXObject();
        IFXObject thatObj = new IFXObject();
        assertTrue(thisObj.doCheckEquals(thisObj, thatObj));
    }

    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String expected = "";
        assertEquals(expected, obj.tab(level));
    }
}