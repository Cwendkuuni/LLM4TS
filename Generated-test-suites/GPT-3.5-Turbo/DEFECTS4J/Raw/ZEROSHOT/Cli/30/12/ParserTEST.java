import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-a", "1", "-b", "2"};
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = {"-a", "1"};
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        Properties properties = new Properties();
        properties.setProperty("a", "2");

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals("2", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "1", "non-option"};
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals(1, cmd.getArgs().length);
        assertEquals("non-option", cmd.getArgs()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseExceptionForUnrecognizedOption() throws ParseException {
        String[] arguments = {"-c", "1"};
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseExceptionForMissingArgument() throws ParseException {
        String[] arguments = {"-a"};
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseExceptionForMissingRequiredOption() throws ParseException {
        String[] arguments = {};
        Option optionA = new Option("a", true, "Option A");
        optionA.setRequired(true);
        options.addOption(optionA);

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseExceptionForMissingRequiredGroup() throws ParseException {
        String[] arguments = {};
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOptionGroup(group);

        parser.parse(options, arguments);
    }

}