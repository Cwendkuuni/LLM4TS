/*
 * JUnit test cases for the RenameLabels class.
 */
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
    
    private static final String DEFAULT_NAME = "a";
    
    private AbstractCompiler compiler;
    private RenameLabels renameLabels;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        renameLabels = new RenameLabels(compiler);
    }
    
    @Test
    public void testRenameLabel() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label1");
        labelNode.addChildToBack(nameNode);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertEquals(DEFAULT_NAME, nameNode.getString());
        assertTrue(labelNode.getParent() == null);
    }
    
    @Test
    public void testRenameLabel_Referenced() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label1");
        labelNode.addChildToBack(nameNode);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertEquals(DEFAULT_NAME, nameNode.getString());
        assertTrue(labelNode.getParent() != null);
    }
    
    @Test
    public void testRenameLabel_Unreferenced() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label1");
        labelNode.addChildToBack(nameNode);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertEquals(DEFAULT_NAME, nameNode.getString());
        assertTrue(labelNode.getParent() == null);
    }
    
    @Test
    public void testRenameLabel_NullNameNode() {
        Node labelNode = new Node(Token.LABEL);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertTrue(labelNode.getFirstChild() == null);
    }
    
    @Test
    public void testRenameLabel_NameNodeHasShortName() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, DEFAULT_NAME);
        labelNode.addChildToBack(nameNode);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertEquals(DEFAULT_NAME, nameNode.getString());
    }
    
    @Test
    public void testRenameLabel_NameNodeHasLongName() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label1");
        labelNode.addChildToBack(nameNode);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertEquals(DEFAULT_NAME, nameNode.getString());
    }
    
    @Test
    public void testRenameLabel_DuplicateName() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label1");
        labelNode.addChildToBack(nameNode);
        
        renameLabels.renameLabel(labelNode, 1);
        
        assertEquals(DEFAULT_NAME, nameNode.getString());
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);
        
        renameLabels.process(externs, root);
        
        // Add assert statements here
        
    }
    
}