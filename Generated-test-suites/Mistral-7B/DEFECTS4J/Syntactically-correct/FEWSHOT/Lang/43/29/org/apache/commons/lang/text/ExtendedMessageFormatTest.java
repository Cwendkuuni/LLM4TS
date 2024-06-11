package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateWithDefaultRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        assertTrue(format instanceof ExtendedMessageFormat);
        assertEquals("", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidPattern() {
        new ExtendedMessageFormat("{0");
    }

    @Test
    public void testCreateWithLocaleAndDefaultRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Locale.ENGLISH);
        assertEquals("{0} {1}", format.toPattern());
    }

    @Test
    public void testCreateWithRegistry() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{custom.formatName({0})} {1}", registry);
        assertEquals("{custom.formatName({0})} {1}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithQuotes() {
        String pattern = "{0} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String[] args = {"arg0", "arg1"};
        Object[] objects = {"test0", "test1"};
        Object result = format.format(objects);
        assertTrue(result instanceof String);
        String formatted = (String) result;
        assertTrue(formatted.startsWith("test0 "));
        assertTrue(formatted.endsWith(" test1"));
    }

    @Test
    public void testApplyPatternWithFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{custom.formatName({0})} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String[] args = {"arg0"};
        CustomFormat customFormat = new CustomFormat();
        Format formatResult = format.format(new Object[]{args[0]}, customFormat);
        assertTrue(formatResult instanceof CustomFormat);
        assertEquals("formattedArg0", formatResult.toString());
    }

    @Test
    public void testApplyPatternWithFormatNameAndStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{custom.formatName({0}, style)} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String[] args = {"arg0"};
        CustomFormat customFormat = new CustomFormat();
        Format formatResult = format.format(new Object[]{args[0]}, customFormat, "style");
        assertTrue(formatResult instanceof CustomFormat);
        assertEquals("formattedArg0_style", formatResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{nonExistent.formatName({0})} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{custom.formatName({0})} {custom.formatName({1}, missingStyle)}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testSetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{custom.formatName({0})} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        CustomFormat customFormat = new CustomFormat();
        format.setFormatByArgumentIndex(0, customFormat);
        Object[] objects = {"arg0"};
        Object result = format.format(objects);
        assertTrue(result instanceof CustomFormat);
        assertEquals("formattedArg0", result.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexWithInvalidIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{custom.formatName({0})} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormatByArgumentIndex(-1, new CustomFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        String pattern = "{custom.formatName({0})} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Format[] formats = new Format[1];
        formats[0] = new CustomFormat();
        format.setFormats(formats);
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new CustomFormat();
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public String format(Object obj, Locale locale) {
            String arg = (String) obj;
            return "formatted" + arg;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public String toString() {
            return "CustomFormat";
        }

        @Override
        public StringBuffer format(MessageFormat formatter, Object[] argArray,
                MessageFormat.FieldPosition fieldPosition) {
            return null;
        }

        @Override
        public int parse(MessageFormat formatter, String source, ParsePosition pos) {
            return 0;
        }

        @Override
        public int parse(MessageFormat formatter, String source, ParsePosition pos, Object[] args) {
            return 0;
        }
    }
}