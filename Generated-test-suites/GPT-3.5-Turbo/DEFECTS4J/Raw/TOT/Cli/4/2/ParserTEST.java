import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_NoOptions_ValidArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, arguments);

        assertEquals(3, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
        assertEquals("arg3", result.getArgs()[2]);
    }

    @Test
    public void testParse_SingleOption_NoArgument() throws ParseException {
        String[] arguments = {"-v"};
        Options options = new Options();
        options.addOption(new Option("v", "verbose", false, "Verbose output"));

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("v"));
        assertFalse(result.hasArg());
        assertNull(result.getArgs());
    }

    @Test
    public void testParse_SingleOption_WithArgument() throws ParseException {
        String[] arguments = {"-f", "file.txt"};
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "File option"));

        Parser parser = new ParserImpl();
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("f"));
        assertTrue(result.hasArg());
        assertEquals("file.txt", result.getArg());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        String[] arguments = {"-x"};
        Options options = new Options();

        Parser parser = new ParserImpl();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_OptionWithMissingArgument_ThrowsMissingArgumentException() throws ParseException {
        String[] arguments = {"-f"};
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "File option"));

        Parser parser = new ParserImpl();
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption_ThrowsMissingOptionException() throws ParseException {
        String[] arguments = {"-v"};
        Options options = new Options();
        options.addRequiredOption(new Option("f", "file", true, "File option"));

        Parser parser = new ParserImpl();
        parser.parse(options, arguments);
    }
}