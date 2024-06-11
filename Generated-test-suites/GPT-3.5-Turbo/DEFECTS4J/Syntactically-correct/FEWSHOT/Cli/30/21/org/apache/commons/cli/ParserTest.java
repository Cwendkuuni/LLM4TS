import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class ParserTest {
    private Options options;
    private Parser parser;
    private CommandLine cmd;

    @Before
    public void setUp() {
        options = new Options();
        parser = new ParserImpl();
    }

    @After
    public void tearDown() {
        options = null;
        parser = null;
        cmd = null;
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        options.addOption("f", "foo", true, "Option foo");
        options.addOption("b", "bar", false, "Option bar");
        options.addOption(Option.builder("c").longOpt("config").hasArg().desc("Option config").build());

        String[] arguments = {"-f", "foo_value", "argument1"};

        cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("foo"));
        assertEquals("foo_value", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("bar"));
        assertFalse(cmd.hasOption("c"));
        assertEquals("argument1", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption("f", "foo", true, "Option foo");
        options.addOption("b", "bar", false, "Option bar");

        String[] arguments = {};

        Properties properties = new Properties();
        properties.put("foo", "foo_value");
        properties.put("bar", "true");

        cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("foo"));
        assertEquals("foo_value", cmd.getOptionValue("f"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("bar"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption("f", "foo", true, "Option foo");
        options.addOption("b", "bar", false, "Option bar");

        String[] arguments = {"-f", "foo_value", "non_option"};

        cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("foo"));
        assertEquals("foo_value", cmd.getOptionValue("f"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("non_option", cmd.getArgs()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        options.addOption("f", "foo", true, "Option foo");

        String[] arguments = {"-f", "foo_value", "-u"};

        cmd = parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addRequiredOption("f", "foo", true, "Option foo");

        String[] arguments = {};

        cmd = parser.parse(options, arguments);
    }

    private static class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}