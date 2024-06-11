package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPairPlanTSListNodes;

public class JSJshopTest {

    private JSJshop jsJshop;

    @Before
    public void setUp() {
        jsJshop = new JSJshop();
    }

    @Test
    public void testJSJshop() {
        JSJshop jsJshop = new JSJshop();
        assertNotNull(jsJshop.dom());
        assertNull(jsJshop.prob());
        assertNull(jsJshop.sol());
        assertNull(jsJshop.tree());
    }

    @Test
    public void testJSJshopStringString() {
        JSJshop jsJshop = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsJshop.dom());
        assertNotNull(jsJshop.prob());
        assertNotNull(jsJshop.sol());
        assertNotNull(jsJshop.tree());
    }

    @Test
    public void testJSJshopStringJSTaskAtom() {
        JSJshop jsJshop = new JSJshop("nameFile", new JSTaskAtom());
        assertNotNull(jsJshop.dom());
        assertNull(jsJshop.prob());
        assertNull(jsJshop.sol());
        assertNull(jsJshop.tree());
    }

    @Test
    public void testGetTree() {
        JSJshopNode tree = jsJshop.getTree();
        assertNull(tree);
    }

    @Test
    public void testGetSolution() {
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNull(solution);
    }

    @Test
    public void testGetAddList() {
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNull(deleteList);
    }

    @Test
    public void testSetFile() {
        JSPairPlanTSListNodes pairPlan = jsJshop.setFile("nameFile", new JSTaskAtom());
        assertNull(pairPlan);
    }

    @Test
    public void testParserFile() {
        boolean result = jsJshop.parserFile("libraryFile");
        assertEquals(true, result);
    }

    @Test
    public void testGetBufferedReader() {
        BufferedReader br = jsJshop.getBufferedReader("dir", "file");
        assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JApplet();
        URL url = jsJshop.getAppletURL("file", applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new FileReader("file"));
        tokenizer.slashSlashComments(true);
        tokenizer.quoteChar('"');
        tokenizer.wordChars('!', '/');
        tokenizer.wordChars(':', '@');
        tokenizer.eolIsSignificant(true);
        tokenizer.parseNumbers();
        tokenizer.whitespaceChars('=', 30);
        tokenizer.whitespaceChars(')', 40);
        tokenizer.whitespaceChars(' ', 20);
        tokenizer.ordinaryChar('?');

        try {
            while (tokenizer.nextToken() != -1) {
                jsJshop.processToken(tokenizer);
            }
        } catch (JSParserError e) {
            e.printStackTrace();
            assertEquals(null, e.getMessage());
        }
        
        assertEquals(0, tokenizer.ttype);
    }

}