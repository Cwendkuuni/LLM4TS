import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.Parser;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  return;"
        + "  alert('unreachable');"
        + "}"
        + "alert('reachable');"
        + "}";

    Parser parser = new Parser();
    Node root = parser.parse(jsCode, null, null);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(null, root);

    Node result = root;
    assertTrue(result.toString().contains("alert('reachable')"));
    assertFalse(result.toString().contains("alert('unreachable')"));

    jsCode = "function foo() {"
        + "if (true) {"
        + "  return;"
        + "  alert('unreachable');"
        + "}"
        + "return 1;"
        + "}";

    root = parser.parse(jsCode, null, null);

    compiler = new AbstractCompiler();
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(null, root);

    result = root;
    assertTrue(result.toString().contains("return 1"));
    assertFalse(result.toString().contains("alert('unreachable')"));

    jsCode = "function foo() {"
        + "if (true) {"
        + "  return;"
        + "}"
        + "return 1;"
        + "}";

    root = parser.parse(jsCode, null, null);

    compiler = new AbstractCompiler();
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    result = root;
    assertTrue(result.toString().contains("return 1"));
    assertFalse(result.toString().contains("if (true)"));

    jsCode = "function foo() {"
        + "return; alert('unreachable');"
        + "}";

    root = parser.parse(jsCode, null, null);

    compiler = new AbstractCompiler();
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(null, root);

    result = root;
    assertTrue(result.isFunction());
    assertTrue(result.getLastChild().isReturn());
    assertFalse(result.toString().contains("alert('unreachable')"));

    jsCode = "function foo() {"
        + "return; alert('unreachable');"
        + "return 1;"
        + "}";

    root = parser.parse(jsCode, null, null);

    compiler = new AbstractCompiler();
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    result = root;
    assertTrue(result.toString().contains("return 1"));
    assertFalse(result.toString().contains("alert('unreachable')"));

    jsCode = "function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) {"
        + "    continue;"
        + "  }"
        + "}"
        + "alert('unreachable');"
        + "}";

    root = parser.parse(jsCode, null, null);

    compiler = new AbstractCompiler();
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    unreachableCodeElimination.process(null, root);

    result = root;
    assertTrue(result.toString().contains("for (var i = 0; i < 10; i++)"));
    assertFalse(result.toString().contains("alert('unreachable')"));
  }

  @Test
  public void testUnreachableCodeEliminationRemoveNoOpStatements() {
    String jsCode = "function foo() {"
        + "return 1;"
        + "alert('unreachable');"
        + "}";

    Parser parser = new Parser();
    Node root = parser.parse(jsCode, null, null);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    Node result = root;
    assertTrue(result.toString().contains("return 1"));
    assertFalse(result.toString().contains("alert('unreachable')"));
  }
}

class NodeUtil {
  public static boolean isForIn(Node parent) {
    return parent.isStatement() && parent.getFirstChild().isToken()
        && parent.getFirstChild().asToken().getType() == Token.FOR_IN;
  }

  public static boolean mayHaveSideEffects(Node n, AbstractCompiler compiler) {
    // This should be replaced with a proper analysis.
    return n.isStatement() && (n.getType() == Token.ASSIGN
        || n.getFirstChild().isExpressionStatement()
        || n.isCall() && n.getFirstChild().isFunctionCall());
  }

  public static void maybeAddFinally(Node tryNode) {
    // This should be replaced with a proper analysis.
    if (tryNode.isTry() && tryNode.getLastChild().isBlock()
        && tryNode.getLastChild().getLastChild().isKeyword("finally")) {
      tryNode.getLastChild().setParent(null);
    }
  }

  public static Node removeNode(Node parent, Node node) {
    NodeUtil.redeclareVarsInsideBranch(node);
    return NodeUtil.removeChild(parent, node);
  }

  public static void redeclareVarsInsideBranch(Node node) {
    // This logic should be improved, for example
    // it doesn't handle redeclarations in loops.
    if (node.isBlock()) {
      for (int i = 0; i < node.getNumChildren(); i++) {
        Node child = node.getChild(i);
        if (child.isVar()) {
          child.setParent(node.getParent());
          Node parent = child.getParent();
          Node prev = parent.getChild(parent.getIndexOfChild(child) - 1);
          if (prev == null || prev.isStatement()) {
            prev = NodeUtil.addStatementBefore(prev, child);
            prev.setParent(parent);
          }
          child.setParent(prev);
        }
      }
    }
  }

  public static Node addStatementBefore(Node node, Node statement) {
    Node parent = node.getParent();
    Node newStatement = new Node(Token.BLOCK, null);
    newStatement.setParent(parent);
    newStatement.addChild(statement);
    parent.addChildBefore(newStatement, node);
    return newStatement;
  }

  public static Node removeChild(Node parent, Node node) {
    int index = parent.getIndexOfChild(node);
    if (index == -1) {
      throw new IllegalStateException("Node not a child of the parent.");
    }
    parent.removeChild(index);
    return parent;
  }
}