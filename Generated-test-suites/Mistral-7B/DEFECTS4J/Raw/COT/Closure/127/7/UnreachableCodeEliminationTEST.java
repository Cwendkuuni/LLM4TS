import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private UnreachableCodeElimination elimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    elimination = new UnreachableCodeElimination(compiler, false);
  }

  @Test
  public void testEmptyFunction() {
    Node root = Node.createBlock();
    elimination.process(Node.rootNode(), root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testFunctionWithReturn() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.RETURN, 0));
    elimination.process(Node.rootNode(), root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testFunctionWithDeadCode() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.IF, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.ALERT, 2));
    elimination.process(Node.rootNode(), root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testFunctionWithDeadCodeAfterReturn() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.IF, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.ALERT, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(1));
    assertNull(root.getChild(2));
  }

  @Test
  public void testFunctionWithDeadCodeBeforeReturn() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.IF, 0));
    root.addChild(Node.createToken(Token.ALERT, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
  }

  @Test
  public void testFunctionWithDeadCodeInsideReturn() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.IF, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.ALERT, 2));
    root.addChild(Node.createToken(Token.RETURN, 3));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(2));
  }

  @Test
  public void testFunctionWithDeadCodeAfterReturnWithSideEffect() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.IF, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.ASSIGN, 2));
    elimination.process(Node.rootNode(), root);
    assertFalse(root.getChild(1).isReturn());
  }

  @Test(expected = NullPointerException.class)
  public void testNullFunction() {
    elimination.process(Node.rootNode(), null);
  }

  @Test
  public void testFunctionWithUnconditionalBranching() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.BREAK, 0));
    root.addChild(Node.createToken(Token.BREAK, 1));
    elimination.process(Node.rootNode(), root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testFunctionWithUnconditionalBranchingToSelf() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.BREAK, 0));
    root.addChild(Node.createToken(Token.BREAK, 1));
    root.addChild(1, Node.createToken(Token.BREAK, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
    assertNull(root.getChild(2));
  }

  @Test
  public void testFunctionWithUnconditionalBranchingToAnotherBreak() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.BREAK, 0));
    root.addChild(Node.createToken(Token.BREAK, 1));
    root.addChild(1, Node.createToken(Token.BREAK, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
    Node secondBreak = root.getChild(2);
    assertNull(secondBreak.getPreviousSibling());
  }

  @Test
  public void testFunctionWithUnconditionalBranchingToContinue() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.CONTINUE, 0));
    root.addChild(Node.createToken(Token.CONTINUE, 1));
    elimination.process(Node.rootNode(), root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testFunctionWithUnconditionalBranchingToReturn() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.RETURN, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
  }

  @Test
  public void testFunctionWithUnconditionalBranchingToFunction() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.RETURN, 0));
    root.addChild(1, Node.createBlock());
    root.addChild(1, Node.createToken(Token.RETURN, 1));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
  }

  @Test
  public void testFunctionWithReturnAfterBranch() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.IF, 0));
    root.addChild(Node.createToken(Token.BREAK, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
  }

  @Test
  public void testFunctionWithBreakInsideLoop() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.BREAK, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(2));
  }

  @Test
  public void testFunctionWithBreakInsideLoopWithSideEffect() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.BREAK, 1));
    root.addChild(Node.createToken(Token.ASSIGN, 2));
    root.addChild(Node.createToken(Token.RETURN, 3));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertFalse(root.getChild(1).isBreak());
  }

  @Test
  public void testFunctionWithContinueInsideLoop() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.CONTINUE, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
  }

  @Test
  public void testFunctionWithContinueInsideLoopWithSideEffect() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.CONTINUE, 1));
    root.addChild(Node.createToken(Token.ASSIGN, 2));
    root.addChild(Node.createToken(Token.RETURN, 3));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertFalse(root.getChild(1).isContinue());
  }

  @Test
  public void testFunctionWithReturnInsideLoop() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
  }

  @Test
  public void testFunctionWithReturnInsideLoopWithSideEffect() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.ASSIGN, 2));
    root.addChild(Node.createToken(Token.RETURN, 3));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertFalse(root.getChild(1).isReturn());
  }

  @Test
  public void testFunctionWithReturnInsideLoopWithNoSideEffect() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.FOR, 0));
    root.addChild(Node.createToken(Token.RETURN, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
  }

  @Test
  public void testFunctionWithSwitchStatement() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.SWITCH, 0));
    root.addChild(Node.createToken(Token.CASE, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(1));
  }

  @Test
  public void testFunctionWithCaseStatementWithBreak() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.SWITCH, 0));
    root.addChild(Node.createToken(Token.CASE, 1));
    root.addChild(Node.createToken(Token.BREAK, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
  }

  @Test
  public void testFunctionWithCaseStatementWithContinue() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.SWITCH, 0));
    root.addChild(Node.createToken(Token.CASE, 1));
    root.addChild(Node.createToken(Token.CONTINUE, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
  }

  @Test
  public void testFunctionWithCaseStatementWithReturn() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.SWITCH, 0));
    root.addChild(Node.createToken(Token.CASE, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
  }

  @Test
  public void testFunctionWithCaseStatementWithDeadCode() {
    Node root = Node.createBlock();
    root.addChild(Node.createToken(Token.SWITCH, 0));
    root.addChild(Node.createToken(Token.CASE, 1));
    root.addChild(Node.createToken(Token.RETURN, 2));
    root.addChild(Node.createToken(Token.ALERT, 3));
    elimination.process(Node.rootNode(), root);
    assertNull(root.getChild(0));
    assertNull(root.getChild(3));
  }
}