package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private static final Locale LOCALE = Locale.getDefault();

    private ExtendedMessageFormat extendedMessageFormat;

    private Map<String, FormatFactory> registry;

    @Before
    public void setup() {
        registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());

        extendedMessageFormat = new ExtendedMessageFormat("", LOCALE, registry);
    }

    @Test
    public void testConstructor() {
        assertNotNull(extendedMessageFormat.toPattern());
        assertEquals(0, extendedMessageFormat.getFormats().length);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        final String pattern = "This is a test pattern";
        extendedMessageFormat = new ExtendedMessageFormat(pattern, LOCALE);

        assertEquals(pattern, extendedMessageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        final String pattern = "This is another test pattern";
        extendedMessageFormat = new ExtendedMessageFormat(pattern, LOCALE, registry);

        assertEquals(pattern, extendedMessageFormat.toPattern());
        assertTrue(extendedMessageFormat.getFormats().length > 0);
    }

    @Test
    public void testSetFormat() {
        try {
            extendedMessageFormat.setFormat(0, null);
            fail("UnsupportedOperationException should be thrown.");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, null);
            fail("UnsupportedOperationException should be thrown.");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormats() {
        try {
            extendedMessageFormat.setFormats(null);
            fail("UnsupportedOperationException should be thrown.");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(null);
            fail("UnsupportedOperationException should be thrown.");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat sameFormat = new ExtendedMessageFormat("", LOCALE, registry);
        assertEquals(extendedMessageFormat.hashCode(), sameFormat.hashCode());

        ExtendedMessageFormat differentFormat = new ExtendedMessageFormat("");
        assertNotEquals(extendedMessageFormat.hashCode(), differentFormat.hashCode());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat sameFormat = new ExtendedMessageFormat("", LOCALE, registry);
        assertTrue(extendedMessageFormat.equals(sameFormat));

        ExtendedMessageFormat differentFormat = new ExtendedMessageFormat("");
        assertFalse(extendedMessageFormat.equals(differentFormat));
    }

    @Test
    public void testToPattern() {
        final String pattern = "This is a test pattern";
        extendedMessageFormat = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithNullRegistry() {
        final String pattern = "This is a test pattern";
        extendedMessageFormat.applyPattern(pattern);
        assertEquals(pattern, extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        final String pattern = "{0:number}";
        extendedMessageFormat.applyPattern(pattern);
        assertEquals(pattern, extendedMessageFormat.toPattern());
        assertTrue(extendedMessageFormat.getFormats().length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        final String pattern = "{0:number";
        extendedMessageFormat.applyPattern(pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedFormatElement() {
        final String pattern = "{0";
        extendedMessageFormat.applyPattern(pattern);
    }

    private static class NumberFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return NumberFormat.getIntegerInstance(locale);
        }

    }
}