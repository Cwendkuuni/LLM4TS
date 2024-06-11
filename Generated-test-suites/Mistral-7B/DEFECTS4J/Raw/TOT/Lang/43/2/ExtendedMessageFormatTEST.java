package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test class for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with default locale.
     */
    @Test
    public void testDefaultLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0}, your age is {1}.", format.toPattern());

        String[] args = {"John Doe", "30"};
        String formattedMessage = format.format(args);
        assertEquals("Hello John Doe, your age is 30.", formattedMessage);
    }

    /**
     * Test constructor with custom locale.
     */
    @Test
    public void testCustomLocale() {
        String pattern = "Hello {0}, your age in {1} is {2}.";
        Locale locale = new Locale("fr", "FR");
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals("Hello {0}, your age in {1} is {2}.", format.toPattern());

        String[] args = {"Jean Doe", "France", "35"};
        String formattedMessage = format.format(args);
        assertEquals("Hello Jean Doe, your age in France is 35.", formattedMessage);
    }

    /**
     * Test constructor with custom registry.
     */
    @Test
    public void testCustomRegistry() {
        String pattern = "The temperature is {0, number, #, ##0} degrees Celsius.";

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new MyNumberFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals("The temperature is {0, number, #, ##0} degrees Celsius.", format.toPattern());

        String[] args = {"25"};
        String formattedMessage = format.format(args);
        assertEquals("The temperature is 25 degrees Celsius.", formattedMessage);
    }

    /**
     * Test invalid pattern.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        String pattern = "Hello {0}.";
        new ExtendedMessageFormat(pattern);
    }

    /**
     * Test invalid format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElement() {
        String pattern = "Hello {0, {1}}.";
        new ExtendedMessageFormat(pattern);
    }

    /**
     * Test missing format in registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatInRegistry() {
        String pattern = "Hello {0, unknownFormat}.{1}.";

        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Test format with custom format factory.
     */
    @Test
    public void testCustomFormatFactory() {
        String pattern = "The number {0, customFormat} is {1}.";

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new MyCustomFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals("The number {0, customFormat} is {1}.", format.toPattern());

        String[] args = {"10", "Ten"};
        String formattedMessage = format.format(args);
        assertEquals("The number 10 is Ten.", formattedMessage);
    }

    /**
     * Test parsing format with custom format name and style.
     */
    @Test
    public void testCustomFormatNameAndStyle() {
        String pattern = "The number {0, customFormat, date, long} is {1}.";

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new MyCustomFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals("The number {0, customFormat, date, long} is {1}.", format.toPattern());

        String[] args = {"2022-01-01", "January 1, 2022"};
        String formattedMessage = format.format(args);
        assertEquals("The number January 1, 2022 is January 1, 2022.", formattedMessage);
    }

    /**
     * Test format with empty arguments.
     */
    @Test
    public void testEmptyArguments() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String[] args = {};
        String formattedMessage = format.format(args);
        assertEquals("Hello.", formattedMessage);
    }

    /**
     * Test format with too many arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTooManyArguments() {
        String pattern = "Hello {0, number, #, ##0} {1}.";
        String[] args = {"1"};
        new ExtendedMessageFormat(pattern).format(args);
    }

    /**
     * Test format with too few arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTooFewArguments() {
        String pattern = "Hello {0, number, #, ##0} {1} {2}.";
        String[] args = {"1"};
        new ExtendedMessageFormat(pattern).format(args);
    }

    /**
     * Test parse position.
     */
    @Test
    public void testParsePosition() throws ParseException {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        int index = format.parse(new Object[]{"1"}, pos);
        assertEquals(1, index);
        assertEquals(6, pos.getIndex());
    }

    /**
     * Test to string.
     */
    @Test
    public void testToString() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("ExtendedMessageFormat[pattern=Hello {0, number, #, ##0}., arguments={}, locale=java.util.Locale[language=, country=], format=null]", format.toString());
    }

    /**
     * Test setting format.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormat(0, new MyFormat());
    }

    /**
     * Test setting format by argument index.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatByArgumentIndex(0, new MyFormat());
    }

    /**
     * Test setting formats.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = {new MyFormat()};
        format.setFormats(formats);
    }

    /**
     * Test setting formats by argument index.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = {new MyFormat()};
        format.setFormatsByArgumentIndex(formats);
    }

    /**
     * Test get format.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetFormat() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.getFormat(0);
    }

    /**
     * Test apply pattern.
     */
    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0}");
        assertEquals("Hello {0}.", format.toPattern());
    }

    /**
     * Test toPattern.
     */
    @Test
    public void testToPattern() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0, number, #, ##0}.", format.toPattern());
    }

    /**
     * Test format.
     */
    @Test
    public void testFormat() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String[] args = {"1"};
        String formattedMessage = format.format(args);
        assertEquals("Hello 1.", formattedMessage);
    }

    /**
     * Test format with locale.
     */
    @Test
    public void testFormatWithLocale() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.CHINA);
        String[] args = {"1"};
        String formattedMessage = format.format(args);
        assertEquals("Hello 一.", formattedMessage);
    }

    /**
     * Test format with empty arguments and locale.
     */
    @Test
    public void testFormatWithEmptyArgumentsAndLocale() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.CHINA);
        String[] args = {};
        String formattedMessage = format.format(args);
        assertEquals("Hello .", formattedMessage);
    }

    /**
     * Test format with exception.
     */
    @Test(expected = ParseException.class)
    public void testFormatWithException() {
        String pattern = "Hello {0, number, #, ##0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String[] args = {"abc"};
        format.format(args);
    }

    /**
     * Test setLocale.
     */
    @Test
    public void testSetLocale() {
        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Locale locale = new Locale("es", "ES");
        format.setLocale(locale);
        assertEquals("ExtendedMessageFormat[pattern=Hello {0}., arguments={}, locale=Locale[language=es, country=ES], format=null]", format.toString());
    }
}

class MyFormat extends Format {
    @Override
    public StringBuffer format(StringBuffer toAppendTo, Object obj,
            FieldPosition pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
}

class MyNumberFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String format, String formatStyle, Locale locale) {
        return new MyFormat();
    }
}

class MyCustomFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String format, String formatStyle, Locale locale) {
        return new CustomFormat();
    }
}

class CustomFormat extends Format {
    @Override
    public StringBuffer format(StringBuffer toAppendTo, Object obj,
            FieldPosition pos) {
        toAppendTo.append("CustomFormatted");
        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
}