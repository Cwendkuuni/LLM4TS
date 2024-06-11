// Test cases for SyntacticScopeCreator class
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

public class SyntacticScopeCreatorTest {

    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
    }

    @Test
    public void testCreateScope_functionDeclaration() {
        Node node = createNode(Token.FUNCTION);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Scope scope = scopeCreator.createScope(node, null);

        assertEquals(node, scope.getRootNode());
        assertNull(scope.getParent());
    }

    @Test
    public void testCreateScope_functionExpression() {
        Node node = createNode(Token.FUNCTION);
        Scope parentScope = new Scope(createNode(Token.SCRIPT), compiler);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Scope scope = scopeCreator.createScope(node, parentScope);

        assertEquals(node, scope.getRootNode());
        assertEquals(parentScope, scope.getParent());
    }

    @Test
    public void testCreateScope_globalBlock() {
        Node node = createNode(Token.SCRIPT);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Scope scope = scopeCreator.createScope(node, null);

        assertEquals(node, scope.getRootNode());
        assertNull(scope.getParent());
    }

    @Test
    public void testScanRoot_functionNodeWithEmptyName() {
        Node node = createNode(Token.FUNCTION);
        Node fnNameNode = createNode(Token.EMPTY, "");
        node.addChildToFront(fnNameNode);
        Node argsNode = createNode(Token.LP);
        node.addChildToBack(argsNode);
        Node bodyNode = createNode(Token.BLOCK);
        node.addChildToBack(bodyNode);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        scopeCreator.scanRoot(node, null);

        assertEquals(node, scopeCreator.scope.getRootNode());
        assertEquals(0, scopeCreator.scope.getDepth());
        assertEquals(0, scopeCreator.scope.getVars().size());
    }

    @Test
    public void testScanRoot_functionNodeWithNonEmptyName() {
        Node node = createNode(Token.FUNCTION);
        Node fnNameNode = createNode(Token.NAME, "testFunction");
        node.addChildToFront(fnNameNode);
        Node argsNode = createNode(Token.LP);
        node.addChildToBack(argsNode);
        Node bodyNode = createNode(Token.BLOCK);
        node.addChildToBack(bodyNode);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        scopeCreator.scanRoot(node, null);

        assertEquals(node, scopeCreator.scope.getRootNode());
        assertEquals(0, scopeCreator.scope.getDepth());
        assertEquals(1, scopeCreator.scope.getVars().size());
        assertEquals("testFunction", scopeCreator.scope.getVar("testFunction").getName());
    }

    @Test
    public void testScanRoot_argumentNode() {
        Node node = createNode(Token.NAME, "arguments");
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        scopeCreator.scanRoot(node, null);

        assertEquals(node, scopeCreator.scope.getRootNode());
        assertEquals(0, scopeCreator.scope.getDepth());
        assertEquals(1, scopeCreator.scope.getVars().size());
        assertEquals("arguments", scopeCreator.scope.getVar("arguments").getName());
    }

    @Test
    public void testScanRoot_varNode() {
        Node node = createNode(Token.VAR);
        Node child1 = createNode(Token.NAME, "x");
        node.addChildToBack(child1);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        scopeCreator.scanRoot(node, null);

        assertEquals(node, scopeCreator.scope.getRootNode());
        assertEquals(0, scopeCreator.scope.getDepth());
        assertEquals(1, scopeCreator.scope.getVars().size());
        assertEquals("x", scopeCreator.scope.getVar("x").getName());
    }

    @Test
    public void testScanRoot_scriptNode() {
        Node node = createNode(Token.SCRIPT);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        scopeCreator.scanRoot(node, null);

        assertEquals(node, scopeCreator.scope.getRootNode());
        assertEquals(0, scopeCreator.scope.getDepth());
        assertEquals(0, scopeCreator.scope.getVars().size());
    }

    private Node createNode(Token token) {
        return new Node(token);
    }

    private Node createNode(Token token, String value) {
        Node node = new Node(token);
        node.setString(value);
        return node;
    }
}