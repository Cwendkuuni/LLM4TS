package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrepareAstTest {
    @Test
    public void testProcess() {
        // Create a compiler and prepare AST object
        Compiler compiler = new Compiler();
        PrepareAst prepareAst = new PrepareAst(compiler);

        // Create a sample externs and roots nodes
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);

        // Call the process method
        prepareAst.process(externs, root);
    }

    @Test
    public void testNormalizeNodeTypes() {
        // Create a PrepareAst object
        PrepareAst prepareAst = new PrepareAst(new Compiler(), true);

        // Create a sample node
        Node node = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));

        // Call the normalizeNodeTypes method
        prepareAst.normalizeNodeTypes(node);

        // Check if the node is normalized
        assertNotNull(node);
        assertEquals(Token.EXPR_RESULT, node.getType());
    }

    @Test
    public void testNormalizeBlocks() {
        // Create a PrepareAst object
        PrepareAst prepareAst = new PrepareAst(new Compiler(), true);

        // Create a sample node
        Node node = new Node(Token.IF, new Node(Token.NAME), new Node(Token.BLOCK));

        // Call the normalizeBlocks method
        prepareAst.normalizeBlocks(node);

        // Check if the blocks are normalized correctly
        assertNotNull(node.getFirstChild());
        assertNotNull(node.getLastChild());
        assertEquals(Token.BLOCK, node.getLastChild().getType());
    }

    @Test
    public void testPrepareAnnotations() {
        // Create a PrepareAst object
        PrepareAst prepareAst = new PrepareAst(new Compiler(), true);

        // Create a sample node
        Node node = new Node(Token.OBJECTLIT, new Node(Token.STRING, "a"), new Node(Token.STRING, "b"));

        // Create a sample parent node
        Node parent = new Node(Token.BLOCK);

        // Create a sample traversal object
        NodeTraversal traversal = new NodeTraversal(new Compiler(), null);
        
        // Create a sample callback object
        PrepareAst.PrepareAnnotations callback = new PrepareAst.PrepareAnnotations();

        // Call the normalizeObjectLiteralAnnotations method
        callback.normalizeObjectLiteralAnnotations(node);

        // Call the shouldTraverse method
        boolean shouldTraverse = callback.shouldTraverse(traversal, node, parent);

        // Call the visit method
        callback.visit(traversal, node, parent);

        // Check the results
        assertTrue(shouldTraverse);
        assertNull(node.getJSDocInfo());
        assertEquals(Token.CALL, node.getType());
    }
}