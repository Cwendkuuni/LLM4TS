package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SyntacticScopeCreatorTest {
    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }

    @Test
    public void testCreateScope_withFunctionNode() {
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "testFunction");
        Node argsNode = new Node(Token.LP);
        Node bodyNode = new Node(Token.BLOCK);

        nameNode.setParent(functionNode);
        argsNode.setParent(functionNode);
        bodyNode.setParent(functionNode);

        functionNode.addChildToBack(nameNode);
        functionNode.addChildToBack(argsNode);
        functionNode.addChildToBack(bodyNode);

        Scope scope = scopeCreator.createScope(functionNode, null);

        assertEquals("testFunction", scope.getVar("testFunction").getName());
    }

    @Test
    public void testCreateScope_withCatchNode() {
        Node catchNode = new Node(Token.CATCH);
        Node varNode = new Node(Token.NAME, "exception");
        Node blockNode = new Node(Token.BLOCK);

        varNode.setParent(catchNode);
        blockNode.setParent(catchNode);

        catchNode.addChildToBack(varNode);
        catchNode.addChildToBack(blockNode);

        Scope scope = scopeCreator.createScope(catchNode, null);

        assertEquals("exception", scope.getVar("exception").getName());
    }

    @Test
    public void testCreateScope_withVarNode() {
        Node varNode = new Node(Token.VAR);
        Node firstAssignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "foo"), new Node(Token.STRING, "bar"));
        Node secondAssignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "baz"), new Node(Token.NUMBER, "42"));

        firstAssignNode.setParent(varNode);
        secondAssignNode.setParent(varNode);

        varNode.addChildToBack(firstAssignNode);
        varNode.addChildToBack(secondAssignNode);

        Scope scope = scopeCreator.createScope(varNode, null);

        assertEquals("foo", scope.getVar("foo").getName());
        assertEquals("baz", scope.getVar("baz").getName());
    }

    @Test
    public void testCreateScope_withScriptNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"));

        varNode.setParent(scriptNode);

        scriptNode.addChildToBack(varNode);

        Scope scope = scopeCreator.createScope(scriptNode, null);

        assertEquals("x", scope.getVar("x").getName());
    }
}