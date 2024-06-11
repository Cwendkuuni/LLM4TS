package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    private AbstractCompiler compiler;
    private CheckSideEffects checkSideEffects;
    private NodeTraversal nodeTraversal;
    private NodeUtil nodeUtil;

    @org.junit.Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        nodeTraversal = mock(NodeTraversal.class);
        nodeUtil = mock(NodeUtil.class);
        when(compiler.getTraverser()).thenReturn(nodeTraversal);
        when(nodeUtil.isExpressionResultUsed(NodeUnitTestHelper.createNode())).thenReturn(false);
        when(nodeUtil.isSimpleOperatorType(NodeUnitTestHelper.createNodeType())).thenReturn(false);
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    }

    @org.junit.Test
    public void testProcess() {
        Node root = NodeUnitTestHelper.createNode();
        checkSideEffects.process(null, root);
    }

    @org.junit.Test
    public void testVisit_VOID_Node() {
        Node parent = NodeUnitTestHelper.createNode();
        Node n = NodeUnitTestHelper.createNode();
        checkSideEffects.visit(nodeTraversal, n, parent);
    }

    @org.junit.Test
    public void testVisit_NonResultUsed_SimpleOp() {
        Node parent = NodeUnitTestHelper.createNode();
        Node n = NodeUnitTestHelper.createNode();
        n.setType(Token.ADD);
        checkSideEffects.visit(nodeTraversal, n, parent);
    }

    @org.junit.Test
    public void testVisit_NonResultUsed_ComplexOp() {
        Node parent = NodeUnitTestHelper.createNode();
        Node n = NodeUnitTestHelper.createNode();
        n.setType(Token.MULT);
        checkSideEffects.visit(nodeTraversal, n, parent);
    }

    @org.junit.Test
    public void testVisit_ResultUsed() {
        Node parent = NodeUnitTestHelper.createNode();
        Node n1 = NodeUnitTestHelper.createNode();
        Node n2 = NodeUnitTestHelper.createNode();
        n1.setType(Token.ASSIGN);
        n1.setRightChild(n2);
        checkSideEffects.visit(nodeTraversal, n1, parent);
    }

    @org.junit.Test
    public void testVisit_ForStatement() {
        Node parent = NodeUnitTestHelper.createNode();
        Node n = NodeUnitTestHelper.createNode();
        Node forStatement = NodeUnitTestHelper.createForStatement();
        parent.setLastChild(forStatement);
        forStatement.setFirstChild(n);
        checkSideEffects.visit(nodeTraversal, n, parent);
    }

    @org.junit.Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(mock(AbstractCompiler.class), CheckLevel.WARNING, true);
        List<Node> problemNodes = Lists.newArrayList();
        checkSideEffects.protectSideEffects();
        assertEquals(0, problemNodes.size());
    }

    @org.junit.Test
    public void testProtectSideEffects_WithProblemNodes() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(mock(AbstractCompiler.class), CheckLevel.WARNING, true);
        Node problemNode1 = NodeUnitTestHelper.createNode();
        Node problemNode2 = NodeUnitTestHelper.createNode();
        List<Node> problemNodes = Lists.newArrayList(problemNode1, problemNode2);
        checkSideEffects.problemNodes = problemNodes;
        checkSideEffects.protectSideEffects();
        assertEquals(2, problemNodes.size());
    }

    @org.junit.Test
    public void testStripProtection() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        StripProtection stripProtection = new StripProtection(compiler);
        Node root = NodeUnitTestHelper.createNode();
        stripProtection.process(null, root);
    }
}