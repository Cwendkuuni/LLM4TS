import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a", "-b"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndProperties() throws ParseException {
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "true");

        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndStopAtNonOption() throws ParseException {
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a", "-b", "nonOption"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals(1, result.getArgs().length);
        assertEquals("nonOption", result.getArgs()[0]);
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndPropertiesAndStopAtNonOption() throws ParseException {
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "true");

        CommandLine result = parser.parse(options, arguments, properties, true);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals(1, result.getArgs().length);
        assertEquals("-a", result.getArgs()[0]);
    }

    @Test
    public void testParseWithNoOptionsAndArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2"};

        CommandLine result = parser.parse(options, arguments);

        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test
    public void testParseWithNoOptionsAndArgumentsAndProperties() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");

        CommandLine result = parser.parse(options, arguments, properties);

        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test
    public void testParseWithNoOptionsAndArgumentsAndStopAtNonOption() throws ParseException {
        String[] arguments = {"arg1", "arg2", "-a"};

        CommandLine result = parser.parse(options, arguments, true);

        assertEquals(3, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
        assertEquals("-a", result.getArgs()[2]);
    }

    @Test
    public void testParseWithNoOptionsAndArgumentsAndPropertiesAndStopAtNonOption() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");

        CommandLine result = parser.parse(options, arguments, properties, true);

        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }
}