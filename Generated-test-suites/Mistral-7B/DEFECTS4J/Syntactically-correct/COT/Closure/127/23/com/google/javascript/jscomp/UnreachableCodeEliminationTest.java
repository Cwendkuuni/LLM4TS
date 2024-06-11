import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractShallowCallback;
import com.google.javascript.jscomp.AbstractCompiler.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.ControlFlowGraph.DiGraphEdge;
import com.google.javascript.jscomp.ControlFlowGraph.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private static final Logger logger =
      Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, false); // Set removeNoOpStatements to false

    Node rootNode = createTestNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testUnreachableCodeEliminationWithRemoveNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, true); // Set removeNoOpStatements to true

    Node rootNode = createTestNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testDeadCodeAfterReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, false);

    Node rootNode = createDeadCodeAfterReturnNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testDeadCodeAfterReturnWithRemoveNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = createDeadCodeAfterReturnNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testDeadCodeWithNoSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, false);

    Node rootNode = createDeadCodeWithNoSideEffectsNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testDeadCodeWithNoSideEffectsWithRemoveNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, true);

    Node rootNode = createDeadCodeWithNoSideEffectsNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testBreakStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, false);

    Node rootNode = createBreakStatementNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testContinueStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, false);

    Node rootNode = createContinueStatementNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  @org.junit.Test
  public void testReturnStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass =
        new UnreachableCodeElimination(compiler, false);

    Node rootNode = createReturnStatementNode();
    Node externs = createTestExterns();
    pass.process(externs, rootNode);
    assertCodeChange(rootNode);
  }

  private Node createTestNode() {
    Node rootNode = new Node(Token.PROGRAM);
    Node a = newNode(Token.NAME, "a");
    Node b = newNode(Token.NAME, "b");
    rootNode.appendChild(new Node(Token.VAR, null, "x", null, null));
    rootNode.appendChild(new Node(Token.BLOCK, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y")),
        new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.NAME, "y")),
        new Node(Token.RETURN, null, null, null),
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y"))
    )));
    return rootNode;
  }

  private Node createTestExterns() {
    return new Node(Token.PROGRAM);
  }

  private Node createDeadCodeAfterReturnNode() {
    Node rootNode = new Node(Token.PROGRAM);
    Node a = newNode(Token.NAME, "a");
    Node b = newNode(Token.NAME, "b");
    rootNode.appendChild(new Node(Token.VAR, null, "x", null, null));
    rootNode.appendChild(new Node(Token.BLOCK, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y")),
        new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.NAME, "y")),
        new Node(Token.RETURN, null, null, null),
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y")),
        new Node(Token.IF, null, null, newNode(Token.NAME, "false"), null,
            new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "z"), newNode(Token.NAME, "y"))))));
    return rootNode;
  }

  private Node createDeadCodeWithNoSideEffectsNode() {
    Node rootNode = new Node(Token.PROGRAM);
    Node a = newNode(Token.NAME, "a");
    Node b = newNode(Token.NAME, "b");
    rootNode.appendChild(new Node(Token.VAR, null, "x", null, null));
    rootNode.appendChild(new Node(Token.BLOCK, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y")),
        new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.NAME, "y")),
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y")),
        new Node(Token.RETURN, null, null, null),
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y"))
    )));
    return rootNode;
  }

  private Node createBreakStatementNode() {
    Node rootNode = new Node(Token.PROGRAM);
    Node label = newNode(Token.LABEL, "label");
    Node ifNode = new Node(Token.IF, null, null, newNode(Token.NAME, "x"), null, new Node(Token.BREAK, null, null, label));
    rootNode.appendChild(ifNode);
    rootNode.appendChild(new Node(Token.BLOCK, null, Lists.newArrayList(
        new Node(Token.VAR, null, "x", null, null),
        new Node(Token.BLOCK, null, Lists.newArrayList(
            new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1)),
            new Node(Token.BREAK, null, null, label),
            new Node(Token.RETURN, null, null, null)
        ))
    )));
    return rootNode;
  }

  private Node createContinueStatementNode() {
    Node rootNode = new Node(Token.PROGRAM);
    Node label = newNode(Token.LABEL, "label");
    rootNode.appendChild(new Node(Token.VAR, null, "x", null, null));
    rootNode.appendChild(new Node(Token.BLOCK, null, Lists.newArrayList(
        new Node(Token.BLOCK, null, Lists.newArrayList(
            new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1)),
            new Node(Token.IF, null, null, newNode(Token.NAME, "x"), null, new Node(Token.RETURN, null, null, null)),
            new Node(Token.CONTINUE, null, null, label),
            new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 2))
        )),
        new Node(Token.BLOCK, null, Lists.newArrayList(label))
    )));
    return rootNode;
  }

  private Node createReturnStatementNode() {
    Node rootNode = new Node(Token.PROGRAM);
    Node a = newNode(Token.NAME, "a");
    Node b = newNode(Token.NAME, "b");
    rootNode.appendChild(new Node(Token.VAR, null, "x", null, null));
    rootNode.appendChild(new Node(Token.BLOCK, null, Lists.newArrayList(
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y")),
        new Node(Token.ASSIGN, null, null, newNode(Token.NAME, "x"), newNode(Token.NAME, "y")),
        new Node(Token.RETURN, null, null, newNode(Token.NAME, "x")),
        new Node(Token.ASSIGN, null, null, a, newNode(Token.NAME, "y"))
    )));
    return rootNode;
  }

  private void assertCodeChange(Node node) {
    assertTrue("Node should have changed", pass.codeChanged);
  }
}