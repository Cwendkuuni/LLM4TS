package org.sourceforge.ifx.basetypes;

import org.junit.Test;

import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals_sameObject() {
        IFXObject obj = new IFXObject();
        assertTrue(obj.equals(obj));
    }

    @Test
    public void testEquals_nullObject() {
        IFXObject obj = new IFXObject();
        assertFalse(obj.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        IFXObject obj = new IFXObject();
        assertFalse(obj.equals(new Object()));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  \n" +
                "}";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify_nullObject() {
        IFXObject obj = null;
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = NULL";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testStringify_exception() {
        IFXObject obj = new IFXObject();
        obj.setStringify(null, 0);  // call with null object to cause exception
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = Exception! java.lang.NullPointerException";
        assertEquals(expected, obj.toString());
    }

    // Additional test cases can be added here for complete coverage

}