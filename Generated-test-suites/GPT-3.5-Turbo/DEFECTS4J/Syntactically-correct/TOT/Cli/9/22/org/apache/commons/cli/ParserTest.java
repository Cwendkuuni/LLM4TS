import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_NormalOptions_Success() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option Alpha"));
        options.addOption(new Option("b", "beta", true, "Option Beta"));

        Parser parser = new ParserImpl();

        String[] arguments = {"-a", "-b", "value"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParse_RequiredOptionMissing_ExceptionThrown() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", true, "Option Alpha"));

        Parser parser = new ParserImpl();

        String[] arguments = {};

        try {
            parser.parse(options, arguments);
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException ex) {
            assertEquals("Missing required option: a", ex.getMessage());
        }
    }

    @Test
    public void testParse_NonOptionEncountered_StopAtNonOptionFlagEnabled() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option Alpha"));
        options.addOption(new Option("b", "beta", false, "Option Beta"));

        Parser parser = new ParserImpl();

        String[] arguments = {"-a", "-b", "value", "nonOption"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertArrayEquals(new String[]{"nonOption"}, result.getArgs());
    }

    @Test
    public void testParse_DoubleDash_StopAtNonOptionFlagEnabled() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option Alpha"));

        Parser parser = new ParserImpl();

        String[] arguments = {"-a", "--", "-b"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertArrayEquals(new String[]{"-b"}, result.getArgs());
    }

    @Test
    public void testParse_ArgumentWithoutOption_Success() {
        Options options = new Options();

        Parser parser = new ParserImpl();

        String[] arguments = {"value"};

        CommandLine result = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"value"}, result.getArgs());
    }

    @Test
    public void testProcessProperties_OptionsWithProperties_Success() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", true, "Option Alpha"));
        options.addOption(new Option("b", "beta", true, "Option Beta"));

        Parser parser = new ParserImpl();

        Properties properties = new Properties();
        properties.setProperty("alpha", "value1");
        properties.setProperty("beta", "value2");

        parser.processProperties(properties);

        assertEquals("value1", options.getOption("a").getValue());
        assertEquals("value2", options.getOption("b").getValue());
    }

    @Test
    public void testCheckRequiredOptions_RequiredOptionMissing_ExceptionThrown() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", true, "Option Alpha"));

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException not thrown");
        } catch (MissingOptionException ex) {
            assertEquals("Missing required option: a", ex.getMessage());
        }
    }

    @Test
    public void testCheckRequiredOptions_RequiredOptionPresent_NoExceptionsThrown() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", true, "Option Alpha"));
        options.addOption(new Option("b", "beta", true, "Option Beta"));

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "alpha", "value"));

        parser.setCommandLine(cmd);

        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessOption_UnknownOption_ExceptionThrown() {
        Options options = new Options();

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        String option = "unknown";

        ListIterator iterator = new ArrayList<String>().listIterator();

        try {
            parser.processOption(option, iterator);
            fail("UnrecognizedOptionException not thrown");
        } catch (UnrecognizedOptionException ex) {
            assertEquals("Unrecognized option: unknown", ex.getMessage());
        }
    }

    @Test
    public void testProcessOption_OptionWithArgument_Success() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", true, "Option Alpha"));

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        String option = "alpha";

        ListIterator iterator = Arrays.asList("value").listIterator();

        parser.processOption(option, iterator);

        assertEquals("value", parser.getCommandLine().getOptionValue("a"));
    }

    @Test
    public void testProcessOption_OptionWithoutArgumentAndStopAtNonOptionEnabled_Success() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option Alpha"));

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        String option = "alpha";

        ListIterator iterator = new ArrayList<String>().listIterator();

        parser.processOption(option, iterator);

        assertTrue(parser.getCommandLine().hasOption("a"));
    }

    @Test
    public void testProcessOption_OptionWithoutArgumentAndStopAtNonOptionDisabled_Fail() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option Alpha"));

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        String option = "alpha";

        ListIterator iterator = new ArrayList<String>().listIterator();

        try {
            parser.processOption(option, iterator);
            fail("MissingArgumentException not thrown");
        } catch (MissingArgumentException ex) {
            assertEquals("Missing argument for option: a", ex.getMessage());
        }
    }
}