package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

    @Test
    public void testNormalizeNodeTypes() {
        PrepareAst ast = new PrepareAst(new AbstractCompiler());

        Node n = new Node(Token.SCRIPT);
        n.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING))));
        n.addChildToBack(new Node(Token.RETURN, new Node(Token.NAME, new Node(Token.STRING))));

        ast.normalizeNodeTypes(n);

        assertEquals(Token.RETURN, n.getLastChild().getType());
        assertEquals(Token.NAME, n.getLastChild().getFirstChild().getType());
    }

    @Test
    public void testNormalizeBlocks() {
        PrepareAst ast = new PrepareAst(new AbstractCompiler());

        Node n = new Node(Token.IF);

        Node child = new Node(Token.NAME, new Node(Token.STRING));
        n.addChildToBack(child);

        ast.normalizeBlocks(n);

        assertEquals(Token.BLOCK, n.getLastChild().getType());
        assertEquals(Token.NAME, n.getLastChild().getFirstChild().getType());
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), annotations);
        Node n = new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING)));

        annotations.annotateCalls(n);

        assertTrue(n.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testVisit() {
        PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), annotations);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();

        annotations.annotateDispatchers(n, parent);

        assertTrue(n.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();

        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.STRING);

        objlit.addChildToBack(key);
        key.addChildToBack(value);

        annotations.normalizeObjectLiteralAnnotations(objlit);

        assertNull(key.getJSDocInfo());
        assertEquals(key.getJSDocInfo(), value.getJSDocInfo());
    }
}