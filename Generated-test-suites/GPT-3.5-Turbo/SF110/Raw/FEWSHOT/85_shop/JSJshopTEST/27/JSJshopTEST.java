package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class JSJshopTest {
    private JSJshop jshop;

    @Before
    public void setup() {
        jshop = new JSJshop();
    }

    @Test
    public void testMain() {
        // Test with valid arguments
        JSJshop.main(new String[]{"domainFile", "problemFile"});
        
        // Test with invalid arguments
        JSJshop.main(new String[]{"domainFile"});
        JSJshop.main(new String[]{"domainFile", "problemFile", "one", "extraArgument"});
        JSJshop.main(new String[]{"domainFile", "problemFile", "invalidArgument"});
        
        assertNotNull(JSJshop.applet);
    }
    
    @Test
    public void testJSJshop() {
        jshop = new JSJshop();
        assertNull(jshop.prob);
    }
    
    @Test
    public void testJSJshopStringString() {
        jshop = new JSJshop("nameDomainFile", "nameProblemFile");
        
        assertNotNull(jshop.probSet);
        assertNotNull(jshop.tree);
    }
    
    @Test
    public void testJSJshopStringJSTaskAtom() {
        JSTaskAtom pred = new JSTaskAtom();
        jshop = new JSJshop("nameFile", pred);
        
        assertNotNull(jshop.probSet);
        assertNotNull(jshop.tree);
    }
    
    @Test
    public void testGetTree() {
        JSJshopNode tree = jshop.getTree();
        assertNull(tree);
    }
    
    @Test
    public void testGetSolution() {
        JSPairPlanTSListNodes solution = jshop.getSolution();
        assertNull(solution);
    }
    
    @Test
    public void testGetAddList() {
        JSListLogicalAtoms addList = jshop.getAddList();
        assertNull(addList);
    }
    
    @Test
    public void testGetDeleteList() {
        JSListLogicalAtoms deleteList = jshop.getDeleteList();
        assertNull(deleteList);
    }
    
    @Test
    public void testSetFile() {
        JSPairPlanTSListNodes pairPlan = jshop.setFile("nameFile", new JSTaskAtom());
        
        assertNotNull(pairPlan);
    }
    
    @Test
    public void testTestParser() {
        jshop.testParser();
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testParserFile() {
        boolean success = jshop.parserFile("libraryFile");
        
        assertEquals(true, success);
        assertNotNull(jshop.dom);
    }
    
    @Test
    public void testGetBufferedReaderStringString() {
        BufferedReader br = jshop.getBufferedReader(".", "file");
        
        assertNotNull(br);
    }
    
    @Test
    public void testGetBufferedReaderStringStringApplet() {
        BufferedReader br = jshop.getBufferedReader(".", "file", JSJshop.applet);
        
        assertNotNull(br);
    }
    
    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.quoteChar('\'');
        tokenizer.quoteChar('\"');
        tokenizer.nextToken();
        
        jshop.processToken(tokenizer);
        
        assertNotNull(jshop.dom);
        
        tokenizer.nextToken();
        
        jshop.processToken(tokenizer);
        
        assertNotNull(jshop.probSet);
        assertNotNull(jshop.prob);
    }
    
    @Test
    public void testDom() {
        JSPlanningDomain dom = jshop.dom();
        assertNull(dom);
    }
    
    @Test
    public void testProb() {
        JSPlanningProblem prob = jshop.prob();
        assertNull(prob);
    }
    
    @Test
    public void testSol() {
        JSPlan sol = jshop.sol();
        assertNull(sol);
    }
    
    @Test
    public void testTree() {
        JSJshopNode tree = jshop.tree();
        assertNull(tree);
    }
}