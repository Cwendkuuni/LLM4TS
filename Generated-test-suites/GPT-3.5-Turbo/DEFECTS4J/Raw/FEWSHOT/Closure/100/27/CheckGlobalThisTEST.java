
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionNotConstructorOrHasThisAnnotation_ReturnTrue() {
        // Test case where the function is not a constructor and does not have @this annotation
        // Should return true
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor_ReturnFalse() {
        // Test case where the function is a constructor
        // Should return false
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        JSDocInfo jsDoc = new JSDocInfo(true, false);
        n.setJSDocInfo(jsDoc);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisAnnotation_ReturnFalse() {
        // Test case where the function has @this annotation
        // Should return false
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        JSDocInfo jsDoc = new JSDocInfo(false, true);
        n.setJSDocInfo(jsDoc);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignLhsChildNotNull_ReturnTrue() {
        // Test case where assignLhsChild is not null
        // Should return true
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EXPR_RESULT);
        CheckGlobalThis.assignLhsChild = new Node(Token.NAME);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ParentIsAssignAndLhsIsPrototype_ReturnFalse() {
        // Test case where parent is ASSIGN and lhs is "prototype"
        // Should return false
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "foo"), new Node(Token.STRING, "prototype"));
        parent.addChildToFront(lhs);
        parent.addChildToFront(n);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsAssignAndLhsContainsPrototype_ReturnFalse() {
        // Test case where parent is ASSIGN and lhs contains "prototype"
        // Should return false
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "foo"), new Node(Token.STRING, "prototype"));
        parent.addChildToFront(lhs);
        parent.addChildToFront(n);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsAssignAndLhsIsNotPrototype_ReturnTrue() {
        // Test case where parent is ASSIGN and lhs is not "prototype"
        // Should return true
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "foo"), new Node(Token.STRING, "bar"));
        parent.addChildToFront(lhs);
        parent.addChildToFront(n);

        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ReturnTrue() {
        // Test case where node is THIS and shouldReportThis returns true
        // Should report error
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EXPR_RESULT);
        CheckGlobalThis.assignLhsChild = null;

        CheckGlobalThis.visit(t, n, parent);

        // Assert that error is reported
        assertEquals(1, t.getErrors().size());
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ReturnFalse() {
        // Test case where node is THIS and shouldReportThis returns false
        // Should not report error
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EXPR_RESULT);
        CheckGlobalThis.assignLhsChild = null;

        CheckGlobalThis.visit(t, n, parent);

        // Assert that no error is reported
        assertEquals(0, t.getErrors().size());
    }

    @Test
    public void testVisit_NodeIsNotThis_CheckAssignLhsChildIsNull_ReturnTrue() {
        // Test case where node is not THIS and assignLhsChild is null
        // Should do nothing
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.EXPR_RESULT);
        CheckGlobalThis.assignLhsChild = null;

        CheckGlobalThis.visit(t, n, parent);

        // Assert that no error is reported
        assertEquals(0, t.getErrors().size());
    }

    @Test
    public void testVisit_NodeIsNotThis_CheckAssignLhsChildIsNotNull_ReturnTrue() {
        // Test case where node is not THIS and assignLhsChild is not null
        // Should set assignLhsChild to null
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.EXPR_RESULT);
        CheckGlobalThis.assignLhsChild = new Node(Token.NAME);

        CheckGlobalThis.visit(t, n, parent);

        // Assert that assignLhsChild is null
        assertNull(CheckGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJSDocInfo_ReturnJSDocInfo() {
        // Test case where function has JSDocInfo
        // Should return the JSDocInfo
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo(true, false);
        n.setJSDocInfo(jsDoc);

        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionHasJSDocInfoInParent_ReturnJSDocInfo() {
        // Test case where function does not have JSDocInfo, but its parent does
        // Should return the JSDocInfo from the parent
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo(true, false);
        parent.setJSDocInfo(jsDoc);
        parent.addChildToBack(n);

        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionDoesNotHaveJSDocInfo_ReturnNull() {
        // Test case where function does not have JSDocInfo and its parent does not have JSDocInfo
        // Should return null
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(n);

        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);

        assertNull(result);
    }
}
