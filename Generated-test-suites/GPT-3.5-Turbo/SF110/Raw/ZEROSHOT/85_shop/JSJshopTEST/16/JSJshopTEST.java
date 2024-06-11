import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import umd.cs.shop.*;
import java.io.*;

public class JSJshopTest {
    private JSJshop jshop;
    private JSJshopNode tree;
    private JSPairPlanTSListNodes solution;

    @Before
    public void setUp() {
        jshop = new JSJshop();
        tree = new JSJshopNode();
        solution = new JSPairPlanTSListNodes();
    }

    @Test
    public void testMain() {
        StringWriter output = new StringWriter();
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        String[] args1 = {};
        JSJshop.main(args1);
        String[] args2 = {"file1.txt"};
        JSJshop.main(args2);
        String[] args3 = {"file1.txt", "file2.txt"};
        JSJshop.main(args3);
        String[] args4 = {"file1.txt", "file2.txt", "one"};
        JSJshop.main(args4);
        String[] args5 = {"file1.txt", "file2.txt", "all"};
        JSJshop.main(args5);
        System.setOut(System.out);
        assertNotNull(jshop);
    }

    @Test
    public void testJSJshop() {
        jshop = new JSJshop();
        assertNotNull(jshop);
    }

    @Test
    public void testJSJshopStringString() {
        jshop = new JSJshop("domain.txt", "problem.txt");
        assertNotNull(jshop);
    }

    @Test
    public void testJSJshopStringJSTaskAtom() {
        JSTaskAtom pred = new JSTaskAtom();
        jshop = new JSJshop("file.txt", pred);
        assertNotNull(jshop);
    }

    @Test
    public void testGetTree() {
        tree = jshop.getTree();
        assertNull(tree);
    }

    @Test
    public void testGetSolution() {
        solution = jshop.getSolution();
        assertNull(solution);
    }

    @Test
    public void testGetAddList() {
        JSListLogicalAtoms addList = jshop.getAddList();
        assertNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSListLogicalAtoms deleteList = jshop.getDeleteList();
        assertNull(deleteList);
    }

    @Test
    public void testSetFile() {
        solution = jshop.setFile("file.txt", new JSTaskAtom());
        assertNull(solution);
    }

    @Test
    public void testTestParser() {
        StringWriter output = new StringWriter();
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        jshop.testParser();
        System.setOut(System.out);
        assertNotNull(jshop);
    }

    @Test
    public void testParserFile() {
        boolean success = jshop.parserFile("file.txt");
        assertFalse(success);
    }

    @Test
    public void testGetBufferedReader() {
        BufferedReader br1 = jshop.getBufferedReader("dir", "file");
        assertNull(br1);
        BufferedReader br2 = jshop.getBufferedReader("dir", "file", new JApplet());
        assertNull(br2);
    }

    @Test
    public void testGetAppletURL() {
        String file = "file.txt";
        JApplet applet = new JApplet();
        URL url = jshop.getAppletURL(file, applet);
        assertNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.wordChars('.', '.');
        tokenizer.wordChars('#', '#');
        tokenizer.wordChars('$', '$');
        tokenizer.wordChars('%', '%');
        tokenizer.wordChars('&', '&');
        tokenizer.wordChars('=', '=');
        tokenizer.wordChars('?', '?');
        tokenizer.wordChars('@', '@');
        tokenizer.wordChars('_', '_');
        tokenizer.wordChars(';', ';');
        tokenizer.wordChars(':', ':');
        tokenizer.wordChars('<', '>');
        tokenizer.wordChars('[', ']');
        tokenizer.wordChars('(', ')');
        tokenizer.wordChars('*', '*');
        tokenizer.wordChars('!', '!');
        tokenizer.wordChars('~', '~');
        tokenizer.wordChars('`', '`');
        tokenizer.wordChars('{', '}');
        tokenizer.wordChars('"', '"');
        tokenizer.wordChars('\'', '\"');
        tokenizer.wordChars(',', ',');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('+');
        tokenizer.ordinaryChar('>');
        tokenizer.eolIsSignificant(true);

        try {
            tokenizer.resetSyntax();
            tokenizer.whitespaceChars(0, ' ');
            tokenizer.nextToken();

            StringWriter output = new StringWriter();
            System.setOut(new PrintStream(new ByteArrayOutputStream()));

            JSJshopVars.flagExit = true;
            tokenizer.pushBack();
            tokenizer.quoteChar('(');
            jshop.processToken(tokenizer);
            tokenizer.quoteChar('"');
            jshop.processToken(tokenizer);
            tokenizer.wordChars(32, 122);
            jshop.processToken(tokenizer);

            System.setOut(System.out);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDom() {
        JSPlanningDomain dom = jshop.dom();
        assertNull(dom);
    }

    @Test
    public void testProb() {
        JSPlanningProblem prob = jshop.prob();
        assertNull(prob);
    }

    @Test
    public void testSol() {
        JSPlan sol = jshop.sol();
        assertNull(sol);
    }

    @Test
    public void testTree() {
        JSJshopNode tree = jshop.tree();
        assertNull(tree);
    }

}