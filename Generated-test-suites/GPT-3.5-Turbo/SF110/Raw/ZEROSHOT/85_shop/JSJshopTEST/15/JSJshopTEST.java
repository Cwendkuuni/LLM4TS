import org.junit.*;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = {"domainFile", "problemFile"};
        JSJshop.main(args);
    }

    @Test
    public void testConstructor1() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getTree());
        assertNull(jshop.getSolution());
        assertNull(jshop.getAddList());
        assertNull(jshop.getDeleteList());
    }

    @Test
    public void testConstructor2() {
        JSJshop jshop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(jshop.getTree());
        assertNotNull(jshop.getSolution());
        assertNotNull(jshop.getAddList());
        assertNotNull(jshop.getDeleteList());
    }

    @Test
    public void testConstructor3() {
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jshop = new JSJshop("nameFile", pred);
        assertNotNull(jshop.getTree());
        assertNotNull(jshop.getSolution());
        assertNotNull(jshop.getAddList());
        assertNotNull(jshop.getDeleteList());
    }

    @Test
    public void testGetTree() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getTree());
    }

    @Test
    public void testGetSolution() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getSolution());
    }

    @Test
    public void testGetAddList() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getAddList());
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getDeleteList());
    }

    @Test
    public void testSetFile() {
        JSJshop jshop = new JSJshop();
        JSPairPlanTSListNodes result = jshop.setFile("nameFile", new JSTaskAtom());
        assertNotNull(result);
    }

    @Test
    public void testTestParser() {
        JSJshop jshop = new JSJshop();
        jshop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop jshop = new JSJshop();
        assertFalse(jshop.parserFile("libraryFile"));
    }

    @Test
    public void testGetBufferedReader1() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getBufferedReader("dir", "file"));
    }

    @Test
    public void testGetBufferedReader2() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getBufferedReader("dir", "file", new JApplet()));
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.getAppletURL("file", new JApplet()));
    }

    @Test
    public void testProcessToken() {
        JSJshop jshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.quoteChar('\'');
        tokenizer.wordChars(36, 126);

        try {
            tokenizer.pushBack();
            tokenizer.pushBack();
        } catch (Exception e) {
            fail("Failed to setup StreamTokenizer");
        }

        try {
            jshop.processToken(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // pass
        }
    }

    @Test
    public void testDom() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.dom());
    }

    @Test
    public void testProb() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.prob());
    }

    @Test
    public void testSol() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.sol());
    }

    @Test
    public void testTree() {
        JSJshop jshop = new JSJshop();
        assertNull(jshop.tree());
    }

}