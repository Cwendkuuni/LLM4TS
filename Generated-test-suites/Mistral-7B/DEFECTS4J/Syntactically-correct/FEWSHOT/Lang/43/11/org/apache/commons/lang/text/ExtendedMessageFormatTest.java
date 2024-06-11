package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.Util;
import org.apache.commons.lang.Validate;
import org.apache.commons.text.StrMatcher;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstanceWithDefaults() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test pattern");
        assertEquals("Test pattern", format.toPattern());
    }

    @Test
    public void testNewInstanceWithCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyCustomFormat", (formatName, formatStyle, locale) -> new MyCustomFormat());

        ExtendedMessageFormat format = new ExtendedMessageFormat("Test pattern", registry);
        assertEquals("Test pattern", format.toPattern());

        String pattern = format.toPattern();
        assertTrue(pattern.contains("{0(MyCustomFormat)}"));
    }

    @Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        String toPattern = format.toPattern();
        assertEquals("", toPattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test{pattern}");
    }

    @Test
    public void testApplyPatternWithQuotedStrings() {
        String pattern = "{0('{}')}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());

        String formatted = format.format(new Object[]{"foo"});
        assertEquals("foo", formatted);
    }

    @Test
    public void testApplyPatternWithCustomFormatElements() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (formatName, formatStyle, locale) -> new CustomFormat());

        String pattern = "{0(CustomFormat)}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());

        String formatted = format.format(new Object[]{"foo"});
        assertNull(formatted);
    }

    @Test
    public void testSetFormatsThrowUnsupportedOperationException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test pattern");
        try {
            format.setFormats(new Format[0]);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndexThrowUnsupportedOperationException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test pattern");
        try {
            format.setFormatByArgumentIndex(0, new CustomFormat());
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatThrowUnsupportedOperationException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test pattern");
        try {
            format.setFormat(0, new CustomFormat());
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    private static class MyCustomFormat implements Format {
        @Override
        public Object format(Object obj, String pattern, Locale locale) {
            return "Custom format";
        }

        @Override
        public ParsePosition parseObject(String source, ParsePosition pos) {
            return new ParsePosition(0);
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public Object format(Object obj, String pattern, Locale locale) {
            return null;
        }

        @Override
        public ParsePosition parseObject(String source, ParsePosition pos) {
            return new ParsePosition(0);
        }
    }
}