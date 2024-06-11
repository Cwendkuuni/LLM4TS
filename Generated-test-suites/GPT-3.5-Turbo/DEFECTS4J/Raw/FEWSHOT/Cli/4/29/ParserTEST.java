
package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new TestableParser();
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        CommandLine result = parser.parse(options, args);

        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test
    public void testParseWithOption() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", false, "description");

        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("a"));
    }

    @Test
    public void testParseWithOptionAndArgument() throws ParseException {
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "description");

        CommandLine result = parser.parse(options, args);

        assertEquals("value", result.getOptionValue("a"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        String[] args = {};
        Options options = new Options();
        options.addRequiredOption("a", "option", true, "description");

        try {
            parser.parse(options, args);
            fail("ParseException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", false, "description1"));
        group.addOption(new Option("b", false, "description2"));
        options.addOptionGroup(group);

        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
    }

    private class TestableParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}
