package org.apache.commons.lang.test;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ExtendedMessageFormat} class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String, Locale, Map)}
     * with default registry.
     */
    @Test
    public void testDefaultRegistry() {
        String pattern = "{0,date,MM/dd/yyyy hh:mm a} {1} {0,time,hh:mm:ss a}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertNotNull(format);
        Object[] args = {"01/01/2022 12:01:01 PM", "Hello"};
        String result = format.format(args);
        assertEquals("01/01/2022 12:01:01 PM Hello 01:01:01 PM", result);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String, Locale)}
     * with custom registry.
     */
    @Test
    public void testCustomRegistry() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,date,customDate} {1} {0,time,hh:mm:ss a}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertNotNull(format);
        Object[] args = {"01/02/2022", "Hello"};
        String result = format.format(args);
        assertEquals("01/02/2022 Hello 01:02:02 PM", result);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#applyPattern(String)} with
     * invalid format pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        String pattern = "InvalidFormat{0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#getFormat(String, String, Locale)}
     * with missing format factory.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatFactory() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        String pattern = "{0,customMissingFormat} {1} {0,time,hh:mm:ss a}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"test", "Hello"};
        format.applyPattern(pattern);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#getFormat(String, String, Locale)}
     * with custom format factory.
     */
    @Test
    public void testCustomFormatFactory() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,customDate,hh:mm a} {1} {0,time,hh:mm:ss a}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"12", "Hello"};
        String result = format.format(args);
        assertEquals("12:00 PM Hello 12:01:01 PM", result);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setLocale(Locale)} with different
     * locales.
     */
    @Test
    public void testDifferentLocales() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,customDate,HH:mm:ss} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"test", "Hello"};

        format.setLocale(Locale.ENGLISH);
        String resultEN = format.format(args);
        assertEquals("12:01:01 test Hello 12:01:01", resultEN);

        format.setLocale(Locale.FRENCH);
        String resultFR = format.format(args);
        assertEquals("12:01:01 test 12:01:01", resultFR);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#applyPattern(String)} with
     * invalid format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElement() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        String pattern = "{0,invalidFormatElement,date} {1} {0,time,hh:mm:ss a}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#applyPattern(String)} with invalid
     * format and style.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatAndStyle() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        String pattern = "{0,customDate,invalidFormat,hh:mm:ss} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Test method for
     * {@link ExtendedMessageFormat#setFormat(int, Format)} and
     * {@link ExtendedMessageFormat#setFormatByArgumentIndex(int, Format)} with
     * different formats.
     */
    @Test
    public void testSetFormat() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,customDate,date} {1} {0,time,time} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"01/02/2022", "Hello", "Goodbye"};

        Format customDateFormat = new CustomDateFormat();
        format.setFormat(0, customDateFormat);
        String result1 = format.format(args);
        assertEquals("01/02/2022 Hello 12:01:01 PM Goodbye 12:01:01 PM", result1);

        Format customTimeFormat = new CustomTimeFormat();
        format.setFormatByArgumentIndex(2, customTimeFormat);
        String result2 = format.format(args);
        assertEquals("01/02/2022 Hello 12:01:01 PM Goodbye 12:02:02 PM", result2);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setFormats(Format[])} with different
     * formats.
     */
    @Test
    public void testSetFormats() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,customDate,date} {1} {0,time,time} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"01/02/2022", "Hello", "Goodbye"};

        Format[] formats = {new CustomDateFormat(), new CustomTimeFormat()};
        format.setFormats(formats);
        String result = format.format(args);
        assertEquals("01/02/2022 Hello 12:01:01 PM Goodbye 12:02:02 PM", result);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#setFormatsByArgumentIndex(Format[])}
     * with different formats.
     */
    @Test
    public void testSetFormatsByArgumentIndex() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,customDate,date} {1} {0,time,time} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"01/02/2022", "Hello", "Goodbye"};

        Format[] formats = {new CustomDateFormat(), new CustomTimeFormat()};
        format.setFormatsByArgumentIndex(new Format[3]);
        format.setFormatsByArgumentIndex(0, formats[0]);
        format.setFormatsByArgumentIndex(2, formats[1]);
        String result = format.format(args);
        assertEquals("01/02/2022 Hello 12:01:01 PM Goodbye 12:02:02 PM", result);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#format(Object[])} with different
     * formats and incorrect argument count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatIncorrectArgumentCount() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        String pattern = "{0,date,date} {1} {2,time,time}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"01/02/2022"};
        format.format(args);
    }

    /**
     * Test method for {@link ExtendedMessageFormat#format(Object[])} with different
     * formats and incorrect argument position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatIncorrectArgumentPosition() {
        Map<String, FormatFactory> registry = new java.util.LinkedHashMap<>();
        String pattern = "{0,date,date} {1,time,time} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"01/02/2022", "12:01:01"};
        format.format(args);
    }
}