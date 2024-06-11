import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("");
        assertNotNull(messageFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternInDefaultConstructor() {
        new ExtendedMessageFormat("Invalid pattern");
    }

    @Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", Locale.US);
        assertNotNull(messageFormat);
    }

    @Test
    public void testLocaleAndRegistryConstructor() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new SimpleFormat();
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0, format}", null, Map.of("format", formatFactory));
        assertNotNull(messageFormat);
    }

    @Test
    public void testRegistryConstructor() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new SimpleFormat();
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", null, Map.of("format", formatFactory));
        assertNotNull(messageFormat);
    }

    @Test
    public void testInvalidRegistryConstructor() {
        expectThrow(IllegalArgumentException.class, () -> new ExtendedMessageFormat("{0}", null, null));
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0, format}", Locale.US);
        String pattern = messageFormat.toPattern();
        assertEquals("{0, format}", pattern);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0, format={}}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, null, null);
        String expected = "{0}";
        messageFormat.applyPattern(expected);
        assertEquals(expected, messageFormat.toPattern());
    }

    @Test
    public void testGetFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                return new SimpleFormat();
            }
        };
        Map<String, FormatFactory> registry = Map.of("format", formatFactory);
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0, format={format}}", null, registry);
        Format format = messageFormat.getFormat(0);
        assertTrue(format instanceof SimpleFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", null);
        messageFormat.setFormat(0, new SimpleFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", null);
        messageFormat.setFormatsByArgumentIndex(new SimpleFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", null);
        messageFormat.setFormats(new SimpleFormat[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", null);
        messageFormat.setFormatsByArgumentIndex(new SimpleFormat[0]);
    }

    static class SimpleFormat implements Format {
        @Override
        public String format(Object object, Locale locale, String format, ParsePosition parsePosition) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "SimpleFormat";
        }
    }
}