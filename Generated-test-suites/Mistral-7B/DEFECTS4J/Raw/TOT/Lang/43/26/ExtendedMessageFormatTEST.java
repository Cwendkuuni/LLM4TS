package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test case for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test creating an {@link ExtendedMessageFormat} for an empty pattern.
     */
    @Test
    public void testEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a simple pattern.
     */
    @Test
    public void testSimplePattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("intFormat", new IntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}({1,intFormat})",
                registry);
        assertEquals("{0}({1,intFormat})", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1, 2};
        Object result = format.format(args);
        assertEquals("1(2)", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with multiple format elements.
     */
    @Test
    public void testMultipleFormatElements() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("dateFormat", new DateFormatFactory());
        registry.put("intFormat", new IntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}({1,dateFormat}) {2,intFormat}", registry);
        assertEquals("{0}({1,dateFormat}) {2,intFormat}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {"John Doe", new ParsePosition(0), 1};
        Object result = format.format(args);
        assertEquals("John Doe({} 1)", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with invalid format elements.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("intFormat", new IntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}({1,intFormat} {2}", registry);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with an undefined format element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUndefinedFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}({1,undefinedFormat})", registry);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with no arguments.
     */
    @Test
    public void testNoArguments() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", format.toPattern());
        String result = format.format(new Object[0]);
        assertEquals("", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with one argument and a locale.
     */
    @Test
    public void testOneArgumentOneLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("intFormat", new IntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0,locale}", registry);
        assertEquals("{0,locale}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1};
        Object result = format.format(args, Locale.GERMAN);
        assertEquals("{0}", result.toString());
        assertEquals(Locale.GERMAN, result.getClass().getComponentType());
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with one argument, no registry, and a locale.
     */
    @Test
    public void testOneArgumentOneLocaleNoRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0,locale}");
        assertEquals("{0,locale}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1};
        Object result = format.format(args, Locale.GERMAN);
        assertEquals("{0}", result.toString());
        assertEquals(Locale.GERMAN, result.getClass().getComponentType());
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with multiple arguments.
     */
    @Test
    public void testMultipleArguments() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("intFormat", new IntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}({1,intFormat}) {2}", registry);
        assertEquals("{0}({1,intFormat}) {2}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1, 2, 3};
        Object result = format.format(args);
        assertEquals("1(2) 3", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with multiple arguments and a registry.
     */
    @Test
    public void testMultipleArgumentsWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("intFormat", new IntFormatFactory());
        registry.put("dateFormat", new DateFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}({1,intFormat}) {2,dateFormat}", registry);
        assertEquals("{0}({1,intFormat}) {2,dateFormat}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1, new ParsePosition(0), "2022-12-31"};
        Object result = format.format(args);
        assertEquals("1({}) {2,date}", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with format elements and escape sequences.
     */
    @Test
    public void testFormatElementsAndEscapeSequences() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("intFormat", new IntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0,number,currency}{1,intFormat,number,currency}'{2}", registry);
        assertEquals("{0,number,currency}{1,intFormat,number,currency}'{2}",
                format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {"$1.23", 1, "'$3.45"};
        Object result = format.format(args);
        assertEquals("$1.23{1,number,currency}'{$3.45}", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with custom format factories.
     */
    @Test
    public void testCustomFormatFactories() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customIntFormat", new CustomIntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0,customIntFormat}", registry);
        assertEquals("{0,customIntFormat}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1};
        Object result = format.format(args);
        assertEquals("custom{1}", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with a custom format factory and a registry.
     */
    @Test
    public void testCustomFormatFactoryWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customIntFormat", new CustomIntFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0,customIntFormat}", registry);
        assertEquals("{0,customIntFormat}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1};
        Object result = format.format(args);
        assertEquals("custom{1}", result);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with a null registry.
     */
    @Test(expected = NullPointerException.class)
    public void testNullRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}", (Map<String, FormatFactory>) null);
    }

    /**
     * Test creating a {@link ExtendedMessageFormat} for a pattern with an empty registry.
     */
    @Test
    public void testEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0}", Collections.emptyMap());
        assertEquals("{0}", format.toPattern());

        String pattern = format.toPattern();
        Object[] args = {1};
        Object result = format.format(args);
        assertEquals("{0}", result);
    }
}