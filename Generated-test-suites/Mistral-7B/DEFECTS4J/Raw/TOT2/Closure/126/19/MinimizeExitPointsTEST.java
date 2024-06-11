import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MinimizeExitPoints.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private Compiler compiler;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    root = new Node("function() {}");
  }

  @After
  public void tearDown() {
    compiler = null;
    root = null;
  }

  @Test
  public void testMinimizeExits_Label() {
    Node labelNode = new Node(Token.LABEL, "label");
    Node exitNode = new Node(Token.BREAK, null);
    Node breakNode = new Node(Token.LABEL, "label");
    Node block = new Node(Token.BLOCK, null);
    Node body = new Node(Token.BLOCK, null);

    labelNode.addChildToBack(block);
    block.addChildToBack(new Node(Token.STATEMENT, null));
    block.addChildToBack(body);
    body.addChildToBack(labelNode);
    body.addChildToBack(exitNode);
    body.addChildToBack(breakNode);

    root.addChildToBack(labelNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    assertNull("Exit node should have been removed", exitNode.getParent());
    assertTrue("Break node should still be present", breakNode.getParent() instanceof Node);
  }

  @Test
  public void testMinimizeExits_If_SingleExit() {
    Node ifNode = new Node(Token.IF, null);
    Node trueBlock = new Node(Token.BLOCK, null);
    Node exitNode = new Node(Token.RETURN, null);
    Node falseBlock = new Node(Token.BLOCK, null);

    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(falseBlock);
    trueBlock.addChildToBack(exitNode);

    root.addChildToBack(ifNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    assertNotNull("The return node should still be present", exitNode.getParent());
  }

  @Test
  public void testMinimizeExits_If_MultipleExits() {
    Node ifNode = new Node(Token.IF, null);
    Node trueBlock = new Node(Token.BLOCK, null);
    Node exitNode1 = new Node(Token.RETURN, null);
    Node exitNode2 = new Node(Token.RETURN, null);
    Node falseBlock = new Node(Token.BLOCK, null);

    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(falseBlock);
    trueBlock.addChildToBack(exitNode1);
    trueBlock.addChildToBack(exitNode2);

    root.addChildToBack(ifNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    // Both exits should be removed and the last one should be the only one left.
    assertNull("First exit node should have been removed", exitNode1.getParent());
    assertNull("Second exit node should have been removed", exitNode2.getParent());
    assertNotNull("The last exit node should still be present", exitNode2.getParent() instanceof Node);
  }

  @Test
  public void testMinimizeExits_If_Else_SingleExit() {
    Node ifNode = new Node(Token.IF, null);
    Node trueBlock = new Node(Token.BLOCK, null);
    Node exitNode = new Node(Token.RETURN, null);
    Node elseBlock = new Node(Token.BLOCK, null);

    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(elseBlock);
    trueBlock.addChildToBack(exitNode);

    root.addChildToBack(ifNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    assertNotNull("The return node should still be present", exitNode.getParent());
  }

  @Test
  public void testMinimizeExits_If_Else_MultipleExits() {
    Node ifNode = new Node(Token.IF, null);
    Node trueBlock = new Node(Token.BLOCK, null);
    Node exitNode1 = new Node(Token.RETURN, null);
    Node exitNode2 = new Node(Token.RETURN, null);
    Node elseBlock = new Node(Token.BLOCK, null);

    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(elseBlock);
    trueBlock.addChildToBack(exitNode1);
    trueBlock.addChildToBack(exitNode2);
    elseBlock.addChildToBack(exitNode2);

    root.addChildToBack(ifNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    // Both exits in the true block should be removed but the one in the else block should remain.
    assertNull("First exit node in true block should have been removed", exitNode1.getParent());
    assertNull("Second exit node in true block should have been removed", exitNode2.getParent());
    assertNotNull("The exit node in the else block should still be present", exitNode2.getParent() instanceof Node);
  }

  @Test
  public void testMinimizeExits_For() {
    Node forNode = new Node(Token.FOR, null);
    Node initNode = new Node(Token.BLOCK, null);
    Node conditionNode = new Node(Token.LITERAL, "true");
    Node loopNode = new Node(Token.BLOCK, null);
    Node exitNode = new Node(Token.BREAK, null);
    Node incrementNode = new Node(Token.BLOCK, null);

    forNode.addChildToBack(initNode);
    forNode.addChildToBack(conditionNode);
    forNode.addChildToBack(loopNode);
    forNode.addChildToBack(incrementNode);

    initNode.addChildToBack(new Node(Token.STATEMENT, null));
    loopNode.addChildToBack(exitNode);

    root.addChildToBack(forNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    assertNull("The break node should have been removed", exitNode.getParent());
  }

  @Test
  public void testMinimizeExits_While() {
    Node whileNode = new Node(Token.WHILE, null);
    Node conditionNode = new Node(Token.LITERAL, "true");
    Node loopNode = new Node(Token.BLOCK, null);
    Node exitNode = new Node(Token.BREAK, null);

    whileNode.addChildToBack(conditionNode);
    whileNode.addChildToBack(loopNode);
    loopNode.addChildToBack(exitNode);

    root.addChildToBack(whileNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    assertNull("The break node should have been removed", exitNode.getParent());
  }

  @Test
  public void testMinimizeExits_DoWhile() {
    Node doNode = new Node(Token.DO, null);
    Node bodyNode = new Node(Token.BLOCK, null);
    Node conditionNode = new Node(Token.LITERAL, "true");
    Node exitNode = new Node(Token.BREAK, null);

    doNode.addChildToBack(bodyNode);
    doNode.addChildToBack(conditionNode);

    bodyNode.addChildToBack(exitNode);

    root.addChildToBack(doNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    assertNull("The break node should have been removed", exitNode.getParent());
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExits_Function_ThrowsException() {
    Node functionNode = new Node(Token.FUNCTION, null);
    Node bodyNode = new Node(Token.BLOCK, null);
    Node throwNode = new Node(Token.THROW, null);

    functionNode.addChildToBack(bodyNode);
    bodyNode.addChildToBack(throwNode);

    root.addChildToBack(functionNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    // This test should fail because minimizing the exits of a function that throws an exception
    // can change its completion type, which is not allowed.
  }

  @Test
  public void testMinimizeExits_TryCatchFinally() {
    Node tryNode = new Node(Token.TRY, null);
    Node tryBlock = new Node(Token.BLOCK, null);
    Node catchNode = new Node(Token.BLOCK, null);
    Node finallyNode = new Node(Token.BLOCK, null);
    Node exitNode = new Node(Token.RETURN, null);
    Node throwNode = new Node(Token.THROW, null);

    tryNode.addChildToBack(tryBlock);
    tryNode.addChildToBack(catchNode);
    tryNode.addChildToBack(finallyNode);

    tryBlock.addChildToBack(exitNode);
    tryBlock.addChildToBack(throwNode);

    root.addChildToBack(tryNode);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    NodeTraversal.traverse(compiler, root, minimizer);

    // The exit node in the try block should be removed but the catch block should remain.
    assertNull("The exit node in the try block should have been removed", exitNode.getParent());
    assertNotNull("The catch block should still be present", catchNode.getParent() instanceof Node);
  }
}