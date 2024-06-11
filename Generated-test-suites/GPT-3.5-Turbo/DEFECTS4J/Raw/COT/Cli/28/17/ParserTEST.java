import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());
        String[] arguments = new String[]{"-a", "valueA", "-b", "valueB"};
        CommandLine commandLine = parser.parse(options, arguments);
        assert(commandLine.getOptionValue("a").equals("valueA"));
        assert(commandLine.getOptionValue("b").equals("valueB"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        String[] arguments = new String[]{"-b", "valueB"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().required().build());
        String[] arguments = new String[]{};
        parser.parse(options, arguments);
    }

}