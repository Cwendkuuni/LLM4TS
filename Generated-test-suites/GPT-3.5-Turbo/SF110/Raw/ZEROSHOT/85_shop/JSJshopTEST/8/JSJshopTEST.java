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
    public void testMainValidArgumentsOnePlan() {
        String[] args = {"domainDef-file-name", "problemDef-file-name", "one"};
        JSJshop.main(args);
        assertFalse(JSJshop.jshopVars.allPlans);
    }
    
    @Test
    public void testMainValidArgumentsAllPlans() {
        String[] args = {"domainDef-file-name", "problemDef-file-name", "all"};
        JSJshop.main(args);
        assertTrue(JSJshop.jshopVars.allPlans);
    }
    
    @Test
    public void testMainInvalidArguments() {
        String[] args = {"domainDef-file-name"};
        JSJshop.main(args);
        // Ensure nothing happens when arguments are invalid
        assertNull(JSJshop.jshopVars);
        assertNull(JSJshop.dom);
    }

    @Test
    public void testJSJshopConstructor() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.probSet);
        assertNull(jshop.tree);
    }
    
    @Test
    public void testJSJshopConstructorWithFiles() {
        JSJshop jshop = new JSJshop("testDomainFile.txt", "testProblemFile.txt");
        assertNotNull(jshop.dom);
        assertNotNull(jshop.probSet);
    }
    
    @Test
    public void testJSJshopConstructorWithFileAndTaskAtom() {
        JSTaskAtom taskAtom = new JSTaskAtom();
        JSJshop jshop = new JSJshop("testFile.txt", taskAtom);
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
        JSPairPlanTSListNodes pair = jshop.setFile("testFile.txt", new JSTaskAtom());
        assertNull(pair);
    }
    
    @Test
    public void testTestParser() {
        jshop.testParser();
        // Ensure no exceptions are thrown
    }
    
    @Test
    public void testParserFile() {
        boolean result = jshop.parserFile("testFile.txt");
        assertFalse(result);
    }
    
    @Test
    public void testParserFileWithInvalidFile() {
        boolean result = jshop.parserFile(null);
        assertFalse(result);
    }
    
    @Test
    public void testProcessTokenWithDefDomain() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.sval = "defdomain";
        tokenizer.lineno = 5;
        assertThrows(JSParserError.class, () -> jshop.processToken(tokenizer));
    }
    
    @Test
    public void testProcessTokenWithDefProblem() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.sval = "defproblem";
        tokenizer.lineno = 5;
        assertThrows(JSParserError.class, () -> jshop.processToken(tokenizer));
    }
    
    @Test
    public void testProcessTokenWithInvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.sval = "invalid";
        tokenizer.lineno = 5;
        assertThrows(JSParserError.class, () -> jshop.processToken(tokenizer));
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