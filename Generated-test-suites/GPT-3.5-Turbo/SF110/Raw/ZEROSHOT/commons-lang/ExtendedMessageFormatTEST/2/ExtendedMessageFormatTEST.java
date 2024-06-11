package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Mock
    private FormatFactory formatFactory;

    private ExtendedMessageFormat extendedMessageFormat;

    private String pattern;
    private Locale locale;
    private Map<String, ? extends FormatFactory> registry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        pattern = "Test pattern";
        locale = Locale.getDefault();
        registry = new HashMap<>();
        extendedMessageFormat = new ExtendedMessageFormat(pattern, locale, registry);
    }

    @Test
    public void testToPattern() {
        assertEquals("Expected pattern to match", pattern, extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithNullRegistry() {
        extendedMessageFormat = new ExtendedMessageFormat(pattern);
        extendedMessageFormat.applyPattern("New pattern");
        assertEquals("Expected pattern to be updated", "New pattern", extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String newPattern = "New pattern";
        extendedMessageFormat.applyPattern(newPattern);
        assertEquals("Expected pattern to be updated", newPattern, extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue("Expected objects to be equal", extendedMessageFormat.equals(extendedMessageFormat));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse("Expected objects to not be equal", extendedMessageFormat.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse("Expected objects to not be equal", extendedMessageFormat.equals("Different class"));
    }

    @Test
    public void testEqualsSamePattern() {
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat(pattern, locale, registry);
        assertTrue("Expected objects to be equal", extendedMessageFormat.equals(otherFormat));
    }

    @Test
    public void testEqualsDifferentPattern() {
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat("Different pattern", locale, registry);
        assertFalse("Expected objects to not be equal", extendedMessageFormat.equals(otherFormat));
    }

    @Test
    public void testEqualsNullPattern() {
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat(null, locale, registry);
        assertFalse("Expected objects to not be equal", extendedMessageFormat.equals(otherFormat));
    }

    @Test
    public void testEqualsDifferentRegistry() {
        Map<String, ? extends FormatFactory> otherRegistry = new HashMap<>();
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat(pattern, locale, otherRegistry);
        assertFalse("Expected objects to not be equal", extendedMessageFormat.equals(otherFormat));
    }

    @Test
    public void testEqualsRegistryNull() {
        Map<String, ? extends FormatFactory> otherRegistry = null;
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat(pattern, locale, otherRegistry);
        assertFalse("Expected objects to not be equal", extendedMessageFormat.equals(otherFormat));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * extendedMessageFormat.hashCode() + registry.hashCode();
        assertEquals("Expected hash codes to match", expectedHashCode, extendedMessageFormat.hashCode());
    }
}