package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;

/**
 * Test class for ExtendedMessageFormat.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with default locale.
     */
    @Test
    public void testConstructorWithDefaultLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertNotNull(format);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test constructor with custom locale.
     */
    @Test
    public void testConstructorWithCustomLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        Locale locale = new Locale("fr", "FR");
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertNotNull(format);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test constructor with custom registry.
     */
    @Test
    public void testConstructorWithCustomRegistry() {
        String pattern = "Hello {0}, your {1} is {2}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new MessageFormat("Custom format: {0}");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertNotNull(format);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test getFormat method.
     */
    @Test
    public void testGetFormat() {
        String pattern = "Hello {0}, your {1} is {2}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new MessageFormat("Custom format: {0}");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Format customFormat = format.getFormat("customFormat");
        assertNotNull(customFormat);
        assertEquals("Custom format: {0}", customFormat.toString());
    }

    /**
     * Test empty pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPattern() {
        new ExtendedMessageFormat("");
    }

    /**
     * Test invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        new ExtendedMessageFormat("Invalid pattern");
    }

    /**
     * Test setFormat method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.setFormat(0, new MessageFormat("New format"));
    }

    /**
     * Test setFormatByArgumentIndex method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.setFormatByArgumentIndex(0, new MessageFormat("New format"));
    }

    /**
     * Test setFormats method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = {new MessageFormat("Format 1"), new MessageFormat("Format 2")};
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.setFormats(formats);
    }

    /**
     * Test setFormatsByArgumentIndex method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = {new MessageFormat("Format 1"), new MessageFormat("Format 2")};
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.setFormatsByArgumentIndex(formats);
    }

    /**
     * Test applyPattern method with no custom formats.
     */
    @Test
    public void testApplyPatternWithNoCustomFormats() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test applyPattern method with custom formats.
     */
    @Test
    public void testApplyPatternWithCustomFormats() {
        String pattern = "Hello {0}, your {1} is {2}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new MessageFormat("Custom format: {0}");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String customPattern = "customFormat";
        format.applyPattern(pattern);
        String expectedPattern = "Hello {0}, your {1} is {2}\n"
                + "    {1} = Custom format: {3}";
        assertEquals(expectedPattern, format.toPattern());
    }

    /**
     * Test format method with no arguments.
     */
    @Test
    public void testFormatWithNoArguments() {
        String pattern = "Hello {0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        String formatted = format.format(new Object[0]);
        assertEquals("Hello", formatted);
    }

    /**
     * Test format method with one argument.
     */
    @Test
    public void testFormatWithOneArgument() {
        String pattern = "Hello {0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        String formatted = format.format(new String[]{"World"});
        assertEquals("Hello World", formatted);
    }

    /**
     * Test format method with multiple arguments.
     */
    @Test
    public void testFormatWithMultipleArguments() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
        Object[] arguments = new Object[]{new String("World"), new Integer(30)};
        String formatted = format.format(arguments);
        assertEquals("Hello World, your age is 30", formatted);
    }

    /**
     * Test format method with custom format.
     */
    @Test
    public void testFormatWithCustomFormat() {
        String pattern = "Hello {0}, your {1} is {2}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new MessageFormat("Custom format: {0}");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Object[] arguments = new Object[]{new String("World"), new Integer(30), new String("years")};
        String formatted = format.format(arguments);
        assertEquals("Hello World, your years is Custom format: 30", formatted);
    }
}