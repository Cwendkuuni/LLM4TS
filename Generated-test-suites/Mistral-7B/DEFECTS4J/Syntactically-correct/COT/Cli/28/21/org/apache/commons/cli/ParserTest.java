package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test cases for the Parser class.
 */
public class ParserTest
{
    @Test
    public void testParseEmptyArgs() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseSingleArg() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("o", "option", false, "This is an option");
        options.addOption(opt);

        CommandLine cmd = parser.parse(options, new String[]{"arg1"});
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("option"));
        assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("o", "option", false, "This is an option");
        options.addOption(opt);

        parser.parse(options, new String[]{"arg1", "arg2"});
        fail("Expected MissingOptionException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("r", "requiredOption", true, "This is a required option");
        options.addOption(opt);

        parser.parse(options, new String[]{"arg1"});
        throw new AssertionError("Expected MissingOptionException");
    }

    @Test
    public void testParseSingleOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("o", "option", false, "This is an option");
        options.addOption(opt);

        CommandLine cmd = parser.parse(options, new String[]{"--option"});
        assertEquals(0, cmd.getArgs().length);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("option"));
        assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testParseOptionWithArg() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("a", "argOption", true, "This is an argument option");
        options.addOption(opt);

        CommandLine cmd = parser.parse(options, new String[]{"--argOption", "value"});
        assertEquals(1, cmd.getArgs().length);
        assertEquals("value", cmd.getArgs()[0]);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("argOption"));
        assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingOptionArg() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("a", "argOption", true, "This is an argument option");
        options.addOption(opt);

        parser.parse(options, new String[]{"--argOption"});
        throw new AssertionError("Expected MissingArgumentException");
    }

    @Test
    public void testParseMultipleOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt1 = new Option("o1", "option1", false, "This is option 1");
        Option opt2 = new Option("o2", "option2", false, "This is option 2");
        options.addOption(opt1);
        options.addOption(opt2);

        CommandLine cmd = parser.parse(options, new String[]{"--option1", "--option2"});
        assertEquals(0, cmd.getArgs().length);
        assertEquals(2, cmd.getOptions().size());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("o", "option", false, "This is an option");
        options.addOption(opt);

        parser.parse(options, new String[]{"--nonexistent"});
        throw new AssertionError("Expected UnrecognizedOptionException");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("o", "option", false, "This is an option");
        options.addOption(opt);

        Properties properties = new Properties();
        properties.setProperty("option", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        assertEquals(0, cmd.getArgs().length);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option opt = new Option("o", "option", false, "This is an option");
        options.addOption(opt);

        CommandLine cmd = parser.parse(options, new String[]{"--arg1", "--option", "--arg2"});
        assertEquals(2, cmd.getArgs().length);
        assertEquals("--arg1", cmd.getArgs()[0]);
        assertEquals("--arg2", cmd.getArgs()[1]);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("option"));
    }
}