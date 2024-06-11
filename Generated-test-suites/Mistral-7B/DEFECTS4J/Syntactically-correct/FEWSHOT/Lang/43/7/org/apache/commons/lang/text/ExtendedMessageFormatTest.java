package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.Format;
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
    public void testEmpty() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", new HashMap<>());
        assertEquals("", format.format(new Object[0][0]));
    }

    @Test
    public void testBasic() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date} {1}", registry);
        Object[] arguments = {"2023-03-31", "afternoon"};
        String result = format.format(arguments);
        assertEquals("2023-03-31 afternoon", result);
    }

    @Test
    public void testCustomArgumentIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{3,customDate}", registry);
        Object[] arguments = {"2023-03-31", null, null, new CustomDate("2023-03-31")};
        String result = format.format(arguments);
        assertEquals("2023-03-31", result);
    }

    @Test
    public void testCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customNumber", new CustomNumberFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,customNumber,number,#,##0.##}", registry);
        Object[] arguments = {123.456};
        String result = format.format(arguments);
        assertEquals("123.46", result);
    }

    @Test
    public void testInvalidFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,invalid}", registry);
        assertThrows(IllegalArgumentException.class, () -> format.format(new Object[0]));
    }

    @Test
    public void testInvalidArgumentIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{5,date}", registry);
        assertThrows(IllegalArgumentException.class, () -> format.format(new Object[0]));
    }

    @Test
    public void testMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,missing}", registry);
        assertThrows(IllegalArgumentException.class, () -> format.format(new Object[0]));
    }

    @Test
    public void testEmptyFormatRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", null);
        Object[] arguments = {"Test"};
        assertThrows(IllegalArgumentException.class, () -> format.format(arguments));
    }

    @Test
    public void testInvalidFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,invalidFormat}", registry);
        assertThrows(IllegalArgumentException.class, () -> format.format(new Object[0]));
    }

    @Test
    public void testInvalidFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date,invalidStyle}", registry);
        assertThrows(IllegalArgumentException.class, () -> format.format(new Object[0]));
    }

    @Test
    public void testMissingArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date} {1}", registry);
        Object[] arguments = {new Object[0]};
        assertThrows(IllegalArgumentException.class, () -> format.format(arguments));
    }

    @Test
    public void testInvalidFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new InvalidFormatFactory());
        assertThrows(RuntimeException.class, () -> new ExtendedMessageFormat("{0,date}", registry));
    }

    static class DateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new SimpleDateFormat(style, locale);
        }
    }

    static class CustomDateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new CustomDate();
        }
    }

    static class CustomNumberFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new DecimalFormat("#,##0.##");
        }
    }

    static class InvalidFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            throw new RuntimeException();
        }
    }

    static class CustomDate implements Format {
        private final String pattern;

        public CustomDate(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String format(Object obj, Locale locale) {
            return pattern;
        }

        @Override
        public Object parseObject(String text, ParsePosition parsePosition) {
            parsePosition.setErrorIndex(-1);
            parsePosition.setIndex(text.length());
            return null;
        }

        @Override
        public Class<?> getParameterTypes() {
            return Object[].class;
        }

        @Override
        public String toString() {
            return "CustomDate{" + pattern + '}';
        }
    }
}