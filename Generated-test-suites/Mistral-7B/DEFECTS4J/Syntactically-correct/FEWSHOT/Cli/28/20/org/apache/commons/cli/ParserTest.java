package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "input.txt", "-o", "output.txt"});

        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        option.setRequired(false);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-o"});
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        Properties props = new Properties();
        props.setProperty("input", "input.txt");
        props.setProperty("output", "output.txt");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], props, false);

        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test
    public void testParseWithCustomFlattener() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        Parser parser = new DefaultParser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                String[] flattened = super.flatten(opts, arguments, stopAtNonOption);
                return new String[]{"--", "--input", "input.txt", "--output", "output.txt"};
            }
        };

        CommandLine cmd = parser.parse(options, new String[]{"--input", "input.txt", "--output", "output.txt"});

        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("unknown"));
    }
}