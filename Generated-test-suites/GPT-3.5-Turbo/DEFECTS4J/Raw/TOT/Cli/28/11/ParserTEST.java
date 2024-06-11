import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParse_NullOptions_EmptyArguments() throws ParseException {
        String[] args = {};
        CommandLine commandLine = parser.parse(options, args);
        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().length == 0);
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_NullOptions_NullArguments() throws ParseException {
        String[] args = null;
        CommandLine commandLine = parser.parse(options, args);
        assertNotNull(commandLine);
        assertTrue(commandLine.getOptions().length == 0);
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_OptionsWithoutArguments_ArgumentsWithFlags() throws ParseException {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        String[] args = {"-a", "-b"};
        CommandLine commandLine = parser.parse(options, args);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_OptionsWithArguments_ArgumentsWithValues() throws ParseException {
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        String[] args = {"-a", "value1", "-b", "value2"};
        CommandLine commandLine = parser.parse(options, args);
        assertNotNull(commandLine);
        assertTrue(commandLine.getOptionValue("a").equals("value1"));
        assertTrue(commandLine.getOptionValue("b").equals("value2"));
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_StopAtNonOption_FlagSet() throws ParseException {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        String[] args = {"-a", "value1", "nonOption", "-b", "value2"};
        CommandLine commandLine = parser.parse(options, args, true);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertTrue(commandLine.getArgs().length == 3);
        assertTrue(commandLine.getArgs()[0].equals("value1"));
        assertTrue(commandLine.getArgs()[1].equals("nonOption"));
        assertTrue(commandLine.getArgs()[2].equals("value2"));
    }

    @Test(expected = ParseException.class)
    public void testParse_UnrecognizedOption_ExceptionThrown() throws ParseException {
        options.addOption("a", false, "Option a");
        String[] args = {"-b"};
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_RequiredOptionWithoutValue_ExceptionThrown() throws ParseException {
        options.addRequiredOption("a", true, "Option a");
        String[] args = {"-a"};
        parser.parse(options, args);
    }

    @Test
    public void testParse_PropertiesProvided_OptionsWithValuesSet() throws ParseException {
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");
        String[] args = {};
        CommandLine commandLine = parser.parse(options, args, properties);
        assertNotNull(commandLine);
        assertTrue(commandLine.getOptionValue("a").equals("value1"));
        assertTrue(commandLine.getOptionValue("b").equals("value2"));
        assertTrue(commandLine.getArgs().length == 0);
    }

    @Test
    public void testParse_RequiredOptionsNotEmpty_ExceptionThrown() {
        options.addRequiredOption("a", true, "Option a");
        options.addRequiredOption("b", true, "Option b");
        String[] args = {};
        try {
            parser.parse(options, args);
            fail("MissingOptionException should have been thrown.");
        } catch (MissingOptionException e) {
            assertTrue(e.getMissingOptions().contains("a"));
            assertTrue(e.getMissingOptions().contains("b"));
        }
    }

}