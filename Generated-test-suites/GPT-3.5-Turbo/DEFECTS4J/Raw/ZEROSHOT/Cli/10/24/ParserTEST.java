import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Properties;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        options = new Options();
    }

    @Test
    public void testParse_withOptionsAndArguments() throws ParseException {
        String[] arguments = {"-a", "value1", "--option", "value2", "argument1"};

        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("option", true, "Option with argument");

        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("option"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("option"));
        assertEquals("argument1", cmd.getArgs()[0]);
    }

    @Test(expected = ParseException.class)
    public void testParse_withInvalidOption() throws ParseException {
        String[] arguments = {"-a"};

        Option option = new Option("b", false, "Option b");

        options.addOption(option);

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_withMissingArgument() throws ParseException {
        String[] arguments = {"--option"};

        Option option = new Option("option", true, "Option with argument");

        options.addOption(option);

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("option", "value2");
        properties.setProperty("b", "yes");
        properties.setProperty("c", "true");
        properties.setProperty("d", "1");

        Option option1 = new Option("a", true, "Option a");
        Option option2 = new Option("option", true, "Option with argument");
        Option option3 = new Option("b", false, "Option b");
        Option option4 = new Option("c", false, "Option c");
        Option option5 = new Option("d", false, "Option d");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);

        parser.processProperties(properties);

        assertTrue(parser.cmd.hasOption("a"));
        assertTrue(parser.cmd.hasOption("option"));
        assertTrue(parser.cmd.hasOption("b"));
        assertTrue(parser.cmd.hasOption("c"));
        assertTrue(parser.cmd.hasOption("d"));
        assertEquals("value1", parser.cmd.getOptionValue("a"));
        assertEquals("value2", parser.cmd.getOptionValue("option"));
        assertEquals("yes", parser.cmd.getOptionValue("b"));
        assertEquals("true", parser.cmd.getOptionValue("c"));
        assertEquals("1", parser.cmd.getOptionValue("d"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_withMissingOption() throws MissingOptionException {
        Option option1 = new Option("a", true, "Option a");
        Option option2 = new Option("b", true, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        parser.checkRequiredOptions();
    }
}