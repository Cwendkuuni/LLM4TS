package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
    private PrepareAst prepareAst;

    @Before
    public void setUp() {
        prepareAst = new PrepareAst(new AbstractCompiler());
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        
        NodeTraversal.Callback callback = new PrepareAst.PrepareAnnotations();

        // Test when both externs and root are null
        prepareAst.process(externs, root);

        // Test when only externs are null
        externs = null;
        root = new Node(Token.SCRIPT);
        prepareAst.process(externs, root);

        // Test when only root is null
        externs = new Node(Token.SCRIPT);
        root = null;
        prepareAst.process(externs, root);

        // Test when neither externs nor root are null
        externs = new Node(Token.SCRIPT);
        root = new Node(Token.SCRIPT);
        prepareAst.process(externs, root);
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node node = new Node(Token.SCRIPT);
        prepareAst.normalizeNodeTypes(node);

        // Test when child parent pointer check is violated
        Node child = new Node(Token.BLOCK);
        child.setParent(new Node(Token.SCRIPT));
        node.addChildToFront(child);
        assertTrue(child.getParent() == node);
    }

    @Test
    public void testNormalizeBlocks() {
        Node node = new Node(Token.WHILE);
        prepareAst.normalizeBlocks(node);

        // Test when isControlStructureCodeBlock() and isBlock() are true
        Node child = new Node(Token.BLOCK);
        node.addChildToFront(child);
        assertEquals(child, node.getFirstChild());

        // Test when isControlStructureCodeBlock() is true but isBlock() is false
        child = new Node(Token.EMPTY);
        node.addChildToFront(child);
        assertEquals(Token.BLOCK, node.getFirstChild().getType());
        assertTrue(child.isEmpty());

        // Test when isControlStructureCodeBlock() is false
        child = new Node(Token.STRING);
        node.addChildToFront(child);
        assertEquals(child, node.getFirstChild());
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), prepareAnnotations);
        NodeTraversal.Callback callback = new PrepareAst.PrepareAnnotations();

        // Test shouldTraverse() method
        Node objlit = new Node(Token.OBJECTLIT);
        prepareAnnotations.shouldTraverse(t, objlit, null);

        // Test visit() method
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.FUNCTION);
        prepareAnnotations.visit(t, n, parent);
    }
}