package org.apache.commons.lang.test;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test cases for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor when given a valid pattern.
     */
    @Test
    public void testValidConstruction() {
        String pattern = "Hello {0, choice, 0} {1} {2, choice, 0} {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Valid pattern should not throw any exception", pattern, format.toPattern());
    }

    /**
     * Test constructor when given an invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstruction() {
        String pattern = "Hello {0, choice, 0} {1, choice, 0} {2, choice, 0}";
        new ExtendedMessageFormat(pattern);
    }

    /**
     * Test setLocale method.
     */
    @Test
    public void testSetLocale() {
        String pattern = "Hello {0} {1}, {2}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Locale locale = Locale.CHINESE;
        format.setLocale(locale);
        assertEquals("Locale should be set correctly", locale, format.getLocale());
    }

    /**
     * Test getFormat method.
     */
    @Test
    public void testGetFormat() {
        String pattern = "Hello {0, date, d} {1} {2, number, 0.0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat("dd-MM-yyyy");
                } else if ("number".equals(name)) {
                    return new DecimalFormat("#0.00");
                }
                throw new IllegalArgumentException("Unknown format: " + name);
            }
        };
        format.setRegistry(Collections.singletonMap("DateFormat", formatFactory));
        Format format1 = format.getFormat(0, null);
        assertTrue("First format should be instance of SimpleDateFormat", format1 instanceof SimpleDateFormat);
        Format format2 = format.getFormat(1, null);
        assertTrue("Second format should be instance of DecimalFormat", format2 instanceof DecimalFormat);
    }

    /**
     * Test applyPattern method with valid pattern and registry.
     */
    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello {0, date, d} {1, number, 0.0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if ("date".equals(name)) {
                    return new SimpleDateFormat("dd-MM-yyyy");
                } else if ("number".equals(name)) {
                    return new DecimalFormat("#0.00");
                }
                throw new IllegalArgumentException("Unknown format: " + name);
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormat", formatFactory);
        format.setRegistry(registry);
        Object[] args = {"25-12-2021", 3.14159265};
        String result = format.format(args);
        assertEquals("Expected result: Hello 25-12-2021 3.14", result, "Hello {0, date, d} {1, number, 0.0}");
    }

    /**
     * Test applyPattern method with invalid pattern and registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidRegistry() {
        String pattern = "Hello {0, date, d} {1, number, 0.0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                throw new UnsupportedOperationException("Unsupported method getFormat");
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormat", formatFactory);
        format.setRegistry(registry);
        format.applyPattern(pattern);
    }

    /**
     * Test setFormats method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0} {1} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = {new SimpleDateFormat("dd-MM-yyyy"), new DecimalFormat("#0.00")};
        format.setFormats(formats);
    }

    /**
     * Test setFormatByArgumentIndex method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0} {1} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    /**
     * Test format method.
     */
    @Test
    public void testFormat() throws ParseException {
        String pattern = "Hello {0, date, d} {1, number, 0.0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"25-12-2021", 3.14159265};
        String result = format.format(args);
        assertEquals("Expected result: Hello 25-12-2021 3.14", result, "Hello {0, date, d} {1, number, 0.0}");
    }

    /**
     * Test parse method.
     */
    @Test
    public void testParse() throws ParseException {
        String pattern = "Hello {0, date, d} {1, number, 0.0}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String text = "Hello 25-12-2021 3.14";
        Object[] args = format.parse(text, new ParsePosition(0));
        assertEquals("Expected arguments: [Date: Mon Dec 25 00:00:00 GMT+08:00 2021, Double: 3.14]",
                args, new Object[]{new Date("Mon Dec 25 00:00:00 GMT+08:00 2021"), 3.14});
    }

    /**
     * Test format method with locale.
     */
    @Test
    public void testFormatWithLocale() {
        String pattern = "Hello {0, choice, 0} {1} {2, choice, 0} {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"Good", "morning", "John", "Doe"};
        String result = format.format(args, Locale.JAPAN);
        assertEquals("Expected result: おはよう{0} {1} {2} {3}", result, "Hello {0, choice, 0} {1} {2, choice, 0} {3}.");
    }

    /**
     * Test parse method with locale.
     */
    @Test(expected = ParseException.class)
    public void testParseWithLocale() {
        String pattern = "Hello {0, choice, 0} {1} {2, choice, 0} {3}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String text = "こんにちは{1} {0, choice, 0} {3} {2}";
        Object[] args = format.parse(text, new ParsePosition(0), Locale.JAPAN);
        fail("Expected ParseException but got: " + args);
    }
}

class FormatFactory {
    public Format getFormat(String name, String args, Locale locale) {
        throw new UnsupportedOperationException("Unsupported method getFormat");
    }
}