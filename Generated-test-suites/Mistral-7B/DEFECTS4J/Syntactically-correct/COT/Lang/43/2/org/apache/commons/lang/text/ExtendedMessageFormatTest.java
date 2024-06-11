import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test cases for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    

    /**
     * Test constructor with default locale and empty pattern.
     */
    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        Assert.assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with default locale, empty pattern, and registry.
     */
    @Test
    public void testDefaultConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        Assert.assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with default locale, custom pattern, and registry.
     */
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format=\"customFormat\"}", registry);
        Assert.assertEquals("{0,format=\"customFormat\"}", format.toPattern());
    }

    /**
     * Test constructor with locale, empty pattern, and registry.
     */
    @Test
    public void testConstructorWithLocaleAndEmptyPattern() {
        Locale locale = Locale.forLanguageTag("fr_FR");
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry, locale);
        Assert.assertEquals(locale, format.getLocale());
        Assert.assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with locale, custom pattern, and registry.
     */
    @Test
    public void testConstructorWithLocalePatternAndRegistry() {
        Locale locale = Locale.forLanguageTag("fr_FR");
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format=\"customFormat\"}", registry, locale);
        Assert.assertEquals(locale, format.getLocale());
        Assert.assertEquals("{0,format=\"customFormat\"}", format.toPattern());
    }

    /**
     * Test constructor with locale, custom pattern, and registry, and invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPatternAndRegistry() {
        Locale locale = Locale.forLanguageTag("fr_FR");
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        new ExtendedMessageFormat("{0,format=\"customFormat\"}", registry, locale, "InvalidPattern");
    }

    /**
     * Test toPattern() method.
     */
    @Test
    public void testToPattern() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format=\"customFormat\"}", registry);
        Assert.assertEquals("{0,format=\"customFormat\"}", format.toPattern());
    }

    /**
     * Test applyPattern() method with empty pattern.
     */
    @Test
    public void testApplyPatternWithEmptyPattern() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        format.applyPattern("{0,format=\"customFormat\"}");
        Assert.assertEquals("{0,format=\"customFormat\"}", format.toPattern());
    }

    /**
     * Test applyPattern() method with custom pattern.
     */
    @Test
    public void testApplyPatternWithCustomPattern() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        format.applyPattern("{0,format=\"customFormat\"}");
        Assert.assertEquals("{0,format=\"customFormat\"}", format.toPattern());
    }

    /**
     * Test applyPattern() method with invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        format.applyPattern("{0,format=\"customFormat\"}InvalidPattern");
    }

    /**
     * Test getFormat() method with registered format.
     */
    @Test
    public void testGetFormatWithRegisteredFormat() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format=\"customFormat\"}", registry);
        Format result = format.getFormat("customFormat");
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof CustomFormat);
    }

    /**
     * Test getFormat() method with unregistered format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithUnregisteredFormat() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format=\"customFormat\"}", registry);
        format.getFormat("UnregisteredFormat");
    }

    /**
     * Test setFormat() method.
     * @throws UnsupportedOperationException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        format.setFormat(0, new Format() {});
    }

    /**
     * Test setFormatByArgumentIndex() method.
     * @throws UnsupportedOperationException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    /**
     * Test setFormats() method.
     * @throws UnsupportedOperationException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormats(formats);
    }

    /**
     * Test setFormatsByArgumentIndex() method.
     * @throws UnsupportedOperationException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormatsByArgumentIndex(0, formats);
    }

    /**
     * Test applyPattern() method with InvalidRegistry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format=\"customFormat\"}", null);
        format.applyPattern("{0,format=\"customFormat\"}");
    }

    /**
     * Test containsElements() method.
     */
    @Test
    public void testContainsElements() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat1", new CustomFormatFactory1());
        registry.put("customFormat2", null);
        Assert.assertTrue(ExtendedMessageFormat.containsElements(registry));
    }

    /**
     * Test insertFormats() method.
     */
    @Test
    public void testInsertFormats() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat1", new CustomFormatFactory1());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        format.applyPattern("{0,format=\"customFormat1\"}");
        String pattern = format.toPattern();
        Assert.assertEquals("{0,format=customFormat1}", pattern);
    }

    /**
     * Test applyPattern() method with invalid argument index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidArgumentIndex() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        format.applyPattern("{1,format=\"customFormat\"}");
    }

    /**
     * Test parseFormatDescription() method with custom format description.
     */
    @Test
    public void testParseFormatDescriptionWithCustomFormatDescription() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        String formatDescription = "format=\"customFormat\"";
        Format result = format.getFormat(formatDescription);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof CustomFormat);
    }

    /**
     * Test parseFormatDescription() method with invalid format description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidFormatDescription() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        String formatDescription = "InvalidFormatDescription";
        format.getFormat(formatDescription);
    }

    /**
     * Test readArgumentIndex() method with custom argument index.
     */
    @Test
    public void testReadArgumentIndexWithCustomArgumentIndex() {
        String pattern = "{0,format=\"customFormat1\"}{1,format=\"customFormat2\"}";
        ParsePosition pos = new ParsePosition(0);
        int result = ExtendedMessageFormat.readArgumentIndex(pattern, pos);
        Assert.assertEquals(1, result);
    }

    /**
     * Test readArgumentIndex() method with invalid argument index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithInvalidArgumentIndex() {
        String pattern = "{0,format=\"customFormat1\"}{1,format=\"customFormat2\"}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat.readArgumentIndex(pattern, pos);
    }

    /**
     * Test getFormats() method.
     * @throws UnsupportedOperationException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetFormats() {
        Map<String, FormatFactory> registry = new LinkedHashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", registry);
        format.getFormats();
    }

    /**
     * Test seekNonWs() method.
     */
    @Test
    public void testSeekNonWs() {
        String pattern = "{0,format=\"customFormat1\"}{1,format=\"customFormat2\"}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat.seekNonWs(pattern, pos);
        Assert.assertEquals(0, pos.getIndex());
    }

    /**
     * Test next() method.
     */
    @Test
    public void testNext() {
        String pattern = "{0,format=\"customFormat1\"}{1,format=\"customFormat2\"}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat.next(pos);
        Assert.assertEquals(1, pos.getIndex());
    }

    

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String argument, Locale locale) {
            return new CustomFormat();
        }
    }

    private static class CustomFormatFactory1 implements FormatFactory {
        @Override
        public Format getFormat(String name, String argument, Locale locale) {
            return new CustomFormat1();
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            return "";
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            return null;
        }
    }

    private static class CustomFormat1 implements Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            return "";
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            return null;
        }

        @Override
        public String toString() {
            return "CustomFormat1";
        }
    }
}