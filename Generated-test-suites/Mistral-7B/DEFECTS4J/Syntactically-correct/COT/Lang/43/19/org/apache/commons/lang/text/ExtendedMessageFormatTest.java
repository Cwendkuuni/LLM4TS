package org.apache.commons.lang.test;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                if ("CustomFormat".equals(name)) {
                    return new CustomFormat();
                }
                throw new UnsupportedOperationException();
            }
        };

        messageFormat = new ExtendedMessageFormat("{0, format=\"CustomFormat{1}\"}", new HashMap<>()) {
            @Override
            public void applyPattern(String pattern) {
                super.applyPattern(pattern);
                this.registry = new HashMap<>(System.mapSizes(super.registry));
                this.registry.put("CustomFormat", formatFactory);
                super.applyPattern(pattern);
            }
        };
    }

    @Test
    public void testEmptyFormat() {
        String format = messageFormat.toPattern();
        assertEquals("", format);
    }

    @Test
    public void testBasicFormat() {
        String format = messageFormat.toPattern();
        assertEquals("{0}", format);

        Object[] args = new Object[] {"Test"};
        String message = messageFormat.format(args);
        assertEquals("Test", message);
    }

    @Test
    public void testCustomFormat() throws ParseException {
        String format = messageFormat.toPattern();
        assertEquals("{0, format=\"CustomFormat{1}\"}", format);

        Object[] args = new Object[] {"Test", "World"};
        String message = messageFormat.format(args);
        assertEquals("Test{1}World", message);

        CustomFormat customFormat = (CustomFormat) messageFormat.getFormats()[0];
        assertEquals("CustomFormat{}", customFormat.pattern);
        assertEquals("CustomFormat", customFormat.name);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        messageFormat.setFormat(0, new StringFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        messageFormat.setFormatByArgumentIndex(0, new StringFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        formats[0] = new StringFormat();
        messageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[1];
        formats[0] = new StringFormat();
        messageFormat.setFormatsByArgumentIndex(formats);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedFormat() {
        messageFormat = new ExtendedMessageFormat("{0, format=\"CustomFormat{1}", new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatName() {
        messageFormat = new ExtendedMessageFormat("{0, format=\"{1}\"}", new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatStyle() {
        messageFormat = new ExtendedMessageFormat("{0, format=\"CustomFormat\"}", new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatValue() {
        messageFormat = new ExtendedMessageFormat("{0, format=\"CustomFormat{1}InvalidFormat\"}", new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingRegistry() {
        try {
            new ExtendedMessageFormat("{0, format=\"CustomFormat\"}", null);
        } catch (IllegalArgumentException e) {
            assertEquals("registry is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testCustomFormatWithNullRegistry() {
        String format = new ExtendedMessageFormat("{0, format=\"CustomFormat{1}\"}", null).toPattern();
        assertEquals("{0}", format);
    }

    private static class CustomFormat extends Format {
        String pattern;

        public CustomFormat() {
            pattern = "{}";
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer to, FieldPosition pos) {
            to.append(pattern);
            return to;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StringFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer to, FieldPosition pos) {
            to.append("StringFormat");
            return to;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }
    }
}
