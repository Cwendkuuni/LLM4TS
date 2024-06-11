package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompilerPass;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

public class CheckSideEffectsTest {
    private AbstractCompiler compiler;
    private Node externsRoot;
    private Node jsRoot;
    private CheckSideEffects checkSideEffects;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        externsRoot = new Node(Token.ROOT);
        jsRoot = new Node(Token.ROOT);
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    }

    @Test
    public void testProcess_withValidCode() {
        Node script = IR.script(IR.block());
        jsRoot.addChildToBack(script);

        checkSideEffects.process(externsRoot, jsRoot);

        // Verify that no warnings were reported
        verify(compiler, times(0)).report(any());
    }

    @Test
    public void testProcess_withSuspiciousCode() {
        Node script = IR.script(
                IR.var(IR.name("a"), IR.string("hello")),
                IR.empty(),
                IR.var(IR.name("b"), IR.string("world")));
        jsRoot.addChildToBack(script);

        checkSideEffects.process(externsRoot, jsRoot);

        // Verify that the warning was reported
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testHotSwapScript_withValidCode() {
        Node script = IR.script(IR.block());
        jsRoot.addChildToBack(script);

        checkSideEffects.hotSwapScript(script, null);

        // Verify that no warnings were reported
        verify(compiler, times(0)).report(any());
    }

    @Test
    public void testHotSwapScript_withSuspiciousCode() {
        Node script = IR.script(
                IR.var(IR.name("a"), IR.string("hello")),
                IR.empty(),
                IR.var(IR.name("b"), IR.string("world")));
        jsRoot.addChildToBack(script);

        checkSideEffects.hotSwapScript(script, null);

        // Verify that the warning was reported
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testVisit_withExprResultNode() {
        Node exprNode = IR.exprResult(IR.string("hello"));
        Node parent = IR.var(exprNode);

        checkSideEffects.visit(null, exprNode, parent);

        // Verify that no warnings were reported
        verify(compiler, times(0)).report(any());
    }

    @Test
    public void testVisit_withBlockNode() {
        Node blockNode = IR.block();
        Node parent = IR.var(blockNode);

        checkSideEffects.visit(null, blockNode, parent);

        // Verify that no warnings were reported
        verify(compiler, times(0)).report(any());
    }

    @Test
    public void testVisit_withQualifiedNameNode() {
        Node qualifiedNameNode = IR.name("a");
        qualifiedNameNode.setJSDocInfo(new JSDocInfoBuilder().build());
        Node parent = IR.var(qualifiedNameNode);

        checkSideEffects.visit(null, qualifiedNameNode, parent);

        // Verify that no warnings were reported
        verify(compiler, times(0)).report(any());
    }

    @Test
    public void testVisit_withUnusedResultAndSimpleOperatorNode() {
        Node operatorNode = IR.add(IR.number(1), IR.number(2));
        Node parent = IR.var(operatorNode);

        checkSideEffects.visit(null, operatorNode, parent);

        // Verify that the warning was reported
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testVisit_withUnusedResultAndNonSimpleOperatorNode() {
        Node operatorNode = IR.assign(IR.name("x"), IR.number(10));
        Node parent = IR.var(operatorNode);

        checkSideEffects.visit(null, operatorNode, parent);

        // Verify that the warning was reported
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testVisit_withStatementNodeAndUnusedResult() {
        Node statementNode = IR.ifNode(IR.trueNode(), IR.block());
        Node parent = IR.var(statementNode);

        checkSideEffects.visit(null, statementNode, parent);

        // Verify that the warning was reported
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testVisit_withForLoopNodeAndUnusedResult() {
        Node forNode = IR.forNode(
                IR.exprResult(IR.assign(IR.name("i"), IR.number(0))),
                IR.lt(IR.name("i"), IR.number(10)),
                IR.inc(IR.name("i")),
                IR.block());
        Node parent = IR.var(forNode);

        checkSideEffects.visit(null, forNode, parent);

        // Verify that the warning was reported
        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testProtectSideEffects_withProblemNodes() {
        Node problemNode1 = IR.string("hello");
        Node problemNode2 = IR.string("world");
        problemNode1.setJSDocInfo(new JSDocInfoBuilder().build());
        problemNode2.setJSDocInfo(new JSDocInfoBuilder().build());
        problemNodes.add(problemNode1);
        problemNodes.add(problemNode2);

        checkSideEffects.protectSideEffects();

        // Verify that the problem nodes were replaced with function calls
        ArgumentCaptor<Node> replacementCaptor = ArgumentCaptor.forClass(Node.class);
        verify(problemNode1.getParent(), times(1)).replaceChild(problemNode1, replacementCaptor.capture());
        verify(problemNode2.getParent(), times(1)).replaceChild(problemNode2, replacementCaptor.capture());

        List<Node> replacements = replacementCaptor.getAllValues();
        assertEquals(2, replacements.size());

        for (Node replacement : replacements) {
            assertTrue(replacement.isCall());
            assertEquals("JSCOMPILER_PRESERVE", replacement.getFirstChild().getString());
            assertEquals(1, replacement.getChildCount());
            assertTrue(replacement.getFirstChild().getBooleanProp(Node.IS_CONSTANT_NAME));
            assertTrue(replacement.getBooleanProp(Node.FREE_CALL));
        }
    }

    @Test
    public void testAddExtern() {
        checkSideEffects.addExtern();

        // Verify that the extern function was added to the synthesized externs input
        ArgumentCaptor<Node> varCaptor = ArgumentCaptor.forClass(Node.class);
        verify(compiler.getSynthesizedExternsInput().getAstRoot(compiler), times(1)).addChildrenToBack(varCaptor.capture());

        Node varNode = varCaptor.getValue();
        assertTrue(varNode.isVar());
        assertEquals("JSCOMPILER_PRESERVE", varNode.getFirstChild().getString());
        assertTrue(varNode.getFirstChild().getBooleanProp(Node.IS_CONSTANT_NAME));
        assertTrue(varNode.getFirstChild().getJSDocInfo().isNoAlias());
    }

    @Test
    public void testStripProtection_withCallNode() {
        Node protectNode = IR.call(IR.name("JSCOMPILER_PRESERVE"));
        Node expressionNode = IR.exprResult(protectNode);
        Node parent = IR.var(expressionNode);

        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        stripProtection.visit(null, protectNode, parent);

        // Verify that the call node was stripped
        verify(parent, times(1)).replaceChild(expressionNode, protectNode.getLastChild());
    }
}