package org.apache.commons.lang.text; // Import necessary classes

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test class for ExtendedMessageFormat.
 */
public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;

    @Before
    public void setup() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        format = new ExtendedMessageFormat("{0, format=customFormat} {1}", Locale.ENGLISH, registry);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        assertNotNull(efmt);
    }

    /**
     * Test constructor with registry.
     */
    @Test
    public void testConstructorWithRegistry() {
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, new HashMap<>());
        assertNotNull(efmt);
    }

    /**
     * Test constructor with pattern and non-default locale.
     */
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.FRENCH, new HashMap<>());
        assertNotNull(efmt);
    }

    /**
     * Test constructor with pattern, non-default locale, and registry.
     */
    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.FRENCH, registry);
        assertNotNull(efmt);
    }

    /**
     * Test toPattern() method.
     */
    @Test
    public void testToPattern() {
        assertEquals("{0}", format.toPattern());
    }

    /**
     * Test applyPattern() method with a valid pattern.
     */
    @Test
    public void testApplyPatternWithValidPattern() {
        String pattern = "{0, format=customFormat} {1}";
        format.applyPattern(pattern);
        String expectedPattern = "{0}{1}";
        assertEquals(expectedPattern, format.toPattern());
    }

    /**
     * Test applyPattern() method with an invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        format.applyPattern("{0}"); // The pattern doesn't have any format element
    }

    /**
     * Test setLocale() method.
     */
    @Test
    public void testSetLocale() {
        format.setLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, format.getLocale());
    }

    /**
     * Test setRegistry() method.
     */
    @Test
    public void testSetRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        format.setRegistry(registry);
        assertEquals(registry, format.getRegistry());
    }

    /**
     * Test applyPattern() method with an empty pattern.
     */
    @Test
    public void testApplyPatternWithEmptyPattern() {
        format.applyPattern("");
        assertEquals("", format.toPattern());
    }

    /**
     * Test setFormat() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        format.setFormat(0, new Format() {});
    }

    /**
     * Test setFormatByArgumentIndex() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    /**
     * Test setFormats() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormats(formats);
    }

    /**
     * Test setFormatsByArgumentIndex() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormatsByArgumentIndex(new int[1], formats);
    }

    /**
     * Test getFormat() method.
     */
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, registry);
        Format format = efmt.getFormat("customFormat");
        assertNotNull(format);
    }

    /**
     * Test getFormat() method with an invalid format name.
     */
    @Test
    public void testGetFormatWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, registry);
        Format format = efmt.getFormat("invalidFormat");
        assertNull(format);
    }

    /**
     * Test getFormat() method with an empty registry.
     */
    @Test
    public void testGetFormatWithEmptyRegistry() {
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, new HashMap<>());
        Format format = efmt.getFormat("customFormat");
        assertNull(format);
    }

    /**
     * Test getFormats() method.
     */
    @Test
    public void testGetFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, registry);
        Format[] formats = efmt.getFormats();
        assertNotNull(formats);
        assertEquals(1, formats.length);
    }

    /**
     * Test getFormats() method with an empty registry.
     */
    @Test
    public void testGetFormatsWithEmptyRegistry() {
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, new HashMap<>());
        Format[] formats = efmt.getFormats();
        assertNotNull(formats);
        assertEquals(0, formats.length);
    }

    /**
     * Test getFormatDescription() method.
     */
    @Test
    public void testGetFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, registry);
        String formatDescription = efmt.getFormatDescription("customFormat");
        assertNotNull(formatDescription);
        assertEquals("customFormat", formatDescription);
    }

    /**
     * Test getFormatDescription() method with an invalid format name.
     */
    @Test
    public void testGetFormatDescriptionWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, registry);
        String formatDescription = efmt.getFormatDescription("invalidFormat");
        assertNull(formatDescription);
    }

    /**
     * Test getFormatDescription() method with an empty registry.
     */
    @Test
    public void testGetFormatDescriptionWithEmptyRegistry() {
        ExtendedMessageFormat efmt = new ExtendedMessageFormat("{0, format=customFormat}", Locale.ENGLISH, new HashMap<>());
        String formatDescription = efmt.getFormatDescription("customFormat");
        assertNull(formatDescription);
    }

    /**
     * Test FormatFactory.
     */
    public static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatArguments, Locale locale) {
            return new CustomFormat();
        }
    }

    /**
     * Test CustomFormat.
     */
    public static class CustomFormat implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "CustomFormat";
        }

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            if (obj instanceof String && CompareToStringBuilder.refEquals(obj, "CustomFormat")) {
                return "{CustomFormat}";
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}