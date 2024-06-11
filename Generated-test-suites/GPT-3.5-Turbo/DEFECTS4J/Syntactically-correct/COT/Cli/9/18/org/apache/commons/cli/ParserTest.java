import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import java.util.Properties;
import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b", "value"};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.getOption("a").setRequired(true);
        options.getOption("b").setRequired(true);

        String[] args = {};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option A");

        List<String> argValues = Arrays.asList("value1", "value2");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processArgs(option, argValues.listIterator());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String arg = "-b";

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments,
                                        boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processOption(arg, null);
    }

}