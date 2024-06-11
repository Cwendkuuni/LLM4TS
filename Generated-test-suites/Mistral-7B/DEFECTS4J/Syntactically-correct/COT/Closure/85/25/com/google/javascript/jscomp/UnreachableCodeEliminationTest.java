import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private UnreachableCodeElimination unreachableCodeElimination;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, false);
  }

  @org.junit.Test
  public void testEmptyMethod() {
    Node root = createNode(Token.SCRIPT);
    unreachableCodeElimination.process(null, root);
    assertEquals(0, root.getChildNodes().size());
  }

  @org.junit.Test
  public void testFunctionMethod() {
    Node root = createNode(Token.FUNCTION);
    unreachableCodeElimination.process(null, root);
    assertEquals(0, root.getChildNodes().size());
  }

  @org.junit.Test
  public void testRemoveDeadCodeFollowingReturnStatement() {
    Node root = createNode(Token.BLOCK);
    Node returnStatement = createNode(Token.RETURN);
    Node statementBeforeReturn = createNode(Token.PRINT, "return value");
    root.addChildToEnd(statementBeforeReturn);
    root.addChildToEnd(returnStatement);
    unreachableCodeElimination.process(null, root);
    assertEquals(1, root.getChildNodes().size());
    assertEquals(Token.RETURN.toString(), root.getChildNodes().get(0).getType().toString());
  }

  @org.junit.Test
  public void testRemoveDeadCodeWithNoSideEffects() {
    Node root = createNode(Token.BLOCK);
    Node statement1 = createNode(Token.ASSIGN, null, createNode(Token.STRING_LITERAL, "a"));
    Node statement2 = createNode(Token.ASSIGN, null, createNode(Token.STRING_LITERAL, "b"));
    root.addChildToEnd(statement1);
    root.addChildToEnd(statement2);
    unreachableCodeElimination.process(null, root);
    assertEquals(0, root.getChildNodes().size());
  }

  @org.junit.Test
  public void testRemoveDeadCodeWithSideEffects() {
    Node root = createNode(Token.BLOCK);
    Node statement1 = createNode(Token.ASSIGN, null, createNode(Token.STRING_LITERAL, "a"));
    Node statement2 = createNode(Token.ASSIGN, null, createNode(Token.STRING_LITERAL, "b"));
    Node statement3 = createNode(Token.ASSIGN, null, createNode(Token.STRING_LITERAL, "c"));
    root.addChildToEnd(statement1);
    root.addChildToEnd(statement2);
    root.addChildToEnd(statement3);
    unreachableCodeElimination.process(null, root);
    assertEquals(3, root.getChildNodes().size());
  }

  @org.junit.Test
  public void testRemoveUnconditionalBranching() {
    Node root = createNode(Token.BLOCK);
    Node breakStatement1 = createNode(Token.BREAK);
    Node breakStatement2 = createNode(Token.BREAK);
    Node statementBeforeBreak1 = createNode(Token.PRINT, "break1");
    Node statementBeforeBreak2 = createNode(Token.PRINT, "break2");
    root.addChildToEnd(statementBeforeBreak1);
    root.addChildToEnd(breakStatement1);
    root.addChildToEnd(breakStatement2);
    root.addChildToEnd(statementBeforeBreak2);
    unreachableCodeElimination.process(null, root);
    assertEquals(3, root.getChildNodes().size());
    assertEquals("break1", root.getChildNodes().get(0).toString());
    assertEquals("break2", root.getChildNodes().get(2).toString());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRemoveUnreachableBlock() {
    Node root = createNode(Token.BLOCK);
    Node blockToRemove = createNode(Token.BLOCK);
    root.addChildToEnd(blockToRemove);
    unreachableCodeElimination.process(null, root);
    assertEquals(0, root.getChildNodes().size());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRemoveUnreachableFunction() {
    Node root = createNode(Token.FUNCTION);
    Node functionToRemove = createNode(Token.FUNCTION);
    root.addChildToEnd(functionToRemove);
    unreachableCodeElimination.process(null, root);
    assertEquals(0, root.getChildNodes().size());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRemoveUnreachableTryCatchBlock() {
    Node root = createNode(Token.TRY);
    Node catchBlockToRemove = createNode(Token.BLOCK);
    root.addChildToEnd(catchBlockToRemove);
    unreachableCodeElimination.process(null, root);
    assertEquals(0, root.getChildNodes().size());
  }

  private Node createNode(int nodeType, Node... children) {
    Node node = new Node();
    node.setType(nodeType);
    if (children != null) {
      for (Node child : children) {
        node.addChildToEnd(child);
      }
    }
    return node;
  }
}