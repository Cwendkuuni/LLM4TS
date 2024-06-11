package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "A test option");
        options.addOption("b", true, "Another test option");
        String[] arguments = {"-f", "-b", "value"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-c"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseEmptyOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A test option");
        String[] arguments = {"-a", ""};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        String[] arguments = {};
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }
}