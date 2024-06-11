import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
    public void testParse_OptionsArguments_ReturnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "value"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParse_NoOptionsArguments_ReturnsEmptyCommandLine() throws ParseException {
        Options options = new Options();

        String[] arguments = {};

        CommandLine commandLine = parser.parse(options, arguments);

        assertFalse(commandLine.hasOptions());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingArgumentForOption_ThrowsMissingArgumentException() throws ParseException {
        Option option = new Option("o", true, "Option O");

        parser.processArgs(option, Arrays.asList("value1", "value2").listIterator());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        parser.processOption("x", Arrays.asList().listIterator());
    }

    @Test
    public void testCheckRequiredOptions_MissingRequiredOption_ThrowsMissingOptionException() {
        Options options = new Options();
        Option option = new Option("r", false, "Option R");
        option.setRequired(true);
        options.addOption(option);

        parser.setOptions(options);

        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException was not thrown");
        } catch (MissingOptionException e) {
            assertNotNull(e.getMessage());
        }
    }
}