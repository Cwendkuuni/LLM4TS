package org.apache.commons.lang.text;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.FormatFactory;
import org.apache.commons.cli.SimpleFormatFactory;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNotNull(format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadPattern() {
        new ExtendedMessageFormat("{");
    }

    @Test
    public void testSetLocale() {
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", locale);
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testApplyPattern_EmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}", Locale.US);
        assertEquals("{0, date, short}", format.toPattern());
    }

    @Test
    public void testApplyPattern_SimpleFormatFactoryRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}", Locale.US, registry);
        assertEquals("{0, date, short}", format.toPattern());

        Object[] args = {"2023-02-14"};
        String result = format.format(args);
        assertEquals("Feb 14, 2023", result);
    }

    @Test
    public void testApplyPattern_CustomFormatFactoryRegistry() {
        FormatFactory customFormatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("custom".equals(name)) {
                    return new CustomFormat();
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleFormatFactory());
        registry.put("custom", customFormatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, custom}", Locale.US, registry);
        assertEquals("{0, custom}", format.toPattern());

        Object[] args = {"2023-02-14"};
        String result = format.format(args);
        assertEquals("Custom format applied", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        format.setFormat(0, new MessageFormat("%d"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        format.setFormatByArgumentIndex(0, new MessageFormat("%d"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        Format[] formats = {new MessageFormat("%d")};
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        Format[] formats = {new MessageFormat("%d")};
        format.setFormatsByArgumentIndex(formats);
    }

    private static class CustomFormat extends Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) throws ParseException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            return "Custom format applied";
        }
    }

}