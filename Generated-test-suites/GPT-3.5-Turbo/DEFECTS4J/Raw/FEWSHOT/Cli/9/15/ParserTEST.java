package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-f", "input.txt", "-o", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("output"));

        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        option1.setRequired(true);
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-f", "input.txt", "-o", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("output"));

        assertEquals("input.txt", cmd.getOptionValue("f"));
        assertEquals("input.txt", cmd.getOptionValue("file"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        option1.setRequired(true);
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-i", "input.txt", "-o", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
    }
}