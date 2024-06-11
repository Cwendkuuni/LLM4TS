import org.apache.commons.lang.TextUtils;
import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testDefaultConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        Assert.assertEquals("Hello {0}!", format.toPattern());
    }

    @Test
    public void testConstructionWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        Assert.assertEquals("Hello {0}!", format.toPattern());
    }

    @Test
    public void testConstructionWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:format}!", registry);
        Assert.assertEquals("Hello {customFormat:format}!", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0, choice, 0} {1, date, short}!";
        ExtendedMessageMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, null);

        format.applyPattern(pattern);
        Assert.assertEquals(pattern, format.toPattern());

        format.applyPattern("Hello {0, choice, 0} {1, date, medium}!");
        Assert.assertNotEquals(pattern, format.toPattern());
    }

    @Test
    public void testSetFormatsAndGetFormats() {
        FormatFactory formatFactory = new CustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:format}!", Locale.US, registry);
        Format[] formats = format.getFormats();
        Assert.assertArrayEquals(new Format[0], formats);

        format.setFormats(new Format[]{formatFactory.getFormat("customFormat", null, Locale.US)});
        formats = format.getFormats();
        Assert.assertArrayEquals(new Format[]{formatFactory.getFormat("customFormat", null, Locale.US)}, formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        format.setFormat(0, new MessageFormat("%d"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        format.setFormatByArgumentIndex(0, new MessageFormat("%d"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        format.setFormats(new Format[]{new MessageFormat("%d")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        format.setFormatsByArgumentIndex(new Format[]{new MessageFormat("%d")});
    }

    @Test
    public void testFormat() throws ParseException {
        FormatFactory formatFactory = new CustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:format}!", Locale.US, registry);
        format.applyPattern("Hello {customFormat:format}!");

        Object[] args = new Object[]{"Test"};
        String formatted = format.format(args);
        Assert.assertEquals("Hello Test!", formatted);
    }

    @Test
    public void testFormatWithEmptyArguments() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        format.applyPattern("Hello {0}!");

        Object[] args = new Object[0];
        String formatted = format.format(args);
        Assert.assertEquals("Hello !", formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidArgumentIndex() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.US);
        format.applyPattern("Hello {2}!");

        Object[] args = new Object[]{1, 2};
        format.format(args);
    }

    @Test
    public void testFormatWithEmptyRegistry() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:format}!", null);
        format.applyPattern("Hello {customFormat:format}!");

        Object[] args = new Object[]{"Test"};
        String formatted = format.format(args);
        Assert.assertEquals("Hello {customFormat:format}!", formatted);
    }

    @Test
    public void testFormatWithNullRegistry() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:format}!", null);
        format.applyPattern("Hello {customFormat:format}!");

        Object[] args = new Object[]{"Test"};
        try {
            format.format(args);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String pattern, Locale locale) {
            return new MessageFormat(pattern, locale);
        }
    }
}