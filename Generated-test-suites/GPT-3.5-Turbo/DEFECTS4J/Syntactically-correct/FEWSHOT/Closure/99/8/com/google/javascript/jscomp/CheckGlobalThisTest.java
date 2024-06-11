package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_Function_NotConstructor_NotThisAnnotation_True() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        // Set up expectations
        boolean expected = true;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check result
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldTraverse_Function_Constructor_False() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        // Set up expectations
        boolean expected = false;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check result
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldTraverse_Function_ThisAnnotation_False() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);

        // Set up expectations
        boolean expected = false;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check result
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldTraverse_Function_OverrideAnnotation_False() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);

        // Set up expectations
        boolean expected = false;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check result
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldTraverse_Function_ParentNotBlockOrScriptOrNameOrAssign_False() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EMPTY);

        // Set up expectations
        boolean expected = false;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check result
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldTraverse_NotParent_Assign_False() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.ASSIGN);

        // Set up expectations
        boolean expected = false;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check result
        assertEquals(expected, actual);
    }

    @Test
    public void testShouldTraverse_Parent_IsAssign_ThisNodeNotAssign_True() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.ASSIGN);

        // Set expectations
        boolean expected = true;

        // Run test
        boolean actual = CheckGlobalThis.shouldTraverse(t, n, parent);

        // Check results
        assertEquals(expected, actual);
    }

    @Test
    public void testVisit_ThisNode_True_AssignLhsChildNotNull_AssignLhsChildNull() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);
        CheckGlobalThis.assignLhsChild = new Node(Token.EMPTY);

        // Set expectations
        CheckGlobalThis.assignLhsChild = null;

        // Run test
        CheckGlobalThis.visit(t, n, parent);

        // Check results
        assertNull(CheckGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_ThisNode_True_AssignLhsChildNull_AssignLhsChildNull() {
        // Create nodes
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);
        CheckGlobalThis.assignLhsChild = null;

        // Set expectations
        CheckGlobalThis.assignLhsChild = null;

        // Run test
        CheckGlobalThis.visit(t, n, parent);

        // Check results
        assertNull(CheckGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_N_HasJSDocInfo_NJSDocInfo() {
        // Create nodes
        Node n = new Node(Token.EMPTY);
        JSDocInfo expected = new JSDocInfo();
        n.setJSDocInfo(expected);

        // Run test
        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        // Check results
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_N_NoJSDocInfo_NoParent_ParentNotName_JSDocInfo() {
        // Create nodes
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);

        // Run test
        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        // Check results
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_N_NoJSDocInfo_NoParent_ParentName_ParentNameJSDocInfo() {
        // Create nodes
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.NAME);
        JSDocInfo expected = new JSDocInfo();
        parent.setJSDocInfo(expected);

        // Run test
        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        // Check results
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFunctionJsDocInfo_N_NoJSDocInfo_NoParent_VarParent_ParentParentJSDocInfo() {
        // Create nodes
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.NAME);
        Node grandparent = new Node(Token.VAR);
        JSDocInfo expected = new JSDocInfo();
        grandparent.setJSDocInfo(expected);
        parent.addChildToFront(grandparent);

        // Run test
        JSDocInfo actual = CheckGlobalThis.getFunctionJsDocInfo(n);

        // Check results
        assertEquals(expected, actual);
    }
}