package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 *
 * @author John Doe (john.doe@example.com)
 */
public class ParserTest {

    /**
     * Test the parse method with valid options and arguments.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseValidOptionsArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 5, "Option D");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "--b=2", "--c=3", "--d"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(2, cmd.getOptionValue("b", 0));
        assertEquals(3, cmd.getOptionValue("c", 0).length());
        assertEquals(5, cmd.getOptionValue("d", 0));
        assertTrue(cmd.getArgList().isEmpty());
    }

    /**
     * Test the parse method with required options.
     *
     * @throws ParseException expected
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a"};
        parser.parse(arguments);
        fail("MissingOptionException expected.");
    }

    /**
     * Test the parse method with unrecognized options.
     *
     * @throws ParseException expected
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--x"};
        parser.parse(arguments);
        fail("UnrecognizedOptionException expected.");
    }

    /**
     * Test the parse method with options and arguments.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseOptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "--b=2", "arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(2, cmd.getOptionValue("b", 0));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    /**
     * Test the parse method with multiple values for an option.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseOptionWithMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--a", "value1", "--a", "value2"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    /**
     * Test the parse method with an option taking an argument.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "arg"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("arg", cmd.getOptionValue("a", 0));
    }

    /**
     * Test the parse method with an option taking multiple arguments.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseOptionWithMultipleArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", 2, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertArrayEquals(new String[]{"arg1", "arg2"}, values);
    }

    /**
     * Test the parse method with an option taking a value and an argument.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseOptionWithValueAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "value", "-b", "arg"};
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a", 0));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg", cmd.getArgList().get(0));
    }
}