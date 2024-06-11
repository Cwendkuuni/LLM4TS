package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setUp() {
        extendedMessageFormat = new ExtendedMessageFormat("Hello, {0}, today is {1,date,full}.");
    }

    @Test
    public void testToPattern() {
        String expected = "Hello, {0}, today is {1,date,full}.";
        String actual = extendedMessageFormat.toPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Goodbye, {0}, see you on {1,date,full}.";
        extendedMessageFormat.applyPattern(pattern);

        String expected = "Goodbye, {0}, see you on {1,date,full}.";
        String actual = extendedMessageFormat.toPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat same = new ExtendedMessageFormat("Hello, {0}, today is {1,date,full}.");
        ExtendedMessageFormat different = new ExtendedMessageFormat("Goodbye, {0}, see you on {1,date,full}.");

        assertTrue(extendedMessageFormat.equals(same));
        assertFalse(extendedMessageFormat.equals(different));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat same = new ExtendedMessageFormat("Hello, {0}, today is {1,date,full}.");
        ExtendedMessageFormat different = new ExtendedMessageFormat("Goodbye, {0}, see you on {1,date,full}.");

        assertEquals(extendedMessageFormat.hashCode(), same.hashCode());
        assertNotEquals(extendedMessageFormat.hashCode(), different.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, NumberFormat.getInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, NumberFormat.getInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(new Format[]{NumberFormat.getInstance()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[]{NumberFormat.getInstance()});
    }
}