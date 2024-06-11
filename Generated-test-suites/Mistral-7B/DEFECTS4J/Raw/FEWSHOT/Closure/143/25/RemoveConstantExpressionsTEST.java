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
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.ReportCodeHasChangedListener;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.ASTChangeProxy;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {
    @Test
    public void testRemoveConstantExpressions() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CompilerPass pass = new RemoveConstantExpressions(compiler);

        Node root = new Node(Token.PROGRAM, null);
        Node expr1 = new Node(Token.EXPR_STATEMENT, null);
        Node add1 = new Node(Token.ADD, null);
        Node constExpr1 = new Node(Token.NUMBER, 1.0);
        Node expr2 = new Node(Token.EXPR_STATEMENT, null);
        Node add2 = new Node(Token.ADD, null);
        Node constExpr2 = new Node(Token.NUMBER, 2.0);
        Node expr3 = new Node(Token.EXPR_STATEMENT, null);
        Node add3 = new Node(Token.ADD, null);
        Node constExpr3 = new Node(Token.NUMBER, 3.0);

        add1.addChildToEnd(constExpr1);
        add2.addChildToEnd(constExpr2);
        add3.addChildToEnd(constExpr3);

        expr1.addChildToEnd(add1);
        expr2.addChildToEnd(add2);
        expr3.addChildToEnd(add3);

        root.addChildToEnd(expr1);
        root.addChildToEnd(expr2);
        root.addChildToEnd(expr3);

        ParallelCompilerPass parallelPass = mock(ParallelCompilerPass.class);
        when(compiler.getParallelCompilerPasses()).thenReturn(Lists.newArrayList(parallelPass));

        RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, root, callback);
        List<Node> replacements = callback.getResult().getReplacements();

        List<Node> expectedReplacements = Lists.newArrayList(
                expr1,
                expr1.getFirstChild(),
                expr2,
                expr2.getFirstChild(),
                expr3);

        assertThat(replacements).isEqualTo(expectedReplacements);
    }

    @Test
    public void testRemoveConstantExpressionsWithSideEffect() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CompilerPass pass = new RemoveConstantExpressions(compiler);

        Node root = new Node(Token.PROGRAM, null);
        Node expr1 = new Node(Token.EXPR_STATEMENT, null);
        Node assign1 = new Node(Token.ASSIGN, null, Lists.newArrayList(
                new Node(Token.IDENT, "foo"),
                new Node(Token.NUMBER, 1.0)
        ));
        Node add1 = new Node(Token.ADD, null);
        Node constExpr1 = new Node(Token.NUMBER, 1.0);

        add1.addChildToEnd(constExpr1);
        expr1.addChildToEnd(assign1);
        expr1.addChildToEnd(add1);

        root.addChildToEnd(expr1);

        ParallelCompilerPass parallelPass = mock(ParallelCompilerPass.class);
        when(compiler.getParallelCompilerPasses()).thenReturn(Lists.newArrayList(parallelPass));

        RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, root, callback);
        List<Node> replacements = callback.getResult().getReplacements();

        List<Node> expectedReplacements = Lists.newArrayList(
                expr1,
                assign1,
                expr1.getFirstChild(),
                expr1.getFirstChild().getFirstChild());

        assertThat(replacements).isEqualTo(expectedReplacements);
    }

    @Test
    public void testRemoveConstantExpressionsEmptyTree() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CompilerPass pass = new RemoveConstantExpressions(compiler);

        Node root = new Node(Token.PROGRAM, null);

        ParallelCompilerPass parallelPass = mock(ParallelCompilerPass.class);
        when(compiler.getParallelCompilerPasses()).thenReturn(Lists.newArrayList(parallelPass));

        RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, root, callback);

        // No changes expected
        assertThat(callback.getResult().getReplacements()).isEmpty();
    }
}