package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
        option1 = new Option("f", "file", true, "input file");
        option2 = new Option("o", "output", true, "output file");
        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        String[] args = new String[] { "-f", "input.txt", "-o", "output.txt" };
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithMissingRequiredArgument() {
        String[] args = new String[] { "-f" };

        try {
            parser.parse(options, args);
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:f", e.getMessage());
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("file", "input.txt");
        properties.setProperty("output", "output.txt");

        String[] args = new String[] {};
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        String[] args = new String[] { "-u" };

        try {
            parser.parse(options, args);
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: u", e.getMessage());
        }
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = new String[] { "-f", "input.txt", "-o", "output.txt", "extra" };
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertTrue(cmd.getArgList().contains("extra"));
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("file", "input.txt");
        properties.setProperty("output", "output.txt");

        CommandLine cmd = new CommandLine();
        parser.processProperties(properties);

        assertTrue(cmd.hasOption("file"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
        assertTrue(cmd.hasOption("output"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testCheckRequiredOptions() {
        CommandLine cmd = new CommandLine();

        options.getRequiredOptions().add(option1);

        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option:f", e.getMessage());
        }

        cmd.addOption(option1);

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        ListIterator iterator = Arrays.asList("input1.txt", "input2.txt").listIterator();

        Option opt = new Option("i", "input", true, "input file");
        parser.processArgs(opt, iterator);

        assertTrue(opt.hasValues());
        assertEquals("input1.txt", opt.getValue(0));
        assertEquals("input2.txt", opt.getValue(1));
    }

    @Test
    public void testProcessOption() throws ParseException {
        String[] args = new String[] { "-f", "input.txt" };
        CommandLine cmd = new CommandLine();

        parser.processOption("f", Arrays.asList(args).listIterator(), cmd);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }
}