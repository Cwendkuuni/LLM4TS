import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", new CustomFormatFactory());
        messageFormat = new ExtendedMessageFormat("{0,format,custom={CustomFormat}}", Locale.getDefault(), registry);
        formatFactory = new CustomFormatFactory();
    }

    @Test
    public void testConstructors() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("{0}");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("{0}", Locale.US);
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("{0}", Locale.US, null);
        ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat("{0}", Locale.US, new HashMap<>());
        ExtendedMessageFormat messageFormat5 = new ExtendedMessageFormat("{0,format,custom=CustomFormat}", Locale.US, new HashMap<>());

        Assert.assertNotNull(messageFormat1);
        Assert.assertNotNull(messageFormat2);
        Assert.assertNotNull(messageFormat3);
        Assert.assertNotNull(messageFormat4);
        Assert.assertNotNull(messageFormat5);
    }

    @Test
    public void testToPattern() {
        Assert.assertEquals("{0}", messageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        messageFormat.setFormat(0, formatFactory.getFormat("CustomFormat"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        messageFormat.setFormatByArgumentIndex(0, formatFactory.getFormat("CustomFormat"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        formats[0] = formatFactory.getFormat("CustomFormat");
        messageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[1];
        formats[0] = formatFactory.getFormat("CustomFormat");
        messageFormat.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testApplyPattern() throws ParseException {
        String pattern = "{0,format,custom=CustomFormat}{1}";
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{"Hello", "World"};
        String formattedMessage = extendedMessageFormat.format(args);
        Assert.assertEquals("Hello{0}World", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() throws ParseException {
        String pattern = "{0,format,custom=CustomFormat}";
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{"Hello"};
        extendedMessageFormat.format(args);
    }

    @Test
    public void testFormat() throws ParseException {
        String pattern = "{0,format,custom=CustomFormat}{1}";
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{"Hello", "World"};
        String formattedMessage = extendedMessageFormat.format(args);
        Assert.assertEquals("HelloWorld", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() throws ParseException {
        String pattern = "{0,format,custom=CustomFormat}{2}";
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{"Hello", "World"};
        extendedMessageFormat.format(args);
    }

    @Test
    public void testFormatWithCustomFormat() throws ParseException {
        String pattern = "{0,format,custom=CustomFormat}{1}";
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{"Hello", "World"};
        formatFactory.registerFormat("CustomFormat", new CustomFormat());
        String formattedMessage = extendedMessageFormat.format(args);
        Assert.assertEquals("Hello[World]", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() throws ParseException {
        String pattern = "{0,format,custom=CustomFormat}";
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{"Hello"};
        extendedMessageFormat.format(args);
    }
}

class CustomFormatFactory implements FormatFactory {

    @Override
    public Format getFormat(String formatName, String formatStyle, Locale locale) {
        return new CustomFormat();
    }
}

class CustomFormat implements Format {

    @Override
    public String format(Object obj) {
        return "[{}]";
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return null;
    }
}