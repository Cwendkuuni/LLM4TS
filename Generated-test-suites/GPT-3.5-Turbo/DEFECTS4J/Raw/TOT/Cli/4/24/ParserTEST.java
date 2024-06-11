import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_NoOptionsAndArguments_ReturnsEmptyCommandLine() {
        // Arrange
        String[] arguments = new String[0];
        Parser parser = new ParserImpl();

        // Act
        CommandLine result = parser.parse(new Options(), arguments);

        // Assert
        assertTrue(result.getOptions().isEmpty());
        assertTrue(result.getArgs().isEmpty());
    }

    @Test
    public void testParse_OptionsWithoutArguments_ReturnsParsedOptions() {
        // Arrange
        String[] arguments = {"-a", "-b"};
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test
    public void testParse_OptionsWithArguments_ReturnsParsedOptions() {
        // Arrange
        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertTrue(result.hasOption("a"));
        assertEquals("valueA", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("valueB", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test (expected = ParseException.class)
    public void testParse_UnrecognizedOption_ThrowsParseException() throws ParseException {
        // Arrange
        String[] arguments = {"-a"};
        Parser parser = new ParserImpl();
        Options options = new Options();
        options.addOption("b", false, "Option B");

        // Act
        parser.parse(options, arguments);

        // Assert (Expected Exception)
    }
}