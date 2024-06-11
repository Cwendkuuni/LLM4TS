package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    // helper method to create a Node with a given type
    private Node createNode(int type) {
        return new Node(type);
    }

    @Test
    public void testCreateScope_functionDeclaration() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node functionNode = createNode(Token.FUNCTION);
        Node nameNode = createNode(Token.NAME);
        nameNode.setString("testFunction");
        functionNode.addChildToFront(nameNode);

        Scope scope = creator.createScope(functionNode, null);

        assertNotNull(scope);
        assertEquals("testFunction", scope.getVar("testFunction").getName());
    }

    @Test
    public void testCreateScope_functionExpression() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node functionNode = createNode(Token.FUNCTION);

        Scope scope = creator.createScope(functionNode, null);

        assertNotNull(scope);
        assertNull(scope.getVar(""));
    }

    @Test
    public void testCreateScope_globalBlock() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node blockNode = createNode(Token.BLOCK);
        blockNode.setProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = creator.createScope(blockNode, null);

        assertNotNull(scope);
        assertNull(scope.getVar(""));
    }

    @Test
    public void testCreateScope_varDeclaration() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node varNode = createNode(Token.VAR);
        Node varNameNode1 = createNode(Token.NAME);
        varNameNode1.setString("var1");
        Node varNameNode2 = createNode(Token.NAME);
        varNameNode2.setString("var2");
        varNode.addChildToBack(varNameNode1);
        varNode.addChildToBack(varNameNode2);

        Scope scope = creator.createScope(varNode, null);

        assertNotNull(scope);
        assertEquals("var1", scope.getVar("var1").getName());
        assertEquals("var2", scope.getVar("var2").getName());
    }

    @Test
    public void testCreateScope_functionArgument() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node blockNode = createNode(Token.BLOCK);
        Node functionNode = createNode(Token.FUNCTION);
        Node nameNode = createNode(Token.NAME);
        nameNode.setString("testFunction");
        Node argsNode = createNode(Token.LP);
        Node argNode1 = createNode(Token.NAME);
        argNode1.setString("arg1");
        Node argNode2 = createNode(Token.NAME);
        argNode2.setString("arg2");

        functionNode.addChildToFront(nameNode);
        functionNode.addChildToBack(argsNode);
        argsNode.addChildToBack(argNode1);
        argsNode.addChildToBack(argNode2);
        blockNode.addChildToBack(functionNode);

        Scope scope = creator.createScope(blockNode, null);

        assertNotNull(scope);
        assertEquals("arg1", scope.getVar("arg1").getName());
        assertEquals("arg2", scope.getVar("arg2").getName());
    }

    @Test
    public void testCreateScope_catchStatement() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node catchNode = createNode(Token.CATCH);
        Node catchVarNode = createNode(Token.NAME);
        catchVarNode.setString("exception");
        Node catchBlockNode = createNode(Token.BLOCK);

        catchNode.addChildToFront(catchVarNode);
        catchNode.addChildToBack(catchBlockNode);

        Scope scope = creator.createScope(catchNode, null);

        assertNotNull(scope);
        assertEquals("exception", scope.getVar("exception").getName());
    }

    @Test
    public void testCreateScope_script() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());

        Node scriptNode = createNode(Token.SCRIPT);
        scriptNode.setProp(Node.SOURCENAME_PROP, "test.js");

        Scope scope = creator.createScope(scriptNode, null);

        assertNotNull(scope);
        assertNull(scope.getVar(""));
    }
}