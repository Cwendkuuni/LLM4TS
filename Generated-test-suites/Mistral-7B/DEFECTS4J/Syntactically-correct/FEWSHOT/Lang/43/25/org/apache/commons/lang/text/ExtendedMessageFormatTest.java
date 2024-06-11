package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.Util;
import org.apache.commons.lang.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmpty() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", null);
        String formatted = format.format(new Object[0], Locale.getDefault());
        assertEquals("", formatted);
    }

    @Test
    public void testBasic() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new MessageFormat(formatName, locale);
            }
        };
        registry.put("MyFormat", factory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, MyFormat}", Locale.getDefault(), registry);
        String formatted = format.format(new String[]{"test"}, Locale.getDefault());
        assertEquals("test", formatted);
    }

    @Test
    public void testNestedFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                if (formatName.equals("MyFormat")) {
                    return new MessageFormat("{0, choice, 0, {0}} {1}", Locale.getDefault());
                } else {
                    return new MessageFormat(formatName, locale);
                }
            }
        };
        registry.put("MyFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, choice, 0, {MyFormat, 1}} {2}", Locale.getDefault(), registry);
        String[] args = {"foo", "bar"};
        String formatted = format.format(args, Locale.getDefault());
        assertEquals("foo bar", formatted);

        args = new String[]{"bar", "foo"};
        formatted = format.format(args, Locale.getDefault());
        assertEquals("foo bar", formatted);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        format.setFormat(0, new MessageFormat("{1}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        format.setFormatByArgumentIndex(0, new MessageFormat("{1}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        Format[] formats = new Format[]{new MessageFormat("{1}"), new MessageFormat("{2}")};
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        Format[] formats = new Format[]{new MessageFormat("{1}"), new MessageFormat("{2}")};
        format.setFormatsByArgumentIndex(new Format[0], formats);
    }

}

class FormatFactory {
    public Format getFormat(String formatName, String formatStyle, Locale locale) {
        return null;
    }
}