package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.ExtendedMessageFormatParser;
import org.apache.commons.lang.text.FormatFactory;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateEmpty() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertEquals("", fmt.toPattern());
        assertNull(fmt.getFormat(0));
    }

    @Test
    public void testCreateWithPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.ENGLISH);
        assertEquals("{0} {1}", fmt.toPattern());
        assertNull(fmt.getFormat(0));
        assertNull(fmt.getFormat(1));
    }

    @Test
    public void testCreateWithRegistry() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("Date")) {
                    return new java.text.SimpleDateFormat("yyyy-MM-dd");
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Date", factory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, Date} {1}",
                Locale.ENGLISH, registry);
        assertEquals("{0, date, Date} {1}", fmt.toPattern());
        assertSame(new java.text.SimpleDateFormat("yyyy-MM-dd"), fmt.getFormat(0));
        assertNull(fmt.getFormat(1));
    }

    @Test
    public void testApplyPattern() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("Date")) {
                    return new java.text.SimpleDateFormat("yyyy");
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Date", factory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, Date} {1}",
                Locale.ENGLISH, registry);
        fmt.applyPattern("{0} {1}");
        assertEquals("{0} {1}", fmt.toPattern());
        assertNull(fmt.getFormat(0));
        assertNull(fmt.getFormat(1));

        fmt.applyPattern("{0, date, Date} {1, number, #}");
        assertEquals("{0, date, Date} {1, number, #}", fmt.toPattern());
        assertSame(new java.text.SimpleDateFormat("yyyy"), fmt.getFormat(0));
        assertSame(new java.text.DecimalFormat("#"), fmt.getFormat(1));
    }

    @Test
    public void testParseWithFormats() throws ParseException {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("Date")) {
                    return new java.text.SimpleDateFormat("yyyy");
                }
                if (name.equals("Number")) {
                    return new java.text.DecimalFormat("#,##0.00");
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Date", factory);
        registry.put("Number", factory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, Date} {1, number, #.##}",
                Locale.ENGLISH, registry);

        Object[] args = new Object[] {"2022", 12345.6};
        ParsePosition pos = new ParsePosition(0);
        String pattern = fmt.format(args);
        assertEquals("2022 12,345.60", pattern);
        assertNull(pos.getErrorIndex());

        Object[] parsedArgs = fmt.parse(pattern, new Object[2], pos);
        assertArrayEquals(new Object[] {"2022", new Float(12345.6)}, parsedArgs);
        assertNull(pos.getErrorIndex());
    }

    @Test
    public void testParseWithCustomFormats() throws ParseException {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("Date")) {
                    return new ExtendedMessageFormatParser.DateFormat("yyyy-MM-dd");
                }
                if (name.equals("Number")) {
                    return new ExtendedMessageFormatParser.NumberFormat("#,##0.000");
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Date", factory);
        registry.put("Number", factory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, Date} {1, number, #.##}",
                Locale.ENGLISH, registry);

        Object[] args = new Object[] {"2022-12-31", 12345.678};
        ParsePosition pos = new ParsePosition(0);
        String pattern = fmt.format(args);
        assertEquals("2022-12-31 12,345.678000", pattern);
        assertNull(pos.getErrorIndex());

        Object[] parsedArgs = fmt.parse(pattern, new Object[2], pos);
        assertArrayEquals(new Object[] {"2022-12-31", new Float(12345.678)}, parsedArgs);
        assertNull(pos.getErrorIndex());
    }

    @Test
    public void testParseWithInvalidFormat() throws ParseException {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("Date")) {
                    return new java.text.SimpleDateFormat("yyyy");
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Date", factory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, Date} {1, date, Date}",
                Locale.ENGLISH, registry);
        String pattern = "{0, date, Date} {1, date, Date}";
        ParsePosition pos = new ParsePosition(0);
        Object[] args = new Object[] {new java.util.Date(), new java.util.Date()};

        assertThrows(ParseException.class, () -> fmt.parse(pattern, args, pos));
    }

    @Test
    public void testParseWithMissingFormat() throws ParseException {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("Date")) {
                    return new java.text.SimpleDateFormat("yyyy");
                }
                return null;
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Date", factory);

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, date, Date} {1}",
                Locale.ENGLISH, registry);
        String pattern = "{0, date, Date} {1}";
        ParsePosition pos = new ParsePosition(0);
        Object[] args = new Object[] {new java.util.Date(), "abc"};

        assertThrows(ParseException.class, () -> fmt.parse(pattern, args, pos));
    }
}

class FormatFactory {
    public Format getFormat(String name, String style, Locale locale) {
        return null;
    }
}

class ExtendedMessageFormatParser {
    public static class DateFormat extends java.text.DateFormat {
        public DateFormat(String pattern) {
            super(pattern, Locale.ENGLISH);
        }
    }

    public static class NumberFormat extends java.text.NumberFormat {
        public NumberFormat(String pattern) {
            super(pattern, Locale.ENGLISH);
        }
    }
}