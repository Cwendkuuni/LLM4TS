
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {

    private AbstractCompiler compiler;
    private SyntacticScopeCreator scopeCreator;
    private Scope globalScope;
    private String sourceName;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
        globalScope = new Scope(new Node(Token.ROOT), compiler);
    }

    @Test
    public void testCreateScope_FunctionWithVarDeclarations() {
        Node functionNode = createFunctionNode("testFunction");
        Node varNode = createVarNode("x");
        functionNode.addChildToFront(varNode);

        Scope functionScope = scopeCreator.createScope(functionNode, globalScope);

        assertFalse(globalScope.isDeclared("x", false));
        assertTrue(functionScope.isDeclared("x", false));
        assertEquals(functionNode, functionScope.getRootNode());
    }

    @Test
    public void testCreateScope_FunctionWithMultipleVarDeclarations() {
        Node functionNode = createFunctionNode("testFunction");
        Node varNode1 = createVarNode("x");
        Node varNode2 = createVarNode("y");
        functionNode.addChildToFront(varNode1);
        functionNode.addChildToFront(varNode2);

        Scope functionScope = scopeCreator.createScope(functionNode, globalScope);

        assertFalse(globalScope.isDeclared("x", false));
        assertFalse(globalScope.isDeclared("y", false));
        assertTrue(functionScope.isDeclared("x", false));
        assertTrue(functionScope.isDeclared("y", false));
        assertEquals(functionNode, functionScope.getRootNode());
    }

    @Test
    public void testCreateScope_FunctionWithLocalArgDeclaration() {
        Node functionNode = createFunctionNode("testFunction");
        Node argNode = createArgNode("x");
        Node varNode = createVarNode("x");
        functionNode.addChildToFront(argNode);
        functionNode.addChildToBack(varNode);

        Scope functionScope = scopeCreator.createScope(functionNode, globalScope);

        assertFalse(globalScope.isDeclared("x", false));
        assertTrue(functionScope.isDeclared("x", false));
        assertEquals(functionNode, functionScope.getRootNode());
    }

    @Test
    public void testCreateScope_FunctionWithShadowingArgDeclaration() {
        Node functionNode = createFunctionNode("testFunction");
        Node argNode = createArgNode("x");
        Node varNode = createVarNode("x");
        functionNode.addChildToFront(argNode);
        functionNode.addChildToBack(varNode);

        compiler.reportError(null, SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR);

        Scope functionScope = scopeCreator.createScope(functionNode, globalScope);

        assertFalse(globalScope.isDeclared("x", false));
        assertFalse(functionScope.isDeclared("x", false));
        assertEquals(functionNode, functionScope.getRootNode());
    }

    @Test
    public void testCreateScope_FunctionWithEmptyBody() {
        Node functionNode = createFunctionNode("testFunction");

        Scope functionScope = scopeCreator.createScope(functionNode, globalScope);

        assertEquals(functionNode, functionScope.getRootNode());
    }

    @Test
    public void testCreateScope_GlobalBlockWithVarDeclaration() {
        Node scriptNode = createScriptNode();
        Node varNode = createVarNode("x");
        scriptNode.addChildToFront(varNode);

        Scope scriptScope = scopeCreator.createScope(scriptNode, null);

        assertTrue(scriptScope.isDeclared("x", false));
        assertEquals(scriptNode, scriptScope.getRootNode());
    }

    @Test
    public void testCreateScope_GlobalBlockWithMultipleVarDeclarations() {
        Node scriptNode = createScriptNode();
        Node varNode1 = createVarNode("x");
        Node varNode2 = createVarNode("y");
        scriptNode.addChildToFront(varNode1);
        scriptNode.addChildToFront(varNode2);

        Scope scriptScope = scopeCreator.createScope(scriptNode, null);

        assertTrue(scriptScope.isDeclared("x", false));
        assertTrue(scriptScope.isDeclared("y", false));
        assertEquals(scriptNode, scriptScope.getRootNode());
    }

    @Test
    public void testCreateScope_GlobalBlockWithEmptyBody() {
        Node scriptNode = createScriptNode();

        Scope scriptScope = scopeCreator.createScope(scriptNode, null);

        assertEquals(scriptNode, scriptScope.getRootNode());
    }

    @Test
    public void testCreateScope_WithNullParent() {
        Node scriptNode = createScriptNode();
        Node varNode = createVarNode("x");
        scriptNode.addChildToFront(varNode);

        Scope scriptScope = scopeCreator.createScope(scriptNode, null);

        assertNull(scriptScope.getParent());
    }

    @Test
    public void testCreateScope_WithParent() {
        Node parentScopeNode = createScriptNode();
        Scope parentScope = scopeCreator.createScope(parentScopeNode, null);
        Node childScopeNode = createFunctionNode("testFunction");
        Node varNode = createVarNode("x");
        childScopeNode.addChildToFront(varNode);

        Scope childScope = scopeCreator.createScope(childScopeNode, parentScope);

        assertNull(childScope.getParent().getParent());
        assertEquals(parentScope, childScope.getParent());
        assertTrue(parentScope.hasChild(childScope));
    }

    private Node createFunctionNode(String name) {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.SOURCENAME_PROP, sourceName);
        functionNode.setFirstChild(new Node(Token.NAME, name));
        return functionNode;
    }

    private Node createVarNode(String name) {
        Node varNode = new Node(Token.VAR);
        varNode.putProp(Node.SOURCENAME_PROP, sourceName);
        varNode.addChildToBack(new Node(Token.NAME, name));
        return varNode;
    }

    private Node createArgNode(String name) {
        Node argNode = new Node(Token.REST);
        argNode.putProp(Node.SOURCENAME_PROP, sourceName);
        argNode.addChildToBack(new Node(Token.NAME, name));
        return argNode;
    }

    private Node createScriptNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.putProp(Node.SOURCENAME_PROP, sourceName);
        return scriptNode;
    }

}
