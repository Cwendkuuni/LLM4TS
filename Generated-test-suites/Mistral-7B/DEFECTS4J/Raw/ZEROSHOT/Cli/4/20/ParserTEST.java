package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test parse method with no options or arguments.
     */
    @org.junit.Test
    public void testParseNoOptionsOrArguments() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test parse method with options but no arguments.
     */
    @org.junit.Test
    public void testParseOptionsNoArguments() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getArgs());
        assertEquals(2, cmd.getOptions().size());
    }

    /**
     * Test parse method with arguments but no options.
     */
    @org.junit.Test
    public void testParseArgumentsNoOptions() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().length);
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test parse method with options and arguments.
     */
    @org.junit.Test
    public void testParseOptionsAndArguments() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");
        String[] arguments = {"arg1", "2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals(2, cmd.getOptionValues("b").length);
        assertEquals("arg1", cmd.getOptionValues("a")[0]);
    }

    /**
     * Test parse method with properties.
     */
    @org.junit.Test
    public void testParseProperties() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertEquals(0, cmd.getArgs().length);
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("a");
        assertNotNull(opt);
        assertEquals("value", opt.getValue());
    }

    /**
     * Test parse method with required options.
     */
    @org.junit.Test
    public void testParseRequiredOptions() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", true, "An option with a flag");
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
        assertEquals(0, cmd.getArgs().length);
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("a");
        assertNotNull(opt);
        assertTrue(opt.isRequired());
    }

    /**
     * Test parse method with invalid option.
     */
    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        String[] arguments = {"--c"};
        parser.parse(options, arguments);
    }

    /**
     * Test parse method with missing argument.
     */
    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");
        String[] arguments = {"arg1"};
        parser.parse(options, arguments);
    }

}