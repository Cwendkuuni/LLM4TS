import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.enterScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.exitScope;
import static com.google.javascript.jscomp.UnreachableCodeElimination.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private UnreachableCodeElimination eliminator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    eliminator = new UnreachableCodeElimination(compiler, false);
  }

  @Test
  public void testSimpleReturn() {
    Node root = createNode("function test() { return; }");;
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("", root.toString());
  }

  @Test
  public void testSimpleReturnWithComment() {
    Node root = createNode("function test() { // this is a comment return; }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("", root.toString());
  }

  @Test
  public void testDeadCodeBeforeReturn() {
    Node root = createNode("function test() { alert('unreachable'); return; }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("return;", root.toString());
  }

  @Test
  public void testDeadCodeAfterReturn() {
    Node root = createNode("function test() { return; alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("return;", root.toString());
  }

  @Test
  public void testDeadCodeInsideBlock() {
    Node root = createNode("function test() { if (true) { alert('unreachable'); } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideIfStatement() {
    Node root = createNode("function test() { if (true) { alert('unreachable'); } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideIfElseStatement() {
    Node root = createNode("function test() { if (true) { alert('unreachable'); } else { return; } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideSwitchStatement() {
    Node root = createNode("function test() { switch (1) { case 1: alert('unreachable'); } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testDeadCodeInsideWhileStatement() {
    Node root = createNode("function test() { while (true) { alert('unreachable'); } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    fail("While loop with unreachable code should have thrown an exception.");
  }

  @Test(expected = RuntimeException.class)
  public void testDeadCodeInsideDoWhileStatement() {
    Node root = createNode("function test() { do { alert('unreachable'); } while (true); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    fail("Do-while loop with unreachable code should have thrown an exception.");
  }

  @Test
  public void testDeadCodeInsideForStatement() {
    Node root = createNode("function test() { for (int i = 0; true; i++) { alert('unreachable'); } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideForEachStatement() {
    Node root = createNode("function test() { for (int i : []) { alert('unreachable'); } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideTryStatement() {
    Node root = createNode("function test() { try { alert('unreachable'); } finally { return; } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideTryCatchStatement() {
    Node root = createNode("function test() { try { alert('unreachable'); } catch (Exception e) { return; } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideTryFinallyStatement() {
    Node root = createNode("function test() { try { alert('unreachable'); } finally { return; } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideTryCatchFinallyStatement() {
    Node root = createNode("function test() { try { alert('unreachable'); } catch (Exception e) { return; } finally { return; } }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideSwitchStatementWithDefaultCase() {
    Node root = createNode("function test() { switch (1) { case 1: alert('unreachable'); } default: return;}}");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideSwitchStatementWithNoDefaultCase() {
    Node root = createNode("function test() { switch (1) { case 1: alert('unreachable'); }}");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    fail("Switch statement with no default case and unreachable code should have thrown an exception.");
  }

  @Test
  public void testDeadCodeInsideVariableDeclarationStatement() {
    Node root = createNode("function test() { var x = 1; alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { var x = 1; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideExpressionStatement() {
    Node root = createNode("function test() { 1 + alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideAssignmentStatement() {
    Node root = createNode("function test() { x = 1 + alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideConditionalExpressionStatement() {
    Node root = createNode("function test() { true ? alert('unreachable') : 1; }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsidePostfixExpressionStatement() {
    Node root = createNode("function test() { 1++ + alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsidePrefixExpressionStatement() {
    Node root = createNode("function test() { ++1 + alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideCallExpressionStatement() {
    Node root = createNode("function test() { alert('unreachable').toString(); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideMethodCallExpressionStatement() {
    Node root = createNode("function test() { someFunction().toString(); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideNewExpressionStatement() {
    Node root = createNode("function test() { new Object().toString(); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideReturnStatementWithExpression() {
    Node root = createNode("function test() { return 1 + alert('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() { return 1; }\n", root.toString());
  }

  @Test
  public void testDeadCodeInsideThrowStatement() {
    Node root = createNode("function test() { throw new Error('unreachable'); }");
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    enterScope(traversal);
    eliminator.process(null, root);
    exitScope(traversal);

    assertEquals("function test() {}\n", root.toString());
  }

  private Node createNode(String code) {
    return NodeUtil.parseJavaScript(code);
  }
}