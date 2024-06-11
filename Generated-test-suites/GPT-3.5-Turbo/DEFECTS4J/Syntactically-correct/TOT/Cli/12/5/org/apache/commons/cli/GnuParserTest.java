
import org.junit.Test;
import org.apache.commons.cli.*;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_WithOptionAtStart_ReturnsFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option option = new Option("a", false, "Option a");
        options.addOption(option);
        
        String[] arguments = new String[] {"-a", "arg1"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }
        
    @Test
    public void testFlatten_WithOptionAtStartAndAlsoOption_ThrowsException() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        Option optionB = new Option("b", false, "Option b");
        options.addOption(optionA);
        options.addOption(optionB);
        
        String[] arguments = new String[] {"-ab", "arg1"};

        GnuParser parser = new GnuParser();

        // Act
        try {
          parser.flatten(options, arguments, false);
          fail("Expected ParseException was not thrown");
        } catch (ParseException e) {
          // Assert
          assertEquals("Unexpected exception message", "Unrecognized option: -ab", e.getMessage());
        }
    }
    
    @Test
    public void testFlatten_WithOptionAtStartAndAlsoLongOption_ReturnsFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        Option optionB = new Option("b", false, "Option b");
        Option optionC = new Option("c", true, "Option c");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        
        String[] arguments = new String[] {"-ac", "valueC"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("valueC", result[2]);
    }
    
    @Test
    public void testFlatten_WithDoubleDashSeparator_ReturnsFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        options.addOption(optionA);
        
        String[] arguments = new String[] {"--", "-a", "arg1"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("arg1", result[2]);
    }
    
    @Test
    public void testFlatten_WithSingleDash_ReturnsFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        options.addOption(optionA);
        
        String[] arguments = new String[] {"-"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
    
    @Test
    public void testFlatten_WithNonOption_ReturnsFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        options.addOption(optionA);
        
        String[] arguments = new String[] {"arg1"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(1, result.length);
        assertEquals("arg1", result[0]);
    }
    
    @Test
    public void testFlatten_WithNonOptionAfterStop_ReturnsFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        options.addOption(optionA);
        
        String[] arguments = new String[] {"-a", "arg1", "nonOption"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }
    
    @Test
    public void testFlatten_WithNonOptionAfterStop_NoFlattenedArguments() {

        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", false, "Option a");
        options.addOption(optionA);
        
        String[] arguments = new String[] {"-a", "arg1", "nonOption"};

        GnuParser parser = new GnuParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("nonOption", result[2]);
    }
}
