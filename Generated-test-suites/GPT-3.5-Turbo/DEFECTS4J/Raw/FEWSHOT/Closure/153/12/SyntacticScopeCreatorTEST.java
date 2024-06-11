package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private AbstractCompiler compiler;
    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope_functionDeclaration() {
        Node functionNode = createFunctionDeclarationNode("testFunction", null, null);
        Scope scope = scopeCreator.createScope(functionNode, null);
        
        assertNotNull(scope);
        assertEquals(1, scope.getVarCount());
        assertEquals("testFunction", scope.getVar(0).getName());
    }

    @Test
    public void testCreateScope_functionDeclarationWithArguments() {
        Node functionNode = createFunctionDeclarationNode("testFunction", new String[]{"arg1", "arg2"}, null);
        Scope scope = scopeCreator.createScope(functionNode, null);
        
        assertNotNull(scope);
        assertEquals(3, scope.getVarCount());
        assertEquals("testFunction", scope.getVar(0).getName());
        assertEquals("arg1", scope.getVar(1).getName());
        assertEquals("arg2", scope.getVar(2).getName());
    }

    @Test
    public void testCreateScope_functionExpression() {
        Node functionNode = createFunctionExpressionNode("testFunction", null, null);
        Scope scope = scopeCreator.createScope(functionNode, null);
        
        assertNotNull(scope);
        assertEquals(2, scope.getVarCount());
        assertEquals("testFunction", scope.getVar(0).getName());
        assertEquals("fnName", scope.getVar(1).getName());
    }

    @Test
    public void testCreateScope_functionExpressionWithArguments() {
        Node functionNode = createFunctionExpressionNode("testFunction", new String[]{"arg1", "arg2"}, null);
        Scope scope = scopeCreator.createScope(functionNode, null);
        
        assertNotNull(scope);
        assertEquals(4, scope.getVarCount());
        assertEquals("testFunction", scope.getVar(0).getName());
        assertEquals("fnName", scope.getVar(1).getName());
        assertEquals("arg1", scope.getVar(2).getName());
        assertEquals("arg2", scope.getVar(3).getName());
    }

    @Test
    public void testCreateScope_globalBlock() {
        Node globalBlockNode = createGlobalBlockNode();
        Scope scope = scopeCreator.createScope(globalBlockNode, null);
        
        assertNotNull(scope);
        assertEquals(0, scope.getVarCount());
    }
    
    private Node createFunctionDeclarationNode(String functionName, String[] arguments, Node body) {
        Node functionNode = new Node(Token.FUNCTION);
        
        Node nameNode = new Node(Token.NAME);
        nameNode.setString(functionName);
        
        functionNode.addChildToBack(nameNode);
        
        if (arguments != null) {
            Node argumentsNode = new Node(Token.LP);
            
            for (String argument : arguments) {
                Node argNode = new Node(Token.NAME);
                argNode.setString(argument);
                
                argumentsNode.addChildToBack(argNode);
            }
            
            functionNode.addChildToBack(argumentsNode);
        }
        
        if (body != null) {
            functionNode.addChildToBack(body);
        }
        
        return functionNode;
    }
    
    private Node createFunctionExpressionNode(String functionName, String[] arguments, Node body) {
        Node functionNode = new Node(Token.FUNCTION);
        
        Node fnNameNode = new Node(Token.NAME);
        fnNameNode.setString("fnName");
        
        functionNode.addChildToBack(fnNameNode);
        
        Node nameNode = new Node(Token.NAME);
        nameNode.setString(functionName);
        
        functionNode.addChildToBack(nameNode);
        
        if (arguments != null) {
            Node argumentsNode = new Node(Token.LP);
            
            for (String argument : arguments) {
                Node argNode = new Node(Token.NAME);
                argNode.setString(argument);
                
                argumentsNode.addChildToBack(argNode);
            }
            
            functionNode.addChildToBack(argumentsNode);
        }
        
        if (body != null) {
            functionNode.addChildToBack(body);
        }
        
        return functionNode;
    }
    
    private Node createGlobalBlockNode() {
        Node blockNode = new Node(Token.BLOCK);
        
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.putProp(Node.SOURCENAME_PROP, "test.js");
        
        scriptNode.addChildToBack(blockNode);
        
        return scriptNode;
    }
}