import org.junit.*;
import static org.junit.Assert.*;

public class JSJshopTest {
    
    @Test
    public void testMainOnePlan() {
        String[] args = {"domainFile", "problemFile", "one"};
        JSJshop.main(args);
        // add assertion statements here
    }
    
    @Test
    public void testMainAllPlans() {
        String[] args = {"domainFile", "problemFile", "all"};
        JSJshop.main(args);
        // add assertion statements here
    }
    
    @Test
    public void testMainInvalidArgs() {
        String[] args = {"domainFile"};
        JSJshop.main(args);
        // add assertion statements here
    }
    
    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        // add assertion statements here
    }
    
    @Test
    public void testJSJshopWithNameFiles() {
        String nameDomainFile = "domainFile";
        String nameProblemFile = "problemFile";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        // add assertion statements here
    }
    
    @Test
    public void testJSJshopWithNameFileAndPred() {
        String nameFile = "file";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjshop = new JSJshop(nameFile, pred);
        // add assertion statements here
    }
    
    @Test
    public void testGetTree() {
        JSJshop jsjshop = new JSJshop();
        JSJshopNode tree = jsjshop.getTree();
        // add assertion statements here
    }
    
    @Test
    public void testGetSolution() {
        JSJshop jsjshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        // add assertion statements here
    }
    
    @Test
    public void testGetAddList() {
        JSJshop jsjshop = new JSJshop();
        JSListLogicalAtoms addList = jsjshop.getAddList();
        // add assertion statements here
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop jsjshop = new JSJshop();
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        // add assertion statements here
    }
    
    @Test
    public void testSetFile() {
        String nameFile = "file";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsjshop.setFile(nameFile, pred);
        // add assertion statements here
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
        // add assertion statements here
    }
    
    @Test
    public void testParserFile() {
        String libraryFile = "file";
        JSJshop jsjshop = new JSJshop();
        boolean result = jsjshop.parserFile(libraryFile);
        // add assertion statements here
    }
    
    @Test
    public void testGetBufferedReader() {
        String dir = "dir";
        String file = "file";
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader(dir, file);
        // add assertion statements here
    }
    
    @Test
    public void testGetBufferedReaderWithApplet() {
        String dir = "dir";
        String file = "file";
        JApplet applet = new JApplet();
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader(dir, file, applet);
        // add assertion statements here
    }
    
    @Test
    public void testGetAppletURL() {
        String file = "file";
        JApplet applet = new JApplet();
        JSJshop jsjshop = new JSJshop();
        URL url = jsjshop.getAppletURL(file, applet);
        // add assertion statements here
    }
    
    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSJshop jsjshop = new JSJshop();
        jsjshop.processToken(tokenizer);
        // add assertion statements here
    }
    
    @Test
    public void testDom() {
        JSJshop jsjshop = new JSJshop();
        JSPlanningDomain domain = jsjshop.dom();
        // add assertion statements here
    }
    
    @Test
    public void testProb() {
        JSJshop jsjshop = new JSJshop();
        JSPlanningProblem problem = jsjshop.prob();
        // add assertion statements here
    }
    
    @Test
    public void testSol() {
        JSJshop jsjshop = new JSJshop();
        JSPlan solution = jsjshop.sol();
        // add assertion statements here
    }
    
    @Test
    public void testTree() {
        JSJshop jsjshop = new JSJshop();
        JSJshopNode tree = jsjshop.tree();
        // add assertion statements here
    }
    
}