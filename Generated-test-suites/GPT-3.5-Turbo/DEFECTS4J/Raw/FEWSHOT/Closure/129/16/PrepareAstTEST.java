package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

    private PrepareAst prepareAst;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        prepareAst = new PrepareAst(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN);

        // Test if externs is not null
        Node root = new Node(Token.ROOT);
        prepareAst.process(externs, root);
        assertNotNull(root);

        // Test if root is not null
        externs = null;
        root = new Node(Token.ROOT);
        prepareAst.process(externs, root);
        assertNotNull(root);
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node node = new Node(Token.SCRIPT);
        prepareAst.normalizeNodeTypes(node);
        assertNotNull(node);
    }

    @Test
    public void testNormalizeBlocks() {
        Node node = new Node(Token.BLOCK);
        prepareAst.normalizeBlocks(node);
        assertNotNull(node);
    }

    @Test
    public void testPrepareAnnotations() {
        NodeTraversal t = new NodeTraversal(compiler, new PrepareAst.PrepareAnnotations());
        Node node = new Node(Token.CALL);
        Node parent = new Node(Token.ROOT);
        t.visit(node, parent);
        assertNotNull(node);

        node = new Node(Token.FUNCTION);
        t.visit(node, parent);
        assertNotNull(node);
    }
}