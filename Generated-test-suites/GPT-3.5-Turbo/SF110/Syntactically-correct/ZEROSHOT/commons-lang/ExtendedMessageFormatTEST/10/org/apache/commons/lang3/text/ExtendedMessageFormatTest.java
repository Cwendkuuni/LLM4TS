package org.apache.commons.lang3.text;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault());
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor3() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", new HashMap<String, FormatFactory>());
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor4() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault(), new HashMap<String, FormatFactory>());
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("");
        assertEquals("", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertTrue(emf1.equals(emf2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNotNull(emf.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNull(emf.getFormat(""));
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals(0, emf.readArgumentIndex("", new ParsePosition(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.parseFormatDescription("", new ParsePosition(0));
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.insertFormats("", new ArrayList<String>()));
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.seekNonWs("", new ParsePosition(0));
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNotNull(emf.next(new ParsePosition(0)));
    }

    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNull(emf.appendQuotedString("", new ParsePosition(0), new StringBuilder(), true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.getQuotedString("", new ParsePosition(0), true);
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertFalse(emf.containsElements(null));
    }
}