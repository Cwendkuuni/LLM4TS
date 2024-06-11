
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsArguments_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");
        String[] arguments = {"--alpha"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("alpha"));
    }

    @Test
    public void testParse_OptionsProperties_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");
        Properties properties = new Properties();
        properties.setProperty("alpha", "true");

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, null, properties);

        assertTrue(cmd.hasOption("alpha"));
    }

    @Test
    public void testParse_OptionsArgumentsProperties_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");
        String[] arguments = {"--alpha"};
        Properties properties = new Properties();
        properties.setProperty("alpha", "true");

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("alpha"));
    }

    @Test
    public void testParse_OptionsArgumentsStopAtNonOption_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");
        options.addOption("f", "file", true, "Option file");
        String[] arguments = {"--alpha", "--", "-f", "test.txt"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("alpha"));
        assertTrue(cmd.hasOption("file"));
        assertEquals("test.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testParse_RequiredOptionNotPresent_ThrowsMissingOptionException() {
        Options options = new Options();
        options.addOption("r", "required", true, "Required option");

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, null);
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            // Test passed
        }
    }

    @Test
    public void testProcessArgs_OptionWithArgument_ParsesValues() throws ParseException {
        Option option = new Option("f", "file", true, "Option file");
        ListIterator iterator = Arrays.asList("-f", "test.txt").listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals("test.txt", option.getValue());
    }

}
