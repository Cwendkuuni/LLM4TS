
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--", "arg2"};
        String[] expected = {"-a", "arg1", "--", "arg2"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParse() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--", "arg2"};
        CommandLine expected = new CommandLine();
        expected.addOption("a", "arg1");
        expected.addArg("--");
        expected.addArg("arg2");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine actual = parser.parse(options, arguments);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1"};
        Properties properties = new Properties();
        properties.setProperty("b", "arg2");
        CommandLine expected = new CommandLine();
        expected.addOption("a", "arg1");
        expected.addOption("b", "arg2");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine actual = parser.parse(options, arguments, properties);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        String[] arguments = {"-a", "arg1", "nonOption"};
        CommandLine expected = new CommandLine();
        expected.addOption("a", "arg1");
        expected.addArg("nonOption");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine actual = parser.parse(options, arguments, true);

        assertEquals(expected, actual);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        String[] arguments = {"-a", "arg1"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.parse(options, arguments);
    }
}
