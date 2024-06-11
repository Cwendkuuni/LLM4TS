package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test class for ExtendedMessageFormat
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1}");
        assertNotNull(format);
    }

    /**
     * Test constructor with locale
     */
    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1}", Locale.US);
        assertNotNull(format);
    }

    /**
     * Test constructor with registry
     */
    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new CustomFormat();
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, customFormat, date, long}", registry);
        assertNotNull(format);
    }

    /**
     * Test toPattern()
     */
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, date, short}", Locale.US);
        String pattern = format.toPattern();
        assertEquals("{0} {1,date,short}", pattern);
    }

    /**
     * Test applyPattern()
     */
    @Test
    public void testApplyPattern() {
        String pattern = "{0, date, short} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("{0, date, short} {1}");
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test applyPattern() with registry
     */
    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "{0} {1, customFormat, date, long}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, new HashMap<String, FormatFactory>() {{
            put("customFormat", new FormatFactory() {
                @Override
                public Format getFormat(String name, String argument, Locale locale) {
                    return new CustomFormat();
                }
            });
        }});
        format.applyPattern(pattern);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test format()
     */
    @Test
    public void testFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, date, short}", Locale.US);
        Object[] args = {"John Doe", new java.util.Date()};
        String result = format.format(args);
        String expected = String.format("%s %s", "John Doe",
                java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, Locale.US).format(new java.util.Date()));
        assertEquals(expected, result);
    }

    /**
     * Test format() with registry
     */
    @Test
    public void testFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new CustomFormat();
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, customFormat, date, long}", registry);
        Object[] args = {"John Doe", new java.util.Date()};
        String result = format.format(args);
        String expected = "John Doe " + new CustomFormat().format(new java.util.Date());
        assertEquals(expected, result);
    }

    /**
     * Test parse()
     */
    @Test
    public void testParse() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, date, short}", Locale.US);
        ParsePosition pos = new ParsePosition(0);
        Object[] args = format.parse("John Doe 01-Feb-19", pos);
        assertArrayEquals(new Object[]{"John Doe", "01-Feb-19"}, args);
        assertEquals(pos.getIndex(), 13);
    }

    /**
     * Test parse() with registry
     */
    @Test
    public void testParseWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new CustomFormat();
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1, customFormat}", registry);
        Object[] args = format.parse("John Doe CustomFormat", new ParsePosition(0));
        assertEquals(args[0], "John Doe");
        assertEquals(args[1], "CustomFormat");
    }

    /**
     * CustomFormat class for testing
     */
    static class CustomFormat extends Format {
        @Override
        public String format(Object object) {
            return "Custom";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    /**
     * FormatFactory interface for testing
     */
    static interface FormatFactory {
        Format getFormat(String name, String argument, Locale locale);
    }
}