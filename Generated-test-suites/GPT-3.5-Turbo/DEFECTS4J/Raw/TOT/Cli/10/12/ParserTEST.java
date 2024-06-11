import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        options = new Options();
    }

    @Test
    public void parse_OptionsAndArguments_ReturnsCommandLine() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
    }

    @Test
    public void parse_NullArguments_ReturnsEmptyCommandLine() throws ParseException {
        String[] arguments = null;
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgList().isEmpty());
    }

    @Test
    public void parse_OptionsAndArgumentsWithProperties_ReturnsCommandLine() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2"};
        Properties properties = new Properties();
        properties.setProperty("c", "value3");
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertNotNull(cmd);
    }

    @Test(expected = MissingOptionException.class)
    public void parse_RequiredOptionMissing_ThrowsMissingOptionException() throws ParseException {
        String[] arguments = {"-a", "value1"};
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").build());
        parser.parse(options, arguments);
    }

    @Test
    public void parse_StopAtNonOptionEnabled_StopParsingAfterNonOption() throws ParseException {
        String[] arguments = {"-a", "value1", "nonOption", "-b", "value2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertEquals(3, cmd.getArgList().size());
    }

    @Test
    public void parse_StopAtNonOptionDisabled_ParseAllArguments() throws ParseException {
        String[] arguments = {"-a", "value1", "nonOption", "-b", "value2"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertNotNull(cmd);
        assertEquals(5, cmd.getArgList().size());
    }

    @Test(expected = MissingArgumentException.class)
    public void parse_ArgumentValueRequiredButMissing_ThrowsMissingArgumentException() throws ParseException {
        String[] arguments = {"-a"};
        options.addOption(Option.builder("a").hasArg().build());
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void parse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        String[] arguments = {"-a", "-b"};
        options.addOption(Option.builder("a").build());
        parser.parse(options, arguments);
    }

}