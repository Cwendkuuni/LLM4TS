import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private String[] args;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
        args = new String[0];
    }

    //---------------------
    // flatten method tests
    //---------------------

    @Test
    public void testFlatten_ReturnsEmptyArray_WhenGivenEmptyOptionsAndArguments() throws ParseException {
        String[] flattenedArgs = parser.flatten(options, new String[0], false);
        assertEquals(0, flattenedArgs.length);
    }

    @Test
    public void testFlatten_ReturnsFlattenedArguments_WhenGivenOptionsWithNonOptionAndArgumentsWithNonOption() throws ParseException {
        options.addOption("a", true, "Option a");
        String[] arguments = {"-a", "value"};
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertEquals(2, flattenedArgs.length);
        assertEquals("-a", flattenedArgs[0]);
        assertEquals("value", flattenedArgs[1]);
    }

    //--------------------
    // parse method tests
    //--------------------

    @Test
    public void testParse_ReturnsCommandLineWithOption_WhenGivenOptionArgument() throws ParseException {
        options.addOption("a", true, "Option a");
        args = new String[]{"-a", "value"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected=UnrecognizedOptionException.class)
    public void testParse_ThrowsUnrecognizedOptionException_WhenGivenInvalidOption() throws ParseException {
        args = new String[]{"-b"};
        parser.parse(options, args);
    }

    //--------------------
    // processOption method tests
    //--------------------

    @Test
    public void testProcessOption_AddsOptionToCommandLine_WhenOptionIsValid() throws ParseException {
        Option option = new Option("a", true, "Option a");
        options.addOption(option);
        args = new String[]{"-a"};
        CommandLine cmd = new CommandLine();
        parser.processOption("a", args, cmd);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testProcessOption_ThrowsUnrecognizedOptionException_WhenOptionIsInvalid() throws ParseException {
        Option option = new Option("a", true, "Option a");
        options.addOption(option);
        args = new String[]{"-b"};
        CommandLine cmd = new CommandLine();
        try {
            parser.processOption("b", args, cmd);
            fail("Expected UnrecognizedOptionException to be thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: b", e.getMessage());
        }
    }

    //--------------
    // Helper classes
    //--------------

    private class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }

}