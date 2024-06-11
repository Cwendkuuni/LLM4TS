package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

        Node root = new Node(Token.SCRIPT, "test");
        Node functionNode = new Node(Token.FUNCTION, null, null);
        Node assignNode = new Node(Token.ASSIGN, null, functionNode);
        Node lhs = new Node(Token.NAME, "a");
        Node rhs = new Node(Token.THIS, null);

        Node functionParent = new Node(Token.NAME, "test");
        Node assignParent = new Node(Token.ASSIGN, null, functionParent);
        Node functionWithPrototypeParent = new Node(Token.NAME, "prototype");
        Node functionWithPrototype = new Node(Token.FUNCTION, null, functionWithPrototypeParent);
        Node getProperty = new Node(Token.GETPROP, null, functionWithPrototype);
        Node prototypeNode = new Node(Token.NAME, "prototype");

        when(functionNode.getParent()).thenReturn(assignParent);
        when(assignParent.getParent()).thenReturn(root);
        when(functionWithPrototypeParent.getParent()).thenReturn(root);
        when(prototypeNode.getString()).thenReturn("prototype");

        NodeTraversal traversal = mock(NodeTraversal.class);

        boolean result = checker.shouldTraverse(traversal, rhs, assignParent);
        assertThat(result).isTrue();

        result = checker.shouldTraverse(traversal, rhs, functionParent);
        result = checker.shouldTraverse(traversal, rhs, functionWithPrototype);
        result = checker.shouldTraverse(traversal, rhs, functionWithPrototypeParent);
        result = checker.shouldTraverse(traversal, rhs, functionNode);
        result = checker.shouldTraverse(traversal, rhs, root);
        result = checker.shouldTraverse(traversal, rhs, lhs);
        result = checker.shouldTraverse(traversal, rhs, prototypeNode);

        assertThat(result).isFalse();
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

        Node root = new Node(Token.SCRIPT, "test");
        Node functionNode = new Node(Token.FUNCTION, null, null);
        Node assignNode = new Node(Token.ASSIGN, null, functionNode);
        Node lhs = new Node(Token.NAME, "a");
        Node rhs = new Node(Token.THIS, null);

        Node functionParent = new Node(Token.NAME, "test");
        Node assignParent = new Node(Token.ASSIGN, null, functionParent);
        Node functionWithPrototypeParent = new Node(Token.NAME, "prototype");
        Node functionWithPrototype = new Node(Token.FUNCTION, null, functionWithPrototypeParent);
        Node getProperty = new Node(Token.GETPROP, null, functionWithPrototype);
        Node prototypeNode = new Node(Token.NAME, "prototype");

        when(functionNode.getParent()).thenReturn(assignParent);
        when(assignParent.getParent()).thenReturn(root);
        when(functionWithPrototypeParent.getParent()).thenReturn(root);
        when(prototypeNode.getString()).thenReturn("prototype");

        NodeTraversal traversal = mock(NodeTraversal.class);

        boolean result = checker.shouldReportThis(rhs, assignParent);
        assertThat(result).isTrue();

        result = checker.shouldReportThis(rhs, functionParent);
        result = checker.shouldReportThis(rhs, functionWithPrototype);
        result = checker.shouldReportThis(rhs, functionWithPrototypeParent);
        result = checker.shouldReportThis(rhs, functionNode);
        result = checker.shouldReportThis(rhs, root);
        result = checker.shouldReportThis(rhs, lhs);
        result = checker.shouldReportThis(rhs, prototypeNode);

        assertThat(result).isFalse();
    }

    @Test
    public void testVisit() {
        CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

        Node root = new Node(Token.SCRIPT, "test");
        Node functionNode = new Node(Token.FUNCTION, null, null);
        Node assignNode = new Node(Token.ASSIGN, null, functionNode);
        Node lhs = new Node(Token.NAME, "a");
        Node rhs = new Node(Token.THIS, null);

        Node functionParent = new Node(Token.NAME, "test");
        Node assignParent = new Node(Token.ASSIGN, null, functionParent);
        Node functionWithPrototypeParent = new Node(Token.NAME, "prototype");
        Node functionWithPrototype = new Node(Token.FUNCTION, null, functionWithPrototypeParent);
        Node getProperty = new Node(Token.GETPROP, null, functionWithPrototype);
        Node prototypeNode = new Node(Token.NAME, "prototype");

        Node functionJsDoc = new JSDocInfo();
        functionJsDoc.setConstructor(true);

        when(functionNode.getParent()).thenReturn(assignParent);
        when(assignParent.getParent()).thenReturn(root);
        when(functionWithPrototypeParent.getParent()).thenReturn(root);
        when(prototypeNode.getString()).thenReturn("prototype");
        when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);

        NodeTraversal traversal = mock(NodeTraversal.class);

        checker.visit(traversal, rhs, assignParent);

        // No report should have been made.
        Node reportNode = traversal.getCurrentError();
        assertNull(reportNode);

        functionJsDoc.setConstructor(false);
        functionJsDoc.setInterface(true);
        functionJsDoc.setOverride(true);
        functionJsDoc.setThisType("foo");

        checker.visit(traversal, rhs, assignParent);

        reportNode = traversal.getCurrentError();
        assertThat(reportNode).isNotNull();
        assertThat(reportNode.getMessage()).isEqualTo("dangerous use of the global 'this' object");
    }
}