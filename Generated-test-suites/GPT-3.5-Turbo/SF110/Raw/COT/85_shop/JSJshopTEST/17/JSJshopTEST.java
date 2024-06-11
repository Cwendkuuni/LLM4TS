import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = {"domainDef-file-name", "problemDef-file-name", "one"};
        JSJshop.main(args);
    }

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNotNull(jsjshop);
    }

    @Test
    public void testJSJshopStringString() {
        String domainFile = "domainDef-file-name";
        String problemFile = "problemDef-file-name";
        JSJshop jsjshop = new JSJshop(domainFile, problemFile);
        assertNotNull(jsjshop);
    }

    @Test
    public void testJSJshopStringJSTaskAtom() {
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjshop = new JSJshop(nameFile, pred);
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
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        JSPairPlanTSListNodes solution = jsjshop.setFile(nameFile, pred);
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
        String libraryFile = "libraryFile";
        boolean result = jsjshop.parserFile(libraryFile);
        assertTrue(result);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        String dir = ".";
        String file = "file";
        BufferedReader br = jsjshop.getBufferedReader(dir, file);
        assertNotNull(br);
    }

    @Test
    public void testGetBufferedReaderWithApplet() {
        JSJshop jsjshop = new JSJshop();
        String dir = ".";
        String file = "file";
        JApplet applet = new JApplet();
        BufferedReader br = jsjshop.getBufferedReader(dir, file, applet);
        assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsjshop = new JSJshop();
        String file = "file";
        JApplet applet = new JApplet();
        URL url = jsjshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsjshop.processToken(tokenizer);
        } catch (JSParserError e) {
            e.printStackTrace();
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