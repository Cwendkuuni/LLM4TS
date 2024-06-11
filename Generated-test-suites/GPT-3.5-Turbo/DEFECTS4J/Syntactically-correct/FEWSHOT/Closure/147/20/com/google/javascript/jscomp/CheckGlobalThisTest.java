package com.google.javascript.jscomp;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionNotConstructor_NotAnnotation_MatchTypes() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(null);
        Node parent = new Node();
        functionNode.setType(Token.SCRIPT);
        parent.setType(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(new JSDocInfo());
        functionNode.getJSDocInfo().setConstructor(true);
        Node parent = new Node();
        functionNode.setType(Token.SCRIPT);
        parent.setType(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisTypeAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(new JSDocInfo());
        functionNode.getJSDocInfo().setHasThisType(true);
        Node parent = new Node();
        functionNode.setType(Token.SCRIPT);
        parent.setType(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionIsOverride() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSDocInfo(new JSDocInfo());
        functionNode.getJSDocInfo().setOverride(true);
        Node parent = new Node();
        functionNode.setType(Token.SCRIPT);
        parent.setType(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_NotFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node node = new Node(Token.NOT);
        Node parent = new Node();
        parent.setType(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_PropertyAccessWithPrototype() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node lhs = new Node(Token.GETPROP);
        Node rhs = new Node();
        Node assign = new Node(Token.ASSIGN, lhs, rhs);
        Node parent = new Node();
        lhs.setLastChild(new Node(Token.STRING, "prototype"));
        parent.setParent(assign);
        assign.setType(Token.GETPROP);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, lhs, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_PropertyAccessWithoutPrototype() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node lhs = new Node(Token.GETPROP);
        Node rhs = new Node();
        Node assign = new Node(Token.ASSIGN, lhs, rhs);
        Node parent = new Node();
        lhs.setLastChild(new Node(Token.STRING, "property"));
        parent.setParent(assign);
        assign.setType(Token.GETPROP);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, lhs, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit_ThisOnLeftSide() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node n = new Node(Token.THIS);
        n.setType(Token.THIS);
        Node parent = new Node(); 
        checkGlobalThis.visit(nodeTraversal, n, parent);
        // Check for the report of error.
    }

    @Test
    public void testVisit_ThisWithPropertyAccess() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);
        Node n = new Node(Token.THIS);
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        checkGlobalThis.visit(nodeTraversal, n, parent);
        // Check for the report of error.
    }
}