package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                // Mock implementation for testing
                return arguments;
            }
        };

        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertEquals(0, cmd.getOptions().length);
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "value"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        parser.parse(options, new String[]{"-d"});
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        parser.parse(options, new String[]{"-b"});
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "value", "non-option"}, true);
        assertNotNull(cmd);
        assertFalse(cmd.hasOption("non-option"));
    }
}