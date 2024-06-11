import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        
        Option option1 = new Option("c", "command", true, "Specifies the command");
        Option option2 = new Option("f", "file", false, "Specifies the file");
        Option option3 = new Option("o", "output", true, "Specifies the output");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
    }
    
    @Test
    public void testFlatten_StopAtNonOptionEnabled() {
        // Arrange
        String[] arguments = {"--command", "run", "--", "--file", "--output"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"--command", "run", "--", "--file", "--output"}, result);
    }
    
    @Test
    public void testFlatten_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"--command", "run", "--", "--file", "--output"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"--command", "run", "--", "--file", "--output"}, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        // Arrange
        String[] arguments = {"-"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"-"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }
    
    @Test
    public void testFlatten_ValidOptionId() {
        // Arrange
        String[] arguments = {"-c", "run"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-c", "run"}, result);
    }
    
    @Test
    public void testFlatten_ValidOptionId_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"-c", "run"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-c", "run"}, result);
    }
    
    @Test
    public void testFlatten_InvalidOptionId_StopAtNonOptionEnabled() {
        // Arrange
        String[] arguments = {"-invalid", "value"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"--", "-invalid", "value"}, result);
    }
    
    @Test
    public void testFlatten_InvalidOptionId_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"-invalid", "value"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-invalid"}, result);
    }
    
    @Test
    public void testFlatten_ValidOptionIdWithArgumentValue() {
        // Arrange
        String[] arguments = {"-c", "run", "-o", "output"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-c", "run", "-o", "output"}, result);
    }
    
    @Test
    public void testFlatten_ValidOptionIdWithArgumentValue_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"-c", "run", "-o", "output"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-c", "run", "-o", "output"}, result);
    }
    
    @Test
    public void testFlatten_ValidOptionIdWithMultipleArgumentValues() {
        // Arrange
        String[] arguments = {"-c", "run", "-o", "output1", "output2"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-c", "run", "-o", "output1", "output2"}, result);
    }
    
    @Test
    public void testFlatten_ValidOptionIdWithMultipleArgumentValues_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"-c", "run", "-o", "output1", "output2"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"-c", "run", "-o", "output1", "output2"}, result);
    }
    
    @Test
    public void testFlatten_NonOption_StopAtNonOptionEnabled() {
        // Arrange
        String[] arguments = {"nonOption1", "nonOption2"};
        
        // Act
        String[] result = parser.flatten(options, arguments, true);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"nonOption1", "nonOption2"}, result);
    }
    
    @Test
    public void testFlatten_NonOption_StopAtNonOptionDisabled() {
        // Arrange
        String[] arguments = {"nonOption1", "nonOption2"};
        
        // Act
        String[] result = parser.flatten(options, arguments, false);
        
        // Assert
        Assert.assertArrayEquals(new String[]{"nonOption1", "nonOption2"}, result);
    }
}