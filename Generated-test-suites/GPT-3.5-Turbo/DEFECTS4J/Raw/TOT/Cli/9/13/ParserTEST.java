import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsAndArguments_ReturnsCommandLine() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "value1", "--opt2"};

        // Act
        CommandLine result = Parser.parse(options, arguments);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("opt1"));
        assertEquals("value1", result.getOptionValue("opt1"));
        assertTrue(result.hasOption("opt2"));
        assertNull(result.getOptionValue("opt2"));
    }

    @Test
    public void testParse_OptionsPropertiesAndArguments_ReturnsCommandLine() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "value1", "--opt2"};
        Properties properties = new Properties();
        properties.setProperty("opt2", "value2");
        
        // Act
        CommandLine result = Parser.parse(options, arguments, properties);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("opt1"));
        assertEquals("value1", result.getOptionValue("opt1"));
        assertTrue(result.hasOption("opt2"));
        assertEquals("value2", result.getOptionValue("opt2"));
    }

    @Test
    public void testParse_OptionsAndArguments_StopAtNonOption_ReturnsCommandLine() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "value1", "arg1", "--", "--opt2"};

        // Act
        CommandLine result = Parser.parse(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("opt1"));
        assertEquals("value1", result.getOptionValue("opt1"));
        assertFalse(result.hasOption("opt2"));
        assertNull(result.getOptionValue("opt2"));
        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("--opt2", result.getArgs()[1]);
    }

    @Test
    public void testParse_OptionsPropertiesAndArguments_StopAtNonOption_ReturnsCommandLine() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "value1", "arg1", "--", "--opt2"};
        Properties properties = new Properties();
        properties.setProperty("opt2", "value2");
        
        // Act
        CommandLine result = Parser.parse(options, arguments, properties, true);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("opt1"));
        assertEquals("value1", result.getOptionValue("opt1"));
        assertFalse(result.hasOption("opt2"));
        assertNull(result.getOptionValue("opt2"));
        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("--opt2", result.getArgs()[1]);
    }

    @Test
    public void testParse_OptionsAndArguments_MissingRequiredOption_ThrowsMissingOptionException() {
        // Arrange
        Options options = new Options();
        Option requiredOption = new Option("opt1", true, "required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        String[] arguments = new String[]{"--opt2"};

        // Act and Assert
        try {
            Parser.parse(options, arguments);
            fail("Exception not thrown");
        } catch (MissingOptionException ex) {
            assertEquals("Missing required option: opt1", ex.getMessage());
        }
    }

    @Test
    public void testParse_OptionsPropertiesAndArguments_MissingRequiredOption_ThrowsMissingOptionException() {
        // Arrange
        Options options = new Options();
        Option requiredOption = new Option("opt1", true, "required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        String[] arguments = new String[]{"--opt2"};
        Properties properties = new Properties();
        properties.setProperty("opt2", "value2");
        
        // Act and Assert
        try {
            Parser.parse(options, arguments, properties);
            fail("Exception not thrown");
        } catch (MissingOptionException ex) {
            assertEquals("Missing required option: opt1", ex.getMessage());
        }
    }

    @Test
    public void testParse_OptionsAndArguments_MissingArgument_ThrowsMissingArgumentException() {
        // Arrange
        Options options = new Options();
        Option optionWithArg = new Option("opt1", true, "option with argument");
        optionWithArg.setArgName("arg1");
        options.addOption(optionWithArg);
        String[] arguments = new String[]{"--opt1"};

        // Act and Assert
        try {
            Parser.parse(options, arguments);
            fail("Exception not thrown");
        } catch (MissingArgumentException ex) {
            assertEquals("Missing argument for option: opt1", ex.getMessage());
        }
    }

    @Test
    public void testParse_OptionsPropertiesAndArguments_MissingArgument_ThrowsMissingArgumentException() {
        // Arrange
        Options options = new Options();
        Option optionWithArg = new Option("opt1", true, "option with argument");
        optionWithArg.setArgName("arg1");
        options.addOption(optionWithArg);
        String[] arguments = new String[]{"--opt1"};
        Properties properties = new Properties();
        properties.setProperty("opt2", "value2");
        
        // Act and Assert
        try {
            Parser.parse(options, arguments, properties);
            fail("Exception not thrown");
        } catch (MissingArgumentException ex) {
            assertEquals("Missing argument for option: opt1", ex.getMessage());
        }
    }
}