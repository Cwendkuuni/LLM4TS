import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testTryMinimizeExits_ExitNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node exitNode = new Node(Token.EXIT);
    Node parent = new Node(Token.BLOCK, exitNode);
    
    mep.tryMinimizeExits(exitNode, Token.EXIT, null);
    
    assertEquals(0, parent.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_IfNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node ifNode = new Node(Token.IF);
    Node trueBlock = new Node(Token.BLOCK);
    Node falseBlock = new Node(Token.BLOCK);
    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(falseBlock);
    Node parent = new Node(Token.BLOCK, ifNode);
    
    mep.tryMinimizeExits(ifNode, Token.EXIT, null);
    
    assertEquals(2, ifNode.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_TryNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node tryNode = new Node(Token.TRY);
    Node tryBlock = new Node(Token.BLOCK);
    tryNode.addChildToBack(tryBlock);
    Node parent = new Node(Token.BLOCK, tryNode);
    
    mep.tryMinimizeExits(tryNode, Token.EXIT, null);
    
    assertEquals(1, tryNode.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_LabelNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node labelNode = new Node(Token.LABEL);
    Node labelBlock = new Node(Token.BLOCK);
    labelNode.addChildToBack(labelBlock);
    Node parent = new Node(Token.BLOCK, labelNode);
    
    mep.tryMinimizeExits(labelNode, Token.EXIT, null);
    
    assertEquals(1, labelNode.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_BlockNode_ExitNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node exitNode = new Node(Token.EXIT);
    Node parent = new Node(Token.BLOCK, exitNode);
    
    mep.tryMinimizeExits(parent, Token.EXIT, null);
    
    assertEquals(0, parent.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_BlockNode_SingleStatementNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node singleStatementNode = new Node(Token.ADD);
    Node parent = new Node(Token.BLOCK, singleStatementNode);
    
    mep.tryMinimizeExits(parent, Token.EXIT, null);
    
    assertEquals(1, parent.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_BlockNode_IfNode_ExitNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node ifNode = new Node(Token.IF);
    Node trueBlock = new Node(Token.BLOCK);
    Node exitNode = new Node(Token.EXIT);
    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(exitNode);
    Node parent = new Node(Token.BLOCK, ifNode);
    
    mep.tryMinimizeExits(parent, Token.EXIT, null);
    
    assertEquals(2, ifNode.getChildCount());
  }

  @Test
  public void testTryMinimizeExits_BlockNode_IfNode_SiblingNode() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node ifNode = new Node(Token.IF);
    Node trueBlock = new Node(Token.BLOCK);
    Node siblingNode = new Node(Token.ADD);
    ifNode.addChildToBack(trueBlock);
    Node parent = new Node(Token.BLOCK, ifNode, siblingNode);
    
    mep.tryMinimizeExits(parent, Token.EXIT, null);
    
    assertEquals(3, parent.getChildCount());
  }

  @Test
  public void testTryMinimizeIfBlockExits() {
    MinimizeExitPoints mep = new MinimizeExitPoints(null);
    Node srcBlock = new Node(Token.BLOCK);
    Node destBlock = new Node(Token.BLOCK);
    Node ifNode = new Node(Token.IF);
    ifNode.addChildToBack(srcBlock);
    ifNode.addChildToBack(destBlock);
    Node parent = new Node(Token.BLOCK, ifNode);
    
    mep.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.EXIT, null);
    
    assertEquals(2, ifNode.getChildCount());
  }

  @Test
  public void testMatchingExitNode_ReturnNode_NoChildren() {
    assertTrue(MinimizeExitPoints.matchingExitNode(new Node(Token.RETURN), Token.RETURN, null));
  }

  @Test
  public void testMatchingExitNode_ReturnNode_WithChildren() {
    assertFalse(MinimizeExitPoints.matchingExitNode(new Node(Token.RETURN, new Node(Token.NAME)), Token.RETURN, null));
  }

  @Test
  public void testMatchingExitNode_ExitNode_WithoutLabel() {
    assertTrue(MinimizeExitPoints.matchingExitNode(new Node(Token.EXIT), Token.EXIT, null));
  }

  @Test
  public void testMatchingExitNode_ExitNode_WithLabel() {
    assertFalse(MinimizeExitPoints.matchingExitNode(new Node(Token.EXIT, new Node(Token.NAME, "label")), Token.EXIT, null));
  }

  @Test
  public void testMoveAllFollowing() {
    Node start = new Node(Token.ADD);
    Node srcParent = new Node(Token.BLOCK, start);
    Node destParent = new Node(Token.BLOCK);
    
    MinimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    
    assertEquals(0, srcParent.getChildCount());
    assertEquals(1, destParent.getChildCount());
  }
  
  // Add more test methods for the other methods in MinimizeExitPoints class

}