package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.junit.Test;

/**
 * ExtendedMessageFormatTest.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor ExtendedMessageFormat.
     */
    @Test
    public void testExtendedMessageFormat() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test method applyPattern.
     */
    @Test
    public void testApplyPattern() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"25/12/2022", "hello", 42.5};
        String expected = "25/12/2022 hello 42.50";
        assertEquals(expected, format.format(args));

        format.applyPattern("{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##} {3}");
        args = new Object[]{"25/12/2022", "hello", 42.5, "world"};
        expected = "25/12/2022 hello 42.50 world";
        assertEquals(expected, format.format(args));
    }

    /**
     * Test method setFormat by argument index.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormatByArgumentIndex(1, new SimpleDateFormat("EEE"));
    }

    /**
     * Test method setFormat.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormat(1, new SimpleDateFormat("EEE"));
    }

    /**
     * Test method getFormat.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetFormat() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.getFormat(1);
    }

    /**
     * Test method setFormats.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        Format[] formats = {new SimpleDateFormat("EEE"), new DecimalFormat("#,##0.##")};
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormats(formats);
    }

    /**
     * Test method setFormatsByArgumentIndex.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##}";
        Map<String, FormatFactory> registry = new HashMap<>();
        Format[] formats = {new SimpleDateFormat("EEE"), new DecimalFormat("#,##0.##")};
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormatsByArgumentIndex(new int[] {1, 2}, formats);
    }

    /**
     * Test format method with custom date format.
     */
    @Test
    public void testCustomDateFormat() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##} {3,customDateFormat,dd.MM.yyyy}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"25/12/2022", "hello", 42.5, "31.12.2022"};
        String expected = "25/12/2022 hello 42.50 31.12.2022";
        assertEquals(expected, format.format(args));
    }

    /**
     * Format factory for testing.
     */
    static class CustomDateFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String pattern, Locale locale) {
            if ("customDateFormat".equals(name)) {
                return new SimpleDateFormat(pattern);
            }
            return null;
        }
    }

    /**
     * Test method parse.
     */
    @Test
    public void testParse() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##} {3,customDateFormat,dd.MM.yyyy}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String text = "25-12-2022 hello 42.50 31.12.2022";
        ParsePosition pos = new ParsePosition(0);
        Object[] objects = format.parse(text, new Object[] {null, null, null, null});
        assertEquals(4, objects.length);
        assertEquals("25", objects[0]);
        assertEquals("hello", objects[1]);
        assertEquals(42.5, objects[2]);
        assertEquals("31.12.2022", objects[3]);
    }

    /**
     * Test method format.
     */
    @Test
    public void testFormat() {
        String pattern = "{0,date,dd/MM/yyyy} {1} {2,number,#,##0.##} {3,customDateFormat,dd.MM.yyyy}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"25/12/2022", "hello", 42.5, "31.12.2022"};
        String expected = "25/12/2022 hello 42.50 31.12.2022";
        assertEquals(expected, format.format(args));
    }
}