package org.sourceforge.ifx.basetypes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IFXObjectTest {
    private IFXObject obj;

    @Before
    public void setUp() {
        obj = new IFXObject();
    }

    @After
    public void tearDown() {
        obj = null;
    }

    @Test
    public void testEquals() {
        IFXObject equalObj = new IFXObject();
        assertTrue(obj.equals(equalObj));

        equalObj = null;
        assertFalse(obj.equals(equalObj));

        Object differentObj = new Object();
        assertFalse(obj.equals(differentObj));
    }

    @Test
    public void testToString() {
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  \n" +
                "}";
        assertEquals(expectedString, obj.toString());
    }

    @Test
    public void testStringify() {
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  \n" +
                "}";
        assertEquals(expectedString, obj.stringify(obj, 0));
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject equalObj = new IFXObject();
        assertTrue(obj.doCheckEquals(obj, equalObj));

        IFXObject unequalObj = new IFXObject();
        unequalObj.toString();
        assertFalse(obj.doCheckEquals(obj, unequalObj));

        unequalObj = null;
        assertFalse(obj.doCheckEquals(obj, unequalObj));
    }

    @Test
    public void testTab() {
        String expectedString = "";
        assertEquals(expectedString, obj.tab(0));

        expectedString = "  ";
        assertEquals(expectedString, obj.tab(1));

        expectedString = "        ";
        assertEquals(expectedString, obj.tab(4));
    }
}