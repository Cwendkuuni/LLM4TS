import org.junit.Test;
import org.apache.commons.cli.*;
import java.util.Arrays;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();

        Option help = new Option("h", "help", false, "Prints help message");
        Option version = new Option("v", "version", false, "Prints version information");

        options.addOption(help);
        options.addOption(version);

        String[] args = {"-h", "-v"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.parse(options, args);

        assert commandLine.hasOption("h");
        assert commandLine.hasOption("v");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();

        Option file = new Option("f", "file", true, "Specifies a file");
        Option number = new Option("n", "number", true, "Specifies a number");

        options.addOption(file);
        options.addOption(number);

        String[] args = {"-f", "test.txt"};

        Properties properties = new Properties();
        properties.setProperty("number", "123");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine commandLine = parser.parse(options, args, properties);

        assert commandLine.hasOption("f");
        assert commandLine.hasOption("number");
        assert commandLine.getOptionValue("number").equals("123");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();

        Option file = new Option("f", "file", true, "Specifies a file");

        options.addOption(file);

        String[] args = {"-f"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();

        String[] args = {"-x"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();

        Option file = new Option("f", "file", true, "Specifies a file");
        file.setRequired(true);

        options.addOption(file);

        String[] args = {};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, args);
    }
}