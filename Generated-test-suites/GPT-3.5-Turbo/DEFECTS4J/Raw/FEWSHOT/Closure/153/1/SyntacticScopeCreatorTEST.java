package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }
    
    @Test
    public void testCreateScope() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = null;
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNull(scope.getParent());
        assertEquals(n, scope.getRootNode());
    }
    
    @Test
    public void testScanRoot_function() {
        Node n = new Node(Token.FUNCTION);
        String sourceName = "example.js";
        n.putProp(Node.SOURCENAME_PROP, sourceName);
        Scope parent = new Scope(null, null);
        
        scopeCreator.scanRoot(n, parent);
        
        assertEquals(sourceName, scopeCreator.sourceName);
    }
    
    @Test
    public void testScanRoot_globalBlock() {
        Node n = new Node(Token.BLOCK);
        Scope parent = null;
        
        scopeCreator.scanRoot(n, parent);
        
        assertNull(scopeCreator.sourceName);
    }
    
    @Test
    public void testScanVars_varDeclaration() {
        Node n = new Node(Token.VAR);
        String varName1 = "x";
        String varName2 = "y";
        
        Node child1 = new Node(Token.NAME, varName1);
        Node child2 = new Node(Token.NAME, varName2);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        
        Node parent = new Node(Token.BLOCK);
        
        scopeCreator.scanVars(n, parent);
        
        Scope.Var var1 = scopeCreator.scope.getVar(varName1);
        Scope.Var var2 = scopeCreator.scope.getVar(varName2);
        
        assertEquals(scopeCreator.scope, var1.getScope());
        assertEquals(child1, var1.getNode());
        assertEquals(scopeCreator.scope, var2.getScope());
        assertEquals(child2, var2.getNode());
    }
    
    @Test
    public void testScanVars_functionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        String functionName = "foo";
        
        Node child1 = new Node(Token.NAME, functionName);        
        n.addChildToBack(child1);
        
        Node parent = new Node(Token.BLOCK);
        
        scopeCreator.scanVars(n, parent);
        
        Scope.Var var = scopeCreator.scope.getVar(functionName);
        
        assertEquals(scopeCreator.scope, var.getScope());
        assertEquals(child1, var.getNode());
    }
    
    @Test
    public void testScanVars_catchBlock() {
        Node n = new Node(Token.CATCH);
        String catchVar = "e";
        
        Node child1 = new Node(Token.NAME, catchVar);        
        Node child2 = new Node(Token.BLOCK);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        
        Node parent = new Node(Token.BLOCK);
        
        scopeCreator.scanVars(n, parent);
        
        Scope.Var var = scopeCreator.scope.getVar(catchVar);
        
        assertEquals(scopeCreator.scope, var.getScope());
        assertEquals(child1, var.getNode());
    }
    
    @Test
    public void testScanVars_script() {
        Node n = new Node(Token.SCRIPT);
        String sourceName = "example.js";
        n.putProp(Node.SOURCENAME_PROP, sourceName);
        
        Node parent = new Node(Token.BLOCK);
        
        scopeCreator.scanVars(n, parent);
        
        assertEquals(sourceName, scopeCreator.sourceName);
    }
}