import org.junit.*;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args1 = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args1);
        String[] args2 = {"invalid-domain", "invalid-problem"};
        JSJshop.main(args2);
        String[] args3 = {};
        JSJshop.main(args3);
        String[] args4 = {"domainDef-file-name", "problemDef-file-name", "one"};
        JSJshop.main(args4);
        String[] args5 = {"domainDef-file-name", "problemDef-file-name", "all"};
        JSJshop.main(args5);
        String[] args6 = {"domainDef-file-name", "problemDef-file-name", "invalid-number"};
        JSJshop.main(args6);
    }
    
    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNotNull(jsjshop);
    }
    
    @Test
    public void testJSJshopStringString() {
        JSJshop jsjshop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjshop);
    }
    
    @Test
    public void testJSJshopStringJSTaskAtom() {
        JSJshop jsjshop = new JSJshop("nameFile", new JSTaskAtom());
        assertNotNull(jsjshop);
    }
    
    @Test
    public void testGetTree() {
        JSJshop jsjshop = new JSJshop();
        JSJshopNode tree = jsjshop.getTree();
        assertNull(tree);
    }
    
    @Test
    public void testGetSolution() {
        JSJshop jsjshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        assertNull(solution);
    }
    
    @Test
    public void testGetAddList() {
        JSJshop jsjshop = new JSJshop();
        JSListLogicalAtoms addList = jsjshop.getAddList();
        assertNull(addList);
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop jsjshop = new JSJshop();
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertNull(deleteList);
    }
    
    @Test
    public void testSetFile() {
        JSJshop jsjshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsjshop.setFile("nameFile", new JSTaskAtom());
        assertNull(solution);
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }
    
    @Test
    public void testParserFile() {
        JSJshop jsjshop = new JSJshop();
        boolean result1 = jsjshop.parserFile("valid-file");
        assertTrue(result1);
        boolean result2 = jsjshop.parserFile("invalid-file");
        assertFalse(result2);
    }
    
    @Test
    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        BufferedReader br1 = jsjshop.getBufferedReader("directory", "file");
        assertNotNull(br1);
        BufferedReader br2 = jsjshop.getBufferedReader("directory", null);
        assertNull(br2);
        BufferedReader br3 = jsjshop.getBufferedReader(null, "file", new JApplet());
        assertNull(br3);
        BufferedReader br4 = jsjshop.getBufferedReader("directory", "file", null);
        assertNull(br4);
    }
    
    @Test
    public void testGetAppletURL() {
        JSJshop jsjshop = new JSJshop();
        URL url1 = jsjshop.getAppletURL("file", new JApplet());
        assertNotNull(url1);
        URL url2 = jsjshop.getAppletURL("file", null);
        assertNull(url2);
    }
    
    @Test
    public void testProcessToken() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        try {
            jsjshop.processToken(tokenizer);
        } catch (JSParserError e) {
            fail("JSParserError should not be thrown");
        }
    }
    
    @Test
    public void testDom() {
        JSJshop jsjshop = new JSJshop();
        JSPlanningDomain dom = jsjshop.dom();
        assertNull(dom);
    }
    
    @Test
    public void testProb() {
        JSJshop jsjshop = new JSJshop();
        JSPlanningProblem prob = jsjshop.prob();
        assertNull(prob);
    }
    
    @Test
    public void testSol() {
        JSJshop jsjshop = new JSJshop();
        JSPlan sol = jsjshop.sol();
        assertNull(sol);
    }
    
    @Test
    public void testTree() {
        JSJshop jsjshop = new JSJshop();
        JSJshopNode tree = jsjshop.tree();
        assertNull(tree);
    }
    
}