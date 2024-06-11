package org.apache.commons.lang3.text;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    ExtendedMessageFormat extendedMessageFormat;
    
    @Test
    public void testConstructor1() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testConstructor2() {
        extendedMessageFormat = new ExtendedMessageFormat("", Locale.getDefault());
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testConstructor3() {
        extendedMessageFormat = new ExtendedMessageFormat("", null);
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testConstructor4() {
        extendedMessageFormat = new ExtendedMessageFormat("", Locale.getDefault(), null);
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testToPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        String pattern = extendedMessageFormat.toPattern();
        assertNotNull(pattern);
    }
    
    @Test
    public void testApplyPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("");
    }
    
    @Test
    public void testSetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormat(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    @Test
    public void testSetFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormats(new Format[0]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(new Format[0]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    @Test
    public void testEquals() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertTrue(extendedMessageFormat.equals(extendedMessageFormat));
        assertFalse(extendedMessageFormat.equals(null));
    }
    
    @Test
    public void testHashCode() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        int hashCode = extendedMessageFormat.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testGetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertNull(extendedMessageFormat.getFormat(""));
    }
    
    @Test
    public void testReadArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.readArgumentIndex("", new ParsePosition(0));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testParseFormatDescription() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.parseFormatDescription("", new ParsePosition(0));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testInsertFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        String pattern = extendedMessageFormat.insertFormats("", null);
        assertNotNull(pattern);
    }
    
    @Test
    public void testSeekNonWs() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.seekNonWs("", new ParsePosition(0));
    }
    
    @Test
    public void testNext() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        ParsePosition pos = extendedMessageFormat.next(new ParsePosition(0));
        assertNotNull(pos);
    }
    
    @Test
    public void testAppendQuotedString() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        StringBuilder sb = extendedMessageFormat.appendQuotedString("", new ParsePosition(0), new StringBuilder(), true);
        assertNotNull(sb);
    }
    
    @Test
    public void testGetQuotedString() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.getQuotedString("", new ParsePosition(0), true);
    }
    
    @Test
    public void testContainsElements() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        assertFalse(extendedMessageFormat.containsElements(null));
    }
}