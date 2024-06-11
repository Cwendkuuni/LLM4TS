package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function foo() {\n" +
        "  return 1;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var x = 1;\n" +
        "  x = foo();\n" +
        "}\n" +
        "function baz() {\n" +
        "  var x = 1;\n" +
        "  x;\n" +
        "}\n" +
        "function qux() {\n" +
        "  return 1;\n" +
        "}\n" +
        "function quux() {\n" +
        "  return 1;\n" +
        "}\n" +
        "function main() {\n" +
        "  foo();; // probably just a stray-semicolon. Doesn't hurt to check though\n" +
        "  bar();\n" +
        "  baz();\n" +
        "}\n" +
        "main();";

    CompilerInput input = new CompilerInput(sourceCode, "Test");
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.VERBOSE, false);

    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;

    Node fooCall = NodeUtil.findFirstDescendantByName(root, "foo");
    Node fooCallParent = fooCall.getParent();
    assertTrue(problemNodes.contains(fooCallParent));

    Node barAssign = NodeUtil.findFirstDescendantByName(root, "x =");
    assertTrue(problemNodes.contains(barAssign));

    Node bazAssign = NodeUtil.findFirstDescendantByName(root, "x;");
    assertTrue(problemNodes.contains(bazAssign));

    Node mainCall = NodeUtil.findFirstDescendantByName(root, "main");
    Node mainCallParent = mainCall.getParent();
    assertNull(problemNodes.contains(mainCallParent));

    List<Node> statements = Lists.newArrayList(NodeUtil.findAllDescendantsByType(root, Token.STATEMENT));
    for (Node statement : statements) {
      if (statement.isExpressionStatement()) {
        Node expression = statement.getExpression();
        if (NodeUtil.isSimpleOperatorType(expression.getType())) {
          assertTrue(problemNodes.contains(statement.getParent()));
        }
      }
    }
  }

  @Test
  public void testProtectSideEffects() {
    String sourceCode = "function foo() {\n" +
        "  return 1;\n" +
        "}\n" +
        "function main() {\n" +
        "  foo();\n" +
        "  JSCOMPILER_PRESERVE(foo());\n" +
        "}\n" +
        "main();";

    CompilerInput input = new CompilerInput(sourceCode, "Test");
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.VERBOSE, true);

    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input, root);
    checkSideEffects.protectSideEffects();

    List<Node> statements = Lists.newArrayList(NodeUtil.findAllDescendantsByType(root, Token.STATEMENT));
    for (Node statement : statements) {
      if (statement.isExpressionStatement()) {
        Node expression = statement.getExpression();
        if (expression.isCall() && NodeUtil.findFirstDescendantByName(expression, "JSCOMPILER_PRESERVE").isFunction()) {
          assertTrue(statement.getParent().getChildCount() > 1);
          assertTrue(statement.getParent().getLastChild().isExpressionStatement());
          assertTrue(statement.getParent().getLastChild().getExpression().isName());
          assertTrue(statement.getParent().getLastChild().getExpression().getString().equals("foo"));
        }
      }
    }

    CompilerPass stripProtection = new StripSideEffects.StripProtection(compiler);
    stripProtection.process(input, root);

    root = input.getAstRoot(compiler);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.VERBOSE, true);
    checkSideEffects.process(input, root);

    int problemNodesSizeBefore = checkSideEffects.problemNodes.size();
    checkSideEffects.protectSideEffects();
    int problemNodesSizeAfter = checkSideEffects.problemNodes.size();

    assertTrue(problemNodesSizeBefore > problemNodesSizeAfter);
  }
}

class StripSideEffects {
  static final class StripProtection extends AbstractPostOrderCallback implements CompilerPass {

    private final AbstractCompiler compiler;

    StripProtection(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal.traverse(compiler, root, this);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall()) {
        Node target = n.getFirstChild();
        if (target.isName() && target.getString().equals("JSCOMPILER_PRESERVE")) {
          n.detachChildren();
        }
      }
    }
  }
}