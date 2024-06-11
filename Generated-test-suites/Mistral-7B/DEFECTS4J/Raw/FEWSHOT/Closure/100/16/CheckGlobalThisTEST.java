package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    private CheckGlobalThis checker;
    private NodeTraversal traversal;
    private AbstractCompiler compilerMock;

    @org.junit.Before
    public void setUp() {
        this.compilerMock = mock(AbstractCompiler.class);
        this.checker = new CheckGlobalThis(this.compilerMock, CheckLevel.WARNING);
        this.traversal = NodeTraversal.create(new Node(Token.PROGRAM, null));
        this.traversal.addCallback(this.checker);
    }

    @org.junit.Test
    public void testShouldTraverseGlobalFunction() {
        Node functionNode = new Node(Token.FUNCTION, null);
        Node result = this.checker.shouldTraverse(this.traversal, functionNode, null);
        assertTrue(result);
    }

    @org.junit.Test
    public void testShouldNotTraverseConstructorFunction() {
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

        Node functionNode = new Node(Token.FUNCTION, null);
        Node result = this.checker.shouldTraverse(this.traversal, functionNode, null);
        assertFalse(result);
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionWithThisAnnotation() {
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setThisType(true);
        when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

        Node functionNode = new Node(Token.FUNCTION, null);
        Node result = this.checker.shouldTraverse(this.traversal, functionNode, null);
        assertFalse(result);
    }

    @org.junit.Test
    public void testShouldReportThisOnLeftSideOfAssignment() {
        Node assignNode = new Node(Token.ASSIGN, null);
        Node leftNode = new Node(Token.THIS, null);
        assignNode.addChildToEnd(leftNode);
        Node result = this.checker.shouldReportThis(assignNode, null);
        assertTrue(result);
    }

    @org.junit.Test
    public void testShouldNotReportThisOnRightSideOfAssignment() {
        Node assignNode = new Node(Token.ASSIGN, null);
        Node leftNode = new Node(Token.GETPROP, null, new Node(Token.IDENT, "someProperty"));
        assignNode.addChildToEnd(leftNode);
        Node rightNode = new Node(Token.THIS, null);
        assignNode.addChildToEnd(rightNode);

        Node result = this.checker.shouldReportThis(rightNode, assignNode);
        assertFalse(result);
    }

    @org.junit.Test
    public void testShouldReportThisOnPropertyAccess() {
        Node getPropNode = new Node(Token.GETPROP, null, new Node(Token.THIS, null));
        Node result = this.checker.shouldReportThis(getPropNode, null);
        assertTrue(result);
    }

    @org.junit.Test
    public void testShouldNotReportThisOnPrototypePropertyAccess() {
        Node getPropNode = new Node(Token.GETPROP, null, new Node(Token.IDENT, "prototype"));
        Node result = this.checker.shouldReportThis(getPropNode, null);
        assertFalse(result);
    }
}