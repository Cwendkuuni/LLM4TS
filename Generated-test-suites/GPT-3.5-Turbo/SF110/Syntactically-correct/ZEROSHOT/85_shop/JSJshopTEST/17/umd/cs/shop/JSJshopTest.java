package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    private JSJshop jsjshop;

    @Before
    public void setUp() {
        jsjshop = new JSJshop();
    }

    @Test
    public void testMain() {
        String[] args = {"domainFile", "problemFile"};
        JSJshop.main(args);
        // Add assertions here
    }

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        // Add assertions here
    }

    @Test
    public void testJSJshopStringString() {
        String nameDomainFile = "domainFile";
        String nameProblemFile = "problemFile";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        // Add assertions here
    }

    @Test
    public void testJSJshopStringJSTaskAtom() {
        String nameFile = "file";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjshop = new JSJshop(nameFile, pred);
        // Add assertions here
    }

    @Test
    public void testGetTree() {
        JSJshopNode result = jsjshop.getTree();
        // Add assertions here
    }

    @Test
    public void testGetSolution() {
        JSPairPlanTSListNodes result = jsjshop.getSolution();
        // Add assertions here
    }

    @Test
    public void testGetAddList() {
        JSListLogicalAtoms result = jsjshop.getAddList();
        // Add assertions here
    }

    @Test
    public void testGetDeleteList() {
        JSListLogicalAtoms result = jsjshop.getDeleteList();
        // Add assertions here
    }

    @Test
    public void testSetFile() {
        String nameFile = "file";
        JSTaskAtom pred = new JSTaskAtom();
        JSPairPlanTSListNodes result = jsjshop.setFile(nameFile, pred);
        // Add assertions here
    }

    @Test
    public void testTestParser() {
        jsjshop.testParser();
        // Add assertions here
    }

    @Test
    public void testParserFile() {
        String libraryFile = "libFile";
        boolean result = jsjshop.parserFile(libraryFile);
        // Add assertions here
    }

    @Test
    public void testGetBufferedReader() {
        String dir = "dir";
        String file = "file";
        BufferedReader result = jsjshop.getBufferedReader(dir, file);
        // Add assertions here
    }

    @Test
    public void testGetBufferedReaderWithApplet() {
        String dir = "dir";
        String file = "file";
        JApplet applet = new JApplet();
        BufferedReader result = jsjshop.getBufferedReader(dir, file, applet);
        // Add assertions here
    }

    @Test
    public void testGetAppletURL() {
        String file = "file";
        JApplet applet = new JApplet();
        URL result = jsjshop.getAppletURL(file, applet);
        // Add assertions here
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        jsjshop.processToken(tokenizer);
        // Add assertions here
    }

    @Test
    public void testDom() {
        JSPlanningDomain result = jsjshop.dom();
        // Add assertions here
    }

    @Test
    public void testProb() {
        JSPlanningProblem result = jsjshop.prob();
        // Add assertions here
    }

    @Test
    public void testSol() {
        JSPlan result = jsjshop.sol();
        // Add assertions here
    }

    @Test
    public void testTree() {
        JSJshopNode result = jsjshop.tree();
        // Add assertions here
    }
}