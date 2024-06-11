import org.junit.Test;
import org.apache.commons.cli.*;
import java.util.Properties;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);

        assert(cmd.hasOption("a"));
        assert(cmd.hasOption("b"));
        assert(cmd.getOptionValue("b").equals("value"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, properties);

        assert(cmd.hasOption("a"));
        assert(cmd.hasOption("b"));
        assert(cmd.getOptionValue("b").equals("value"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value", "nonOption"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments, true);

        assert(cmd.hasOption("a"));
        assert(cmd.hasOption("b"));
        assert(cmd.getOptionValue("b").equals("value"));
        assert(cmd.getArgs().length == 1);
        assert(cmd.getArgs()[0].equals("nonOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = {"-a"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);
    }
}
