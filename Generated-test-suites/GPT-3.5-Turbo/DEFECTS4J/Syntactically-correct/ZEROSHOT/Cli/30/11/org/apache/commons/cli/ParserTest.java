import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Properties;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new ParserImpl();
    }

    @Test
    public void testParseWithNullArguments() throws ParseException {
        Options options = new Options();
        CommandLine cmd = parser.parse(options, null);
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[]{});
        assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        CommandLine cmd = parser.parse(options, new String[]{"-b"});
    }

    @Test
    public void testParseWithOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        CommandLine cmd = parser.parse(options, new String[]{"-a"});

        assertEquals(true, cmd.hasOption("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        CommandLine cmd = parser.parse(options, new String[]{"-a"});
    }

    @Test
    public void testParseWithArgValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        CommandLine cmd = parser.parse(options, new String[]{"-a", "value"});

        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine cmd = parser.parse(options, new String[]{}, properties);

        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOption() throws MissingOptionException {
        Options options = new Options();
        Option option = new Option("a", false, "Option a");
        option.setRequired(true);
        options.addOption(option);

        parser.setOptions(options);

        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptionsWithAllOptions() throws MissingOptionException {
        Options options = new Options();
        Option option = new Option("a", false, "Option a");
        option.setRequired(true);
        options.addOption(option);

        parser.setOptions(options);
        parser.getRequiredOptions().add(option.getKey());

        parser.checkRequiredOptions();
    }

    private static class ParserImpl extends Parser {

        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}