import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "''";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE_FORMAT = '{';
    private static final char START_FE_ARGUMENT = '{';
    private static final char QUOTE = '\'';

    private ExtendedMessageFormat extFormat;
    private MessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        String pattern = "Hello {0, format, foo.MyFormat}{1}";
        Map<String, FormatFactory> registry = new HashMap<>();
        formatFactory = new MyFormatFactory();
        registry.put("foo.MyFormat", formatFactory);

        extFormat = new ExtendedMessageFormat(pattern, registry);
        messageFormat = new MessageFormat(DUMMY_PATTERN);
    }

    @Test
    public void testConstructors() {
        // Test constructor with pattern and default locale
        ExtendedMessageFormat extFormat1 = new ExtendedMessageFormat("{0}");
        Assert.assertNotNull(extFormat1);

        // Test constructor with pattern, locale, and empty registry
        ExtendedMessageFormat extFormat2 = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        Assert.assertNotNull(extFormat2);

        // Test constructor with pattern, locale, and registry
        ExtendedMessageFormat extFormat3 = new ExtendedMessageFormat("{0}", Locale.ENGLISH, new HashMap<>());
        Assert.assertNotNull(extFormat3);
    }

    @Test
    public void testToPattern() {
        String expected = extFormat.toPattern();
        Assert.assertEquals(expected, "Hello {0,format,foo.MyFormat}{1}");
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0, format, foo.MyFormat}{1}";
        ExtendedMessageFormat extFormat = new ExtendedMessageFormat(pattern, null, null);
        String input = "Hello {0} World";

        extFormat.applyPattern(input);

        String actual = extFormat.format(new Object[]{new Object(), "World"});
        String expected = "Hello {0} World";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extFormat.setFormat(0, MessageFormat.getIntegerFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extFormat.setFormatByArgumentIndex(0, MessageFormat.getIntegerFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        extFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[1];
        extFormat.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testGetFormat() {
        String input = "Hello {0, format, foo.MyFormat}{1}";
        String formatDescription = "MyCustomFormat";
        ExtendedMessageFormat extFormat = new ExtendedMessageFormat(input, null, null);

        FormatFactory formatFactory = new MyFormatFactory();
        formatFactory.registerFormat("MyCustomFormat", new MyCustomFormat());
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("foo.MyFormat", formatFactory);

        extFormat.applyPattern(input);
        extFormat.setRegistry(registry);

        Format expectedFormat = new MyCustomFormat();
        Format actualFormat = extFormat.getFormat(0);
        Assert.assertEquals(expectedFormat, actualFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElementIndex() {
        String pattern = "Hello {0, format, foo.MyFormat}{1, 2}";
        ExtendedMessageFormat extFormat = new ExtendedMessageFormat(pattern, null, null);

        try {
            extFormat.applyPattern(pattern);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unterminated format element at position 15", e.getMessage());
        }
    }

    @Test
    public void testFormatWithCustomFormats() {
        String pattern = "Hello {0, format, foo.MyFormat}{1}";
        ExtendedMessageFormat extFormat = new ExtendedMessageFormat(pattern, null, null);

        FormatFactory formatFactory = new MyFormatFactory();
        MyCustomFormat customFormat = new MyCustomFormat();
        formatFactory.registerFormat("MyCustomFormat", customFormat);
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("foo.MyFormat", formatFactory);

        extFormat.applyPattern(pattern);
        extFormat.setRegistry(registry);

        Object[] args = new Object[]{new Object(), "World"};
        String actual = extFormat.format(args);
        String expected = "Hello {0, format, MyCustomFormat}World";
        Assert.assertEquals(expected, actual);
    }

    private static class MyFormatFactory implements FormatFactory {

        private Map<String, Format> formats = new HashMap<>();

        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            Format format = formats.get(formatName);
            if (format == null) {
                format = new MyFormat();
                formats.put(formatName, format);
            }
            return format;
        }
    }

    private static class MyFormat implements Format {

        @Override
        public String format(Object object, Locale locale, String format, ParsePosition parsePosition) {
            return "CustomFormat";
        }

        @Override
        public ParsePosition parseObject(String source, ParsePosition pos) {
            return pos;
        }
    }

    private static class MyCustomFormat implements Format {

        @Override
        public String format(Object object, Locale locale, String format, ParsePosition parsePosition) {
            return "MyCustomFormat";
        }

        @Override
        public ParsePosition parseObject(String source, ParsePosition pos) {
            return pos;
        }
    }
}