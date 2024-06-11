import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_basicOptions() {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "First option");
        Option option2 = new Option("b", "option2", true, "Second option");
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "-b", "value"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParse_requiredOptions() {
        // Arrange
        Options options = new Options();
        Option requiredOption1 = new Option("a", "option1", true, "Required option 1");
        Option requiredOption2 = new Option("b", "option2", true, "Required option 2");
        options.addRequiredOption(requiredOption1);
        options.addRequiredOption(requiredOption2);

        Parser parser = new ParserImpl();
        String[] arguments = {};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertFalse(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertTrue(result.getRequiredOptions().contains("a"));
        assertTrue(result.getRequiredOptions().contains("b"));
    }

    @Test
    public void testParse_stopAtNonOption() {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "First option");
        Option option2 = new Option("b", "option2", true, "Second option");
        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "nonOption", "-b", "value"};

        // Act
        CommandLine result = parser.parse(options, arguments, true);

        // Assert
        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertTrue(result.getArgs().contains("nonOption"));
    }

    @Test
    public void testParse_properties() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", "option", true, "Option");
        options.addOption(option);

        Parser parser = new ParserImpl();
        Properties properties = new Properties();
        properties.setProperty("option", "value");

        // Act
        CommandLine result = parser.parse(options, null, properties);

        // Assert
        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOptions() {
        // Arrange
        Options options = new Options();
        Option requiredOption1 = new Option("a", "option1", true, "Required option 1");
        Option requiredOption2 = new Option("b", "option2", true, "Required option 2");
        options.addRequiredOption(requiredOption1);
        options.addRequiredOption(requiredOption2);

        Parser parser = new ParserImpl();
        String[] arguments = {"-a"};

        // Act
        parser.parse(options, arguments);

        // Assert (Expected Exception)
    }

}