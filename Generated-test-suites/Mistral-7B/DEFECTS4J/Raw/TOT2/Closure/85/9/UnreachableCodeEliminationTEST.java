import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope. Root;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testUnreachableCodeElimination_SimpleReturn() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('unreachable');" +
        "}";

    Node rootNode = NodeUtil.parseJs(input);

    elimination.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, elimination);
    assertTrue(traversal.getRoot().getFirstChild().getType() == Token.RETURN);
  }

  @Test
  public void testUnreachableCodeElimination_EmptyBlock() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  {" +
        "  }" +
        "  alert('unreachable');" +
        "}";

    Node rootNode = NodeUtil.parseJs(input);

    elimination.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, elimination);
    assertTrue(traversal.getRoot().getFirstChild().getType() == Token.EMPTY);
  }

  @Test
  public void testUnreachableCodeElimination_NoOpStatement() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  true;" +
        "  alert('unreachable');" +
        "}";

    Node rootNode = NodeUtil.parseJs(input);

    elimination.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, elimination);
    assertTrue(traversal.getRoot().getFirstChild().getType() == Token.EMPTY);
  }

  @Test
  public void testUnreachableCodeElimination_ConditionalBranch() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    alert('unreachable');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(input);

    elimination.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, elimination);
    assertTrue(traversal.getRoot().getFirstChild().getType() == Token.TYPE);
  }

  @Test
  public void testUnreachableCodeElimination_ExceptionHandling() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  try {" +
        "    throw new Error('foo');" +
        "  } catch (e) {" +
        "    alert('catch');" +
        "  }" +
        "  alert('unreachable');" +
        "}";

    Node rootNode = NodeUtil.parseJs(input);

    elimination.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, elimination);
    assertTrue(traversal.getRoot().getFirstChild().getType() == Token.TRY);
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeElimination_UnreachableCodeWithSideEffect() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    String input = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  document.write('Hello World')"; +
        "}";

    Node rootNode = NodeUtil.parseJs(input);

    elimination.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, elimination);
  }
}