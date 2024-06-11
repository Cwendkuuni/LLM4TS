package org.apache.commons.cli.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEmpty() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    assertNull(root.getLastChild());
  }

  @Test
  public void testSimpleReturn() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = Node.objectLiteral()
        .setProperty("x", Node.numberLiteral(5))
        .setProperty("f", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.RETURN,
            Node.numberLiteral(10)
        ))
        .setProperty("g", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.RETURN,
            Node.IDENTIFIER("f")
        ));
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    Node g = root.getNamedProperty("g");
    Node f = root.getNamedProperty("f");
    Node returnStatement = f.getFirstChild().getFirstChild();
    Node result = returnStatement.getNext();
    assertThat(g.getLastChild().getNext()).isNull();
    assertEquals(result, root.getNamedProperty("g").getLastChild());
  }

  @Test
  public void testSimpleBreak() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = Node.objectLiteral()
        .setProperty("x", Node.numberLiteral(5))
        .setProperty("f", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.LABEL("label"),
            Node.BREAK,
            Node.IDENTIFIER("label")
        ));
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    Node f = root.getNamedProperty("f");
    Node label = f.getFirstChild();
    Node breakStatement = label.getNext();
    Node result = f.getLastChild();
    assertNull(f.getLastChild().getPrevious());
    assertEquals(result, root.getNamedProperty("f").getLastChild());
  }

  @Test
  public void testSimpleContinue() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = Node.objectLiteral()
        .setProperty("x", Node.numberLiteral(5))
        .setProperty("f", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.LABEL("label"),
            Node.CONTINUE,
            Node.IDENTIFIER("label")
        ));
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    Node f = root.getNamedProperty("f");
    Node label = f.getFirstChild();
    Node continueStatement = label.getNext();
    Node result = f.getLastChild();
    assertNull(f.getLastChild().getPrevious());
    assertEquals(result, root.getNamedProperty("f").getLastChild());
  }

  @Test
  public void testRemoveNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    Node root = Node.objectLiteral()
        .setProperty("x", Node.numberLiteral(5))
        .setProperty("f", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.RETURN,
            Node.IDENTIFIER("x")
        ));
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    Node f = root.getNamedProperty("f");
    Node returnStatement = f.getFirstChild();
    Node result = returnStatement.getNext();
    Node x = root.getNamedProperty("x");
    assertNull(f.getLastChild());
    assertEquals(x, f.getLastChild());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = Node.objectLiteral()
        .setProperty("x", Node.numberLiteral(5))
        .setProperty("f", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.LABEL("label1"),
            Node.BREAK,
            Node.IDENTIFIER("label1"),
            Node.LABEL("label2"),
            Node.BREAK,
            Node.IDENTIFIER("label2")
        ));
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    Node f = root.getNamedProperty("f");
    Node label1 = f.getFirstChild();
    Node breakStatement1 = label1.getNext();
    Node label2 = breakStatement1.getNext();
    Node breakStatement2 = label2.getNext();
    Node result = f.getLastChild();
    Node x = root.getNamedProperty("x");
    assertNull(f.getLastChild().getPrevious());
    assertEquals(result, root.getNamedProperty("f").getLastChild());
    assertEquals(x, f.getLastChild());
  }

  @Test
  public void testRemoveUnreachableBlock() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    CompilerPass unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
    Node root = Node.objectLiteral()
        .setProperty("x", Node.numberLiteral(5))
        .setProperty("f", Node.functionLiteral(
            Node.IDENTIFIER("x"),
            Node.BLOCK,
            Node.RETURN,
            Node.IDENTIFIER("x")
        ));
    NodeTraversal traversal = new NodeTraversal(root, new ScopedCallback() {}, unreachableCodeElimination);
    traversal.traverse(root);
    Node f = root.getNamedProperty("f");
    Node block = f.getFirstChild();
    Node returnStatement = block.getLastChild();
    Node result = returnStatement.getNext();
    Node x = root.getNamedProperty("x");
    assertNull(f.getLastChild());
    assertEquals(x, f.getLastChild());
  }
}