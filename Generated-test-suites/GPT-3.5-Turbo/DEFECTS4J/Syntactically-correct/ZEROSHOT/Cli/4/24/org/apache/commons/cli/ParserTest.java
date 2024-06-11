import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_WithNullArguments_ShouldReturnEmptyCommandLine() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = null;

        CommandLine result = parser.parse(options, arguments);

        assertEquals(0, result.getOptions().length);
        assertEquals(0, result.getArgs().length);
    }

    @Test
    public void testParse_WithEmptyArguments_ShouldReturnEmptyCommandLine() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = new String[]{};

        CommandLine result = parser.parse(options, arguments);

        assertEquals(0, result.getOptions().length);
        assertEquals(0, result.getArgs().length);
    }

    @Test
    public void testParse_WithOptionArgument_ShouldAddOptionToCommandLine() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("f", false, "Flag option");
        options.addOption("v", true, "Value option");
        String[] arguments = new String[]{"-f", "-v", "example"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("f"));
        assertTrue(result.hasOption("v"));
        assertEquals("example", result.getOptionValue("v"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_WithMissingValueForOptionArgument_ShouldThrowMissingArgumentException() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("v", true, "Value option");
        String[] arguments = new String[]{"-v"};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_WithUnrecognizedOption_ShouldThrowUnrecognizedOptionException() throws ParseException {
        Parser parser = new ParserImpl();
        Options options = new Options();
        String[] arguments = new String[]{"-u"};

        parser.parse(options, arguments);
    }
}