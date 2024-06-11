package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {
    private JSJshop jshop;
    
    @Before
    public void setUp() {
        jshop = new JSJshop();
    }
    
    @Test
    public void testParser() {
        jshop.testParser();
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testParserFile() {
        assertTrue(jshop.parserFile("testFile"));
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testGetBufferedReader() {
        assertNotNull(jshop.getBufferedReader("", ""));
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testGetAppletURL() {
        assertNotNull(jshop.getAppletURL("", null));
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(null);
        jshop.processToken(tokenizer);
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testDom() {
        JSPlanningDomain dom = jshop.dom();
        assertNull(dom);
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testProb() {
        JSPlanningProblem prob = jshop.prob();
        assertNull(prob);
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testSol() {
        JSPlan sol = jshop.sol();
        assertNull(sol);
        // Add assert statement here to validate the test
    }
    
    @Test
    public void testTree() {
        JSJshopNode tree = jshop.tree();
        assertNull(tree);
        // Add assert statement here to validate the test
    }  
}