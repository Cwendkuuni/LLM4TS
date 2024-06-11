package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test cases for RemoveConstantExpressions class.
 */
public class RemoveConstantExpressionsTest {
    @Test
    public void testRemoveConstantExpressions() {
        Compiler compiler = mock(Compiler.class);
        RemoveConstantExpressions removeConstantExpressions =
                new RemoveConstantExpressions(compiler);

        Node rootNode = new Node(Token.PROGRAM, null);

        Node expr1 = new Node(Token.EXPR_RESULT, null);
        Node add1 = new Node(Token.ADD, null);
        Node constant1 = new Node(Token.NUMBER, 1);
        Node expr2 = new Node(Token.EXPR_RESULT, null);
        Node foo = new Node(Token.IDENTIFIER, "foo");
        Node add2 = new Node(Token.ADD, null);
        Node constant2 = new Node(Token.NUMBER, 2);
        Node expr3 = new Node(Token.EXPR_RESULT, null);
        Node bar = new Node(Token.IDENTIFIER, "bar");

        add1.addChild(constant1);
        add1.addChild(expr2);
        expr1.addChild(add1);

        add2.addChild(foo);
        add2.addChild(expr3);
        expr2.addChild(add2);

        rootNode.addChild(expr1);

        RemoveConstantRValuesCallback cb = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, rootNode, cb);

        List<Node> expectedReplacements = Lists.newArrayList(
                foo, bar
        );

        List<Node> actualReplacements = cb.getResult().getReplacements();

        assertThat(actualReplacements).isEqualTo(expectedReplacements);
    }

    @Test
    public void testRemoveConstantExpressionsWithSideEffectSubexpressions() {
        Compiler compiler = mock(Compiler.class);
        RemoveConstantExpressions removeConstantExpressions =
                new RemoveConstantExpressions(compiler);

        Node rootNode = new Node(Token.PROGRAM, null);

        Node expr1 = new Node(Token.EXPR_RESULT, null);
        Node add1 = new Node(Token.ADD, null);
        Node constant1 = new Node(Token.NUMBER, 1);
        Node expr2 = new Node(Token.EXPR_RESULT, null);
        Node foo = new Node(Token.IDENTIFIER, "foo");
        Node assign1 = new Node(Token.ASSIGN, null);
        Node lhs1 = new Node(Token.IDENTIFIER, "x");
        Node rhs1 = new Node(Token.NUMBER, 5);
        Node add2 = new Node(Token.ADD, null);
        Node constant2 = new Node(Token.NUMBER, 2);
        Node expr3 = new Node(Token.EXPR_RESULT, null);
        Node bar = new Node(Token.IDENTIFIER, "bar");
        Node assign2 = new Node(Token.ASSIGN, null);
        Node lhs2 = new Node(Token.IDENTIFIER, "y");
        Node rhs2 = new Node(Token.NUMBER, 7);

        add1.addChild(constant1);
        add1.addChild(expr2);
        expr1.addChild(add1);

        add2.addChild(foo);
        add2.addChild(expr3);
        expr2.addChild(add2);

        assign1.addChild(lhs1);
        assign1.addChild(rhs1);
        foo.addChild(assign1);

        add2 = new Node(Token.ADD, null);
        add2.addChild(expr3);
        add2.addChild(bar);
        expr3.addChild(add2);

        rootNode.addChild(expr1);

        cb = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, rootNode, cb);

        List<Node> expectedReplacements = Lists.newArrayList(
                        foo, bar,
                        lhs1, rhs1,
                        lhs2, rhs2
                );

        List<Node> actualReplacements = cb.getResult().getReplacements();

        assertThat(actualReplacements).isEqualTo(expectedReplacements);
    }

    @Test
    public void testRemoveConstantExpressionsNoSideEffectSubexpressions() {
        Compiler compiler = mock(Compiler.class);
        RemoveConstantExpressions removeConstantExpressions =
                new RemoveConstantExpressions(compiler);

        Node rootNode = new Node(Token.PROGRAM, null);

        Node expr1 = new Node(Token.EXPR_RESULT, null);
        Node add1 = new Node(Token.ADD, null);
        Node constant1 = new Node(Token.NUMBER, 1);
        Node expr2 = new Node(Token.EXPR_RESULT, null);
        Node foo = new Node(Token.IDENTIFIER, "foo");
        Node add2 = new Node(Token.ADD, null);
        Node constant2 = new Node(Token.NUMBER, 2);
        Node expr3 = new Node(Token.EXPR_RESULT, null);
        Node bar = new Node(Token.IDENTIFIER, "bar");

        add1.addChild(constant1);
        add1.addChild(expr2);
        expr1.addChild(add1);

        add2.addChild(foo);
        add2.addChild(expr3);
        expr2.addChild(add2);

        rootNode.addChild(expr1);

        cb = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, rootNode, cb);

        List<Node> expectedReplacements = Lists.newArrayList();
        List<Node> actualReplacements = cb.getResult().getReplacements();

        assertThat(actualReplacements).isEqualTo(expectedReplacements);
    }

    @Test
    public void testRemoveConstantExpressionsWithNullInput() {
        Compiler compiler = mock(Compiler.class);
        RemoveConstantExpressions removeConstantExpressions =
                new RemoveConstantExpressions(compiler);

        Node rootNode = null;

        cb = new RemoveConstantRValuesCallback();
        NodeTraversal.traverse(null, rootNode, cb);

        assertThat(cb.getResult().changed).isFalse();
    }
}