package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatArgs, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0(MyFormat)} {1}", registry);
        assertEquals("{0(MyFormat)} {1}", fmt.toPattern());
        assertNull(fmt.getFormat(0));
        assertNull(fmt.getFormat(1));

        String pattern = "{0(MyFormat)} {1} {2(AnotherFormat)}";
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(pattern, registry);
        assertEquals("{0(MyFormat)} {1} {2(AnotherFormat)}", fmt2.toPattern());
        assertNull(fmt2.getFormat(0));
        assertNull(fmt2.getFormat(1));
        assertNull(fmt2.getFormat(2));

        registry.put("AnotherFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatArgs, Locale locale) {
                return new AnotherFormat();
            }
        });

        Format[] formats = fmt2.getFormats();
        assertArrayEquals(new Format[0], formats);

        fmt2.applyPattern(pattern);
        formats = fmt2.getFormats();
        assertArrayEquals(new Format[]{new MyFormat(), new AnotherFormat()}, formats);
    }

    @Test
    public void testExtendedMessageFormatWithFormatElements() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatArgs, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0(MyFormat)} {1}", registry);
        fmt.applyPattern("{0(MyFormat)} {1}");
        Format[] formats = fmt.getFormats();
        assertArrayEquals(new Format[]{new MyFormat()}, formats);

        MyFormat myFormat = new MyFormat();
        MessageFormat mf = new MessageFormat("{0}", myFormat);
        assertEquals("{0}", mf.toPattern());
        fmt.setFormat(0, mf);

        String pattern = "{0(MyFormat)} {1(AnotherFormat)}";
        fmt.applyPattern(pattern);
        formats = fmt.getFormats();
        assertArrayEquals(new Format[]{new MyFormat(), new AnotherFormat()}, formats);
        assertSame(myFormat, formats[0]);
        assertSame(new AnotherFormat(), formats[1]);

        PatternFormat anotherFormat = new PatternFormat("{1}");
        fmt.setFormat(1, anotherFormat);

        String formatted = fmt.format(new Object[]{"Hello", "World"});
        assertEquals("Hello{1}World", formatted);
    }

    @Test
    public void testExtendedMessageFormatWithEmptyRegistry() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        fmt.applyPattern("{0} {1}");
        Format[] formats = fmt.getFormats();
        assertArrayEquals(new Format[0], formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", new HashMap<>());
        fmt.setFormat(0, new Object(){});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", new HashMap<>());
        Format[] formats = new Format[1];
        fmt.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", new HashMap<>());
        Format[] formats = new Format[1];
        fmt.setFormatsByArgumentIndex(formats);
    }

    static class MyFormat extends Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public StringBuffer format(MessageFormat mf, Object obj, StringBuffer toAppendTo,
                FieldPosition pos) {
            throw new UnsupportedOperationException();
        }
    }

    static class AnotherFormat extends Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public StringBuffer format(MessageFormat mf, Object obj, StringBuffer toAppendTo,
                FieldPosition pos) {
            throw new UnsupportedOperationException();
        }
    }
}

class FormatFactory {
    public abstract Format getFormat(String formatName, String formatArgs, Locale locale);
}

class PatternFormat extends Format {
    private final String pattern;

    public PatternFormat(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StringBuffer format(MessageFormat mf, Object obj, StringBuffer toAppendTo,
            FieldPosition pos) {
        toAppendTo.append(pattern);
        return toAppendTo;
    }
}