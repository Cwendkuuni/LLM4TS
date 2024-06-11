package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_shouldReturnTrueWhenNodeTypeIsFunctionAndJsDocIsNull() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueWhenNodeIsNotAnnotatedWithConstructorOrThis() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(false);
        jsDoc.setThisType(false);
        n.setJSDocInfo(jsDoc);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseWhenNodeIsAnnotatedWithConstructor() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        jsDoc.setThisType(false);
        n.setJSDocInfo(jsDoc);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseWhenNodeIsAnnotatedWithThis() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(false);
        jsDoc.setThisType(true);
        n.setJSDocInfo(jsDoc);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseWhenNodeIsAnnotatedWithOverride() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseWhenNotExecutableFunction() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.STRING);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseWhenFunctionHasParentTypeOtherThanBlockScriptNameAssign() {
        Node n = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.NAME);
        Node greatGrandParent = new Node(Token.SCRIPT);

        greatGrandParent.addChildToBack(parent);
        parent.addChildToBack(grandParent);
        grandParent.addChildToBack(n);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisitt_shouldReportThisWhenNodeIsThisAndShouldReportThisReturnsTrue() {
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.FUNCTION);

        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        NodeTraversal.Callback callback = new CheckGlobalThis(t, CheckGlobalThis.checkLevel);

        assertTrue(callback.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_shouldNotReportThisWhenNodeIsThisAndShouldReportThisReturnsFalse() {
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);

        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        NodeTraversal.Callback callback = new CheckGlobalThis(t, CheckGlobalThis.checkLevel);

        assertFalse(callback.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_shouldReportThisWhenNodeIsThisAndAssignLhsChildIsNotNull() {
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);
        Node assignNode = new Node(Token.ASSIGN);

        assignNode.addChildToBack(n);
        parent.addChildToBack(assignNode);

        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        NodeTraversal.Callback callback = new CheckGlobalThis(t, CheckGlobalThis.checkLevel);

        assertTrue(callback.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_shouldNotReportThisWhenNodeIsThisAndAssignLhsChildIsNull() {
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.SCRIPT);

        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        NodeTraversal.Callback callback = new CheckGlobalThis(t, CheckGlobalThis.checkLevel);

        assertFalse(callback.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_shouldSetAssignLhsChildToNullWhenNodeIsAssignLhsChild() {
        Node n = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        Node rhs = new Node(Token.STRING);

        lhs.addChildToBack(n);
        parent.addChildToBack(lhs);
        parent.addChildToBack(rhs);

        CheckGlobalThis.checkLevel = CheckLevel.WARNING;
        NodeTraversal.Callback callback = new CheckGlobalThis(t, CheckGlobalThis.checkLevel);
        callback.assignLhsChild = lhs;

        callback.visit(t, n, parent);

        assertNull(callback.assignLhsChild);
    }
}