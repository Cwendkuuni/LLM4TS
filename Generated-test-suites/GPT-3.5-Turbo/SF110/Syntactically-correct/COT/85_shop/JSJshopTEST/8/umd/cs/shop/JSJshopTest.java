import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testMain() {
        assertFalse(JSJshop.main(new String[] {"file1", "file2"}));
    }
    
    @Test
    public void testJSJshop() {
        JSJshop shop = new JSJshop();
        assertNotNull(shop);
    }
    
    @Test
    public void testJSJshopStringString() {
        JSJshop shop = new JSJshop("domainFile", "problemFile");
        assertNotNull(shop);
    }
    
    @Test
    public void testJSJshopStringJSTaskAtom() {
        JSJshop shop = new JSJshop("file", new JSTaskAtom());
        assertNotNull(shop);
    }
    
    @Test
    public void testGetTree() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getTree());
    }
    
    @Test
    public void testGetSolution() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getSolution());
    }
    
    @Test
    public void testGetAddList() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getAddList());
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getDeleteList());
    }
    
    @Test
    public void testSetFile() {
        JSJshop shop = new JSJshop();
        assertNull(shop.setFile("fileName", new JSTaskAtom()));
    }
    
    @Test
    public void testTestParser() {
        JSJshop shop = new JSJshop();
        shop.testParser();
        // Manual analysis required to check correctness
    }
    
    @Test
    public void testParserFile() {
        JSJshop shop = new JSJshop();
        assertFalse(shop.parserFile("file"));
    }
    
    @Test
    public void testGetBufferedReader() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getBufferedReader("dir", "file"));
    }
    
    @Test
    public void testGetBufferedReaderWithApplet() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getBufferedReader("dir", "file", new JApplet()));
    }
    
    @Test
    public void testProcessToken() {
        JSJshop shop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.sval = "(defdomain";
        try {
            shop.processToken(tokenizer);
        } catch (JSParserError e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDom() {
        JSJshop shop = new JSJshop();
        assertNull(shop.dom());
    }
    
    @Test
    public void testProb() {
        JSJshop shop = new JSJshop();
        assertNull(shop.prob());
    }
    
    @Test
    public void testSol() {
        JSJshop shop = new JSJshop();
        assertNull(shop.sol());
    }
    
    @Test
    public void testTree() {
        JSJshop shop = new JSJshop();
        assertNull(shop.tree());
    }

}