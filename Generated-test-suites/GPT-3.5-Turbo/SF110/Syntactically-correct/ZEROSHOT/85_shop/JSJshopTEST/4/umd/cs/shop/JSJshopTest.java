package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;

public final class JSJshopTest {
    @Test
    public void testMain() {
        String[] args = {"domainDef-file-name", "problemDef-file-name"};
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        JSJshop.main(args);
        
        System.setOut(originalOut);
        String[] output = outputStream.toString().split(System.lineSeparator());
        
        Assert.assertEquals("Usage :", output[0].trim());
        Assert.assertEquals("java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]", output[1].trim());
        Assert.assertEquals("[number-of-plans] can be 'one' or 'all'. The default value is 'one'", output[2].trim());
    }
    
    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        
        Assert.assertNotNull(jsjshop.probSet);
        Assert.assertNull(jsjshop.tree);
    }
    
    @Test
    public void testJSJshopWithParameters() {
        JSJshop jsjshop = new JSJshop("nameDomainFile", "nameProblemFile");
        
        Assert.assertNotNull(jsjshop.probSet);
        Assert.assertNull(jsjshop.tree);
    }
    
    @Test
    public void testGetTree() {
        JSJshop jsjshop = new JSJshop();
        
        JSJshopNode tree = jsjshop.getTree();
        
        Assert.assertNull(tree);
    }
    
    @Test
    public void testGetSolution() {
        JSJshop jsjshop = new JSJshop();
        
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        
        Assert.assertNull(solution);
    }
    
    @Test
    public void testGetAddList() {
        JSJshop jsjshop = new JSJshop();
        
        JSListLogicalAtoms addList = jsjshop.getAddList();
        
        Assert.assertNull(addList);
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop jsjshop = new JSJshop();
        
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        
        Assert.assertNull(deleteList);
    }
    
    @Test
    public void testSetFile() {
        JSJshop jsjshop = new JSJshop();
        
        JSPairPlanTSListNodes pairPlanTSListNodes = jsjshop.setFile("nameFile", new JSTaskAtom());
        
        Assert.assertNull(pairPlanTSListNodes);
    }
    
    @Test
    public void testTestParser() {
        JSJshop jsjshop = new JSJshop();
        
        jsjshop.testParser();
        
        // No assertion possible as the method only uses print statements
    }
    
    @Test
    public void testParserFile() {
        JSJshop jsjshop = new JSJshop();
        
        boolean result = jsjshop.parserFile("libraryFile");
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        
        BufferedReader br = jsjshop.getBufferedReader("dir", "file");
        
        Assert.assertNull(br);
    }
    
    @Test
    public void testGetBufferedReaderWithApplet() {
        JSJshop jsjshop = new JSJshop();
        JApplet applet = new JApplet();
        
        BufferedReader br = jsjshop.getBufferedReader("dir", "file", applet);
        
        Assert.assertNull(br);
    }
    
    @Test
    public void testGetAppletURL() {
        JSJshop jsjshop = new JSJshop();
        JApplet applet = new JApplet();
        
        URL url = jsjshop.getAppletURL("file", applet);
        
        Assert.assertNull(url);
    }
    
    @Test
    public void testProcessTokenWithDefdomain() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( defdomain"));
        
        jsjshop.processToken(tokenizer);
        
        // No assertion possible as the method only initializes variables
    }
    
    @Test
    public void testProcessTokenWithDefproblem() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( defproblem"));
        
        jsjshop.processToken(tokenizer);
        
        // No assertion possible as the method only initializes variables
    }
    
    @Test(expected = JSParserError.class)
    public void testProcessTokenWithInvalidToken() {
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( invalid"));
        
        jsjshop.processToken(tokenizer);
    }
    
    @Test
    public void testDom() {
        JSJshop jsjshop = new JSJshop();
        
        JSPlanningDomain dom = jsjshop.dom();
        
        Assert.assertNull(dom);
    }
    
    @Test
    public void testProb() {
        JSJshop jsjshop = new JSJshop();
        
        JSPlanningProblem prob = jsjshop.prob();
        
        Assert.assertNull(prob);
    }
    
    @Test
    public void testSol() {
        JSJshop jsjshop = new JSJshop();
        
        JSPlan sol = jsjshop.sol();
        
        Assert.assertNull(sol);
    }
    
    @Test
    public void testTree() {
        JSJshop jsjshop = new JSJshop();
        
        JSJshopNode tree = jsjshop.tree();
        
        Assert.assertNull(tree);
    }
}