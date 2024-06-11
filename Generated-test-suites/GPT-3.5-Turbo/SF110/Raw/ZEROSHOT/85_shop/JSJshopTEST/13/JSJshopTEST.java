import junit.framework.TestCase;

public class JSJshopTest extends TestCase {

    public void testMain() {
        String[] args = {"domainFile", "problemFile", "one"};
        JSJshop.main(args);
    }

    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNotNull(jsjshop);
    }

    public void testJSJshopStringString() {
        JSJshop jsjshop = new JSJshop("domainFile", "problemFile");
        assertNotNull(jsjshop);
    }

    public void testJSJshopStringJSTaskAtom() {
        JSJshop jsjshop = new JSJshop("nameFile", new JSTaskAtom());
        assertNotNull(jsjshop);
    }

    public void testGetTree() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getTree());
    }

    public void testGetSolution() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getSolution());
    }

    public void testGetAddList() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getAddList());
    }

    public void testGetDeleteList() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getDeleteList());
    }

    public void testSetFile() {
        JSJshop jsjshop = new JSJshop();
        JSPairPlanTSListNodes result = jsjshop.setFile("nameFile", new JSTaskAtom());
        assertNull(result);
    }

    public void testTestParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }

    public void testParserFile() {
        JSJshop jsjshop = new JSJshop();
        boolean result = jsjshop.parserFile("libraryFile");
        assertFalse(result);
    }

    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader("dir", "file");
        assertNull(br);
    }

    public void testGetBufferedReaderJApplet() {
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader("dir", "file", null);
        assertNull(br);
    }

    public void testGetAppletURL() {
        JSJshop jsjshop = new JSJshop();
        String file = "file";
        JApplet applet = new JApplet();
        URL url = jsjshop.getAppletURL(file, applet);
        assertNull(url);
    }

    public void testProcessToken() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        try {
            jsjshop.processToken(tokenizer);
        } catch (JSParserError e) {
            fail("Test failed due to JSParserError");
        }
    }

    public void testDom() {
        JSJshop jsjshop = new JSJshop();
        JSPlanningDomain result = jsjshop.dom();
        assertNull(result);
    }

    public void testProb() {
        JSJshop jsjshop = new JSJshop();
        JSPlanningProblem result = jsjshop.prob();
        assertNull(result);
    }

    public void testSol() {
        JSJshop jsjshop = new JSJshop();
        JSPlan result = jsjshop.sol();
        assertNull(result);
    }

    public void testTree() {
        JSJshop jsjshop = new JSJshop();
        JSJshopNode result = jsjshop.tree();
        assertNull(result);
    }
}