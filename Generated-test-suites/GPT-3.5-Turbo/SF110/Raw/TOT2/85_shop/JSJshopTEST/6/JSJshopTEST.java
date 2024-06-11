import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class JSJshopTest {
    
    @Test
    public void testMain() {
        JSJshop main = new JSJshop();
        // Test main method with valid arguments
        String[] validArgs = {"domainFile.txt", "problemFile.txt"};
        main.main(validArgs);
        assertNotNull(main.getSolution());
        
        // Test main method with invalid number of arguments
        String[] invalidArgs = {"domainFile.txt"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        main.main(invalidArgs);
        assertEquals("Usage :\n java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n[number-of-plans] can be 'one' or 'all'. The default value is 'one'\n", outContent.toString());
        System.setOut(originalOut);
    }
    
    @Test
    public void testConstructor() {
        JSJshop shop = new JSJshop();
        assertNotNull(shop.getTree());
        
        // Test constructor with valid file names
        JSJshop shopFile = new JSJshop("domainFile.txt", "problemFile.txt");
        assertNotNull(shopFile.getSolution());
        
        // Test constructor with invalid file names
        JSJshop shopInvalid = new JSJshop("invalidDomainFile.txt", "invalidProblemFile.txt");
        assertNull(shopInvalid.getSolution());
    }
    
    @Test
    public void testParserFile() {
        JSJshop shop = new JSJshop();
        // Test successful parsing of a valid file
        assertTrue(shop.parserFile("validFile.txt"));
        
        // Test handling of IOException
        assertFalse(shop.parserFile("nonExistentFile.txt"));
    }
  
    // Add more test cases for other methods
    
}