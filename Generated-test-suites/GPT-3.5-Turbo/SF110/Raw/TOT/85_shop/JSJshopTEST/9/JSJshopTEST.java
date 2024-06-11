import org.junit.Test;

import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        // Typical use case
        String[] args = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args);
        // Edge cases
        args = new String[] {};
        JSJshop.main(args);
        args = new String[] {"domainDef-file-name"};
        JSJshop.main(args);
        args = new String[] {"domainDef-file-name", "problemDef-file-name", "one"};
        JSJshop.main(args);
        args = new String[] {"domainDef-file-name", "problemDef-file-name", "all"};
        JSJshop.main(args);
    }

    @Test
    public void testJSJshop() {
        // Typical use case
        JSJshop jsjShop = new JSJshop();
        assertNotNull(jsjShop.probSet);
        assertNull(jsjShop.tree);
        // Edge cases
        jsjShop = new JSJshop(null, null);
        assertNotNull(jsjShop.probSet);
        assertNull(jsjShop.tree);
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.probSet);
        assertNotNull(jsjShop.tree);
        jsjShop = new JSJshop("nameFile", new JSTaskAtom());
        assertNotNull(jsjShop.probSet);
        assertNull(jsjShop.tree);
    }

    @Test
    public void testGetTree() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.getTree());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.getTree());
    }

    @Test
    public void testGetSolution() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.getSolution());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.getSolution());
    }

    @Test
    public void testGetAddList() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.getAddList());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.getAddList());
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.getDeleteList());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.getDeleteList());
    }

    @Test
    public void testSetFile() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.setFile("nameFile", new JSTaskAtom()));
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.setFile("nameFile", new JSTaskAtom()));
    }

    @Test
    public void testTestParser() {
        JSJshop jsjShop = new JSJshop();
        jsjShop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop jsjShop = new JSJshop();
        assertFalse(jsjShop.parserFile("libraryFile"));
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertTrue(jsjShop.parserFile("libraryFile"));
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.getBufferedReader("dir", "file"));
        assertNull(jsjShop.getBufferedReader("dir", "file", new JApplet()));
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.getAppletURL("file", new JApplet()));
    }

    @Test
    public void testProcessToken() {
        JSJshop jsjShop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.pushBack();
        try {
            jsjShop.processToken(tokenizer);
            fail("Should have thrown JSParserError");
        } catch (JSParserError e) {
            // Exception expected
        }
    }

    @Test
    public void testDom() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.dom());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.dom());
    }

    @Test
    public void testProb() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.prob());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.prob());
    }

    @Test
    public void testSol() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.sol());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.sol());
    }

    @Test
    public void testTree() {
        JSJshop jsjShop = new JSJshop();
        assertNull(jsjShop.tree());
        jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jsjShop.tree());
    }
}