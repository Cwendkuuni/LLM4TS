import org.junit.Test;
import org.apache.commons.cli.*;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithNoProperties() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description for option1");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", true, "Description for option2");
        options.addOption(option2);

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine commandLine = parser.parse(options, arguments);

        assert(commandLine.hasOption(option1.getOpt()));
        assert(commandLine.getOptionValue(option2.getOpt()).equals("value"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description for option1");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", true, "Description for option2");
        options.addOption(option2);

        Properties properties = new Properties();
        properties.setProperty("option1", "true");
        properties.setProperty("option2", "value");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine commandLine = parser.parse(options, new String[0], properties);

        assert(commandLine.hasOption(option1.getOpt()));
        assert(commandLine.getOptionValue(option2.getOpt()).equals("value"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description for option1");
        option1.setRequired(true);
        options.addOption(option1);

        String[] arguments = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "Description for option1");
        options.addOption(option1);

        String[] arguments = {"value"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine commandLine = new CommandLine();

        ListIterator iterator = Arrays.asList(arguments).listIterator();
        parser.processArgs(option1, iterator);

        assert(commandLine.getOptionValue(option1.getOpt()).equals("value"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "Description for option1");
        options.addOption(option1);

        String[] arguments = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine commandLine = new CommandLine();

        ListIterator iterator = Arrays.asList(arguments).listIterator();
        parser.processArgs(option1, iterator);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description for option1");
        options.addOption(option1);

        String[] arguments = {"-b"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine commandLine = new CommandLine();

        ListIterator iterator = Arrays.asList(arguments).listIterator();
        parser.processOption((String)iterator.next(), iterator);
    }
}