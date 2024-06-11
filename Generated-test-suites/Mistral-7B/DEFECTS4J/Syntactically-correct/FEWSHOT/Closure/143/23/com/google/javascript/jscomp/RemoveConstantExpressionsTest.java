package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {
    @Test
    public void testRemoveConstantExpressions() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CompilerPass pass = new RemoveConstantExpressions(compiler);

        // Test case 1: Simple expression statement
        Node root = new Node(Token.PROGRAM, null);
        Node expr1 = new Node(Token.EXPR_STATEMENT, null);
        Node expr1Body = new Node(Token.ADD, null);
        Node left = new Node(Token.NUMBER, 1);
        Node right = new Node(Token.NAME, "foo");
        expr1Body.addChildToEnd(left);
        expr1Body.addChildToEnd(right);
        expr1.addChildToEnd(expr1Body);
        root.addChildToEnd(expr1);

        Result result = new Result();
        NodeTraversal traversal = new NodeTraversal();
        RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
        cb.traverse(traversal, root, null);
        cb.getResult().notifyCompiler(compiler);

        Node simplifiedRoot = result.getTransformedSource();
        Node simplifiedExpr1 = simplifiedRoot.getFirstChild();
        Node simplifiedExpr1Body = simplifiedExpr1.getFirstChild();
        List<Node> expectedSideEffectNodes = Lists.newArrayList(right);
        List<Node> actualSideEffectNodes = Lists.newArrayList(simplifiedExpr1Body.getFirstChild(),
                                                              simplifiedExpr1Body.getLastChild());

        assertThat(simplifiedRoot).isNotNull();
        assertThat(simplifiedExpr1).isNotNull();
        assertThat(simplifiedExpr1Body).isNotNull();
        assertThat(expectedSideEffectNodes).isEqualTo(actualSideEffectNodes);
        assertThat(NodeUtil.nodeTypeMayHaveSideEffects(simplifiedExpr1Body.getFirstChild())).isTrue();
        assertThat(NodeUtil.nodeTypeMayHaveSideEffects(simplifiedExpr1Body.getLastChild())).isTrue();

        // Test case 2: Expression statement with multiple constant subexpressions
        Node root2 = new Node(Token.PROGRAM, null);
        Node expr2 = new Node(Token.EXPR_STATEMENT, null);
        Node expr2Body = new Node(Token.ADD, null);
        Node left2 = new Node(Token.NUMBER, 1);
        Node right2 = new Node(Token.NAME, "foo");
        Node constantExpr = new Node(Token.EXPR_STATEMENT, null);
        Node constantExprBody = new Node(Token.NUMBER, 2);
        expr2Body.addChildToEnd(left2);
        expr2Body.addChildToEnd(right2);
        constantExpr.addChildToEnd(constantExprBody);
        expr2.addChildToEnd(expr2Body);
        expr2.addChildToEnd(constantExpr);
        root2.addChildToEnd(expr2);

        result = new Result();
        cb = new RemoveConstantRValuesCallback();
        cb.traverse(traversal, root2, null);
        cb.getResult().notifyCompiler(compiler);

        Node simplifiedRoot2 = result.getTransformedSource();
        Node simplifiedExpr2 = simplifiedRoot2.getFirstChild();
        Node simplifiedExpr2Body = simplifiedExpr2.getFirstChild();
        Node simplifiedConstantExpr = simplifiedExpr2.getLastChild();

        List<Node> expectedSideEffectNodes2 = Lists.newArrayList(right2);
        List<Node> actualSideEffectNodes2 = Lists.newArrayList(simplifiedExpr2Body.getFirstChild(),
                                                               simplifiedExpr2Body.getLastChild(),
                                                               simplifiedConstantExpr.getFirstChild());

        assertThat(simplifiedRoot2).isNotNull();
        assertThat(simplifiedExpr2).isNotNull();
        assertThat(simplifiedExpr2Body).isNotNull();
        assertThat(simplifiedConstantExpr).isNotNull();
        assertThat(expectedSideEffectNodes2).isEqualTo(actualSideEffectNodes2);
        assertThat(NodeUtil.nodeTypeMayHaveSideEffects(simplifiedExpr2Body.getFirstChild())).isTrue();
        assertThat(NodeUtil.nodeTypeMayHaveSideEffects(simplifiedExpr2Body.getLastChild())).isTrue();
        assertThat(NodeUtil.nodeTypeMayHaveSideEffects(simplifiedConstantExpr.getFirstChild())).isTrue();
    }
}

class NodeUtil {
    static boolean nodeTypeMayHaveSideEffects(Node node) {
        return node.getType() == Token.ASSIGN || node.getType() == Token.CALL ||
               node.getType() == Token.IDENT || node.getType() == Token.LET ||
               node.getType() == Token.VAR || node.getType() == Token.COND;
    }
}

class Util {
    static void assertEquals(Object expected, Object actual) {
        // JUnit 4 equivalent of Truth's assertThat
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }
}

class ReportCodeHasChangedListener {
    // JUnit 4 equivalent of Google's ReportCodeHasChangedListener
    public void nodeRemoved(Node node) {
        // Empty implementation
    }
}

class GatherSideEffectSubexpressionsCallback {
    // JUnit 4 equivalent of Google's GatherSideEffectSubexpressionsCallback
    private Node parent;
    private List<Node> subexpressions;

    GatherSideEffectSubexpressionsCallback(Node p, List<Node> l) {
        this.parent = p;
        this.subexpressions = l;
    }

    void copySideEffectSubexpressions(GatherSideEffectSubexpressionsCallback other) {
        // Empty implementation
    }
}

class CopySideEffectSubexpressions {
    // JUnit 4 equivalent of Google's CopySideEffectSubexpressions
    private Node parent;
    private List<Node> subexpressions;

    CopySideEffectSubexpressions(Node p, List<Node> l) {
        this.parent = p;
        this.subexpressions = l;
    }
}