package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;

import org.apache.commons.cli.Parser;
import org.apache.commons.cli.test.dto.TestParser;

public class ParserTest
{
    @org.junit.Test
    public void testSetOptions()
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @org.junit.Test
    public void testGetOptions()
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @org.junit.Test
    public void testGetRequiredOptions()
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList("a", "b");
        options.setRequiredOptions(requiredOptions);
        parser.setOptions(options);

        List<String> expected = Arrays.asList("a", "b");
        List<String> actual = parser.getRequiredOptions();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFlatten_returnsExpectedResult() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        String[] arguments = {"arg1", "--arg2", "--arg3", "arg4", "--"};
        String[] expected = {"arg1", "arg2", "arg3", "--", "arg4"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @org.junit.Test
    public void testParse_noArguments() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        CommandLine result = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), result);
    }

    @org.junit.Test
    public void testParse_emptyArguments() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        CommandLine result = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), result);
    }

    @org.junit.Test
    public void testParse_withSingleOption() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option");
        options.addOption(option);
        String[] arguments = {"--opt"};
        CommandLine result = parser.parse(options, arguments);
        assertTrue(result.hasOption("opt"));
    }

    @org.junit.Test
    public void testParse_withSingleValueOption() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        String[] arguments = {"--opt=value"};
        CommandLine result = parser.parse(options, arguments);
        assertEquals("value", result.getOptionValue("opt"));
    }

    @org.junit.Test
    public void testParse_withMultipleValueOption() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        String[] arguments = {"--opt=value1", "--opt=value2"};
        CommandLine result = parser.parse(options, arguments);
        String[] values = result.getOptionValues("opt");
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @org.junit.Test
    public void testParse_withMultipleOptions() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--opt1", "--opt2"};
        CommandLine result = parser.parse(options, arguments);
        assertTrue(result.hasOption("opt1"));
        assertTrue(result.hasOption("opt2"));
    }

    @org.junit.Test
    public void testParse_withSingleValueAndMultipleOptions() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1");
        Option option2 = new Option("o2", "opt2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--opt1=value1", "--opt2=value2"};
        CommandLine result = parser.parse(options, arguments);
        String value1 = result.getOptionValue("opt1");
        String value2 = result.getOptionValue("opt2");
        assertEquals("value1", value1);
        assertEquals("value2", value2);
    }

    @org.junit.Test
    public void testParse_withMissingOption() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option");
        options.addOption(option);
        String[] arguments = {"--arg"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @org.junit.Test
    public void testParse_withMissingValueOption() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        String[] arguments = {"--opt"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @org.junit.Test
    public void testParse_withStopAtNonOption() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        String[] arguments = {"--arg1", "--opt", "--arg2"};
        CommandLine result = parser.parse(options, arguments, true);
        assertTrue(result.hasArgArray());
        assertEquals(2, result.getArgArray().length);
        assertEquals("arg1", result.getArgArray()[0]);
        assertEquals("arg2", result.getArgArray()[1]);
    }

    @org.junit.Test
    public void testParse_withProperties() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option");
        options.addOption(option);
        Properties properties = new Properties();
        properties.put("opt", "value");
        CommandLine result = parser.parse(options, new String[0], properties, false);
        String value = result.getOptionValue("opt");
        assertEquals("value", value);
    }

    @org.junit.Test
    public void testParse_withRequiredOptions() throws ParseException
    {
        TestParser parser = new TestParser();
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        options.setRequiredOptions(Arrays.asList("opt1", "opt2"));
        String[] arguments = {"--opt2"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    private static void assertArrayEquals(Object expected, Object actual)
    {
        if (expected == null)
        {
            assertEquals(null, actual);
        }
        else if (actual == null)
        {
            fail("Expected non-null array but was null");
        }
        else if (expected.getClass().isArray() && actual.getClass().isArray())
        {
            Object[] expectedArray = (Object[]) expected;
            Object[] actualArray = (Object[]) actual;
            assertEquals(expectedArray.length, actualArray.length);
            for (int i = 0; i < expectedArray.length; i++)
            {
                assertEquals(expectedArray[i], actualArray[i]);
            }
        }
        else
        {
            fail("Expected array but was " + actual.getClass().getName());
        }
    }
}