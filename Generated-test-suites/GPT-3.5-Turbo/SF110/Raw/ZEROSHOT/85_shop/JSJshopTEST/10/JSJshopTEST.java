package umd.cs.shop;

import junit.framework.TestCase;

public class JSJshopTest extends TestCase {

    public void testMain() {
        String[] args = new String[2];
        args[0] = "example_domain.txt";
        args[1] = "example_problem.txt";

        JSJshop.main(args);
    }

    public void testJSJshop() {
        JSJshop jshop = new JSJshop();
    }

    public void testJSJshopStringString() {
        JSJshop jshop = new JSJshop("example_domain.txt", "example_problem.txt");
    }

    public void testJSJshopStringJSTaskAtom() {
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jshop = new JSJshop("example_problem.txt", pred);
    }

    public void testGetTree() {
        JSJshop jshop = new JSJshop();
        JSJshopNode tree = jshop.getTree();
        assertNotNull(tree);
    }

    public void testGetSolution() {
        JSJshop jshop = new JSJshop();
        JSPairPlanTSListNodes solution = jshop.getSolution();
        assertNotNull(solution);
    }

    public void testGetAddList() {
        JSJshop jshop = new JSJshop();
        JSListLogicalAtoms addList = jshop.getAddList();
        assertNull(addList);
    }

    public void testGetDeleteList() {
        JSJshop jshop = new JSJshop();
        JSListLogicalAtoms deleteList = jshop.getDeleteList();
        assertNull(deleteList);
    }

    public void testSetFile() {
        JSJshop jshop = new JSJshop();
        JSPairPlanTSListNodes solution = jshop.setFile("example_file.txt", new JSTaskAtom());
        assertNotNull(solution);
    }

    public void testTestParser() {
        JSJshop jshop = new JSJshop();
        jshop.testParser();
    }

    public void testParserFile() {
        JSJshop jshop = new JSJshop();
        boolean parsed = jshop.parserFile("example_file.txt");
        assertFalse(parsed);
    }

    public void testGetBufferedReader() {
        JSJshop jshop = new JSJshop();
        BufferedReader br = jshop.getBufferedReader(".", "example_file.txt");
        assertNotNull(br);
    }

    public void testGetBufferedReaderWithApplet() {
        JSJshop jshop = new JSJshop();
        BufferedReader br = jshop.getBufferedReader(".", "example_file.txt", new JApplet());
        assertNotNull(br);
    }

    public void testGetAppletURL() {
        JSJshop jshop = new JSJshop();
        URL url = jshop.getAppletURL("example_file.txt", new JApplet());
        assertNotNull(url);
    }

    public void testProcessToken() {
        JSJshop jshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        tokenizer.commentChar('#');
        tokenizer.lowerCaseMode(true);
        tokenizer.slashSlashComments(false);
        tokenizer.slashStarComments(false);
        tokenizer.wordChars('!', '~');
        tokenizer.quoteChar('"');
        tokenizer.ordinaryChar('(');
        tokenizer.ordinaryChar(')');
        tokenizer.ordinaryChar(':');
        tokenizer.ordinaryChar(';');
        tokenizer.ordinaryChar('\'');
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('<');
        tokenizer.ordinaryChar('=');
        tokenizer.ordinaryChar('>');
        tokenizer.ordinaryChar('+');
        tokenizer.ordinaryChar('`');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar(',');
        tokenizer.ordinaryChar('*');
        tokenizer.ordinaryChar(']');
        tokenizer.ordinaryChar('[');

        jshop.processToken(tokenizer);
    }

    public void testDom() {
        JSJshop jshop = new JSJshop();
        JSPlanningDomain dom = jshop.dom();
        assertNull(dom);
    }

    public void testProb() {
        JSJshop jshop = new JSJshop();
        JSPlanningProblem prob = jshop.prob();
        assertNull(prob);
    }

    public void testSol() {
        JSJshop jshop = new JSJshop();
        JSPlan sol = jshop.sol();
        assertNull(sol);
    }

    public void testTree() {
        JSJshop jshop = new JSJshop();
        JSJshopNode tree = jshop.tree();
        assertNull(tree);
    }
}