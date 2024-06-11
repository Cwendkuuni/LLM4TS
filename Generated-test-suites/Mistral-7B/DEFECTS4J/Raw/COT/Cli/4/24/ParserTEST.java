package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();

        options = new Options();
        options.addOption("a", "alpha", true, "an option with boolean value");
        options.addOption("b", "beta", false, "an optional option with no arg");
        options.addOption("c", "char", true, "an option with character value");
        options.addOption("d", "delta", 1, "an option with integer value");
        options.addOption("e", "epsilon", "value", "an option with string value");
        options.addOption("f", "file", true, "an option with file value");
        options.addOptionGroup(new OptionGroup("group1", "group1")
            .addOption("g", "gamma", true, "an option in group1")
            .addOption("h", "help", false, "print this help message"));
    }

    

    @Test
    public void testParse_argumentsNull() throws ParseException {
        CommandLine cmd = parser.parse(options, (String[]) null);
        assertNull(cmd);
    }

    @Test
    public void testParse_noArgument() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParse_simple() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--alpha"});
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("a");
        assertTrue(opt.isSet());
        assertEquals("true", opt.getValue());
    }

    @Test
    public void testParse_stringValue() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-e", "--epsilon", "value"});
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("e");
        assertTrue(opt.isSet());
        assertEquals("value", opt.getValue());
    }

    @Test
    public void testParse_fileValue() throws ParseException {
        String filePath = "test.txt";
        CommandLine cmd = parser.parse(options, new String[]{"-f", "--file", filePath});
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("f");
        assertTrue(opt.isSet());
        assertEquals(filePath, opt.getValue());
    }

    @Test
    public void testParse_integerValue() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-d", "--delta", "3"});
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("d");
        assertTrue(opt.isSet());
        assertEquals(3, ((Number) opt.getValue()).intValue());
    }

    @Test
    public void testParse_booleanValue() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--alpha"});
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption(options.getOption("a"));
        expectedCmd.addArg("");
        assertSame(expectedCmd, cmd);
    }

    @Test
    public void testParse_optionalBooleanValue() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-b", "--beta"});
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("b");
        assertTrue(opt.isSet());
        assertNull(opt.getValue());
    }

    @Test
    public void testParse_groupOption() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-g", "--gamma"});
        assertEquals(1, cmd.getOptions().size());
        Option opt = cmd.getOption("g");
        assertSame(options.getOptionGroup("group1"), opt.getOptGroup());
        assertTrue(opt.isSet());
        assertNull(opt.getValue());
    }

    @Test(expected = ParseException.class)
    public void testParse_missingOption() throws ParseException {
        parser.parse(options, new String[]{"-x", "--x"});
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        parser.parse(options, new String[]{"-z", "--z"});
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_missingArgument() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-d", "--delta"});
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption(options.getOption("d"));
        expectedCmd.addArg("");
        assertSame(expectedCmd, cmd);

        List<String> args = new ArrayList<>();
        args.add("1");

        CommandLine parsedCmd = parser.parse(options, args.toArray(new String[0]));
        assertEquals(1, parsedCmd.getOptions().size());
        Option opt = parsedCmd.getOption("d");
        assertTrue(opt.isSet());
        assertEquals(1, ((Number) opt.getValue()).intValue());

        args.remove(0);
        assertFalse(parser.parse(options, args.toArray(new String[0])).isSet());
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption() throws ParseException {
        String[] args = new String[]{"-a"};
        parser.parse(options, args);
    }

    @Test
    public void testParse_help() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-h", "--help"});
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        assertEquals(formatter.getHelpText(), cmd.getDescription());
    }

    
}