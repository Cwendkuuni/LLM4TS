package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for ExtendedMessageFormat.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test case for constructor with default locale and empty pattern.
     */
    @Test
    public void testEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        try {
            format.format(new Object[]{});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test case for constructor with default locale, valid pattern, and no custom format factories.
     */
    @Test
    public void testValidPatternNoCustomFactories() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());

        Object[] args = {"John", 30};
        String result = format.format(args);
        assertEquals("Hello, John, your age is 30.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(args.length, format1.getNumberOfArguments());
        assertEquals(args[0], format1.format(new Object[args.length]));
    }

    /**
     * Test case for constructor with default locale, valid pattern, and custom format factories.
     */
    @Test
    public void testValidPatternCustomFactories() {
        String pattern = "Hello, {0, choice, 0#Mr|1#Mrs|1#Ms|1#Mx|#Other}{1}, your age is {1, number, integer, 0#zero|1#one|2#two|3#three|4#four|5#five|6#six|7#seven|8#eight|9#nine}{1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("choice", new ChoiceFormatFactory());
        registry.put("number", new NumberFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());

        Object[] args = {"John", 30};
        String result = format.format(args);
        assertEquals("Hello, Mr.John, your age is thirty.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(args.length, format1.getNumberOfArguments());
        assertEquals(args[0], format1.format(new Object[args.length]));
    }

    /**
     * Test case for constructor with locale, valid pattern, and no custom format factories.
     */
    @Test
    public void testValidPatternWithLocaleNoCustomFactories() {
        String pattern = "Hello, {0}, your age is {1}.";
        Locale locale = Locale.FRANCE;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals(pattern, format.toPattern());

        Object[] args = {"Jean", 30};
        String result = format.format(args);
        assertEquals("Bonjour, Jean, votre âge est trente.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(args.length, format1.getNumberOfArguments());
        assertEquals(args[0], format1.format(new Object[args.length]));
    }

    /**
     * Test case for constructor with locale, valid pattern, and custom format factories.
     */
    @Test
    public void testValidPatternWithLocaleCustomFactories() {
        String pattern = "Hello, {0, choice, 0#Mr|1#Mme|1#Mlle|1#M.|#Other}{1}, your age is {1, number, integer, 0#zéro|1#un|2#deux|3#trois|4#quatre|5#cinq|6#six|7#sept|8#huit|9#neuf}{1}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("choice", new ChoiceFormatFactory());
        registry.put("number", new NumberFormatFactory());

        Locale locale = Locale.FRANCE;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, format.toPattern());

        Object[] args = {"Jean", 30};
        String result = format.format(args);
        assertEquals("Bonjour, Mme.Jean, votre âge est trente.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(args.length, format1.getNumberOfArguments());
        assertEquals(args[0], format1.format(new Object[args.length]));
    }

    /**
     * Test case for constructor with pattern containing invalid format element.
     */
    @Test
    public void testInvalidFormatElement() {
        String pattern = "Hello, {0, choice, 0#Mr|1#Mrs|1#Ms|1#Mx|#Other}{2}, your age is {1, number, integer, 0#zero|1#one|2#two|3#three|4#four|5#five|6#six|7#seven|8#eight|9#nine}{1}.";
        ExtendedMessageFormat format = null;

        try {
            format = new ExtendedMessageFormat(pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            format = new ExtendedMessageFormat(pattern, Locale.getDefault());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            format = new ExtendedMessageFormat(pattern, new HashMap<>());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test case for constructor with pattern containing invalid format description.
     */
    @Test
    public void testInvalidFormatDescription() {
        String pattern = "Hello, {0, choice, 0#Mr|1#Mrs|1#Ms|1#Mx|#Other}{1, choice, 0#zero|1#one|2#two|3#three|4#four|5#five|6#six|7#seven|8#eight|9#nine}{1}.";
        ExtendedMessageFormat format = null;

        try {
            format = new ExtendedMessageFormat(pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            format = new ExtendedMessageFormat(pattern, Locale.getDefault());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            format = new ExtendedMessageFormat(pattern, new HashMap<>());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test case for format with no arguments.
     */
    @Test
    public void testFormatNoArguments() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String result = format.format(new Object[]{});
        assertEquals("Hello,", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, new Object[0], pos);
        assertEquals(format, format1);
        assertEquals(0, format1.getNumberOfArguments());
        assertNull(format1.format(new Object[0]));
    }

    /**
     * Test case for format with one argument of String type.
     */
    @Test
    public void testFormatOneArgumentStringType() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        Object[] args = {"Alice"};
        String result = format.format(args);
        assertEquals("Hello, Alice.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(1, format1.getNumberOfArguments());
        assertEquals("Alice", format1.format(new Object[args.length]));
    }

    /**
     * Test case for format with one argument of int type.
     */
    @Test
    public void testFormatOneArgumentIntType() {
        String pattern = "The number is {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        Object[] args = {10};
        String result = format.format(args);
        assertEquals("The number is 10.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(1, format1.getNumberOfArguments());
        assertEquals(10, format1.format(new Object[args.length]));
    }

    /**
     * Test case for format with one argument of long type.
     */
    @Test
    public void testFormatOneArgumentLongType() {
        String pattern = "The number is {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        Object[] args = {100L};
        String result = format.format(args);
        assertEquals("The number is 100.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(1, format1.getNumberOfArguments());
        assertEquals(100L, format1.format(new Object[args.length]));
    }

    /**
     * Test case for format with multiple arguments.
     */
    @Test
    public void testFormatMultipleArguments() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        Object[] args = {"Alice", 30};
        String result = format.format(args);
        assertEquals("Hello, Alice, your age is 30.", result);

        ParsePosition pos = new ParsePosition(0);
        Format format1 = format.parse(result, args, pos);
        assertEquals(format, format1);
        assertEquals(2, format1.getNumberOfArguments());
        assertEquals("Alice", format1.format(new Object[args.length]));
        assertEquals(30, format1.format(new Object[args.length]))[0];
    }

    /**
     * Test case for parse with no arguments.
     */
    @Test
    public void testParseNoArguments() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String input = "Hello, Alice.";
        ParsePosition pos = new ParsePosition(0);
        Format[] formats = format.parse(input, new Object[0], pos);
        assertEquals(format, formats[0]);
        assertEquals(0, formats[0].getNumberOfArguments());
        assertNull(formats[0].format(new Object[0]));
    }

    /**
     * Test case for parse with one argument of String type.
     */
    @Test
    public void testParseOneArgumentStringType() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String input = "Hello, Alice.";
        Object[] args = {"Bob"};
        ParsePosition pos = new ParsePosition(0);
        Format[] formats = format.parse(input, args, pos);
        assertEquals(format, formats[0]);
        assertEquals(1, formats[0].getNumberOfArguments());
        assertEquals("Alice", formats[0].format(new Object[args.length]));
        assertEquals("Bob", formats[0].format(args));
    }

    /**
     * Test case for parse with one argument of int type.
     */
    @Test
    public void testParseOneArgumentIntType() {
        String pattern = "The number is {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String input = "The number is 10.";
        Object[] args = {20};
        ParsePosition pos = new ParsePosition(0);
        Format[] formats = format.parse(input, args, pos);
        assertEquals(format, formats[0]);
        assertEquals(1, formats[0].getNumberOfArguments());
        assertEquals(10, formats[0].format(new Object[args.length]));
        assertEquals(20, formats[0].format(args));
    }

    /**
     * Test case for parse with multiple arguments.
     */
    @Test
    public void testParseMultipleArguments() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String input = "Hello, Alice, your age is 30.";
        Object[] args = {"Bob", 40};
        ParsePosition pos = new ParsePosition(0);
        Format[] formats = format.parse(input, args, pos);
        assertEquals(format, formats[0]);
        assertEquals(2, formats[0].getNumberOfArguments());
        assertEquals("Alice", formats[0].format(new Object[args.length]));
        assertEquals(30, formats[0].format(new Object[args.length]))[0];
        assertEquals("Bob", formats[0].format(args));
        assertEquals(40, formats[0].format(args)[0]);
    }
}