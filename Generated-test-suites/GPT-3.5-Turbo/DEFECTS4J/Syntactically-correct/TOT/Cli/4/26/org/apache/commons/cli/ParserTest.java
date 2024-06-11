import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {
    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new ParserImpl();
    }

    @Test
    public void testParse_OptionsArguments_Success() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "value", "-c"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertTrue(commandLine.hasOption("c"));
    }

    @Test
    public void testParse_OptionsProperties_Success() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");
        properties.setProperty("c", "true");

        String[] arguments = {};

        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertTrue(commandLine.hasOption("c"));
    }

    @Test
    public void testParse_OptionsArgumentsProperties_StopAtNonOption_Success() throws ParseException {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "value", "-c", "nonOption"};

        CommandLine commandLine = parser.parse(options, arguments, true);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertFalse(commandLine.hasOption("c"));
        assertEquals("nonOption", commandLine.getArgs()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption_ThrowsMissingOptionException() throws ParseException {
        options.addOption(Option.builder("a").required().build());

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        options.addOption(Option.builder("a").build());

        String[] arguments = {"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgumentForOption_ThrowsMissingArgumentException() throws ParseException {
        options.addOption(Option.builder("a").hasArg().build());

        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_OptionGroup_Success() throws ParseException {
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder("a").build());
        group.addOption(Option.builder("b").build());
        options.addOptionGroup(group);

        String[] arguments = {"-a"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }
}