import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

    @Test
    public void testParse_OptionsArguments_ReturnsCommandLine() throws ParseException {
        // Arrange
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertTrue(result.hasOption("a"));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidOption_ThrowsParseException() throws ParseException {
        // Arrange
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-b"};

        // Act
        parser.parse(options, arguments);

        // Assert: ParseException is thrown
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_MissingRequiredOption_ThrowsMissingOptionException() throws MissingOptionException {
        // Arrange
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", true, "Option A (required)");
        String[] arguments = {};

        // Act
        parser.parse(options, arguments);

        // Assert: MissingOptionException is thrown
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_MissingArgument_ThrowsMissingArgumentException() throws MissingArgumentException {
        // Arrange
        Parser parser = new ParserImpl();
        Option option = new Option("a", true, "Option A (with argument)");
        ListIterator<String> iterator = Arrays.asList("").listIterator();

        // Act
        parser.processArgs(option, iterator);

        // Assert: MissingArgumentException is thrown
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws UnrecognizedOptionException {
        // Arrange
        Parser parser = new ParserImpl();
        ListIterator<String> iterator = Arrays.asList("-b").listIterator();

        // Act
        parser.processOption("-b", iterator);

        // Assert: UnrecognizedOptionException is thrown
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}