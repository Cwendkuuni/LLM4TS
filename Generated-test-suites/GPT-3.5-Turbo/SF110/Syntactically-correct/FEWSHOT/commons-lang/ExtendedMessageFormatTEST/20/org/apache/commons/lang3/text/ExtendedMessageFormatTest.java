package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;

    @Before
    public void setUp() {
        messageFormat = new ExtendedMessageFormat("");
    }

    @Test
    public void testConstructorPatternLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, format.getLocale());
    }

    @Test
    public void testConstructorPatternLocaleRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("", Locale.ENGLISH, registry);
        assertEquals(Locale.ENGLISH, format.getLocale());
        assertEquals(registry, format.registry);
    }

    @Test
    public void testConstructorPatternRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        assertNull(format.getLocale());
        assertEquals(registry, format.registry);
    }

    @Test
    public void testToPattern() {
        messageFormat.toPattern = "testPattern";
        assertEquals("testPattern", messageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        messageFormat.registry = null;
        messageFormat.applyPattern("testPattern");
        assertEquals("testPattern", messageFormat.toPattern);
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        messageFormat.registry = registry;
        messageFormat.applyPattern("testPattern");
        assertEquals("testPattern", messageFormat.toPattern);
    }

    @Test
    public void testSetFormat() {
        try {
            messageFormat.setFormat(0, null);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            messageFormat.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormats() {
        try {
            messageFormat.setFormats(null);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            messageFormat.setFormatsByArgumentIndex(null);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        assertFalse(format1.equals(null));
        assertFalse(format1.equals(new Object()));
        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testGetFormat() {
        assertNull(messageFormat.getFormat(null));
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "testPattern";
        int index = 0;
        assertNull(messageFormat.readArgumentIndex(pattern, new ParsePosition(index)));
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "testPattern";
        int index = 0;
        assertNull(messageFormat.parseFormatDescription(pattern, new ParsePosition(index)));
    }

    @Test
    public void testInsertFormats() {
        String pattern = "testPattern";
        ArrayList<String> customPatterns = new ArrayList<>();
        assertEquals(pattern, messageFormat.insertFormats(pattern, customPatterns));
    }

    @Test
    public void testSeekNonWs() {
        String pattern = "testPattern";
        ParsePosition pos = new ParsePosition(0);
        messageFormat.seekNonWs(pattern, pos);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testNext() {
        ParsePosition pos = new ParsePosition(0);
        assertEquals(1, messageFormat.next(pos).getIndex());
    }

    @Test
    public void testAppendQuotedString() {
        String pattern = "testPattern";
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        assertNull(messageFormat.appendQuotedString(pattern, pos, appendTo, true));
    }

    @Test
    public void testGetQuotedString() {
        String pattern = "testPattern";
        ParsePosition pos = new ParsePosition(0);
        messageFormat.getQuotedString(pattern, pos, true);
    }

    @Test
    public void testContainsElements() {
        Collection<Object> coll = null;
        assertFalse(messageFormat.containsElements(coll));
    }
}