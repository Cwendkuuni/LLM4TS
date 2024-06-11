package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = { "-a", "-b", "input.txt" };

        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("input.txt", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = { "-a" };
        Properties properties = new Properties();
        properties.setProperty("b", "input.txt");

        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("input.txt", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = { "-a", "-b", "input.txt", "arg1", "arg2" };

        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("input.txt", cmd.getOptionValue("b"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithStopAtNonOptionAndProperties() throws ParseException {
        String[] args = { "-a", "-b", "input.txt", "arg1", "arg2" };
        Properties properties = new Properties();
        properties.setProperty("c", "value");

        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, args, properties, true);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("input.txt", cmd.getOptionValue("b"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals("value", cmd.getOptionValue("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = { "-a", "-b" };

        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingOption() throws ParseException {
        String[] args = {};

        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        optionA.setRequired(true);
        optionB.setRequired(true);
        options.addOption(optionA);
        options.addOption(optionB);

        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = { "-a", "-b", "input.txt" };

        Option optionA = new Option("a", false, "Option A");
        options.addOption(optionA);

        parser.parse(options, args);
    }
}