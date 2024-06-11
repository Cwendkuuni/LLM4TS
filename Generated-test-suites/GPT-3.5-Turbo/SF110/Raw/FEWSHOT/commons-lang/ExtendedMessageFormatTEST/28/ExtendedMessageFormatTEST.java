package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat format;

    @Before
    public void setUp() {
        format = new ExtendedMessageFormat("");
    }

    @Test
    public void testConstructor() {
        format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());

        format = new ExtendedMessageFormat("", null);
        assertEquals("", format.toPattern());

        format = new ExtendedMessageFormat("", null, null);
        assertEquals("", format.toPattern());

        format = new ExtendedMessageFormat("", null, null);
        assertEquals("", format.toPattern());
    }

    @Test
    public void testToPattern() {
        format.applyPattern("{0,number,integer}");
        assertEquals("{0,number,integer}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        format.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        format.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        format.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        format.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        format.applyPattern("{0,number,integer}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        assertFalse(format.equals(format2));

        format2.applyPattern("{0,number,integer}");
        assertTrue(format.equals(format2));

        assertTrue(format.equals(format));
        assertFalse(format.equals(null));
        assertFalse(format.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        format.applyPattern("{0,number,integer}");
        assertEquals(1154046435, format.hashCode());
    }
}