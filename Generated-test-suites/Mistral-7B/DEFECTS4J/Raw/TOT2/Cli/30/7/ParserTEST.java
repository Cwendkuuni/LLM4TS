package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParserWithEmptyArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParserWithSimpleArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "1", "--optionB", "2"};

        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("optionB"));
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-b", "2"};
        options.addRequiredOption("a", true, "An option A");

        parser.parse(options, arguments);
        fail("Expected ParseException when required option 'a' is missing");
    }

    @Test(expected = ParseException.class)
    public void testParserWithInvalidOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--invalidOption"};

        parser.parse(options, arguments);
        fail("Expected ParseException when an unrecognized option 'invalidOption' is encountered");
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingValueForOptionalOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-c"};
        options.addOption("c", false, "An option C");

        parser.parse(options, arguments);
        fail("Expected ParseException when an optional option 'c' doesn't have a value");
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("optionA", "1");
        properties.setProperty("optionB", "3");

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("optionA"));
        assertEquals("3", cmd.getOptionValue("optionB"));
    }

    @Test
    public void testParserWithGroups() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group1");
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        String[] arguments = {"--group1", "-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(group.isSelected());
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }
}