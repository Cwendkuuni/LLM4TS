import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_OptionsAndArguments_ReturnsCommandLine() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[]{"-a", "-b", "value"};

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse_NoOptions_ReturnsEmptyCommandLine() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();

        String[] arguments = new String[]{"arg1", "arg2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getOptions().length);
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();

        String[] arguments = new String[]{"-x"};

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs_NoArgumentOption_NoValueAddedToOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Option option = new Option("a", "Option A");

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        parser.processArgs(option, iterator);

        assertFalse(option.hasArg());
    }

    @Test
    public void testProcessArgs_RequiredOption_AddsValueToOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Option option = new Option("a", true, "Option A");

        ListIterator<String> iterator = Arrays.asList("-a", "value").listIterator();

        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_RequiredOption_NoValue_ThrowsMissingArgumentException() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Option option = new Option("a", true, "Option A");

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption_ValidOption_AddsOptionToCommandLine() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();
        options.addOption("a", false, "Option A");

        CommandLine cmd = new CommandLine();

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        parser.processOption("-a", iterator);

        assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        Options options = new Options();

        ListIterator<String> iterator = Arrays.asList("-x").listIterator();

        parser.processOption("-x", iterator);
    }

    @Test
    public void testCheckRequiredOptions_RequiredOptionsPresent_NoExceptionThrown() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.requiredOptions = Arrays.asList("a", "b");

        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_RequiredOptionsMissing_ThrowsMissingOptionException() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.requiredOptions = Arrays.asList("a", "b");

        parser.checkRequiredOptions();
    }

}