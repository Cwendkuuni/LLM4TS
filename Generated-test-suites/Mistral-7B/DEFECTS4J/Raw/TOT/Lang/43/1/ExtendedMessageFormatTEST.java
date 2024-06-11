package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParsePosition;
import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test case for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test ExtendedMessageFormat with no custom format elements.
     */
    @Test
    public void testNoCustomFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!");
        Object[] args = new Object[]{"Alice", "Bob"};
        String result = format.format(args);
        assertEquals("Hello Alice, Bob!", result);
    }

    /**
     * Test ExtendedMessageFormat with a custom format element.
     */
    @Test
    public void testCustomFormat() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("customFormat".equals(name)) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, customFormat} {1}!", registry);
        Object[] args = new Object[]{"Alice", "Bob"};
        String result = format.format(args);
        assertEquals("Hello Alice CustomFormat Bob!", result);
    }

    /**
     * Test ExtendedMessageFormat with invalid custom format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCustomFormat() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, customFormat} {1}", registry);
    }

    /**
     * Test ExtendedMessageFormat with multiple custom format elements.
     */
    @Test
    public void testMultipleCustomFormats() {
        FormatFactory factory1 = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("customFormat1".equals(name)) {
                    return new CustomFormat1();
                }
                return null;
            }
        };

        FormatFactory factory2 = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("customFormat2".equals(name)) {
                    return new CustomFormat2();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat1", factory1);
        registry.put("customFormat2", factory2);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, customFormat1} {1, customFormat2}!", registry);
        Object[] args = new Object[]{"Alice", "Bob"};
        String result = format.format(args);
        assertEquals("Hello AliceCustomFormat1 BobCustomFormat2!", result);
    }

    /**
     * Test ExtendedMessageFormat with missing custom format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingCustomFormat() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("customFormat1".equals(name)) {
                    return new CustomFormat1();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat1", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, customFormat2} {1}", registry);
    }

    /**
     * Test ExtendedMessageFormat with whitespace in custom format element.
     */
    @Test
    public void testCustomFormatWithWhitespace() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("custom Format".equals(name)) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom Format", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, custom Format} {1}!", registry);
        Object[] args = new Object[]{"Alice", "Bob"};
        String result = format.format(args);
        assertEquals("Hello Alice custom Format Bob!", result);
    }

    /**
     * Custom format implementation for tests.
     */
    static class CustomFormat implements Format {
        @Override
        public String format(Object object, Locale locale, String format,
                ParsePosition parsePosition) {
            return "CustomFormat";
        }

        @Override
        public StringBuffer format(Object object, StringBuffer toAppendTo,
                FormatWriteBuffer fwb, Locale locale) {
            toAppendTo.append("CustomFormat");
            return toAppendTo;
        }

        @Override
        public boolean isPattern(String pattern, ParsePosition parsePosition) {
            return false;
        }

        @Override
        public ParsePosition parse(String source, ParsePosition pos) {
            return pos;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public int parseInteger(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public long parseLong(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public float parseFloat(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public double parseDouble(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && getClass() == obj.getClass();
        }

        @Override
        public String toString() {
            return "CustomFormat";
        }
    }

    /**
     * Custom format implementation for tests, version 1.
     */
    static class CustomFormat1 implements Format {
        @Override
        public String format(Object object, Locale locale, String format,
                ParsePosition parsePosition) {
            return "CustomFormat1";
        }

        @Override
        public StringBuffer format(Object object, StringBuffer toAppendTo,
                FormatWriteBuffer fwb, Locale locale) {
            toAppendTo.append("CustomFormat1");
            return toAppendTo;
        }

        @Override
        public boolean isPattern(String pattern, ParsePosition parsePosition) {
            return false;
        }

        @Override
        public ParsePosition parse(String source, ParsePosition pos) {
            return pos;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public int parseInteger(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public long parseLong(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public float parseFloat(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public double parseDouble(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && getClass() == obj.getClass();
        }

        @Override
        public String toString() {
            return "CustomFormat1";
        }
    }

    /**
     * Custom format implementation for tests, version 2.
     */
    static class CustomFormat2 implements Format {
        @Override
        public String format(Object object, Locale locale, String format,
                ParsePosition parsePosition) {
            return "CustomFormat2";
        }

        @Override
        public StringBuffer format(Object object, StringBuffer toAppendTo,
                FormatWriteBuffer fwb, Locale locale) {
            toAppendTo.append("CustomFormat2");
            return toAppendTo;
        }

        @Override
        public boolean isPattern(String pattern, ParsePosition parsePosition) {
            return false;
        }

        @Override
        public ParsePosition parse(String source, ParsePosition pos) {
            return pos;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public int parseInteger(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public long parseLong(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public float parseFloat(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public double parseDouble(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && getClass() == obj.getClass();
        }

        @Override
        public String toString() {
            return "CustomFormat2";
        }
    }
}