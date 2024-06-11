import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.apache.commons.lang.text.SimpleFormatFactory;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        String result = format.format(new Object[]{"World"});
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testLocalizedConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", Locale.FRENCH);
        String result = format.format(new Object[]{"World"});
        Assert.assertEquals("Hello World!", result); // Should be the same as default construction for simplicity

        // Localized construction with registry
        FormatFactory formatFactory = new SimpleFormatFactory();
        ExtendedMessageFormat localizedFormat = new ExtendedMessageFormat("Hello {0}!", Locale.FRENCH, Collections.singletonMap("CustomFormatKey", formatFactory));
        result = localizedFormat.format(new Object[]{"World"});
        Assert.assertEquals("Hello World!", result); // Should be the same as default construction for simplicity

        // Test localized construction with custom format
        Format customFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
        formatFactory = new SimpleFormatFactory().withFormat("CustomFormatKey", customFormat);
        ExtendedMessageFormat localizedFormatWithCustomFormat = new ExtendedMessageFormat("{CustomFormatKey, date, dd-MM-yyyy} {0}!", Locale.FRENCH, Collections.singletonMap("CustomFormatKey", formatFactory));
        Object[] args = new Object[]{"World", new java.util.Date()};
        result = localizedFormatWithCustomFormat.format(args);
        String expected = String.format("{%1$td-%1$tm-%1$ty} %1$s!", new Object[]{"World", args[1]});
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRegistryConstruction() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {CustomFormatKey, number, #, ##0}{0}!", Collections.singletonMap("CustomFormatKey", formatFactory));
        Object[] args = new Object[]{Integer.valueOf(42), Integer.valueOf(100)};
        String result = format.format(args);
        String expected = String.format("Hello 4200{0}!", new Object[]{"100"});
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadPatternConstruction() {
        new ExtendedMessageFormat("Hello {0} {1}!"); // Missing registry
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatNameConstruction() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        new ExtendedMessageFormat("Hello {malformedFormatKey}!", Collections.singletonMap("malformedFormatKey", formatFactory));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatNameConstruction() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        new ExtendedMessageFormat("Hello {0} {1}!", Collections.singletonMap(null, formatFactory));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFormatNameConstruction() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        new ExtendedMessageFormat("Hello {0} {1}!", Collections.singletonMap("", formatFactory));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatchingFormatConstruction() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        new ExtendedMessageFormat("Hello {MalformedFormatKey}!", Collections.singletonMap("MalformedFormatKey", formatFactory));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateFormatNameConstruction() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("FormatKey", formatFactory);
        registry.put("FormatKey", formatFactory); // Duplicate
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {FormatKey}!", registry);
    }

    @Test
    public void testFormatFactoryReturnNullFormat() {
        FormatFactory formatFactory = new SimpleFormatFactory() {
            @Override
            public Format getFormat(String formatName, String format, Locale locale) {
                return null;
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {FormatKey}!", Collections.singletonMap("FormatKey", formatFactory));
        Assert.assertNull(format.getFormats());
        try {
            format.applyPattern("Hello {FormatKey}!");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetFormats() {
        FormatFactory formatFactory = new SimpleFormatFactory();
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {FormatKey}!", Collections.singletonMap("FormatKey", formatFactory));
        Format[] formats = format.getFormats();
        Assert.assertNotNull(formats);
        Assert.assertEquals(1, formats.length);
        Assert.assertEquals(formatFactory, formats[0].getClass().cast(formats[0]));
    }

    @Test
    public void testApplyPatternWithRegistry() throws ParseException {
        FormatFactory formatFactory = new SimpleFormatFactory();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{FormatKey, date, MM/dd/yyyy h:mm a} {0}!", Collections.singletonMap("FormatKey", formatFactory), new Locale("en", "US"));
        format.applyPattern("{FormatKey, date, MM/dd/yyyy h:mm a} {0}!");
        ParsePosition pos = new ParsePosition(0);
        Object[] args = new Object[]{"World", new java.util.Date()};
        String formatted = format.format(args);
        SimpleDateFormat expectedDateFormat = formatFactory.getFormat("FormatKey", null, new Locale("en", "US"));
        String expected = String.format("{%1$tm/%1$td/%1$ty %1$tb:%1$ta %1$tp %1$tz} %1$s!", new Object[]{"World", args[1]});
        Assert.assertEquals(expected, formatted);
        String formattedDateOnly = expectedDateFormat.format(args[1]);
        Assert.assertEquals(formattedDateOnly, format.format(args[1]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", new Locale("en", "US"));
        format.setFormat(0, new SimpleDateFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", new Locale("en", "US"));
        format.setFormatByArgumentIndex(0, new SimpleDateFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", new Locale("en", "US"));
        format.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!", new Locale("en", "US"));
        format.setFormatsByArgumentIndex(new Format[0]);
    }
}

class SimpleFormatFactory implements FormatFactory {

    @Override
    public Format getFormat(String formatName, String format, Locale locale) {
        if ("number".equals(formatName)) {
            return new DecimalFormat("#,###.##");
        }
        if ("date".equals(formatName)) {
            return new SimpleDateFormat("MM/dd/yyyy h:mm a");
        }
        throw new IllegalArgumentException("Invalid format name: " + formatName);
    }
}