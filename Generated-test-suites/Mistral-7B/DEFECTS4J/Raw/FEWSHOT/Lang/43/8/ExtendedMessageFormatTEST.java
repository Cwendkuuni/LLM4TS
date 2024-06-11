package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstruction() {
        String pattern = "{0,number,#} {1,date,MM/dd/yyyy}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testConstructionWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadPattern() {
        String pattern = "{0,number,#} {1,customDate} }";
        new ExtendedMessageFormat(pattern);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0,number,#} {1,date,MM/dd/yyyy}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String result = format.format(new Object[]{1, "2022-05-31"});
        assertEquals("1 05/31/2022", result);
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(new Object[]{1, "2022-05-31"});
        assertEquals("1 05/31/2022", result);
    }

    @Test
    public void testFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(new Object[]{1, "2022-05-31"});
        assertEquals("1 05/31/2022", result);
    }

    @Test
    public void testFormatWithArgs() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#,date,MM/dd/yyyy} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(new Object[]{1, "2022-05-31"}, Locale.US);
        assertEquals("1 05/31/2022", result);
    }

    @Test
    public void testSetLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setLocale(Locale.CHINA);
        String result = format.format(new Object[]{1, "2022-05-31"});
        assertEquals("1 05/31/2022", result);
    }

    @Test
    public void testSetFormat() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetFormats() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testGetFormats() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testGetFormatsByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParse() {
        String pattern = "{0,number,#} {1,customDate}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = format.parse(new StringBuffer(pattern), pos);
        assertEquals(2, args.length);
        assertNull(args[0]);
        assertNull(args[1]);
    }

    @Test
    public void testParseWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertEquals(2, args.length);
        assertNull(args[0]);
        assertNull(args[1]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByIndex() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testToString() {
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String result = format.toString();
        assertEquals(pattern, result);
    }

    @Test
    public void testGetLocale() {
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Locale locale = format.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testFormatWithNullObject() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(null, new Object[]{1, "2022-05-31"});
        assertNull(result);
    }

    @Test
    public void testFormatWithNullArgumentIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(new Object[]{null}, new Object[]{1, "2022-05-31"});
        assertNull(result);
    }

    @Test
    public void testFormatWithEmptyArgumentList() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String result = format.format(new Object[0], new Object[0]);
        assertNull(result);
    }

    @Test
    public void testParseWithNullInput() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(null, new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
    }

    @Test
    public void testParseWithEmptyInput() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
    }

    @Test
    public void testParseWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,foo}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    @Test
    public void testParseWithInvalidFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate,foobar}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    @Test
    public void testParseWithInvalidFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate} {2,foo}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    @Test
    public void testParseWithInvalidFormatElementOrder() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{1,customDate} {0,number,#}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    @Test
    public void testParseWithArgumentMismatch() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,customDate} {1,number,#}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    @Test
    public void testParseWithFormatMismatch() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customFormat}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    @Test
    public void testParseWithFormatNameInRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertEquals(2, args.length);
        assertNull(args[0]);
        assertNull(args[1]);
    }

    @Test
    public void testParseWithFormatNameMissingInRegistry() {
        Map<String, FormatFactory> registry = Collections.emptyMap();
        String pattern = "{0,number,#} {1,customDate}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = format.parse(new StringBuffer(pattern), new ParsePosition(0));
        assertArrayEquals(new Object[0], args);
        assertThrows(IllegalArgumentException.class, () -> format.parse(new StringBuffer(pattern), new ParsePosition(0)));
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return new CustomFormat();
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public String format(Object obj, Locale locale, String format) {
            return "";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    private static class CustomDateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return new CustomDateFormat();
        }
    }

    private static class CustomDateFormat implements Format {
        @Override
        public String format(Object obj, Locale locale, String format) {
            return "";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FormatContext fc) {
            return toAppendTo;
        }
    }
}