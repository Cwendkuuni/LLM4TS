package org.apache.commons.lang3.text;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello World");
        assertEquals("Hello World", emf.toPattern());
    }

    @Test
    public void testConstructor_2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.US);
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testConstructor_3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", registry);
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testConstructor_4() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormatMessageFormat());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Today is {0,date}", Locale.US, registry);
        assertEquals("Today is {0,date}", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Greetings {0}");
        assertEquals("Greetings {0}", emf.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormat(0, new DecimalFormat("#,###"));
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormatByArgumentIndex(0, new DecimalFormat("#,###"));
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormats(new Format[] {new DecimalFormat("#,###")});
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormatsByArgumentIndex(new Format[] {new DecimalFormat("#,###")});
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("Greetings {0}");

        assertTrue(emf1.equals(emf2));
        assertFalse(emf1.equals(emf3));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}");

        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        try {
            emf.parseFormatDescription("invalid", new ParsePosition(0));
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("date");

        String pattern = emf.insertFormats("Hello {0,date}", customPatterns);
        assertEquals("Hello {0,date}", pattern);
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        ParsePosition pos = new ParsePosition(0);
        emf.seekNonWs("  Hello", pos);
        assertEquals(2, pos.getIndex());
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        ParsePosition pos = new ParsePosition(0);
        pos.setIndex(2);
        emf.next(pos);
        assertEquals(3, pos.getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        ParsePosition pos = new ParsePosition(0);
        StringBuilder sb = new StringBuilder();
        emf.appendQuotedString("'quoted'", pos, sb, true);
        assertEquals("quoted", sb.toString());
    }

    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        ParsePosition pos = new ParsePosition(0);
        emf.getQuotedString("'quoted'", pos, true);
        assertEquals(8, pos.getIndex());
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello");
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("element");

        assertFalse(emf.containsElements(null));
        assertFalse(emf.containsElements(list1));
        assertTrue(emf.containsElements(list2));
    }

}