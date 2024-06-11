import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_Options_StringArray() throws Exception {
        Options options = new Options();
        String[] arguments = { "" };
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test
    public void testParse_Options_StringArray_Properties() throws Exception {
        Options options = new Options();
        String[] arguments = { "" };
        Properties properties = new Properties();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        CommandLine result = parser.parse(options, arguments, properties);
        assertNotNull(result);
    }

    @Test
    public void testParse_Options_StringArray_boolean() throws Exception {
        Options options = new Options();
        String[] arguments = { "" };
        boolean stopAtNonOption = false;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        CommandLine result = parser.parse(options, arguments, stopAtNonOption);
        assertNotNull(result);
    }

    @Test
    public void testParse_Options_StringArray_Properties_boolean() throws Exception {
        Options options = new Options();
        String[] arguments = { "" };
        Properties properties = new Properties();
        boolean stopAtNonOption = false;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        CommandLine result = parser.parse(options, arguments, properties, stopAtNonOption);
        assertNotNull(result);
    }

    @Test
    public void testProcessArgs_Option_ListIterator() throws Exception {
        Option opt = new Option("opt", true, "option");
        ListIterator iter = Arrays.asList("").listIterator();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processArgs(opt, iter);
        assertTrue(opt.getValues().length > 0);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_Option_ListIterator_MissingArgument() throws Exception {
        Option opt = new Option("opt", true, "option");
        ListIterator iter = Arrays.asList().listIterator();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption_String_ListIterator() throws Exception {
        String arg = "arg";
        ListIterator iter = Arrays.asList("").listIterator();
        Options options = new Options();
        Option opt = new Option(arg, true, "option");
        options.addOption(opt);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processOption(arg, iter);
        assertTrue(parser.getCommandLine().hasOption(arg));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_String_ListIterator_UnrecognizedOption() throws Exception {
        String arg = "arg";
        ListIterator iter = Arrays.asList("").listIterator();
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.processOption(arg, iter);
    }

    @Test
    public void testProcessProperties_Properties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("prop", "value");
        Options options = new Options();
        options.addOption(new Option("prop", true, "option"));
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setCommandLine(new CommandLine());
        parser.processProperties(properties);
        assertTrue(parser.getCommandLine().hasOption("prop"));
    }

    @Test
    public void testCheckRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption(new Option("opt", false, "option"));
        List requiredOptions = Arrays.asList("opt");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_MissingOption() throws Exception {
        Options options = new Options();
        options.addOption(new Option("opt", false, "option"));
        List requiredOptions = Arrays.asList();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        parser.checkRequiredOptions();
    }
}