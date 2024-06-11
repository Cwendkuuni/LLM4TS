import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        parser.setOptions(options);

        assertSame(options, parser.getOptions());
        assertSame(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};

        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertFalse(cmd.hasOption("a"));
        assertArrayEquals(arguments, cmd.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertNotNull(cmd);
        assertFalse(cmd.hasOption("a"));
        assertArrayEquals(arguments, cmd.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        parser.setOptions(new Options());
        parser.getRequiredOptions().add("a");
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList("value").listIterator();

        parser.processArgs(option, iterator);

        assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList().listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        String option = "a";
        ListIterator iterator = Arrays.asList().listIterator();

        parser.setOptions(options);
        parser.processOption(option, iterator);

        assertTrue(parser.getOptions().hasOption("a"));
        assertNotNull(parser.getOptions().getOption("a"));
        assertTrue(parser.getRequiredOptions().isEmpty());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();
        String option = "a";
        ListIterator iterator = Arrays.asList().listIterator();

        parser.setOptions(options);
        parser.processOption(option, iterator);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        parser.processProperties(properties);

        assertTrue(parser.getOptions().hasOption("a"));
        assertNotNull(parser.getOptions().getOption("a"));
    }

    @Test
    public void testProcessPropertiesSkipValue() {
        Properties properties = new Properties();
        properties.setProperty("a", "false");

        parser.processProperties(properties);

        assertFalse(parser.getOptions().hasOption("a"));
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "value"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

}