package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test class for ExtendedMessageFormat.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with default locale.
     */
    @Test
    public void testDefaultLocale() {
        String pattern = "{0,date,dd.MM.yyyy} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"25.12.2022", "Merry Christmas!"};
        String result = format.format(args);
        Assert.assertEquals("25.12.2022 Merry Christmas!", result);
    }

    /**
     * Test constructor with custom locale.
     */
    @Test
    public void testCustomLocale() {
        String pattern = "{0,date,dd.MM.yyyy} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        Object[] args = {"12.25.2022", "Merry Christmas!"};
        String result = format.format(args);
        Assert.assertEquals("12.25.2022 Merry Christmas!", result);
    }

    /**
     * Test constructor with custom registry.
     */
    @Test
    public void testCustomRegistry() {
        String pattern = "{0,format,MyCustomFormat} {1}";
        MyCustomFormatFactory factory = new MyCustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyCustomFormat", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"Hello, {0}!", "John"};
        String result = format.format(args);
        Assert.assertEquals("Hello, John!", result);
    }

    /**
     * Test toPattern() method.
     */
    @Test
    public void testToPattern() {
        String pattern = "{0,format,MyCustomFormat} {1}";
        MyCustomFormatFactory factory = new MyCustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyCustomFormat", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        String originalPattern = format.toPattern();
        format.applyPattern("{1} {0}");
        String newPattern = format.toPattern();

        Assert.assertNotEquals(originalPattern, newPattern);
        Assert.assertEquals("{1} {0}", newPattern);
    }

    /**
     * Test getFormat() method.
     */
    @Test
    public void testGetFormat() {
        String pattern = "{0,format,MyCustomFormat} {1}";
        MyCustomFormatFactory factory = new MyCustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyCustomFormat", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Format customFormat = format.getFormat("MyCustomFormat");
        Assert.assertNotNull(customFormat);
        Assert.assertTrue(customFormat instanceof MyCustomFormat);
    }

    /**
     * Test setFormat() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "{0,format,MyCustomFormat} {1}";
        MyCustomFormatFactory factory = new MyCustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyCustomFormat", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.setFormat(0, new Format() {});
    }

    /**
     * Test applyPattern() method.
     */
    @Test
    public void testApplyPattern() {
        String pattern = "{0,date,dd.MM.yyyy} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("{1} {0,date,dd.MM.yyyy}");

        Object[] args = {"Merry Christmas!", "25.12.2022"};
        String result = format.format(args);
        Assert.assertEquals("25.12.2022 Merry Christmas!", result);
    }

    /**
     * Test format() method.
     */
    @Test
    public void testFormat() {
        String pattern = "{0,date,dd.MM.yyyy} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        Object[] args = {"25.12.2022", "Merry Christmas!"};
        String result = format.format(args);
        Assert.assertEquals("25.12.2022 Merry Christmas!", result);

        args = new Object[]{"", "Merry Christmas!"};
        result = format.format(args);
        Assert.assertEquals("Merry Christmas!", result);

        args = new Object[]{"25.12.2022", null};
        try {
            result = format.format(args);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parse() method.
     */
    @Test
    public void testParse() {
        String pattern = "{0,date,dd.MM.yyyy} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String input = "25.12.2022 Merry Christmas!";
        Object[] args = format.parse(input);
        Assert.assertArrayEquals(new Object[]{"25.12.2022", "Merry Christmas!"}, args);

        input = "Merry Christmas! 25.12.2022";
        try {
            args = format.parse(input);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Custom format factory.
     */
    public static class MyCustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arg, Locale locale) {
            if ("MyCustomFormat".equals(name)) {
                return new MyCustomFormat();
            }
            return null;
        }

        private static class MyCustomFormat extends Format {
            @Override
            public String format(Object obj, Locale locale, String format,
                                ParsePosition parsePosition) {
                return "MyCustomFormat: " + obj.toString();
            }

            @Override
            public Object parseObject(String source, ParsePosition parsePosition) {
                return "MyCustomFormat: " + source;
            }
        }
    }
}