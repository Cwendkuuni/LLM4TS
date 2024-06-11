package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {
    
    @Test 
    public void testMain() {
        // Test case 1: Empty args
        JSJshop.main(new String[] {});
        
        // Test case 2: Less than 2 args
        JSJshop.main(new String[] {"domainFile"});
        
        // Test case 3: More than 4 args
        JSJshop.main(new String[] {"domainFile", "problemFile", "param1", "param2", "param3"});
        
        // Test case 4: Equal to 2 args
        JSJshop.main(new String[] {"domainFile", "problemFile"});
        
        // Test case 5: Valid args with number-of-plans being "one"
        JSJshop.main(new String[] {"domainFile", "problemFile", "one"});
        
        // Test case 6: Valid args with number-of-plans being "all"
        JSJshop.main(new String[] {"domainFile", "problemFile", "all"});
    }
    
    @Test
    public void testConstructor() {
        // Test case 1: Empty constructor
        JSJshop jsjShop = new JSJshop();
        
        // Test case 2: Constructor with nameDomainFile and nameProblemFile
        JSJshop jsjShop2 = new JSJshop("domainFile", "problemFile");
        
        // Test case 3: Constructor with nameFile and pred
        JSJshop jsjShop3 = new JSJshop("fileName", new JSTaskAtom());
        
        // Test case 4: Getters
        assertNotNull(jsjShop2.getTree());
        assertNotNull(jsjShop2.getSolution());
        assertNull(jsjShop2.getAddList());
        assertNull(jsjShop2.getDeleteList());
    }
    
    @Test
    public void testSetFile() {
        // Test case 1: Empty fileName and pred
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.setFile("", new JSTaskAtom()));
        
        // Test case 2: Valid fileName and pred
        assertNotNull(jsjShop.setFile("fileName", new JSTaskAtom()));
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsjShop = new JSJshop();
        // No assert statement as this method only prints output
        jsjShop.testParser();
    }
    
    @Test
    public void testParserFile() {
        JSJshop jsjShop = new JSJshop();
        // Test case 1: Valid libraryFile
        assertTrue(jsjShop.parserFile("libraryFile"));
        
        // Test case 2: Invalid libraryFile
        assertFalse(jsjShop.parserFile(""));
    }
    
    @Test
    public void testGetBufferedReader() {
        JSJshop jsjShop = new JSJshop();
        // Test case 1: Applet is null
        assertNull(jsjShop.getBufferedReader("dir", "file"));
        
        // Test case 2: Valid applet
        assertNull(jsjShop.getBufferedReader("dir", "file", new JApplet()));
    }
    
    @Test
    public void testProcessToken() {
        JSJshop jsjShop = new JSJshop();
        // No assert statement as this method throws JSParserError
        jsjShop.processToken(new StreamTokenizer());
    }
    
    @Test
    public void testDom() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.dom());
    }
    
    @Test
    public void testProb() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.prob());
    }
    
    @Test
    public void testSol() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.sol());
    }
    
    @Test
    public void testTree() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.tree());
    }
    
}