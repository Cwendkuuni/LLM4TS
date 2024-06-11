import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("opt", false, "an option");
        options.addOption(option);
        options.addRequiredOption("opt");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);

        List requiredOptions = parser.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option.getKey()));
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        String[] args = new String[] {"-a", "-b", "value"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(1, cmd.getOptionValues("b").length);
        assertEquals("value", cmd.getOptionValues("b")[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("opt", false, "an option");
        options.addOption(option);
        options.addRequiredOption("opt");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(options);

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("opt", true, "an option");
        options.addOption(option);
        ListIterator<String> iterator = new ArrayList<String>().listIterator();
        iterator.add("value");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        parser.processArgs(option, iterator);

        assertTrue(option.getValue() != null);
        assertEquals(1, option.getValues().length);
        assertEquals("value", option.getValues()[0]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("opt", true, "an option");
        options.addOption(option);
        ListIterator<String> iterator = new ArrayList<String>().listIterator();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        parser.processArgs(option, iterator);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        Options options = new Options();
        Option option = new Option("opt", true, "an option");
        options.addOption(option);
        ListIterator<String> iterator = new ArrayList<String>().listIterator();
        iterator.add("-unknown");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        parser.processOption("-unknown", iterator);
    }
}