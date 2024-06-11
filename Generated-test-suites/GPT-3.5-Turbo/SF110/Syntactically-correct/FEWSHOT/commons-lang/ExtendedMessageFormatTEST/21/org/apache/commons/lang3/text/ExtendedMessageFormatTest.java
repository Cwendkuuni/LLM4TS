package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.text.*;
import java.util.*;

public class ExtendedMessageFormatTest {
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        assertEquals("Hello {0}!", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        format.applyPattern("Hello {0}, welcome to {1}!");
        assertEquals("Hello {0}, welcome to {1}!", format.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        try {
            format.setFormat(0, NumberFormat.getInstance());
        } catch (UnsupportedOperationException e) {
            assertEquals("java.lang.UnsupportedOperationException", e.toString());
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        try {
            format.setFormatByArgumentIndex(0, DateFormat.getInstance());
        } catch (UnsupportedOperationException e) {
            assertEquals("java.lang.UnsupportedOperationException", e.toString());
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        try {
            format.setFormats(new Format[]{NumberFormat.getInstance()});
        } catch (UnsupportedOperationException e) {
            assertEquals("java.lang.UnsupportedOperationException", e.toString());
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        try {
            format.setFormatsByArgumentIndex(new Format[]{DateFormat.getInstance()});
        } catch (UnsupportedOperationException e) {
            assertEquals("java.lang.UnsupportedOperationException", e.toString());
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}!");
        assertEquals(true, format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}!");
        assertEquals(format1.hashCode(), format2.hashCode());
    }
}