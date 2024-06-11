package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParse_EmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testParse_SingleOption() throws ParseException {
        options.addOption("option", true, "o");
        String[] arguments = new String[]{"-o"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertSame("option", commandLine.getOptionValues("option")[0]);
        assertFalse(commandLine.hasOption("anotherOption"));
    }

    @Test
    public void testParse_SingleArgument() throws ParseException {
        options.addOption("option", true, "o");
        String[] arguments = new String[]{"-o", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(1, commandLine.getArgs().size());
        assertSame("value", commandLine.getArgs()[0]);
        assertSame("value", commandLine.getOptionValues("option")[0]);
        assertFalse(commandLine.hasOption("anotherOption"));
    }

    @Test
    public void testParse_MultipleOptions() throws ParseException {
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] arguments = new String[]{"-o1", "-o2"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertSame("true", commandLine.getOptionValues("option1")[0]);
        assertSame("true", commandLine.getOptionValues("option2")[0]);
        assertFalse(commandLine.hasOption("anotherOption"));
    }

    @Test
    public void testParse_MultipleArguments() throws ParseException {
        options.addOption("option", true, "o");
        String[] arguments = new String[]{"-o", "arg1", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(2, commandLine.getArgs().size());
        assertSame("arg1", commandLine.getArgs()[0]);
        assertSame("arg2", commandLine.getArgs()[1]);
        assertSame("arg1", commandLine.getOptionValues("option")[0]);
        assertFalse(commandLine.hasOption("anotherOption"));
    }

    @Test
    public void testParse_MixedOptionsAndArguments() throws ParseException {
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] arguments = new String[]{"-o1", "arg1", "-o2"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(1, commandLine.getArgs().size());
        assertSame("arg1", commandLine.getArgs()[0]);
        assertSame("true", commandLine.getOptionValues("option1")[0]);
        assertSame("true", commandLine.getOptionValues("option2")[0]);
        assertFalse(commandLine.hasOption("anotherOption"));
    }

    @Test
    public void testParse_RequiredOptions() throws ParseException {
        options.addRequiredOption("option1", true, "o1");
        String[] arguments = new String[]{"-o1", "arg1"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(1, commandLine.getArgs().size());
        assertSame("arg1", commandLine.getArgs()[0]);
        assertSame("arg1", commandLine.getOptionValues("option1")[0]);
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption() throws ParseException {
        options.addRequiredOption("option1", true, "o1");
        String[] arguments = new String[]{"-o2"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_ArgumentRequiredOption() throws ParseException {
        options.addOption("option1", true, "o1").requiredArg();
        String[] arguments = new String[]{"-o1"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
        String[] argumentsWithArgument = new String[]{"-o1", "arg"};
        CommandLine commandLine = parser.parse(options, argumentsWithArgument);
        assertEquals(1, commandLine.getArgs().size());
        assertSame("arg", commandLine.getArgs()[0]);
        assertSame("arg", commandLine.getOptionValues("option1")[0]);
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"-unrecognizedOption"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_Help() throws ParseException {
        options.addOption("help", false, null);
        HelpFormatter formatter = new HelpFormatter();
        String[] arguments = new String[]{"--help"};
        CommandLine commandLine = parser.parse(options, arguments);
        formatter.printHelp("ParserTest", options);
        String printHelp = new String(System.out.getBytes());
        assertTrue(printHelp.contains("--help"));
        assertTrue(printHelp.contains("unrecognized option: --help"));
    }

    @Test
    public void testParse_HelpWithOptions() throws ParseException {
        options.addOption("help", false, null);
        HelpFormatter formatter = new HelpFormatter();
        String[] arguments = new String[]{"--help", "-o"};
        CommandLine commandLine = parser.parse(options, arguments);
        formatter.printHelp("ParserTest", options, true);
        String printHelp = new String(System.out.getBytes());
        assertTrue(printHelp.contains("--help"));
        assertTrue(printHelp.contains("-o"));
    }
}