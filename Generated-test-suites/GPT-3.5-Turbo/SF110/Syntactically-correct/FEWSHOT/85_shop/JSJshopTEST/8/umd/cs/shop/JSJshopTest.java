package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testConstructor1() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.probSet);
        assertNull(jsj.tree);
    }
    
    @Test
    public void testConstructor2() {
        JSJshop jsj = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsj.probSet);
        assertNull(jsj.tree);
    }
    
    @Test
    public void testConstructor3() {
        JSJshop jsj = new JSJshop("nameFile", null);
        assertNotNull(jsj.probSet);
        assertNull(jsj.tree);
    }
    
    @Test
    public void testGetTree() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.getTree());
    }
    
    @Test
    public void testGetSolution() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.getSolution());
    }
    
    @Test
    public void testGetAddList() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.getAddList());
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.getDeleteList());
    }
    
    @Test
    public void testSetFile() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.setFile("nameFile", null));
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsj = new JSJshop();
        jsj.testParser();
    }
    
    @Test
    public void testParserFile() {
        JSJshop jsj = new JSJshop();
        assertTrue(jsj.parserFile("libraryFile"));
    }
    
    @Test
    public void testGetBufferedReader() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.getBufferedReader("dir", "file"));
    }
    
    @Test
    public void testGetAppletURL() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.getAppletURL("file", null));
    }
    
    @Test
    public void testProcessToken() {
        JSJshop jsj = new JSJshop();
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(new FileReader("libraryFile"));
            tokenizer.nextToken();
            jsj.processToken(tokenizer);
        } catch (JSParserError e) {
            fail();
        } catch (IOException e) {
            fail();
        }
    }
    
    @Test
    public void testDom() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.dom());
    }
    
    @Test
    public void testProb() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.prob());
    }
    
    @Test
    public void testSol() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.sol());
    }
    
    @Test
    public void testTree() {
        JSJshop jsj = new JSJshop();
        assertNull(jsj.tree());
    }
    
    @Test
    public void testMain() {
        String[] args = {"arg1", "arg2"};
        JSJshop.main(args);
    }
}