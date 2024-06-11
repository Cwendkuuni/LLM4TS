package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat extendedMessageFormat;

    @Test
    public void testExtendedMessageFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testExtendedMessageFormatWithLocale() {
        extendedMessageFormat = new ExtendedMessageFormat("", Locale.getDefault());
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        extendedMessageFormat = new ExtendedMessageFormat("", null);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testExtendedMessageFormatWithLocaleAndRegistry() {
        extendedMessageFormat = new ExtendedMessageFormat("", Locale.getDefault(), null);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testToPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("");
        assertNotNull(extendedMessageFormat.toPattern());
    }

    @Test
    public void testSetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> {
                extendedMessageFormat.setFormat(0, null);
        });
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> {
                extendedMessageFormat.setFormatByArgumentIndex(0, null);
        });
    }

    @Test
    public void testSetFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> {
                extendedMessageFormat.setFormats(null);
        });
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> {
                extendedMessageFormat.setFormatsByArgumentIndex(null);
        });
    }

    @Test
    public void testEquals() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertTrue(extendedMessageFormat.equals(extendedMessageFormat));
    }

    @Test
    public void testHashCode() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat.hashCode());
    }

    @Test
    public void testGetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNull(extendedMessageFormat.getFormat(""));
    }

    @Test
    public void testReadArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertThrows(IllegalArgumentException.class, () -> {
                extendedMessageFormat.readArgumentIndex("", new ParsePosition(0));
        });
    }

    @Test
    public void testParseFormatDescription() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertThrows(IllegalArgumentException.class, () -> {
                extendedMessageFormat.parseFormatDescription("", new ParsePosition(0));
        });
    }

    @Test
    public void testInsertFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        List<String> customPatterns = new ArrayList<>();
        customPatterns.add("");
        assertNotNull(extendedMessageFormat.insertFormats("", customPatterns));
    }

    @Test
    public void testSeekNonWs() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.seekNonWs("", new ParsePosition(0));
    }

    @Test
    public void testNext() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat.next(new ParsePosition(0)));
    }

    @Test
    public void testAppendQuotedString() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat.appendQuotedString("", new ParsePosition(0), new StringBuilder(), true));
    }

    @Test
    public void testGetQuotedString() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.getQuotedString("", new ParsePosition(0), true);
    }

    @Test
    public void testContainsElements() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        List<String> coll = new ArrayList<>();
        coll.add("");
        assertFalse(extendedMessageFormat.containsElements(coll));
    }
}