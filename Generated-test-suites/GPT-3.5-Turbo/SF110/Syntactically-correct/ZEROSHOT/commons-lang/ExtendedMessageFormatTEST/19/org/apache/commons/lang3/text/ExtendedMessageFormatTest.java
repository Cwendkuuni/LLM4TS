package org.apache.commons.lang3.text;

import java.text.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        assertEquals("Hello {0}!", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        format.applyPattern("Hi {0}!");
        assertEquals("Hi {0}!", format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormatFormatFactory(new SimpleDateFormat("yyyy-MM-dd")));
        ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {0,date}!", registry);
        format.applyPattern("Today is {0,date}, tomorrow is {1,date}!");
        assertEquals("Today is {0,date}, tomorrow is {1,date}!", format.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}!");
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}!");
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        format.setFormats(new Format[]{new SimpleDateFormat("yyyy-MM-dd")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        format.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("yyyy-MM-dd")});
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", null);
        assertNull(format.getFormat("date"));
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ParsePosition pos = new ParsePosition(7);
        assertEquals(0, format.readArgumentIndex("Hello {0}!", pos));
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ParsePosition pos = new ParsePosition(7);
        assertEquals("", format.parseFormatDescription("Hello {0}!", pos));
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("date");
        assertEquals("Hello {0,date}!", format.insertFormats("Hello {0}!", customPatterns));
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ParsePosition pos = new ParsePosition(6);
        format.seekNonWs("Hello {0}!", pos);
        assertEquals(7, pos.getIndex());
    }

    @Test
    public void testNext() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ParsePosition pos = new ParsePosition();
        format.next(pos);
        assertEquals(1, pos.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ParsePosition pos = new ParsePosition(7);
        format.appendQuotedString("Hello '{0}!", pos, new StringBuilder(), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        ParsePosition pos = new ParsePosition(7);
        format.getQuotedString("Hello '{0}!", pos, false);
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        assertFalse(format.containsElements(null));
        assertFalse(format.containsElements(Collections.emptyList()));
        assertTrue(format.containsElements(Arrays.asList("date")));
    }
}