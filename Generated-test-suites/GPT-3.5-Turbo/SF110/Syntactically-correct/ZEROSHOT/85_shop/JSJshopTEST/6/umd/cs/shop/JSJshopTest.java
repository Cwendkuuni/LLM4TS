package umd.cs.shop.test;

import org.junit.Test;
import umd.cs.shop.JSJshop;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        // Test case 1: with valid inputs
        String[] args1 = {"domainDef-file-name", "problemDef-file-name", "one"};
        JSJshop.main(args1);
        
        // Test case 2: with invalid inputs
        String[] args2 = {"domainDef-file-name"};
        JSJshop.main(args2);
        
        // Test case 3: with invalid inputs
        String[] args3 = {"domainDef-file-name", "problemDef-file-name", "invalid"};
        JSJshop.main(args3);
        
        // Test case 4: with valid inputs
        String[] args4 = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args4);
    }

    @Test
    public void testJSJshop() {
        // Test case 1: with default constructor
        JSJshop jsShop1 = new JSJshop();
        
        // Test case 2: with parameterized constructor
        JSJshop jsShop2 = new JSJshop("domainDef-file-name", "problemDef-file-name");
        
        // Test case 3: with parameterized constructor
        JSJshop jsShop3 = new JSJshop("nameFile", new JSTaskAtom());
    }

    @Test
    public void testGetTree() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getTree());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.getTree());
    }

    @Test
    public void testGetSolution() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getSolution());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.getSolution());
    }

    @Test
    public void testGetAddList() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getAddList());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.getAddList());
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getDeleteList());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.getDeleteList());
    }

    @Test
    public void testSetFile() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.setFile("nameFile", new JSTaskAtom()));
    }

    @Test
    public void testTestParser() {
        JSJshop jsShop = new JSJshop();
        jsShop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop jsShop = new JSJshop();
        assertFalse(jsShop.parserFile("libraryFile"));
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getBufferedReader("dir", "file"));
        assertNull(jsShop.getBufferedReader("dir", "file", new JApplet()));
    }

    @Test
    public void testGetBufferedReader2() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getBufferedReader("dir", "file"));
        assertNull(jsShop.getBufferedReader("dir", "file", new JApplet()));
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getAppletURL("file", new JApplet()));
    }

    @Test
    public void testProcessToken() {
        JSJshop jsShop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new FileReader("libraryFile"));
        assertThrows(JSParserError.class, () -> jsShop.processToken(tokenizer));
    }

    @Test
    public void testDom() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.dom());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.dom());
    }

    @Test
    public void testProb() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.prob());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.prob());
    }

    @Test
    public void testSol() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.sol());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.sol());
    }

    @Test
    public void testTree() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.tree());
        
        jsShop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsShop.tree());
    }

}