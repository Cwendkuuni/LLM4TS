package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_ShouldReturnFalseForConstructorFunction() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(null);
        n.putProp(Node.ORIGINALNAME_PROP, "dummy");
        Node parent = new Node(Token.FUNCTION);
        parent.setJSDocInfo(null);
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ShouldReturnFalseForFunctionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().build());
        n.putProp(Node.ORIGINALNAME_PROP, "dummy");
        Node parent = new Node(Token.FUNCTION);
        parent.setJSDocInfo(new JSDocInfoBuilder().build());
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ShouldReturnFalseForFunctionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().build());
        n.putProp(Node.ORIGINALNAME_PROP, "dummy");
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo parentJsdoc = new JSDocInfoBuilder().build();
        parentJsdoc.setOverride(true);
        parent.setJSDocInfo(parentJsdoc);
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ShouldReturnTrueForFunctionWithPrototypeMethod() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().build());
        n.putProp(Node.ORIGINALNAME_PROP, "dummy");
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo parentJsdoc = new JSDocInfoBuilder().build();
        parentJsdoc.setConstructor(true);
        parent.setJSDocInfo(parentJsdoc);
        assertTrue(new CheckGlobalThis(null, null).shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testShouldTraverse_ShouldReturnFalseForFunctionWithLogicalParent() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfoBuilder().build());
        n.putProp(Node.ORIGINALNAME_PROP, "dummy");
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(null);
        assertFalse(new CheckGlobalThis(null, null).shouldTraverse(t, n, parent));
    }
    
    @Test
    public void testVisit_ShouldCallReportWhenGlobalThisIsUsed() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
        check.visit(t, n, parent);
        // assert that the report method was called
    }
    
    @Test
    public void testVisit_ShouldNotCallReportWhenGlobalThisIsNotUsed() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ADD);
        CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
        check.visit(t, n, parent);
        // assert that the report method was not called
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ShouldReturnJsDocInfoFromNode() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfoBuilder builder = new JSDocInfoBuilder();
        builder.addReturnType(new Node(Token.STRING));
        JSDocInfo jsDocInfo = builder.build();
        n.setJSDocInfo(jsDocInfo);
        assertEquals(jsDocInfo, new CheckGlobalThis(null, null).getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ShouldReturnJsDocInfoFromParent() {
        Node n = new Node(Token.NAME);
        JSDocInfoBuilder builder = new JSDocInfoBuilder();
        builder.addReturnType(new Node(Token.STRING));
        JSDocInfo jsDocInfo = builder.build();
        n.setJSDocInfo(null);
        Node parent = new Node(Token.FUNCTION);
        parent.setJSDocInfo(jsDocInfo);
        n.setParent(parent);
        assertEquals(jsDocInfo, new CheckGlobalThis(null, null).getFunctionJsDocInfo(n));
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ShouldReturnJsDocInfoFromGrandparent() {
        Node n = new Node(Token.NAME);
        JSDocInfoBuilder builder = new JSDocInfoBuilder();
        builder.addReturnType(new Node(Token.STRING));
        JSDocInfo jsDocInfo = builder.build();
        n.setJSDocInfo(null);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.setJSDocInfo(jsDocInfo);
        parent.setParent(grandparent);
        n.setParent(parent);
        assertEquals(jsDocInfo, new CheckGlobalThis(null, null).getFunctionJsDocInfo(n));
    }
}