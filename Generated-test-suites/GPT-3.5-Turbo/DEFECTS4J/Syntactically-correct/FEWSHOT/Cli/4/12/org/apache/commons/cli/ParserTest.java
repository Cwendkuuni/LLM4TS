package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").hasArg().build());

        Parser parser = new DefaultParser();

        String[] args1 = {"-f", "-o", "output"};
        CommandLine cmd1 = parser.parse(options, args1);
        assertTrue(cmd1.hasOption("f"));
        assertEquals("output", cmd1.getOptionValue("o"));

        String[] args2 = {"-o", "output"};
        CommandLine cmd2 = parser.parse(options, args2);
        assertTrue(cmd2.hasOption("o"));
        assertEquals("output", cmd2.getOptionValue("o"));

        String[] args3 = {};
        CommandLine cmd3 = parser.parse(options, args3);
        assertNull(cmd3.getOptionValue("o"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").required(true).build());

        Parser parser = new DefaultParser();

        String[] args = {};
        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").build());

        Parser parser = new DefaultParser();

        String[] args = {"-o"};
        parser.parse(options, args);
    }
}