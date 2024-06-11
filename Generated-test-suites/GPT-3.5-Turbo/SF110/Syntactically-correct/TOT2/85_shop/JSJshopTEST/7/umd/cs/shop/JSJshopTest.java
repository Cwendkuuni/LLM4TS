package umd.cs.shop;
import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {
    
    @Test
    public void testMain() {
        // Test with valid arguments
        String[] validArgs = {"domain.txt", "problem.txt"};
        JSJshop.main(validArgs);
        
        // Test with invalid number of arguments
        String[] invalidArgs = {"domain.txt"};
        JSJshop.main(invalidArgs);
        
        // Add more test cases for different scenarios
    }
    
    @Test
    public void testConstructor() {
        // Test constructor with valid arguments
        JSJshop shop = new JSJshop("domain.txt", "problem.txt");
        
        // Test constructor with invalid arguments
        JSJshop shopInvalid = new JSJshop("invalidDomain.txt", "invalidProblem.txt");
        
        // Add more test cases for different scenarios
    }
    
    @Test
    public void testParserFile() {
        // Test parsing a valid file
        assertTrue(new JSJshop().parserFile("validFile.txt"));
        
        // Test parsing an invalid file
        assertFalse(new JSJshop().parserFile("invalidFile.txt"));
        
        // Add more test cases for different scenarios
    }
    
    // Add more test cases for other public methods in JSJshop class
    
}