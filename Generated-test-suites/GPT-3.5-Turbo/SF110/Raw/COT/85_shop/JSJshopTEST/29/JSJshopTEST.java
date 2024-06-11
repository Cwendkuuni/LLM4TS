import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        // Test1: args.length < 2
        String[] args1 = new String[0];
        JSJshop.main(args1);

        // Test2: args.length > 4
        String[] args2 = new String[5];
        JSJshop.main(args2);

        // Test3: args.length == 2
        String[] args3 = new String[2];
        JSJshop.main(args3);

        // Test4: args.length == 3, one is passed
        String[] args4 = new String[3];
        args4[2] = "one";
        JSJshop.main(args4);

        // Test5: args.length == 3, all is passed
        String[] args5 = new String[3];
        args5[2] = "all";
        JSJshop.main(args5);

        // Test6: args.length == 3, invalid number
        String[] args6 = new String[3];
        args6[2] = "invalid";
        JSJshop.main(args6);
    }

    @Test
    public void testJSJshop() {
        JSJshop shop = new JSJshop();
        assertNotNull(shop);

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(shop2);
    }

    @Test
    public void testGetTree() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getTree());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.getTree());
    }

    @Test
    public void testGetSolution() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getSolution());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.getSolution());
    }

    @Test
    public void testGetAddList() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getAddList());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.getAddList());
    }

    @Test
    public void testGetDeleteList() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getDeleteList());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.getDeleteList());
    }

    @Test
    public void testSetFile() {
        JSJshop shop = new JSJshop();
        assertNull(shop.setFile("nameFile", JSTaskAtom));

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.setFile("nameFile", JSTaskAtom));
    }

    @Test
    public void testTestParser() {
        JSJshop shop = new JSJshop();
        shop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop shop = new JSJshop();
        assertFalse(shop.parserFile("libraryFile"));

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertFalse(shop2.parserFile("libraryFile"));
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getBufferedReader("dir", "file"));
        assertNull(shop.getBufferedReader("dir", "file", JApplet));
    }

    @Test
    public void testGetAppletURL() {
        JSJshop shop = new JSJshop();
        assertNull(shop.getAppletURL("file", JApplet));
    }

    @Test
    public void testProcessToken() {
        JSJshop shop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
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

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.dom());
    }

    @Test
    public void testProb() {
        JSJshop shop = new JSJshop();
        assertNull(shop.prob());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.prob());
    }

    @Test
    public void testSol() {
        JSJshop shop = new JSJshop();
        assertNull(shop.sol());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.sol());
    }

    @Test
    public void testTree() {
        JSJshop shop = new JSJshop();
        assertNull(shop.tree());

        JSJshop shop2 = new JSJshop("domainFile", "problemFile");
        assertNull(shop2.tree());
    }
}