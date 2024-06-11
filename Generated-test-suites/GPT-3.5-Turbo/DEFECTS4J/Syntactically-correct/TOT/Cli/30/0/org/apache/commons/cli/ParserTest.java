
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ConcreteParser();
        options = new Options();
    }

    @Test
    public void testParse_ValidArguments_ReturnsCommandLineWithParsedOptions() throws Exception {
        Option option1 = new Option("a", false, "An option without argument");
        Option option2 = new Option("b", true, "An option with argument");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "-b", "arg"};

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg", cmd.getOptionValue("b"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_InvalidOption_ThrowsUnrecognizedOptionException() throws Exception {
        Option option = new Option("a", false, "An option without argument");
        options.addOption(option);

        String[] arguments = {"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption_ThrowsMissingOptionException() throws Exception {
        Option option = new Option("a", false, "An option without argument");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    private static class ConcreteParser extends Parser {

        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}
