import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {
    
    @Test
    public void testMain() {
        // Test when args length is less than 2
        JSJshop.main(new String[0]);
        assertEquals("Usage :\n java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n[number-of-plans] can be 'one' or 'all'. The default value is 'one'\n", JSUtil.getPrintOutput());
        
        // Test when args length is greater than 4
        JSJshop.main(new String[5]);
        assertEquals("Usage :\n java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n[number-of-plans] can be 'one' or 'all'. The default value is 'one'\n", JSUtil.getPrintOutput());
        
        // Test when args length is between 2 and 4
        JSJshop.main(new String[] {"domain.txt", "problem.txt", "one"});
        // assert statements
    }
    
    @Test
    public void testJSJshop() {
        // Test constructor 1
        JSJshop jsjshop1 = new JSJshop();
        assertNotNull(jsjshop1);
        
        // Test constructor 2
        JSJshop jsjshop2 = new JSJshop("domain.txt", "problem.txt");
        assertNotNull(jsjshop2);
        
        // Test constructor 3
        // Create JSTaskAtom object
        JSTaskAtom atom = new JSTaskAtom();
        
        JSJshop jsjshop3 = new JSJshop("data.txt", atom);
        assertNotNull(jsjshop3);
    }
    
    @Test
    public void testGetTree() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.getTree());
    }
    
    @Test
    public void testGetSolution() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.getSolution());
    }
    
    @Test
    public void testGetAddList() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.getAddList());
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.getDeleteList());
    }
    
    @Test
    public void testSetFile() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.setFile("file.txt", new JSTaskAtom()));
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsjshop = new JSJshop();
        
        jsjshop.testParser();
        // assert statements to check the results
    }
    
    @Test
    public void testParserFile() {
        JSJshop jsjshop = new JSJshop();
        
        assertTrue(jsjshop.parserFile("file.txt"));
    }
    
    @Test
    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.getBufferedReader(".", "file.txt"));
    }
    
    @Test
    public void testProcessToken() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        
        tokenizer.sval = "defdomain";
        jsjshop.processToken(tokenizer);
        assertNotNull(jsjshop.dom());
        
        tokenizer.sval = "defproblem";
        jsjshop.processToken(tokenizer);
        assertNotNull(jsjshop.prob());
        
        tokenizer.sval = "invalid";
        try {
            jsjshop.processToken(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // expected
        }
    }
    
    @Test
    public void testDom() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.dom());
    }
    
    @Test
    public void testProb() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.prob());
    }
    
    @Test
    public void testSol() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.sol());
    }
    
    @Test
    public void testTree() {
        JSJshop jsjshop = new JSJshop();
        
        assertNull(jsjshop.tree());
    }
}