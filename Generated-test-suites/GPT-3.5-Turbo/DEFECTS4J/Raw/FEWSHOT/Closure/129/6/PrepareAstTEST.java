package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
    public void testNormalizeBlocks() {
        Node n = new Node(Token.BLOCK, IR.empty());
        prepareAst.normalizeBlocks(n);
        assertTrue(n.isEmpty());
        
        n = new Node(Token.IF, new Node(Token.BLOCK, IR.empty()));
        prepareAst.normalizeBlocks(n);
        assertTrue(n.getFirstChild().isBlock());
        
        n = new Node(Token.LABEL, new Node(Token.BLOCK, IR.empty()));
        prepareAst.normalizeBlocks(n);
        assertFalse(n.getFirstChild().isBlock());

        n = new Node(Token.SWITCH, new Node(Token.BLOCK, IR.empty()));
        prepareAst.normalizeBlocks(n);
        assertFalse(n.getFirstChild().isBlock());
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node n = new Node(Token.BLOCK, IR.empty());
        prepareAst.normalizeNodeTypes(n);
        assertEquals(Node.Type.BLOCK, n.getType());
        
        n = new Node(Token.EXPR_VOID, IR.empty());
        prepareAst.normalizeNodeTypes(n);
        assertEquals(Node.Type.EXPR_RESULT, n.getType());
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = null;
        Node n = new Node(Token.OBJECTLIT, IR.empty());
        Node parent = n.getParent();
        PrepareAst.PrepareAnnotations prepareAnnotations = prepareAst.new PrepareAnnotations();
        assertTrue(prepareAnnotations.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit() {
        NodeTraversal t = null;
        Node n = new Node(Token.CALL, IR.empty());
        Node parent = n.getParent();
        PrepareAst.PrepareAnnotations prepareAnnotations = prepareAst.new PrepareAnnotations();
        prepareAnnotations.visit(t, n, parent);
    }

    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT, IR.empty());
        Node key = new Node(Token.STRING, new Node(Token.STRING));
        Node value = key.getLastChild();
        prepareAst.new PrepareAnnotations().normalizeObjectLiteralAnnotations(objlit);
        assertEquals(Node.Type.OBJECTLIT, objlit.getType());
    }

    @Test
    public void testAnnotateCalls() {
        Node n = new Node(Token.CALL, IR.empty());
        prepareAst.new PrepareAnnotations().annotateCalls(n);
        assertNull(n.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testAnnotateDispatchers() {
        Node n = new Node(Token.FUNCTION, IR.empty());
        Node parent = n.getParent();
        parent.setJSDocInfo(new JSDocInfo());
        prepareAst.new PrepareAnnotations().annotateDispatchers(n, parent);
        assertFalse(n.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testNormalizeObjectLiteralKeyAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT, IR.empty());
        Node key = new Node(Token.STRING, new Node(Token.STRING));
        Node value = key.getLastChild();
        objlit.setJSDocInfo(new JSDocInfo());
        prepareAst.new PrepareAnnotations().normalizeObjectLiteralKeyAnnotations(objlit, key, value);
        assertNotNull(value.getJSDocInfo());
    }

}