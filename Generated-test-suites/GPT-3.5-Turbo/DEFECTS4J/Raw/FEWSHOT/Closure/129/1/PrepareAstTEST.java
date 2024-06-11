package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

    @Test
    public void testNormalizeBlocks() {
        Node node = new Node(Token.IF);
        node.addChildToBack(new Node(Token.NAME, "test"));
        
        PrepareAst prepareAst = new PrepareAst(null);
        prepareAst.normalizeBlocks(node);
        
        assertTrue(node.getFirstChild().isBlock());
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node node = new Node(Token.NAME, "test");
        
        PrepareAst prepareAst = new PrepareAst(null);
        prepareAst.normalizeNodeTypes(node);
        
        assertEquals(Token.EXPR_RESULT, node.getType());
    }

    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        Node node = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.OBJECTLITKEY, new Node(Token.NAME, "key"));
        Node value = new Node(Token.STRING, new Node(Token.STRING, "value"));
        key.addChildToBack(value);
        node.addChildToBack(key);
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.normalizeObjectLiteralAnnotations(node);
        
        assertNull(key.getJSDocInfo());
        assertEquals(key.getJSDocInfo(), value.getJSDocInfo());
    }

    @Test
    public void testAnnotateCalls() {
        Node node = new Node(Token.CALL);
        node.addChildToBack(new Node(Token.NAME, "func"));
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.annotateCalls(node);
        
        assertTrue(node.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testAnnotateDispatchers() {
        Node functionNode = new Node(Token.FUNCTION);
        Node assignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "var"), functionNode);
        
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.annotateDispatchers(functionNode, assignNode);
        
        assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
    }
}