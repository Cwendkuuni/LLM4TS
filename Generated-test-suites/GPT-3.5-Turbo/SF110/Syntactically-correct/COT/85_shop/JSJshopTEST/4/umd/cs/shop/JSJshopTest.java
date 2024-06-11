import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = {"domainFile", "problemFile"};
        JSJshop.main(args);
        // TODO: Add assertions
    }

    @Test
    public void testJSJshop() {
        JSJshop jsjShop = new JSJshop();
        // TODO: Add assertions
    }

    @Test
    public void testJSJshopStringString() {
        String domainFile = "domainFile";
        String problemFile = "problemFile";
        JSJshop jsjShop = new JSJshop(domainFile, problemFile);
        // TODO: Add assertions
    }

    @Test
    public void testJSJshopStringJSTaskAtom() {
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjShop = new JSJshop(nameFile, pred);
        // TODO: Add assertions
    }

    @Test
    public void testGetTree() {
        JSJshop jsjShop = new JSJshop();
        JSJshopNode tree = jsjShop.getTree();
        // TODO: Add assertions
    }

    @Test
    public void testGetSolution() {
        JSJshop jsjShop = new JSJshop();
        JSPairPlanTSListNodes solution = jsjShop.getSolution();
        // TODO: Add assertions
    }

    @Test
    public void testGetAddList() {
        JSJshop jsjShop = new JSJshop();
        JSListLogicalAtoms addList = jsjShop.getAddList();
        // TODO: Add assertions
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsjShop = new JSJshop();
        JSListLogicalAtoms deleteList = jsjShop.getDeleteList();
        // TODO: Add assertions
    }

    @Test
    public void testSetFile() {
        JSJshop jsjShop = new JSJshop();
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        JSPairPlanTSListNodes pair = jsjShop.setFile(nameFile, pred);
        // TODO: Add assertions
    }

    @Test
    public void testTestParser() {
        JSJshop jsjShop = new JSJshop();
        jsjShop.testParser();
        // TODO: Add assertions
    }

    @Test
    public void testParserFile() {
        JSJshop jsjShop = new JSJshop();
        String libraryFile = "libraryFile";
        boolean result = jsjShop.parserFile(libraryFile);
        assertTrue(result);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsjShop = new JSJshop();
        String dir = ".";
        String file = "file";
        BufferedReader br = jsjShop.getBufferedReader(dir, file);
        // TODO: Add assertions
    }

    @Test
    public void testGetBufferedReaderWithApplet() {
        JSJshop jsjShop = new JSJshop();
        String dir = ".";
        String file = "file";
        JApplet applet = new JApplet();
        BufferedReader br = jsjShop.getBufferedReader(dir, file, applet);
        // TODO: Add assertions
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsjShop = new JSJshop();
        String file = "file";
        JApplet applet = new JApplet();
        URL url = jsjShop.getAppletURL(file, applet);
        // TODO: Add assertions
    }

    @Test
    public void testProcessToken() {
        JSJshop jsjShop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.wordChars();
        jsjShop.processToken(tokenizer);
        // TODO: Add assertions
    }

    @Test
    public void testDom() {
        JSJshop jsjShop = new JSJshop();
        JSPlanningDomain domain = jsjShop.dom();
        // TODO: Add assertions
    }

    @Test
    public void testProb() {
        JSJshop jsjShop = new JSJshop();
        JSPlanningProblem problem = jsjShop.prob();
        // TODO: Add assertions
    }

    @Test
    public void testSol() {
        JSJshop jsjShop = new JSJshop();
        JSPlan solution = jsjShop.sol();
        // TODO: Add assertions
    }

    @Test
    public void testTree() {
        JSJshop jsjShop = new JSJshop();
        JSJshopNode tree = jsjShop.tree();
        // TODO: Add assertions
    }
}