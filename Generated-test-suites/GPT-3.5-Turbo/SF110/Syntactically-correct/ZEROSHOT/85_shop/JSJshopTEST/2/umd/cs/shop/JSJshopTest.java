package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class JSJshopTest {
    @Test
    public void testMain() {
        // Test case 1
        String[] args = {"domain.def", "problem.def"};
        JSJshop.main(args);

        // Test case 2
        String[] args2 = {"domain2.def", "problem2.def", "all"};
        JSJshop.main(args2);
    }

    @Test
    public void testConstructor() {
        JSJshop jsjShop = new JSJshop();

        Assert.assertNull(jsjShop.getTree());
        Assert.assertNull(jsjShop.getSolution());
        Assert.assertNull(jsjShop.getAddList());
        Assert.assertNull(jsjShop.getDeleteList());
    }

    @Test
    public void testConstructorWithParams() {
        JSJshop jsjShop = new JSJshop("nameDomainFile", "nameProblemFile");

        Assert.assertNotNull(jsjShop.getTree());
        Assert.assertNotNull(jsjShop.getSolution());
        Assert.assertNotNull(jsjShop.getAddList());
        Assert.assertNotNull(jsjShop.getDeleteList());
    }

    @Test
    public void testSetFile() {
        JSJshop jsjShop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom();

        jsjShop.setFile("nameFile", pred);

        Assert.assertNotNull(jsjShop.getTree());
        Assert.assertNotNull(jsjShop.getSolution());
        Assert.assertNotNull(jsjShop.getAddList());
        Assert.assertNotNull(jsjShop.getDeleteList());
    }

    @Test
    public void testTestParser() {
        JSJshop jsjShop = new JSJshop();

        jsjShop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop jsjShop = new JSJshop();
        boolean result = jsjShop.parserFile("libraryFile");

        Assert.assertTrue(result);
    }

    @Test
    public void testGetBufferedReader1() {
        JSJshop jsjShop = new JSJshop();
        BufferedReader br = jsjShop.getBufferedReader("dir", "file");

        Assert.assertNotNull(br);
    }

    @Test
    public void testGetBufferedReader2() {
        JSJshop jsjShop = new JSJshop();
        JApplet applet = new JApplet();
        BufferedReader br = jsjShop.getBufferedReader("dir", "file", applet);

        Assert.assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsjShop = new JSJshop();
        JApplet applet = new JApplet();
        String file = "file";
        String expectedURL = "expectedURL";

        URL url = jsjShop.getAppletURL(file, applet);

        Assert.assertNotNull(url);
        Assert.assertEquals(expectedURL, url.toString());
    }

    @Test
    public void testProcessToken() {
        JSJshop jsjShop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer();

        jsjShop.processToken(tokenizer);
    }

    @Test
    public void testDom() {
        JSJshop jsjShop = new JSJshop();

        Assert.assertNull(jsjShop.dom());
    }

    @Test
    public void testProb() {
        JSJshop jsjShop = new JSJshop();

        Assert.assertNull(jsjShop.prob());
    }

    @Test
    public void testSol() {
        JSJshop jsjShop = new JSJshop();

        Assert.assertNull(jsjShop.sol());
    }

    @Test
    public void testTree() {
        JSJshop jsjShop = new JSJshop();

        Assert.assertNull(jsjShop.tree());
    }
}