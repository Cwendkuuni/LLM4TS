package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    @Test
    public void testExtendedMessageFormatConstructor() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testExtendedMessageFormatConstructorWithLocale() {
        Locale locale = Locale.getDefault();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("", locale);
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testExtendedMessageFormatConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("", registry);
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testExtendedMessageFormatConstructorWithLocaleAndRegistry() {
        Locale locale = Locale.getDefault();
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("", locale, registry);
        assertNotNull(extendedMessageFormat);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        String pattern = extendedMessageFormat.toPattern();
        assertNotNull(pattern);
    }
    
    @Test
    public void testApplyPatternWithNullRegistry() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("");
        
        String pattern = extendedMessageFormat.toPattern();
        assertNotNull(pattern);
    }
    
    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("", registry);
        extendedMessageFormat.applyPattern("");
        
        String pattern = extendedMessageFormat.toPattern();
        assertNotNull(pattern);
    }
    
    @Test
    public void testSetFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        Format format = new DecimalFormat("#");
        
        try {
            extendedMessageFormat.setFormat(0, format);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        Format format = new DecimalFormat("#");
        
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, format);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        Format[] formats = new Format[1];
        Format format = new DecimalFormat("#");
        formats[0] = format;
        
        try {
            extendedMessageFormat.setFormats(formats);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        Format[] formats = new Format[1];
        Format format = new DecimalFormat("#");
        formats[0] = format;
        
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(formats);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("");
        
        assertEquals(extendedMessageFormat1, extendedMessageFormat2);
        
        extendedMessageFormat1 = new ExtendedMessageFormat("");
        extendedMessageFormat2 = new ExtendedMessageFormat("", Locale.getDefault());
        
        assertNotEquals(extendedMessageFormat1, extendedMessageFormat2);
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        int hashCode = extendedMessageFormat.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testHelperMethods() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");

        // Test seekNonWs method
        ParsePosition pos = new ParsePosition(0);
        extendedMessageFormat.seekNonWs("", pos);
        int index = pos.getIndex();
        assertEquals(0, index);

        // Test next method
        pos = new ParsePosition(0);
        extendedMessageFormat.next(pos);
        index = pos.getIndex();
        assertEquals(1, index);

        // Test containsElements method
        Collection<String> coll = new ArrayList<>();
        boolean contains = extendedMessageFormat.containsElements(coll);
        assertFalse(contains);
        
        coll.add(null);
        contains = extendedMessageFormat.containsElements(coll);
        assertFalse(contains);
        
        coll.add("test");
        contains = extendedMessageFormat.containsElements(coll);
        assertTrue(contains);
    }
}