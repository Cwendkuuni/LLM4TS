package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;
    private SyntacticScopeCreator.RedeclarationHandler redeclarationHandler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        redeclarationHandler = scopeCreator.new DefaultRedeclarationHandler();
        scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
    }

    @Test
    public void testCreateScope_GlobalBlock() {
        Node n = new Node(Token.SCRIPT);
        Scope parent = null;

        Scope scope = scopeCreator.createScope(n, parent);

        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
    }

    @Test
    public void testScanVars_VariableDeclaration() {
        Node n = new Node(Token.VAR);
        Node varNode1 = new Node(Token.NAME, "x");
        Node varNode2 = new Node(Token.NAME, "y");
        Node varNode3 = new Node(Token.NAME, "z");
        n.addChildToBack(varNode1);
        n.addChildToBack(varNode2);
        n.addChildToBack(varNode3);
        Node parent = new Node(Token.SCRIPT);

        scopeCreator.scanVars(n, parent);

        assertEquals("x", scopeCreator.scope.getVar("x").getName());
        assertEquals("y", scopeCreator.scope.getVar("y").getName());
        assertEquals("z", scopeCreator.scope.getVar("z").getName());
    }

    @Test
    public void testScanVars_FunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "foo");
        Node args = new Node(Token.LP);
        Node body = new Node(Token.BLOCK);
        n.addChildToBack(nameNode);
        n.addChildToBack(args);
        n.addChildToBack(body);
        Node parent = new Node(Token.SCRIPT);

        scopeCreator.scanVars(n, parent);

        assertEquals("foo", scopeCreator.scope.getVar("foo").getName());
    }

    @Test
    public void testScanVars_CatchBlock() {
        Node n = new Node(Token.CATCH);
        Node catchVar = new Node(Token.NAME, "error");
        Node block = new Node(Token.BLOCK);
        n.addChildToBack(catchVar);
        n.addChildToBack(block);
        Node parent = new Node(Token.SCRIPT);

        scopeCreator.scanVars(n, parent);

        assertEquals("error", scopeCreator.scope.getVar("error").getName());
    }

    @Test
    public void testScanVars_ControlStructure() {
        Node n = new Node(Token.FOR);
        Node init = new Node(Token.VAR);
        Node initVar = new Node(Token.NAME, "i");
        Node initValue = new Node(Token.NUMBER, "0");
        initVar.addChildToBack(initValue);
        init.addChildToBack(initVar);
        n.addChildToFront(init);
        Node parent = new Node(Token.SCRIPT);

        scopeCreator.scanVars(n, parent);

        assertEquals("i", scopeCreator.scope.getVar("i").getName());
    }

    // Add more test cases here to cover all methods in the SyntacticScopeCreator class

}