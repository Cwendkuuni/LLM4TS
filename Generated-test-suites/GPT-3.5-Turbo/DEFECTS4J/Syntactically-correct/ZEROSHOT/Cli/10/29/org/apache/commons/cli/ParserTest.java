import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsArguments_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-a", "-b"};
        CommandLine expected = new CommandLine();
        expected.addOption(Option.builder("a").build());
        expected.addOption(Option.builder("b").build());
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
    public void testParse_OptionsProperties_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        String[] arguments = {};
        CommandLine expected = new CommandLine();
        expected.addOption(Option.builder("a").build());
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
    public void testParse_OptionsArgumentsStopAtNonOption_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-a", "-b", "arg1", "arg2"};
        CommandLine expected = new CommandLine();
        expected.addOption(Option.builder("a").build());
        expected.addOption(Option.builder("b").build());
        expected.addArg("arg1");
        expected.addArg("arg2");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        CommandLine actual = parser.parse(options, arguments, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse_OptionsArgumentsStopAtNonOptionWithDoubleDash_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-a", "--", "-b"};
        CommandLine expected = new CommandLine();
        expected.addOption(Option.builder("a").build());
        expected.addArg("--");
        expected.addArg("-b");
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
    public void testParse_RequiredOptionMissing_ThrowsMissingOptionException() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        String[] arguments = {};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.parse(options, arguments);
    }

    @Test
    public void testProcessProperties_ValidProperties_ProcessesProperties() {
        Option option = Option.builder("a").hasArg().build();
        Options options = new Options();
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        CommandLine expected = new CommandLine();
        expected.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {
                // Do nothing for this test
            }

            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.setOptions(options);
        parser.processProperties(properties);
        CommandLine actual = parser.getCommandLine();
        assertEquals(expected, actual);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        String arg = "-c";
        ListIterator iter = null;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.setOptions(new Options());
        parser.processOption(arg, iter);
    }
}