package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser class.
 */
public class ParserTest
{
    @Test
    public void testSetOptions()
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        List<String> requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals("option", requiredOptions.get(0));
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"--option", "value"};
        String[] expectedArgs = {"--option", "value"};
        assertEquals(expectedArgs, parser.flatten(options, args, false));
    }

    @Test
    public void testFlattenWithStopAtNonOptionTrue() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"--option", "value", "--other"};
        String[] expectedArgs = {"--option", "value"};
        assertEquals(expectedArgs, parser.flatten(options, args, true));
    }

    @Test
    public void testParseWithNoOptions() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = {};
        CommandLine commandLine = parser.parse(options, args);
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOneOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"--option", "value"};
        CommandLine commandLine = parser.parse(options, args);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("value", commandLine.getArgs().get(0));
        assertTrue(commandLine.getOptions().isPresent());
        Option actualOption = commandLine.getOptions().get();
        assertEquals(option, actualOption);
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 description");
        Option option2 = new Option("o2", "option2", true, "Option2 description");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        String[] args = {"--option1", "value1", "--option2", "value2"};
        CommandLine commandLine = parser.parse(options, args);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("value1", commandLine.getArgs().get(0));
        assertEquals("value2", commandLine.getArgs().get(1));
        assertTrue(commandLine.getOptions().isPresent());
        Option actualOption1 = commandLine.getOptions().get();
        assertEquals(option1, actualOption1);
        assertTrue(commandLine.getOptions().isPresent());
        Option actualOption2 = commandLine.getOptions().get();
        assertEquals(option2, actualOption2);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnknownOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"--unknown-option", "value"};
        parser.parse(options, args);
        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithRequiredOptionMissing() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {};
        try
        {
            parser.parse(options, args);
            fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        CommandLine commandLine = parser.parse(options, null, properties);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("value", commandLine.getArgs().get(0));
        assertTrue(commandLine.getOptions().isPresent());
        Option actualOption = commandLine.getOptions().get();
        assertEquals(option, actualOption);
    }

    @Test
    public void testParseWithStopAtNonOptionTrue() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"--option", "value", "--unknown"};
        CommandLine commandLine = parser.parse(options, args, true);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("value", commandLine.getArgs().get(0));
        assertTrue(commandLine.getOptions().isEmpty());
    }
}