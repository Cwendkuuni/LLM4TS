package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        assertNull(format.getFormat(0));
    }

    @Test
    public void testDefaultConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", format.toPattern());
        assertNull(format.getFormat(0));
    }

    @Test
    public void testDefaultConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        assertEquals("{0}", format.toPattern());
        assertNull(format.getFormat(0));
    }

    @Test
    public void testDefaultConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory customFormatFactory = new FormatFactory() {

            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new TestFormat();
            }
        };
        registry.put("custom", customFormatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{custom}", registry);
        assertEquals("{custom}", format.toPattern());
        assertTrue(format.getFormat(0) instanceof TestFormat);
    }

    @Test
    public void testDefaultConstructorWithPatternAndLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory customFormatFactory = new FormatFactory() {

            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new TestFormat();
            }
        };
        registry.put("custom", customFormatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{custom}", Locale.US, registry);
        assertEquals("{custom}", format.toPattern());
        assertTrue(format.getFormat(0) instanceof TestFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        new ExtendedMessageFormat("InvalidPattern");
    }

    @Test
    public void testSetlocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setLocale(Locale.US);
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testSetPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        format.applyPattern("{1}");
        assertEquals("{1}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        format.setFormat(0, new TestFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        format.setFormatsByArgumentIndex(new TestFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        format.setFormats(new TestFormat[0]);
    }

    @Test
    public void testExtendedMessageFormatWithCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory customFormatFactory = new FormatFactory() {

            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new TestFormat();
            }
        };
        registry.put("custom", customFormatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{custom}", registry);
        format.applyPattern("{custom, number, #.##} {0}");
        assertEquals("{custom, number, #.##} {0}", format.toPattern());
        assertTrue(format.getFormat(0) instanceof TestFormat);
    }

    @Test
    public void testFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{custom, number, #.##} {0}", Locale.US, registry());
        Object[] args = new Object[] {"42.42"};
        String result = format.format(args);
        assertEquals("custom, number, #.## 42.42", result);
    }

    @Test
    public void testParse() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{custom, number, #.##} {0}", Locale.US, registry());
        String text = "custom, number, #.## 123.45";
        ParsePosition pos = new ParsePosition(0);
        Object[] args = format.parse(text, pos);
        assertEquals(2, args.length);
        assertEquals("123.45", args[1]);
        assertEquals(4, pos.getIndex());
    }

    private static Map<String, FormatFactory> registry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory customFormatFactory = new FormatFactory() {

            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new TestFormat();
            }
        };
        registry.put("custom", customFormatFactory);
        return registry;
    }

    private static class TestFormat implements Format {

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            return obj.toString();
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            Object result = parseNumber(source, pos);
            if (result == null) {
                throw new ParseException("Invalid input", pos);
            }
            return result;
        }

        @Override
        public Number parseNumber(String source, ParsePosition pos) {
            return new Double(source);
        }

        @Override
        public String toString() {
            return "TestFormat";
        }

        @Override
        public void setParsePosition(ParsePosition pos) {
            // empty
        }

        @Override
        public int parse(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public boolean matches(CharSequence source, ParsePosition pos) {
            return false;
        }

        @Override
        public void setErrorMessage(String errorMessage) {
            // empty
        }

        @Override
        public void setLength(int length) {
            // empty
        }

        @Override
        public int parseBigInteger(String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public BigInteger parseBigDecimal(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public void setFlag(int flag, boolean on, Locale locale) {
            // empty
        }

        @Override
        public boolean isParsePartial() {
            return false;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos, Object defaultValue) {
            return parseObject(source, pos);
        }

        @Override
        public String getFormat() {
            return null;
        }

        @Override
        public void setFormat(String format) {
            // empty
        }

        @Override
        public Object parseObject(String source, ParsePosition pos, Object defaultValue, String format) {
            return parseObject(source, pos);
        }
    }
}