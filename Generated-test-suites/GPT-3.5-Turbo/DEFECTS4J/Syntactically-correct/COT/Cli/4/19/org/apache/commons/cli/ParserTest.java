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
    public void testParse_OptionsAndArgumentsProvided_Success() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a"));
        options.addOption(new Option("b"));

        String[] arguments = {"-a", "arg1", "-b", "arg2"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("arg1", result.getOptionValue("a"));
        assertEquals("arg2", result.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParse_UnrecognizedOption_ThrowsException() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a"));

        String[] arguments = {"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgumentForOption_ThrowsException() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a").setArgs(1));

        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_StopAtNonOption_Success() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a"));

        String[] arguments = {"-a", "-b", "arg"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertEquals(1, result.getArgs().length);
        assertEquals("arg", result.getArgs()[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_Success() {
        Options options = new Options();
        options.addOption(new Option("a"));

        String[] arguments = {"-a", "-b", "arg"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    // Additional test cases for edge cases and exceptions can be added here

}

