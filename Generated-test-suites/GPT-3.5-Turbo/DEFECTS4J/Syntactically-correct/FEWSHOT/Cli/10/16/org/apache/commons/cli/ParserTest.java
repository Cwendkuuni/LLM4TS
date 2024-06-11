package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new MyParser();
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = { "-f", "file.txt" };

        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = { "-f", "file.txt" };
        Properties properties = new Properties();
        properties.setProperty("o", "output.txt");

        CommandLine cmd = parser.parse(options, args, properties);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);

        String[] args = {};

        CommandLine cmd = parser.parse(options, args);
    }

    private class MyParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}