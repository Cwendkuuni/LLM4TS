import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private CommandLine cmd;

    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        options = new Options();
        cmd = new CommandLine();
        parser.setOptions(options);
    }

    @Test
    public void testParse_ValidOptions_ReturnsCommandLine() throws ParseException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = { "-a", "-b", "value" };

        cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        Option option = new Option("a", "option", false, "Option");
        options.addOption(option);

        String[] arguments = { "-b" };

        cmd = parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingArgument_ThrowsMissingArgumentException() throws ParseException {
        Option option = new Option("a", "option", true, "Option");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = { "-a" };

        parser.processArgs(option, Arrays.asList(arguments).listIterator());
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_MissingRequiredOption_ThrowsMissingOptionException() throws MissingOptionException {
        Option option = new Option("a", "option", false, "Required Option");
        option.setRequired(true);
        options.addOption(option);

        parser.checkRequiredOptions();
    }

}