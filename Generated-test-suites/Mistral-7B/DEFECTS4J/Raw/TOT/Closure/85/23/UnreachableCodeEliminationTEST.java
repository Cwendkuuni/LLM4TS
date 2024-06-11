import static com.google.javascript.jscomp.NodeTraversal.javalite;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    compiler = new CompilerOptions().newCompiler();
    options = compiler.getOptions();
  }

  @Test
  public void testNoUnreachableCode() {
    String jsCode = "function foo() { return 1; }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
  }

  @Test
  public void testRemoveDeadCodeAfterReturn() {
    String jsCode = "function foo() { return 1; alert('unreachable'); }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
    assertTrue(result.getChildCount() == 1);
    assertFalse(result.getLastChild().getChildCount() > 0);
  }

  @Test
  public void testRemoveDeadCodeAfterConditionalStatement() {
    String jsCode = "function foo() { if (true) { return 1; } alert('unreachable'); }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
    assertTrue(result.getChildCount() == 1);
    assertFalse(result.getLastChild().getChildCount() > 0);
  }

  @Test
  public void testRemoveDeadCodeAfterUnconditionalStatement() {
    String jsCode = "function foo() { break; alert('unreachable'); }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
    assertTrue(result.getChildCount() == 0);
  }

  @Test
  public void testRemoveDeadCodeAfterLoop() {
    String jsCode = "function foo() { for (var i = 0; i < 10; i++) { return; alert('unreachable'); } }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
    assertTrue(result.getChildCount() == 0);
  }

  @Test
  public void testRemoveDeadCodeAfterSwitchStatement() {
    String jsCode = "function foo() { switch (1) { case 1: return; default: alert('unreachable'); } }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
    assertTrue(result.getChildCount() == 0);
  }

  @Test
  public void testRemoveDeadCodeAfterTryCatch() {
    String jsCode = "try { return 1; } catch (e) { alert('unreachable'); }";
    Node root = NodeUtil.parseJs(jsCode);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(root, options, compiler);
    traversal.traverse(pass, root);
    Node result = traversal.getCompiledResult();
    assertTrue(NodeUtil.isFunctionNode(result));
    assertTrue(result.getChildCount() == 1);
    assertTrue(result.getLastChild().getType() == Token.RETURN);
  }
}