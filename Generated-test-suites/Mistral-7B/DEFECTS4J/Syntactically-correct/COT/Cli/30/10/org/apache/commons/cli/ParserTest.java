package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for Parser class.
 */
@RunWith(JUnit4.class)
public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testSetOptions()
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions()
    {
        Parser parser = new DefaultParser();

        assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithNoArguments() throws ParseException
    {
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testParseWithNullOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];

        parser.parse(null, arguments);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException
    {
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException
    {
        String[] arguments = new String[]{"arg"};
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);

        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();

        assertEquals(1, optionsList.size());
        Option optionAtIndex = optionsList.get(0);
        assertEquals("opt", optionAtIndex.getLongOpt());
        assertEquals("o", optionAtIndex.getOpt());
        assertFalse(optionAtIndex.hasArg());
        assertEquals("an option", optionAtIndex.getDescription());
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithSingleArgumentAndOption() throws ParseException
    {
        String[] arguments = new String[]{"arg", "-o", "value"};
        Option option = new Option("o", "opt", true, "an option");
        option.setArgName("value");
        options.addOption(option);

        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();

        assertEquals(1, optionsList.size());
        Option optionAtIndex = optionsList.get(0);
        assertEquals("opt", optionAtIndex.getLongOpt());
        assertEquals("o", optionAtIndex.getOpt());
        assertTrue(optionAtIndex.hasArg());
        assertEquals("value", cmd.getOptionValue("opt"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithMultipleArgumentsAndOptions() throws ParseException
    {
        String[] arguments = new String[]{"arg1", "-o1", "value1", "-o2", "value2"};
        Option option1 = new Option("o1", "opt1", true, "an option 1");
        option1.setArgName("value1");
        Option option2 = new Option("o2", "opt2", true, "an option 2");
        option2.setArgName("value2");
        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();

        assertEquals(2, optionsList.size());
        Option option1AtIndex = optionsList.get(0);
        assertEquals("opt1", option1AtIndex.getLongOpt());
        assertEquals("o1", option1AtIndex.getOpt());
        assertTrue(option1AtIndex.hasArg());
        assertEquals("value1", cmd.getOptionValue("opt1"));
        Option option2AtIndex = optionsList.get(1);
        assertEquals("opt2", option2AtIndex.getLongOpt());
        assertEquals("o2", option2AtIndex.getOpt());
        assertTrue(option2AtIndex.hasArg());
        assertEquals("value2", cmd.getOptionValue("opt2"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnknownOption() throws ParseException
    {
        String[] arguments = new String[]{"arg", "-u", "value"};
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);

        parser.parse(options, arguments);
        fail("ParseException expected but not thrown");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnknownOptionException() throws ParseException
    {
        String[] arguments = new String[]{"arg", "--u", "value"};
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] arguments = new String[]{"arg", "--", "nonOption", "arg2"};
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);

        CommandLine cmd = parser.parse(options, arguments, true);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionAtIndex = optionsList.get(0);
        assertEquals("opt", optionAtIndex.getLongOpt());
        assertEquals("o", optionAtIndex.getOpt());
        assertFalse(optionAtIndex.hasArg());
        assertEquals("arg", cmd.getOptionValue("opt"));
        assertArrayEquals(new String[]{"nonOption", "arg2"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOptionAndUnknownOption() throws ParseException
    {
        String[] arguments = new String[]{"arg", "--u", "value"};
        Option option = new Option("o", "opt", false, "an option");
        options.addOption(option);

        parser.parse(options, arguments, true);
        fail("ParseException expected but not thrown");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("arg1", "value1");
        properties.setProperty("arg2", "value2");

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments, properties);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(0, optionsList.size());
        assertArrayEquals(new String[0], cmd.getArgs());
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("arg1"));
        assertEquals("value2", properties.getProperty("arg2"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        String[] arguments = new String[0];

        Option option1 = new Option("o1", "opt1", true, "an option 1");
        Option option2 = new Option("o2", "opt2", true, "an option 2");
        options.addOption(option1);
        options.addOption(option2);
        parser.setOptions(options);

        try
        {
            parser.parse(options, arguments);
            fail("MissingOptionException expected but not thrown");
        }
        catch (MissingOptionException e)
        {
            List<String> requiredOptions = e.getMissingOptions();
            assertEquals(2, requiredOptions.size());
            assertTrue(requiredOptions.contains("opt1"));
            assertTrue(requiredOptions.contains("opt2"));
        }
    }
}