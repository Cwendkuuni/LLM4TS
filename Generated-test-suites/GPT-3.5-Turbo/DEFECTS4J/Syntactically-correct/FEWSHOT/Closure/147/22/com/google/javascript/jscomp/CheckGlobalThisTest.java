package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithInterfaceAnnotation_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordInterface();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordOverride();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithInvalidParent_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.ADD);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithValidParent_ReturnsTrue() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.BLOCK);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithAssignParent_ReturnsTrue() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithAssignParentAndPrototypeProperty_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        Node lhs = new Node();
        lhs.setType(Token.GETPROP);
        Node prototype = new Node();
        prototype.setString("prototype");
        lhs.addChildToBack(prototype);
        parent.addChildToFront(lhs);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithAssignParentAndNestedPrototypeProperty_ReturnsFalse() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        Node lhs = new Node();
        lhs.setType(Token.GETPROP);
        Node prototype = new Node();
        prototype.setString("prototype");
        lhs.addChildToBack(prototype);
        Node lhs2 = new Node();
        lhs2.setType(Token.GETPROP);
        lhs2.addChildToBack(lhs);
        parent.addChildToFront(lhs2);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldTraverse(traversal, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldReportThis_LeftSideOfAssignment_ReturnsTrue() {
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_PropertyAccess_ReturnsTrue() {
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_NotLeftSideOfAssignmentAndNotPropertyAccess_ReturnsFalse() {
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.VAR);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_GlobalThisEncountered_CallsCompilerReport() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.THIS);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        checkGlobalThis.visit(traversal, n, parent);
        // TODO: Verify that compiler.report() was called
    }

    @Test
    public void testVisit_NotGlobalThis_NoCallToCompilerReport() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.NULL);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        checkGlobalThis.visit(traversal, n, parent);
        // TODO: Verify that compiler.report() was NOT called
    }

    @Test
    public void testVisit_AssignLhsChild_SetToNullAfterTraversal() {
        NodeTraversal traversal = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.THIS);
        Node assignLhsChild = new Node();
        assignLhsChild.setType(Token.THIS);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        checkGlobalThis.assignLhsChild = assignLhsChild;
        checkGlobalThis.visit(traversal, n, null);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJsDocInfo_ReturnsJsDocInfo() {
        Node n = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n, parent);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithoutJsDocInfoButParentWithName_ReturnsJsDocInfoFromParent() {
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n, parent);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithoutJsDocInfoAndParentWithVar_ReturnsJsDocInfoFromGrandparent() {
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.VAR);
        Node grandparent = new Node();
        grandparent.setJSDocInfo(new JSDocInfo());
        parent.addChildToFront(grandparent);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n, parent);
        assertEquals(grandparent.getJSDocInfo(), result);
    }
}