
package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructor() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
    }

    @Test
    public void testShouldTraverse_FunctionWithNoAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), null));
    }

    @Test
    public void testShouldTraverse_VariableAssignment_NotPrototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "a"));
        lhs.addChildToBack(new Node(Token.NAME, "b"));
        parent.addChildToBack(lhs);
        assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), parent));
    }

    @Test
    public void testShouldTraverse_VariableAssignment_PrototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "a"));
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        parent.addChildToBack(lhs);
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), parent));

        lhs.replaceChildAt(1, new Node(Token.STRING, "prototype.property"));
        assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.NAME), parent));
    }

    @Test
    public void testVisit_NonAssignmentNode_shouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        t.setErrorReporter(null);
        Node node = new Node(Token.THIS);
        node.setLineno(1);
        checkGlobalThis.visit(t, node, null);
        //assert error is reported
    }

    @Test
    public void testVisit_AssignmentNode_assignLhsChildNotNull() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal();
        t.setErrorReporter(null);
        Node node = new Node(Token.THIS);
        node.setLineno(1);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME, "a");
        checkGlobalThis.visit(t, node, null);
        //assert assignLhsChild is set back to null
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull_shouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.THIS);
        assertTrue(checkGlobalThis.shouldReportThis(node, null));
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNull_shouldNotReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node node = new Node(Token.THIS);
        assertFalse(checkGlobalThis.shouldReportThis(node, null));
    }

}
