import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.getFormat(0));
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        assertEquals("{0} {1}", format.toPattern());
        assertNull(format.getFormat(0));
        assertNull(format.getFormat(1));
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), Locale.ENGLISH);
        assertEquals("{0} {1}", format.toPattern());
        assertNull(format.getFormat(0));
        assertNull(format.getFormat(1));
    }

    @Test
    public void testConstructorWithRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MessageFormat(pattern, style, locale);
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        assertEquals("{0} {1}", format.toPattern());
        assertEquals(MessageFormat.class, format.getFormat(0).getClass());
        assertEquals(MessageFormat.class, format.getFormat(1).getClass());
    }

    @Test
    public void testApplyPatternWithNoCustomFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        format.applyPattern("{0} {1}");
        assertEquals("{0} {1}", format.toPattern());
        assertNull(format.getFormat(0));
        assertNull(format.getFormat(1));
    }

    @Test
    public void testApplyPatternWithCustomFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MyFormat();
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        format.applyPattern("{0} {1}");
        assertEquals("{0} {1}", format.toPattern());
        assertEquals(MyFormat.class, format.getFormat(0).getClass());
        assertEquals(MyFormat.class, format.getFormat(1).getClass());
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MessageFormat(pattern, style, locale);
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        format.applyPattern("{0} {1}");
        Format originalFormat = format.getFormat(0);

        format.setFormatByArgumentIndex(0, new MyFormat());
        assertNotEquals(originalFormat, format.getFormat(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        format.setFormat(0, new MyFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        Format[] formats = new Format[2];
        formats[0] = new MyFormat();
        formats[1] = new MyFormat();
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        Format[] formats = new Format[2];
        formats[0] = new MyFormat();
        formats[1] = new MyFormat();
        format.setFormatsByArgumentIndex(formats);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
        format.applyPattern("{0} {1} {2}"); // Invalid pattern
    }

    @Test
    public void testFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MessageFormat(pattern, style, locale);
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        format.applyPattern("{0} {1}");

        String expected = "Hello {0} World!";
        Object[] args = {"Hello", "World!"};

        String result = format.format(args);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithCustomFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MyFormat();
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        format.applyPattern("{0} {1}");

        String expected = "Hello {} World!";
        Object[] args = {"Hello", "World!"};

        String result = format.format(args);
        assertEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MessageFormat(pattern, style, locale);
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        format.applyPattern("{0} {1}");

        Object[] args = {"Hello", "World!"};
        ParsePosition pos = new ParsePosition(0);

        format.parse(args, pos);
        assertEquals(2, pos.getIndex());

        String result = format.format(args);
        assertEquals("Hello World!", result);
    }

    @Test
    public void testParseWithCustomFormat() throws ParseException {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String pattern, String style, Locale locale) {
                return new MyFormat();
            }
        };

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>(), formatFactory);
        format.applyPattern("{0} {1}");

        Object[] args = {"Hello", "World!"};
        ParsePosition pos = new ParsePosition(0);

        format.parse(args, pos);
        assertEquals(2, pos.getIndex());

        String result = format.format(args);
        assertEquals("Hello {} World!", result);
    }

    private static class FormatFactory {
        public Format getFormat(String pattern, String style, Locale locale) {
            return new MessageFormat(pattern, style, locale);
        }
    }

    private static class MyFormat extends MessageFormat {
        public MyFormat() {
            super("", Locale.getDefault());
        }

        @Override
        public String format(Object arg0, Object[] arg1, String arg2, Locale arg3) {
            return "{}";
        }
    }
}