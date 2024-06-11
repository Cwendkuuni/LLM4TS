import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = new String[]{"domainFile", "problemFile"};
        JSJshop.main(args);
    }

    @Test
    public void testConstructor() {
        JSJshop shop = new JSJshop();
        assertNotNull(shop);
    }

    @Test
    public void testConstructorWithNameFiles() {
        JSJshop shop = new JSJshop("nameDomainFile", "nameProblemFile");
        assertNotNull(shop);
    }

    @Test
    public void testConstructorWithNameFileAndTaskAtom() {
        JSJshop shop = new JSJshop("nameFile", new JSTaskAtom());
        assertNotNull(shop);
    }

    @Test
    public void testGetTree() {
        JSJshop shop = new JSJshop();
        JSJshopNode node = shop.getTree();
        assertNull(node);
    }

    @Test
    public void testGetSolution() {
        JSJshop shop = new JSJshop();
        JSPairPlanTSListNodes solution = shop.getSolution();
        assertNull(solution);
    }

    @Test
    public void testGetAddList() {
        JSJshop shop = new JSJshop();
        JSListLogicalAtoms addList = shop.getAddList();
        assertNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSJshop shop = new JSJshop();
        JSListLogicalAtoms deleteList = shop.getDeleteList();
        assertNull(deleteList);
    }

    @Test
    public void testSetFile() {
        JSJshop shop = new JSJshop();
        JSPairPlanTSListNodes solution = shop.setFile("nameFile", new JSTaskAtom());
        assertNull(solution);
    }

    @Test
    public void testTestParser() {
        JSJshop shop = new JSJshop();
        shop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop shop = new JSJshop();
        boolean result = shop.parserFile("libraryFile");
        assertFalse(result);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop shop = new JSJshop();
        BufferedReader reader = shop.getBufferedReader("dir", "file");
        assertNull(reader);
    }

    @Test
    public void testGetBufferedReaderWithApplet() {
        JSJshop shop = new JSJshop();
        BufferedReader reader = shop.getBufferedReader("dir", "file", new JApplet());
        assertNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop shop = new JSJshop();
        URL url = shop.getAppletURL("file", new JApplet());
        assertNull(url);
    }

    @Test
    public void testProcessToken() {
        JSJshop shop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();
        try {
            shop.processToken(tokenizer);
        } catch (JSParserError e) {
            assertEquals("umd.cs.shop.JSParserError", e.getClass().getName());
        }
    }

    @Test
    public void testDom() {
        JSJshop shop = new JSJshop();
        JSPlanningDomain dom = shop.dom();
        assertNull(dom);
    }

    @Test
    public void testProb() {
        JSJshop shop = new JSJshop();
        JSPlanningProblem prob = shop.prob();
        assertNull(prob);
    }

    @Test
    public void testSol() {
        JSJshop shop = new JSJshop();
        JSPlan sol = shop.sol();
        assertNull(sol);
    }

    @Test
    public void testTree() {
        JSJshop shop = new JSJshop();
        JSJshopNode node = shop.tree();
        assertNull(node);
    }
}