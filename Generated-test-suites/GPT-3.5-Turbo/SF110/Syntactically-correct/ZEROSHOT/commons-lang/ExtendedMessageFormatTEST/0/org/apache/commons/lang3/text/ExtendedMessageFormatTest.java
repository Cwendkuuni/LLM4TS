package org.apache.commons.lang3.text;

import org.junit.*;
import static org.junit.Assert.*;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setUp() {
        extendedMessageFormat = new ExtendedMessageFormat("");
    }

    @After
    public void tearDown() {
        extendedMessageFormat = null;
    }

    @Test
    public void testToPattern() {
        extendedMessageFormat.applyPattern("Pattern");
        String expected = "Pattern";
        String actual = extendedMessageFormat.toPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testApplyPattern() {
        extendedMessageFormat.applyPattern("Pattern");
        String expected = "Pattern";
        String actual = extendedMessageFormat.toPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetFormat() {
        try {
            extendedMessageFormat.setFormat(0, new DecimalFormat());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, new DecimalFormat());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetFormats() {
        try {
            extendedMessageFormat.setFormats(new Format[]{new DecimalFormat()});
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("Pattern");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("Pattern");
        assertTrue(extendedMessageFormat1.equals(extendedMessageFormat2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("Pattern");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("Pattern");
        assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat2.hashCode());
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("format", new DecimalFormatFactory());
        extendedMessageFormat = new ExtendedMessageFormat("", registry);
        Format expected = new DecimalFormat();
        Format actual = extendedMessageFormat.getFormat("format");
        assertEquals(expected, actual);
    }

    @Test
    public void testReadArgumentIndex() {
        ParsePosition pos = new ParsePosition(0);
        String pattern = "";
        int expected = 0;
        int actual = extendedMessageFormat.readArgumentIndex(pattern, pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFormatDescription() {
        ParsePosition pos = new ParsePosition(0);
        String pattern = "";
        String expected = "";
        String actual = extendedMessageFormat.parseFormatDescription(pattern, pos);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertFormats() {
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("pattern");
        customPatterns.add("");
        String pattern = "Pattern";
        String expected = "Pattern";
        String actual = extendedMessageFormat.insertFormats(pattern, customPatterns);
        assertEquals(expected, actual);
    }

    @Test
    public void testSeekNonWs() {
        String pattern = " Pattern";
        ParsePosition pos = new ParsePosition(0);
        extendedMessageFormat.seekNonWs(pattern, pos);
        int expected = 1;
        int actual = pos.getIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testNext() {
        ParsePosition pos = new ParsePosition(0);
        extendedMessageFormat.next(pos);
        int expected = 1;
        int actual = pos.getIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendQuotedString() {
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        boolean escapingOn = true;
        StringBuilder expected = new StringBuilder("'\'");
        StringBuilder actual = extendedMessageFormat.appendQuotedString("''", pos, appendTo, escapingOn);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testContainsElements() {
        ArrayList<String> coll = new ArrayList<>();
        assertFalse(extendedMessageFormat.containsElements(coll));
        coll.add(null);
        assertFalse(extendedMessageFormat.containsElements(coll));
        coll.add("name");
        assertTrue(extendedMessageFormat.containsElements(coll));
    }

    private class DecimalFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DecimalFormat();
        }
    }
}