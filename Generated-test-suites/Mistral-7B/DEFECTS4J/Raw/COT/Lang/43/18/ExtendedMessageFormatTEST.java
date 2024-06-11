import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new DummyFormat(formatName, formatStyle);
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DUMMY", formatFactory);
        format = new ExtendedMessageFormat("{0, format=\"DUMMY\", style=\"test\"}{1}", registry);
    }

    @Test
    public void testConstructorWithPattern() {
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        assertEquals("{0, format=\"DUMMY\", style=\"test\"}{1}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0, format=\"DUMMY\", style=\"test\"}{1}";
        format.applyPattern(pattern);
        assertEquals("{0, format=\"DUMMY\", style=\"test\"}{1}", format.toPattern());
    }

    @Test
    public void testInvalidPattern() {
        assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat("invalid pattern"));
    }

    @Test
    public void testNullPattern() {
        assertThrows(NullPointerException.class, () -> new ExtendedMessageFormat(null));
    }

    @Test
    public void testSetLocale() {
        format.setLocale(Locale.US);
        assertEquals("{0, format=\"DUMMY\", style=\"test\"}{1}", format.toPattern());
        format.applyPattern("{0, format=\"DUMMY\", style=\"test\"}{1}");
        assertEquals("{0, format=\"DUMMY\", style=\"test\"}{1}", format.toPattern());
        assertEquals("{0, format=\"DUMMY\", style=\"test\"}{1}", format.format(new Object[]{"Test1", "Test2"}));
    }

    @Test
    public void testSetRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DUMMY2", new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new DummyFormat("DUMMY2", formatStyle);
            }
        });
        format = new ExtendedMessageFormat("{0, format=\"DUMMY\", style=\"test\"}{1}", registry);
        format.applyPattern("{0, format=\"DUMMY\", style=\"test\"}{1}");
        assertEquals("{0, format=\"DUMMY\", style=\"test\"}{1}", format.toPattern());
        format.setLocale(Locale.US);
        assertEquals("{0, format=\"DUMMY2\", style=\"test\"}{1}", format.toPattern());
        format.applyPattern("{0, format=\"DUMMY2\", style=\"test\"}{1}");
        assertEquals("{0, format=\"DUMMY2\", style=\"test\"}{1}", format.toPattern());
        assertEquals("{0, format=\"DUMMY2\", style=\"test\"}{1}", format.format(new Object[]{"Test1", "Test2"}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        format.setFormat(0, new DummyFormat("DUMMY3", "test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        format.setFormatByArgumentIndex(0, new DummyFormat("DUMMY3", "test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        format.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        format.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testFormat() {
        String[] args = {"Test1", "Test2"};
        String expected = "Test1Test2";
        String result = format.format(args);
        assertTrue(result.equals(expected) || result.equals(expected.replaceAll("\\s", "")));
    }

    @Test(expected = ParseException.class)
    public void testFormatWithInvalidArgumentIndex() {
        format.format(new Object[]{new Object[]{"Test1"}, 1});
    }

    @Test
    public void testFormatWithEmptyArguments() {
        String[] args = {};
        String result = format.format(args);
        assertNull(result);
    }

    @Test
    public void testFormatWithNullArguments() {
        String[] args = {null};
        String result = format.format(args);
        assertNull(result);
    }

    static class DummyFormat implements Format {
        private final String formatName;
        private final String formatStyle;

        public DummyFormat(String formatName, String formatStyle) {
            this.formatName = formatName;
            this.formatStyle = formatStyle;
        }

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) throws ParseException {
            if (parsePosition != null) {
                parsePosition.setIndex(parsePosition.getIndex() + format.length());
            }
            return formatName;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer sb, Locale locale) {
            sb.append(formatName);
            return sb;
        }

        @Override
        public synchronized Object parseObject(String source, ParsePosition pos) throws ParseException {
            pos.setIndex(pos.getIndex() + formatName.length());
            return null;
        }

        @Override
        public synchronized ParsePosition parse(String source, ParsePosition pos, Object param) throws ParseException {
            pos.setIndex(pos.getIndex() + formatName.length());
            return pos;
        }

        @Override
        public synchronized String format(Object obj, Locale locale) {
            return formatName;
        }

        @Override
        public synchronized String parse(String source, ParsePosition pos) throws ParseException {
            pos.setIndex(pos.getIndex() + formatName.length());
            return formatName;
        }

        @Override
        public synchronized StringBuffer format(Object obj, StringBuffer sb, String format, Locale locale) {
            sb.append(formatName);
            return sb;
        }

        @Override
        public synchronized Object parseObject(String source, ParsePosition pos) throws ParseException {
            pos.setIndex(pos.getIndex() + formatName.length());
            return null;
        }

        @Override
        public synchronized ParsePosition parse(String source, ParsePosition pos) throws ParseException {
            pos.setIndex(pos.getIndex() + formatName.length());
            return pos;
        }
    }
}