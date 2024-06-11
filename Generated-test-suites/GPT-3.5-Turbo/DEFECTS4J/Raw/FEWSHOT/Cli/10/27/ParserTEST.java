import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{});
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").hasArgs().build());

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "value", "-c", "arg1", "arg2"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getOptionValues("c"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addOption(Option.builder("a").required().build());

        parser.parse(options, new String[]{});
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").hasArg().build());

        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");

        CommandLine cmd = parser.parse(options, new String[]{}, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());

        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "value", "arg1", "arg2"}, true);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithDoubleDash() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());

        CommandLine cmd = parser.parse(options, new String[]{"--", "-a", "-b", "value"});

        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertArrayEquals(new String[]{"-a", "-b", "value"}, cmd.getArgs());
    }

    @Test
    public void testParseWithSingleDash() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());

        CommandLine cmd = parser.parse(options, new String[]{"-", "-a", "-b", "value"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        parser.parse(options, new String[]{"-a"});
    }
}