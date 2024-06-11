package umd.cs.shop;

import org.junit.*;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = {"domainFile", "problemFile"};
        
        JSJshop.main(args);
        
        // add your assertions here
    }
    
    @Test
    public void testConstructorNoArgs() {
        JSJshop jsJshop = new JSJshop();
        
        // add your assertions here
    }
    
    @Test
    public void testConstructorWithNameFiles() {
        String nameDomainFile = "domainFile";
        String nameProblemFile = "problemFile";
        
        JSJshop jsJshop = new JSJshop(nameDomainFile, nameProblemFile);
        
        // add your assertions here
    }
    
    @Test
    public void testConstructorWithNameFileAndTaskAtom() {
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        
        JSJshop jsJshop = new JSJshop(nameFile, pred);
        
        // add your assertions here
    }
    
    @Test
    public void testGetTree() {
        JSJshop jsJshop = new JSJshop();
        
        JSJshopNode tree = jsJshop.getTree();
        
        // add your assertions here
    }
    
    @Test
    public void testGetSolution() {
        JSJshop jsJshop = new JSJshop();
        
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        
        // add your assertions here
    }
    
    @Test
    public void testGetAddList() {
        JSJshop jsJshop = new JSJshop();
        
        JSListLogicalAtoms addList = jsJshop.getAddList();
        
        // add your assertions here
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop jsJshop = new JSJshop();
        
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        
        // add your assertions here
    }
    
    @Test
    public void testSetFile() {
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        
        JSJshop jsJshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsJshop.setFile(nameFile, pred);
        
        // add your assertions here
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
        
        // add your assertions here
    }
    
    @Test
    public void testParserFile() {
        String libraryFile = "libraryFile";
        JSJshop jsJshop = new JSJshop();
        boolean result = jsJshop.parserFile(libraryFile);
        
        // add your assertions here
    }
    
    @Test
    public void testGetBufferedReader() {
        String dir = "dir";
        String file = "file";
        
        JSJshop jsJshop = new JSJshop();
        BufferedReader br = jsJshop.getBufferedReader(dir, file);
        
        // add your assertions here
    }
    
    @Test
    public void testGetBufferedReaderWithApplet() {
        String file = "file";
        JApplet applet = new JApplet();
        
        JSJshop jsJshop = new JSJshop();
        BufferedReader br = jsJshop.getBufferedReader(file, applet);
        
        // add your assertions here
    }
    
    @Test
    public void testGetAppletURL() {
        String file = "file";
        JApplet applet = new JApplet();
        
        JSJshop jsJshop = new JSJshop();
        URL url = jsJshop.getAppletURL(file, applet);
        
        // add your assertions here
    }
    
    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
        
        // add your assertions here
    }
    
    @Test
    public void testDom() {
        JSJshop jsJshop = new JSJshop();
        
        JSPlanningDomain dom = jsJshop.dom();
        
        // add your assertions here
    }
    
    @Test
    public void testProb() {
        JSJshop jsJshop = new JSJshop();
        
        JSPlanningProblem prob = jsJshop.prob();
        
        // add your assertions here
    }
    
    @Test
    public void testSol() {
        JSJshop jsJshop = new JSJshop();
        
        JSPlan sol = jsJshop.sol();
        
        // add your assertions here
    }
    
    @Test
    public void testTree() {
        JSJshop jsJshop = new JSJshop();
        
        JSJshopNode tree = jsJshop.tree();
        
        // add your assertions here
    }
}