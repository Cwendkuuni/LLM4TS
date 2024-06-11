package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    
    private ExtendedMessageFormat format;

    @Before
    public void setup() {
        format = new ExtendedMessageFormat("{0}, {1}, {2}");
    }

    @Test
    public void testToPattern() {
        assertEquals("{0}, {1}, {2}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        format.applyPattern("{0}, {1}");

        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat equalsFormat = new ExtendedMessageFormat("{0}, {1}, {2}");

        assertTrue(format.equals(equalsFormat));
    }

    @Test
    public void testNotEquals() {
        ExtendedMessageFormat notEqualsFormat = new ExtendedMessageFormat("{0}, {1}, {3}");

        assertFalse(format.equals(notEqualsFormat));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat hashCodeFormat = new ExtendedMessageFormat("{0}, {1}, {2}");

        assertEquals(format.hashCode(), hashCodeFormat.hashCode());
    }

    @Test
    public void testSetFormat() {
        try {
            format.setFormat(0, null);
        } catch (UnsupportedOperationException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            format.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testSetFormats() {
        try {
            format.setFormats(null);
        } catch (UnsupportedOperationException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            format.setFormatsByArgumentIndex(null);
        } catch (UnsupportedOperationException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullPattern() {
        ExtendedMessageFormat constructorFormat = new ExtendedMessageFormat(null);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat constructorFormat = new ExtendedMessageFormat("{0}, {1}, {2}");

        assertEquals("{0}, {1}, {2}", constructorFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat constructorFormat = new ExtendedMessageFormat("{0}, {1}, {2}", Locale.getDefault());

        assertEquals("{0}, {1}, {2}", constructorFormat.toPattern());
        assertEquals(Locale.getDefault(), constructorFormat.getLocale());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();

        ExtendedMessageFormat constructorFormat = new ExtendedMessageFormat("{0}, {1}, {2}", registry);

        assertEquals("{0}, {1}, {2}", constructorFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternRegistryAndLocale() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();

        ExtendedMessageFormat constructorFormat = new ExtendedMessageFormat("{0}, {1}, {2}", Locale.getDefault(), registry);

        assertEquals("{0}, {1}, {2}", constructorFormat.toPattern());
        assertEquals(Locale.getDefault(), constructorFormat.getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatElement() {
        format.applyPattern("{0}, {1}, {");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatArgumentIndex() {
        format.applyPattern("{0}, {1}, {3}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedFormatElement() {
        format.applyPattern("{0}, {1}, {2");
    }

    @Test
    public void testApplyPatternWithNoFormatElement() {
        format.applyPattern("{0}, {1}, {2}");

        assertEquals("{0}, {1}, {2}", format.toPattern());
    }

    @Test
    public void testFormat() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        registry.put("title", new FormatFactory() {
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MessageFormat("Title " + arguments);
            }
        });

        format = new ExtendedMessageFormat("{0, number}, {1, time}, {2, title}", Locale.getDefault(), registry);

        assertEquals("1, 12:00:00 AM, Title My Title", format.format(new Object[] {1, new java.util.Date(0), "My Title"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidArgumentType() {
        format.format(true);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidInput() throws ParseException {
        format.parse(null);
    }

    @Test
    public void testParse() throws ParseException {
        assertEquals(new Object[]{1, false, "string"}, format.parse("1, false, string"));
    }

}