package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {
    
    private AbstractCompiler compiler;
    private RuntimeTypeCheck runtimeTypeCheck;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        runtimeTypeCheck = new RuntimeTypeCheck(compiler, "log");
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        runtimeTypeCheck.process(externs, root);
    }
    
    @Test
    public void testAddMarkers() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.ROOT);
        Node parent = new Node(Token.EXPR_RESULT);
        
        RuntimeTypeCheck.AddMarkers addMarkers = runtimeTypeCheck.new AddMarkers(compiler);
        addMarkers.visit(t, node, parent);
    }
    
    @Test
    public void testAddChecks() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.ROOT);
        Node parent = new Node(Token.EXPR_RESULT);
        
        RuntimeTypeCheck.AddChecks addChecks = runtimeTypeCheck.new AddChecks(compiler);
        addChecks.visit(t, node, parent);
    }
    
    @Test
    public void testCreateCheckTypeCallNode() {
        JSType type = new JSType() {
            public String toString() {
                return "String";
            }
        };
        Node expr = new Node(Token.NAME);
        
        RuntimeTypeCheck.AddChecks addChecks = runtimeTypeCheck.new AddChecks(compiler);
        Node checkNode = addChecks.createCheckTypeCallNode(type, expr);
        
        assertEquals(Token.CALL, checkNode.getType());
        assertEquals("checkType", checkNode.getFirstChild().getString());
        assertEquals(expr, checkNode.getSecondChild());
        assertEquals(Token.ARRAYLIT, checkNode.getLastChild().getType());
    }
    
    @Test
    public void testCreateCheckerNode() {
        ObjectType type = new ObjectType() {
            public String getReferenceName() {
                return "Foo";
            }
            public Node getSource() {
                return new Node(Token.NAME);
            }
            public boolean isConstructor() {
                return true;
            }
            public boolean isInterface() {
                return false;
            }
        };
        
        RuntimeTypeCheck.AddChecks addChecks = runtimeTypeCheck.new AddChecks(compiler);
        Node checkerNode = addChecks.createCheckerNode(type);
        
        assertEquals(Token.CALL, checkerNode.getType());
        assertEquals("classChecker", checkerNode.getFirstChild().getString());
    }
    
    @Test
    public void testGetBoilerplateCode() {
        String logFunction = "log";
        
        Node boilerplateCode = RuntimeTypeCheck.getBoilerplateCode(compiler, logFunction);
        
        assertNotNull(boilerplateCode);
    }
}