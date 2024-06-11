package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CollapseVariableDeclarationsTest {

    private Compiler compiler;
    private CollapseVariableDeclarations pass;

    @Before
    public void setUp() {
        compiler = new Compiler();
        pass = new CollapseVariableDeclarations(compiler);
    }

    @Test
    public void testProcess_emptyAST() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.EMPTY);
        pass.process(externs, root);
        assertFalse(compiler.hasCodeChanged());
    }

    @Test
    public void testProcess_noCollapses() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "a")),
                new Node(Token.NAME, new Node(Token.STRING, "b")));
        root.addChildToBack(varNode);
        pass.process(externs, root);
        assertFalse(compiler.hasCodeChanged());
    }

    @Test
    public void testProcess_collapseVariableDeclarations() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Node varNode1 = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "a")));
        Node varNode2 = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "b")));
        Node varNode3 = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "c")));
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        root.addChildToBack(varNode3);
        pass.process(externs, root);
        assertTrue(compiler.hasCodeChanged());
        assertEquals(Token.VAR, root.getFirstChild().getType());
        assertEquals(3, root.getFirstChild().getChildCount());
    }

    @Test
    public void testProcess_collapseVariableDeclarationsWithAssignments() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Node varNode1 = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "a")));
        Node assignNode1 = new Node(Token.ASSIGN,
                new Node(Token.NAME, new Node(Token.STRING, "b")),
                new Node(Token.TRUE));
        Node assignNode2 = new Node(Token.ASSIGN,
                new Node(Token.NAME, new Node(Token.STRING, "c")),
                new Node(Token.TRUE));
        root.addChildToBack(varNode1);
        root.addChildToBack(assignNode1);
        root.addChildToBack(assignNode2);
        pass.process(externs, root);
        assertTrue(compiler.hasCodeChanged());
        assertEquals(Token.VAR, root.getFirstChild().getType());
        assertEquals(3, root.getFirstChild().getChildCount());
        assertEquals(Token.NAME, root.getFirstChild().getChildAtIndex(1).getChildAtIndex(0).getType());
        assertEquals(Token.NAME, root.getFirstChild().getChildAtIndex(2).getChildAtIndex(0).getType());
    }

    @Test
    public void testProcess_noDuplicateDeclaration() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);
        Node varNode1 = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "a")));
        Node varNode2 = new Node(Token.VAR,
                new Node(Token.NAME, new Node(Token.STRING, "b")));
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        Node functionNode = createFunctionNode("testFunction");
        root.addChildToBack(functionNode);
        pass.process(externs, root);
        assertFalse(compiler.hasCodeChanged());
    }

    private Node createFunctionNode(String functionName) {
        Node nameNode = new Node(Token.NAME, new Node(Token.STRING, functionName));
        return new Node(Token.FUNCTION, nameNode, new Node(Token.BLOCK));
    }
}