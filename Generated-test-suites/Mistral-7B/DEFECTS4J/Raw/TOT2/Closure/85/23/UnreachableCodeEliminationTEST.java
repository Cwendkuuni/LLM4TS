package com.google.javascript.jscomp.tests;

import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Root;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testSimpleUnreachableCode() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { return; alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testUnreachableCodeInConditional() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { if (true) { return; alert('unreachable'); } }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testUnreachableCodeInLoop() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { for (;;) { return; alert('unreachable'); } }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testUnreachableCodeInFunctionCall() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { return bar(); alert('unreachable'); } function bar() {}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testUnreachableCodeInTryCatch() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { try { return; } catch (e) { alert('unreachable'); } }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testUnreachableCodeWithFunctionsInsideBlock() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { if (true) { function bar() {} return; } alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeInsideConditional() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { if (true) { return; } return; alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    Node alertNode = t.getFirstChildOfType(Node.ALERT);
    assertNotNull(alertNode);
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeInsideLoop() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { for (;;) { return; } return; alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    Node alertNode = t.getFirstChildOfType(Node.ALERT);
    assertNotNull(alertNode);
  }

  @Test
  public void testNoOpStatements() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { a.b.MyClass.prototype.propertyName; }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, false).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertEquals(0, t.getChildCount());
  }

  @Test
  public void testUnreachableCodeAfterReturn() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { return; alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testBreakStatement() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { for (int i = 0; i < 10; ++i) { if (i == 3) break; } alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testContinueStatement() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { for (int i = 0; i < 10; ++i) { if (i % 2 == 0) continue; alert('unreachable'); } }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  @Test
  public void testReturnStatement() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    String jsCode = "function foo() { if (true) { return; } alert('unreachable'); }";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);

    NodeTraversal t = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    assertNull(t.getFirstChildOfType(Node.ALERT));
  }

  private static NodeUtil NodeUtil = new NodeUtil();

  private static class TestCallback extends NodeTraversal.AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (parent != null) {
        assertFalse("Unexpected node type: " + n.getType(), n.getType() == Token.ALERT);
      }
    }
  }
}