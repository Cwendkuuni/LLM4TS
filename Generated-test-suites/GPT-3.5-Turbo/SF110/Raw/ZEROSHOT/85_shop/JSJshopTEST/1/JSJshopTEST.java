import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JSJshopTest {

    private JSJshop jshop;

    @Before
    public void setUp() {
        jshop = new JSJshop();
    }

    @Test
    public void testMain() {
        String[] args = new String[2];
        args[0] = "domainDef-file-name";
        args[1] = "problemDef-file-name";

        JSJshop.main(args);
    }

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNotNull(jsjshop);
    }

    @Test
    public void testJSJshop2() {
        JSJshop jsjshop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjshop);
    }

    @Test
    public void testJSJshop3() {
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjshop = new JSJshop("nameFile", pred);
        assertNotNull(jsjshop);
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
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        JSPairPlanTSListNodes result = jshop.setFile(nameFile, pred);
        assertNull(result);
    }

    @Test
    public void testTestParser() {
        jshop.testParser();
    }

    @Test
    public void testParserFile() {
        String libraryFile = "libraryFile";
        boolean result = jshop.parserFile(libraryFile);
        assertFalse(result);
    }

    @Test
    public void testGetBufferedReader() {
        String dir = "dir";
        String file = "file";
        BufferedReader br = jshop.getBufferedReader(dir, file);
        assertNull(br);
    }

    @Test
    public void testGetBufferedReader2() {
        String dir = "dir";
        String file = "file";
        JApplet applet = new JApplet();
        BufferedReader br = jshop.getBufferedReader(dir, file, applet);
        assertNull(br);
    }

    @Test
    public void testGetAppletURL() {
        String file = "file";
        JApplet applet = new JApplet();
        URL url = jshop.getAppletURL(file, applet);
        assertNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        try {
            jshop.processToken(tokenizer);
            fail("Exception not thrown");
        } catch (JSParserError e) {
            // Exception successfully thrown
        }
    }

    @Test
    public void testDom() {
        JSPlanningDomain domain = jshop.dom();
        assertNull(domain);
    }

    @Test
    public void testProb() {
        JSPlanningProblem problem = jshop.prob();
        assertNull(problem);
    }

    @Test
    public void testSol() {
        JSPlan plan = jshop.sol();
        assertNull(plan);
    }

    @Test
    public void testTree() {
        JSJshopNode node = jshop.tree();
        assertNull(node);
    }
}