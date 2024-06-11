package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckSideEffectsTest {

  private Compiler compiler;
  private CheckSideEffects checkSideEffects;

  @Test
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testProcess() {
    String source = "function foo() { return 1; }";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
  }

  @Test
  public void testProcessWithWarning() {
    String source = "function foo() { return 1; } var x = foo();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNotNull(warning);
    assertThat(warning.getString()).contains("Suspicious code. This code lacks side-effects. Is there a bug?");
  }

  @Test
  public void testProcessWithProtectSideEffectFreeCode() {
    String source = "function foo() { return 1; } var x = foo();JSCOMPILER_PRESERVE(x);";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testHotSwapScript() {
    String source = "function foo() { return 1; } var x = foo();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects.hotSwapScript(null, root);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNotNull(warning);
    checkSideEffects.protectSideEffects();
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testVisit() {
    String source = "function foo() { return 1; } var x = foo();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNotNull(warning);
  }

  @Test
  public void testVisitWithProtectSideEffectFreeCode() {
    String source = "function foo() { return 1; } var x = foo();JSCOMPILER_PRESERVE(x);";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testVisitWithVoidStatement() {
    String source = "function foo() { return 1; } var x = foo();;";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testVisitWithStringLiteral() {
    String source = "function foo() { return 1; } 'this string is continued on the next line but you forgot the +'";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNotNull(warning);
    assertThat(warning.getString()).contains("Is there a missing '+' on the previous line?");
  }

  @Test
  public void testVisitWithSimpleOperator() {
    String source = "function foo() { return 1; } var x = foo() > 0;";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNotNull(warning);
  }

  @Test
  public void testVisitWithForStatement() {
    String source = "for (var i = 0; i < 10; i++) {}";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testVisitWithCallExpression() {
    String source = "eval('for (var i = 0; i < 10; i++) {}');";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testVisitWithExprResult() {
    String source = "function foo() { return 1; } var x = foo();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testVisitWithStatementExpression() {
    String source = "function foo() { return 1; } var x = foo();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node warning = getFirstWarning(root);
    assertNull(warning);
  }

  @Test
  public void testProtectSideEffects() {
    String source = "function foo() { return 1; } var x = foo();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.protectSideEffects();
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node call = getCallExpression(root, PROTECTOR_FN);
    assertNotNull(call);
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    call = getCallExpression(root, PROTECTOR_FN);
    assertNotNull(call);
  }

  @Test
  public void testStripProtection() {
    String source = "function foo() { return 1; } JSCOMPILER_PRESERVE(function foo() { return 1; })();";
    Node root = NodeOperations.parseJs(source);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeTraversal.traverse(compiler, root, new StripProtection(compiler));
    NodeTraversal.traverse(compiler, root, new NodePrintingVisitor());
    Node call = getCallExpression(root, PROTECTOR_FN);
    assertNull(call);
  }

  private Node getCallExpression(Node root, String name) {
    return NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getFirstChild().isName() && n.getFirstChild().getString().equals(name)) {
          return;
        }
        t.traverseChildren(n);
      }

      @Override
      public boolean shouldTraverseChildren(Node n) {
        return true;
      }
    }).getLastVisitorValue();
  }

  private Node getFirstWarning(Node root) {
    Node warning = null;
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ERROR) {
          warning = n;
          return;
        }
        t.traverseChildren(n);
      }

      @Override
      public boolean shouldTraverseChildren(Node n) {
        return true;
      }
    }).getLastVisitorValue();
    return warning;
  }

  private static class NodePrintingVisitor extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isError()) {
        System.out.println("Error: " + n.getString());
      } else if (n.isWarning()) {
        System.out.println("Warning: " + n.getString());
      } else {
        System.out.println(NodeUtil.printNode(n));
      }
      t.traverseChildren(n);
    }

    @Override
    public boolean shouldTraverseChildren(Node n) {
      return true;
    }
  }

  private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";
}