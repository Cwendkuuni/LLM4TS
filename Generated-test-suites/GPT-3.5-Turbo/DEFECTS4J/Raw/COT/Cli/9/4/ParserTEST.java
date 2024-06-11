import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new ParserImpl();
    }

    @Test
    public void testParse() {
        // Test case for parse(Options options, String[] arguments)
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithProperties() {
        // Test case for parse(Options options, String[] arguments, Properties properties)
        Options options = new Options();
        options.addOption("a", false, "option a");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        // Test case for parse(Options options, String[] arguments, boolean stopAtNonOption)
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a", "arg1", "-b"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getArgList().get(0));
    }

    @Test
    public void testParseWithPropertiesAndStopAtNonOption() {
        // Test case for parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
        Options options = new Options();
        options.addOption("a", false, "option a");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        String[] arguments = {"-a", "arg1", "-b"};
        CommandLine cmd = parser.parse(options, arguments, properties, true);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getArgList().get(0));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        options.addOption("a", true, "required option");

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "false");

        parser.processProperties(properties);

        CommandLine cmd = parser.getCmd();
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList("-a").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList("-a", "value").listIterator();

        parser.processArgs(option, iterator);

        assertEquals("value", option.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        ListIterator iterator = Arrays.asList("-x").listIterator();

        parser.processOption("-x", iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        ListIterator iterator = Arrays.asList("-a").listIterator();

        parser.processOption("-a", iterator);

        CommandLine cmd = parser.getCmd();
        assertTrue(cmd.hasOption("a"));
    }

    private class ParserImpl extends Parser {

        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}