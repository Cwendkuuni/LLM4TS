import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_withNullOptions_returnsNull() {
        Parser parser = new ParserMock();
        assertNull(parser.parse(null, new String[]{"arg1", "arg2"}));
    }

    @Test
    public void testParse_withNullArguments_returnsNull() {
        Parser parser = new ParserMock();
        assertNull(parser.parse(new Options(), null));
    }

    @Test
    public void testParse_withEmptyArguments_returnsEmptyCommandLine() {
        Parser parser = new ParserMock();
        CommandLine cmd = parser.parse(new Options(), new String[]{});
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParse_withHelpOption_returnsHelpCommandLine() {
        Parser parser = new ParserMock();
        Options options = new Options();
        options.addOption(new Option("h", "help"));
        CommandLine cmd = parser.parse(options, new String[]{"-h"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testParse_withRequiredOption_returnsCommandLineWithRequiredOption() {
        Parser parser = new ParserMock();
        Options options = new Options();
        Option requiredOption = new Option("r", "required", true, "required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        CommandLine cmd = parser.parse(options, new String[]{"-r", "value"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("r"));
        assertEquals("value", cmd.getOptionValue("r"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption_throwsMissingOptionException() throws ParseException {
        Parser parser = new ParserMock();
        Options options = new Options();
        Option requiredOption = new Option("r", "required", true, "required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        parser.parse(options, new String[]{});
    }

    // Add more test cases for each method in the Parser class

}

class ParserMock extends Parser {
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
    }
}