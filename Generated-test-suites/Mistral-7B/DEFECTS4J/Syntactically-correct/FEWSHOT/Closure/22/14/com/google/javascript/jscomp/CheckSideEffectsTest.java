package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TreeMaker;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

    private static class TestChecker extends CheckSideEffects {
        TestChecker(CheckLevel level, boolean protectSideEffectFreeCode) {
            super(null, level, protectSideEffectFreeCode);
        }
    }

    @Test
    public void testCheckSideEffects() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        TestChecker checker = new TestChecker(level, protectSideEffectFreeCode);

        // Test empty statement
        Node root = TreeMaker.makeStatement(Token.EOF);
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test void statement
        root = TreeMaker.makeStatement(Token.VOID);
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test comment
        root = TreeMaker.makeStatement(TreeMaker.makeComment("/* This is a comment */"));
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test assignment expression
        Node lhs = TreeMaker.makeName("x");
        Node rhs = TreeMaker.makeNumber(42);
        Node statement = TreeMaker.makeStatement(TreeMaker.makeAssignment(lhs, rhs));
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test declaration statement
        root = TreeMaker.makeStatement(TreeMaker.makeDeclaration("x", TreeMaker.makeNumberLiteral(42)));
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test expression statement
        root = TreeMaker.makeStatement(TreeMaker.makeExpressionStatement(rhs));
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test simple assignment statement
        lhs = TreeMaker.makeName("x");
        statement = TreeMaker.makeStatement(TreeMaker.makeAssignment(lhs, rhs));
        root = TreeMaker.makeStatement(statement);
        NodeTraversal.traverse(compiler, root, checker);
        assertTrue(checker.problemNodes.isEmpty());

        // Test expression statement with side effect
        Node sideEffect = TreeMaker.makeStatement(TreeMaker.makeCall(TreeMaker.makeName("console.log"), TreeMaker.makeString("Hello, world!")));
        root = TreeMaker.makeStatement(sideEffect);
        NodeTraversal.traverse(compiler, root, checker);
        List<Node> problemNodes = checker.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(sideEffect, problemNodes.get(0));

        // Test statement with side effect
        root = TreeMaker.makeStatement(TreeMaker.makeCall(TreeMaker.makeName("console.log"), TreeMaker.makeString("Hello, world!")));
        NodeTraversal.traverse(compiler, root, checker);
        problemNodes = checker.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(sideEffect, problemNodes.get(0));

        // Test complex expression statement with side effect
        Node complexExpression = TreeMaker.makeExpression(
            TreeMaker.makeBinaryOperator(
                TreeMaker.makeBinaryOperator(
                    TreeMaker.makeBinaryOperator(
                        TreeMaker.makeMemberExpression(TreeMaker.makeName("document"), "write"),
                        TreeMaker.makeString("Hello, world!")),
                    Token.PLUS,
                    TreeMaker.makeNumber(1)
                ),
                Token.MULTIPLY,
                TreeMaker.makeNumber(2)
            )
        );
        root = TreeMaker.makeStatement(TreeMaker.makeExpressionStatement(complexExpression));
        NodeTraversal.traverse(compiler, root, checker);
        problemNodes = checker.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(complexExpression, problemNodes.get(0));

        // Test complex statement with side effect
        root = TreeMaker.makeStatement(complexExpression);
        NodeTraversal.traverse(compiler, root, checker);
        problemNodes = checker.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(complexExpression, problemNodes.get(0));

        // Test complex expression statement with side effect and protectSideEffectFreeCode = false
        root = TreeMaker.makeStatement(complexExpression);
        checker = new TestChecker(level, false);
        NodeTraversal.traverse(compiler, root, checker);
        problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());

        // Test complex statement with side effect and protectSideEffectFreeCode = false
        root = TreeMaker.makeStatement(TreeMaker.makeCall(TreeMaker.makeName("console.log"), TreeMaker.makeString("Hello, world!")));
        checker = new TestChecker(level, false);
        NodeTraversal.traverse(compiler, root, checker);
        problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());

        // Test expression statement with side effect and protectSideEffectFreeCode = false
        root = TreeMaker.makeStatement(TreeMaker.makeCall(TreeMaker.makeName("console.log"), TreeMaker.makeString("Hello, world!")));
        NodeTraversal.traverse(compiler, root, checker);
        problemNodes = checker.problemNodes;
        assertFalse(problemNodes.isEmpty());
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        TestChecker checker = new TestChecker(level, protectSideEffectFreeCode);
        Node root = TreeMaker.makeStatement(TreeMaker.makeCall(TreeMaker.makeName("console.log"), TreeMaker.makeString("Hello, world!")));
        NodeTraversal.traverse(compiler, root, checker);
        List<Node> problemNodes = checker.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(root, problemNodes.get(0));

        checker.protectSideEffects();

        root = compiler.getRootNode();
        Node protectorFn = null;
        for (Node child : root.getChildren()) {
            if (child.isName() && child.getString().equals("JSCOMPILER_PRESERVE")) {
                protectorFn = child;
                break;
            }
        }
        assertNotNull(protectorFn);

        Node callToProtectorFn = null;
        for (Node child : root.getChildren()) {
            if (child.isCall()) {
                Node target = child.getFirstChild();
                if (target.isName() && target.getString().equals("JSCOMPILER_PRESERVE")) {
                    callToProtectorFn = child;
                    break;
                }
            }
        }
        assertNotNull(callToProtectorFn);

        Node protectedStatement = callToProtectorFn.getLastChild();
        assertEquals(root.getLastChild(), protectedStatement);

        NodeTraversal.traverse(compiler, root, new StripProtection(compiler));

        problemNodes = checker.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }
}