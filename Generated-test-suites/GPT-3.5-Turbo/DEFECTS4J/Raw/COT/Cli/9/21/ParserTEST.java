
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }

    @Test
    public void testParse_WithOptionsAndArguments_ShouldReturnCommandLine() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_WithRequiredOptionAndNoValue_ShouldThrowMissingArgumentException() throws ParseException {
        Option option = new Option("a", true, "description");
        parser.processArgs(option, Arrays.asList("").listIterator());
    }

    @Test
    public void testProcessArgs_WithOptionalOptionAndNoValue_ShouldNotThrowException() throws ParseException {
        Option option = new Option("a", false, "description");
        parser.processArgs(option, Arrays.asList("").listIterator());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_WithUnrecognizedOption_ShouldThrowUnrecognizedOptionException() throws ParseException {
        parser.processOption("-c", Arrays.asList("").listIterator());
    }

    @Test
    public void testProcessOption_WithOptionAndNoArgs_ShouldAddOptionToCommandLine() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", false, "description");
        options.addOption(option);
        CommandLine cmd = new CommandLine();
        parser.processOption("-a", Arrays.asList("").listIterator());
        assertEquals(cmd.hasOption("a"), true);
    }

    @Test
    public void testCheckRequiredOptions_WithRequiredOptionNotPresent_ShouldThrowMissingOptionException() {
        Options options = new Options();
        Option option = new Option("a", true, "description");
        options.addOption(option);
        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add("a");
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException e) {
            assertNotNull(e);
        }
    }

}
