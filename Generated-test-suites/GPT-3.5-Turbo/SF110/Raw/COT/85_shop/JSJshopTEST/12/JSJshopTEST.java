import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args);
    }

    @Test
    public void testJSJshop() {
        JSJshop jsJshop = new JSJshop();
        Assert.assertNotNull(jsJshop);
    }

    @Test
    public void testJSJshopWithNameFiles() {
        JSJshop jsJshop = new JSJshop("nameDomainFile", "nameProblemFile");
        Assert.assertNotNull(jsJshop);
    }

    @Test
    public void testJSJshopWithNameFileAndTaskAtom() {
        JSJshop jsJshop = new JSJshop("nameFile", new JSTaskAtom());
        Assert.assertNotNull(jsJshop);
    }

    @Test
    public void testGetTree() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.getTree();
        Assert.assertNull(tree);
    }

    @Test
    public void testGetSolution() {
        JSJshop jsJshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        Assert.assertNull(solution);
    }

    @Test
    public void testGetAddList() {
        JSJshop jsJshop = new JSJshop();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        Assert.assertNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsJshop = new JSJshop();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        Assert.assertNull(deleteList);
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        JSPairPlanTSListNodes pair = jsJshop.setFile("nameFile", new JSTaskAtom());
        Assert.assertNull(pair);
    }

    @Test
    public void testTestParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        boolean result = jsJshop.parserFile("libraryFile");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetBufferedReader1() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader br = jsJshop.getBufferedReader("dir", "file1");
        Assert.assertNull(br);
    }

    @Test
    public void testGetBufferedReader2() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader br = jsJshop.getBufferedReader("dir", "file2", new JApplet());
        Assert.assertNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsJshop = new JSJshop();
        URL url = jsJshop.getAppletURL("file", new JApplet());
        Assert.assertNull(url);
    }
    
    @Test
    public void testDom() {
        JSJshop jsJshop = new JSJshop();
        JSPlanningDomain dom = jsJshop.dom();
        Assert.assertNull(dom);
    }

    @Test
    public void testProb() {
        JSJshop jsJshop = new JSJshop();
        JSPlanningProblem prob = jsJshop.prob();
        Assert.assertNull(prob);
    }

    @Test
    public void testSol() {
        JSJshop jsJshop = new JSJshop();
        JSPlan sol = jsJshop.sol();
        Assert.assertNull(sol);
    }

    @Test
    public void testTree() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.tree();
        Assert.assertNull(tree);
    }
}