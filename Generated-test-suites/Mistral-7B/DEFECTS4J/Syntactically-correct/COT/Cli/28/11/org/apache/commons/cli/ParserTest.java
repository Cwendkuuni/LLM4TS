package org.apache.commons.cli; // make sure to replace with the actual package name if it's different

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Parser class.
 */
public class ParserTest
{
    @Test
    public void testParserWithEmptyArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), cmd);
    }

    @Test
    public void testParserWithValidArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "Show apples.");
        options.addOption("b", "banana", false, "Show bananas.");

        String[] arguments = {"-a", "-b", "--"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);
        assertTrue(optionA.isSet());
        assertTrue(optionB.isSet());
        assertFalse(optionA.getValue().isPresent());
        assertFalse(optionB.getValue().isPresent());

        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParserWithValidArgumentsAndValues() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "Show apples.");
        options.addOption("b", "banana", false, "Show bananas.");
        options.addOption("c", "color", true, "Show color.");

        String[] arguments = {"-a", "--color=red", "-b"};
        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());
        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);
        Option optionC = optionsList.get(2);
        assertTrue(optionA.isSet());
        assertFalse(optionA.getValue().isPresent());
        assertTrue(optionB.isSet());
        assertFalse(optionB.getValue().isPresent());
        assertTrue(optionC.isSet());
        assertEquals("red", optionC.getValue().get());

        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParserWithInvalidArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "Show apples.");
        options.addOption("b", "banana", false, "Show bananas.");

        String[] arguments = {"--apple", "--", "-a"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParserWithMissingRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "apple", true, "Show apples.");

        String[] arguments = {"-b"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParserWithMissingRequiredOptionInOptionGroup() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption(new Option("a", "apple", true, "Show apples.").setRequired(true));
        group.addOption(new Option("b", "banana", false, "Show bananas."));
        options.addOptionGroup(group);

        String[] arguments = {"-b"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParserWithMultipleValuesForOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "apple", true, "Show apples with color.");

        String[] arguments = {"-a", "--color=red,blue"};
        CommandLine cmd = parser.parse(options, arguments);
        Option optionA = cmd.getOption("a");
        assertEquals(Arrays.asList("red", "blue"), optionA.getValue());
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "apple", true, "Show apples.");

        Properties properties = new Properties();
        properties.setProperty("apple", "red,green");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        Option optionA = cmd.getOption("a");
        assertEquals(Arrays.asList("red", "green"), optionA.getValue());
    }
}