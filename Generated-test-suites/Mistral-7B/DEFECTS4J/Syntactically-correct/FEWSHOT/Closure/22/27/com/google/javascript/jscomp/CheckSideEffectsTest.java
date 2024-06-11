package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.Traverse;
import java.util.List;

public class CheckSideEffectsTest {
  private final TestCompiler compiler = new TestCompiler();

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    String source = "function foo() {} var x = 1; x = foo();";

    Node root = compiler.parse(source, null);
    checker.process(null, root);
    List<Node> problems = checker.problemNodes;

    Traverse.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == problems.get(0)) {
          assertTrue(n.getJSDocInfo() != null);
          return;
        }
        if (n.isString() && n.getString().equals("foo")) {
          assertFalse(NodeUtil.isExpressionResultUsed(n));
          return;
        }
        if (n.isName() && n.getString().equals("x")) {
          assertTrue(NodeUtil.isExpressionResultUsed(n));
          return;
        }
        if (n.isAssign()) {
          Node lhs = n.getLhs();
          Node rhs = n.getRhs();
          if (lhs.isName() && lhs.getString().equals("x") && rhs == problems.get(0)) {
            assertTrue(NodeUtil.isExpressionResultUsed(lhs));
            return;
          }
        }
      }
    });

    // Check that there is only one warning
    assertEquals(1, problems.size());

    // Check warning message
    Node warning = problems.get(0);
    String expectedMessage = "This code lacks side-effects. Is there a bug?";
    assertEquals(expectedMessage, warning.getError().getMessage());
  }

  @Test
  public void testProtectSideEffects() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String source = "function foo() {} var x = 1; x = foo();";

    Node root = compiler.parse(source, null);
    checker.process(null, root);

    // Protect side-effect free statements
    checker.protectSideEffects();

    String protectedSource = "function PROTECTOR_FN(x) {" +
        "  var __temp__ = x;" +
        "  return __temp__;" +
        "}" +
        source;

    Node protectedRoot = compiler.parse(protectedSource, null);
    Traverse.traverse(compiler, protectedRoot, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getFirstChild().isName() && n.getFirstChild().getString().equals(CheckSideEffects.PROTECTOR_FN)) {
          Node argument = n.getArgument(0);
          if (argument.isAssign()) {
            Node lhs = argument.getLhs();
            Node rhs = argument.getRhs();
            if (lhs.isName() && lhs.getString().equals("x")) {
              assertEquals(1, lhs.getChildren().size());
              Node protectedExpression = lhs.getChildren().get(0);
              assertEquals("x", protectedExpression.getParent().getParent().getLastChild().getString());
              assertEquals("PROTECTOR_FN", protectedExpression.getString());
              return;
            }
          }
        }
      }
    });
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);

    String protectedSource = "function PROTECTOR_FN(x) {" +
        "  var __temp__ = x;" +
        "  return __temp__;" +
        "}" +
        "function foo() {} var x = 1; x = PROTECTOR_FN(foo());";

    Node protectedRoot = compiler.parse(protectedSource, null);
    checker.process(null, protectedRoot);
    checker.protectSideEffects();

    Node rootAfterProtection = protectedRoot;

    Traverse.traverse(compiler, rootAfterProtection, stripper);

    String strippedSource = rootAfterProtection.toString();
    Node strippedRoot = compiler.parse(strippedSource, null);

    // Check that the side-effect free statement has been restored
    Traverse.traverse(compiler, strippedRoot, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isAssign() && n.getLhs().isName() && n.getLhs().getString().equals("x")) {
          Node rhs = n.getRhs();
          if (rhs.isName() && rhs.getString().equals("foo")) {
            assertTrue(NodeUtil.isExpressionResultUsed(n.getLhs()));
            return;
          }
        }
      }
    });
  }
}