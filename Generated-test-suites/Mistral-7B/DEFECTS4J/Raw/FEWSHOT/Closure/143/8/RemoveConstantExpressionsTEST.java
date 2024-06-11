package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.Rewriter;
import com.google.javascript.jscomp.test.NodeMatcher;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {
    @Test
    public void testRemoveConstantExpressions() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(mock(Compiler.class));
        ParallelCompilerPass.Context context = new ParallelCompilerPass.Context();
        Node rootNode = NodeUtil.createTestRoot();

        List<Node> astNodesBefore = Lists.newArrayList(rootNode);
        ParallelCompilerPass.Result result = pass.process(null, rootNode, context);
        List<Node> astNodesAfter = NodeTraversal.deepNodes(rootNode);

        Rewriter rewriter = new Rewriter();
        List<Node> expectedNodes = Lists.newArrayList();

        // Test simplification of constant expression with no side effects
        Node constantExpressionNode = NodeUtil.createBinaryOperator(
            Token.ADD,
            NodeUtil.createNumberLiteral(1),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo"))
        );
        Node simplifiedNode = NodeUtil.createSequenceStatement(
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo")),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
        );
        expectedNodes.add(simplifiedNode);
        rewriter.rewrite(constantExpressionNode, simplifiedNode);
        NodeMatcher.assertNodesEqual(astNodesBefore, astNodesAfter);
        NodeMatcher.assertNodesEqual(expectedNodes, astNodesAfter);

        // Test simplification of constant expression with side effects
        Node sideEffectExpressionNode = NodeUtil.createBinaryOperator(
            Token.ADD,
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo")),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
        );
        expectedNodes.clear();
        expectedNodes.add(NodeUtil.createSequenceStatement(
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo")),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
        ));
        rewriter.rewrite(sideEffectExpressionNode, simplifiedNode);
        NodeMatcher.assertNodesEqual(astNodesBefore, astNodesAfter);
        NodeMatcher.assertNodesEqual(expectedNodes, astNodesAfter);

        // Test simplification of constant expression with no side effects as subexpression
        Node constantExpressionWithSubexpressionNode = NodeUtil.createBinaryOperator(
            Token.ADD,
            NodeUtil.createBinaryOperator(
                Token.ADD,
                NodeUtil.createNumberLiteral(1),
                NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo"))
            ),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
        );
        simplifiedNode = NodeUtil.createSequenceStatement(
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo")),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
        );
        expectedNodes.clear();
        expectedNodes.add(simplifiedNode);
        rewriter.rewrite(constantExpressionWithSubexpressionNode, simplifiedNode);
        NodeMatcher.assertNodesEqual(astNodesBefore, astNodesAfter);
        NodeMatcher.assertNodesEqual(expectedNodes, astNodesAfter);

        // Test simplification of constant expression with side effects as subexpression
        Node sideEffectExpressionWithSubexpressionNode = NodeUtil.createBinaryOperator(
            Token.ADD,
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo")),
            NodeUtil.createBinaryOperator(
                Token.ADD,
                NodeUtil.createNumberLiteral(1),
                NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
            )
        );
        simplifiedNode = NodeUtil.createSequenceStatement(
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("foo")),
            NodeUtil.createFunctionCall(NodeUtil.createIdentifier("bar"))
        );
        expectedNodes.clear();
        expectedNodes.add(simplifiedNode);
        rewriter.rewrite(sideEffectExpressionWithSubexpressionNode, simplifiedNode);
        NodeMatcher.assertNodesEqual(astNodesBefore, astNodesAfter);
        NodeMatcher.assertNodesEqual(expectedNodes, astNodesAfter);

        // Test no simplification of non-constant expression
        Node nonConstantExpressionNode = NodeUtil.createBinaryOperator(
            Token.ADD,
            NodeUtil.createNumberLiteral(1),
            NodeUtil.createIdentifier("x")
        );
        NodeMatcher.assertNodesNotEqual(astNodesBefore, astNodesAfter);

        // Test no simplification of expression with side effects and no constant subexpressions
        Node sideEffectExpressionNode = NodeUtil.createBinaryOperator(
            Token.ADD,
            NodeUtil.createNumberLiteral(1),
            NodeUtil.createIdentifier("x")
        );
        NodeMatcher.assertNodesNotEqual(astNodesBefore, astNodesAfter);
    }

    @Test
    public void testRemoveConstantExpressionsNullInput() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(mock(Compiler.class));
        Node rootNode = null;
        ParallelCompilerPass.Context context = new ParallelCompilerPass.Context();

        List<Node> astNodesBefore = Lists.newArrayList();
        ParallelCompilerPass.Result result = pass.process(null, rootNode, context);
        List<Node> astNodesAfter = NodeTraversal.deepNodes(rootNode);

        assertNull(result.changed);
        NodeMatcher.assertNodesEqual(astNodesBefore, astNodesAfter);
    }
}

class NodeMatcher {
    public static void assertNodesEqual(List<Node> nodesBefore, List<Node> nodesAfter) {
        for (int i = 0; i < nodesBefore.size(); i++) {
            Node nodeBefore = nodesBefore.get(i);
            Node nodeAfter = nodesAfter.get(i);
            assertThat(nodeBefore).isEqualTo(nodeAfter);
        }
        for (int i = nodesAfter.size(); i < nodesBefore.size(); i++) {
            Node nodeBefore = nodesBefore.get(nodesBefore.size() - i);
            assertThat(nodeBefore).isNull();
        }
    }

    public static void assertNodesNotEqual(List<Node> nodesBefore, List<Node> nodesAfter) {
        for (int i = 0; i < nodesBefore.size(); i++) {
            Node nodeBefore = nodesBefore.get(i);
            Node nodeAfter = nodesAfter.get(i);
            if (nodeBefore == nodeAfter) {
                throw new AssertionError("Nodes should not be equal: " + nodeBefore + " != " + nodeAfter);
            }
        }
    }
}

class NodeUtil {
    // Node creation methods
}

class ReportCodeHasChangedListenerTest {
    // Test utility methods for ReportCodeHasChangedListener
}

class RemoveConstantRValuesCallbackTest {
    // Test utility methods for RemoveConstantRValuesCallback
}

class GatherSideEffectSubexpressionsCallbackTest {
    // Test utility methods for GatherSideEffectSubexpressionsCallback
}

class AstChangeProxyTest {
    // Test utility methods for AstChangeProxy
}