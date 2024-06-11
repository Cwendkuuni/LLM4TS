import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseOptionsStringArray() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "-b", "value"};

        // Act
        CommandLine result = new Parser().parse(options, arguments);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsStringArrayProperties() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "-b", "value"};

        Properties properties = new Properties();
        properties.put("b", "value2");

        // Act
        CommandLine result = new Parser().parse(options, arguments, properties);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertEquals("value2", result.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsStringArrayBoolean() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "-b"};

        // Act
        CommandLine result = new Parser().parse(options, arguments, true);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-c"};

        // Act
        new Parser().parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOptionException() throws MissingOptionException {
        // Arrange
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {};

        // Act
        new Parser().parse(options, arguments);
    }
}