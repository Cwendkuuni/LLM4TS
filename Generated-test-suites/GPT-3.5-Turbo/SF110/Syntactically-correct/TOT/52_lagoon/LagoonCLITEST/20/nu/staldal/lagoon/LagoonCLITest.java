import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain_NoArgs_ErrorMessagePrinted() {
        // Arrange
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        // Act
        LagoonCLI.main(new String[]{});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_InvalidInterval_ErrorMessagePrinted() {
        // Arrange
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        // Act
        LagoonCLI.main(new String[]{"property_file", "invalid_interval"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_PropertyFileNotFoundException_ErrorMessagePrinted() {
        // Arrange
        String propertyFile = "non_existent_property_file";
        String expectedOutput = "Error while initializing Lagoon:\nFile not found: " + propertyFile + "\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "build"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_IOException_ErrorMessagePrinted() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Error while initializing Lagoon:\nI/O error: " + e3.toString() + "\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "build"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_AuthenticationMissingException_ErrorMessagePrinted() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Error while initializing Lagoon:\nPassword is required but not specified\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "build"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_AuthenticationException_ErrorMessagePrinted() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Error while initializing Lagoon:\nIncorrect password\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "build"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_BuildFlag_BuildSuccessful() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Building website...\n...with errors...\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "build"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_ForceFlag_ForceBuildSuccessful() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Force building website...\n...with errors...\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "force"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_IntervalFlag_IntervalBuildSuccessful() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Building website...\n...with errors...\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "1000"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_InvalidArgs_ErrorMessagePrinted() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile, "invalid_arg"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_InvalidSitemapFile_ErrorMessagePrinted() {
        // Arrange
        String sitemapFile = "non_existent_sitemap.xml";
        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        // Act
        LagoonCLI.main(new String[]{sitemapFile, "1000"});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_UserInput_BuildSuccessful() {
        // Arrange
        String propertyFile = "property_file.txt";
        String userInput = "b\n";
        String expectedOutput = "Ready\nBuilding website...\n...with errors...\n";
        // Set user input
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        // Act
        LagoonCLI.main(new String[]{propertyFile});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_UserInput_ForceBuildSuccessful() {
        // Arrange
        String propertyFile = "property_file.txt";
        String userInput = "f\n";
        String expectedOutput = "Ready\nForce building website...\n...with errors...\n";
        // Set user input
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        // Act
        LagoonCLI.main(new String[]{propertyFile});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_UserInput_Quit() {
        // Arrange
        String propertyFile = "property_file.txt";
        String userInput = "q\n";
        String expectedOutput = "Ready\n";
        // Set user input
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        // Act
        LagoonCLI.main(new String[]{propertyFile});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testMain_ThreadStillRunning_ErrorPrinted() {
        // Arrange
        String propertyFile = "property_file.txt";
        String expectedOutput = "Thread still running: [Thread name]\n";
        // Act
        LagoonCLI.main(new String[]{propertyFile});
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testGetProperty_PropertyNotFound_LagoonExceptionThrown() {
        // Arrange
        String propertyName = "non_existent_property";
        String expectedErrorMessage = "Property " + propertyName + " not specified";
        // Act & Assert
        LagoonException exception = assertThrows(LagoonException.class, () -> LagoonCLI.getProperty(propertyName));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
    
    @Test
    public void testGetProperty_PropertyFound_PropertyReturned() throws LagoonException {
        // Arrange
        String propertyName = "targetURL";
        String expectedPropertyValue = "http://example.com";
        // Act
        String actualPropertyValue = LagoonCLI.getProperty(propertyName);
        // Assert
        assertEquals(expectedPropertyValue, actualPropertyValue);
    }
    
    @Test
    public void testShowTime_LessThanTenSeconds_TimePrintedInMilliseconds() {
        // Arrange
        long timeInMilliseconds = 5000L;
        String expectedOutput = "in " + timeInMilliseconds + " ms";
        // Act
        LagoonCLI.showTime(timeInMilliseconds);
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testShowTime_MoreThanTenSeconds_TimePrintedInSeconds() {
        // Arrange
        long timeInMilliseconds = 15000L;
        long timeInSeconds = timeInMilliseconds / 1000L;
        String expectedOutput = "in " + timeInSeconds + " s";
        // Act
        LagoonCLI.showTime(timeInMilliseconds);
        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }
}