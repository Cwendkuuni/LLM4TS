import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  
  MinimizeExitPoints minimizeExitPoints;
  
  @Before
  public void setup() {
    minimizeExitPoints = new MinimizeExitPoints();
  }
  
  @Test
  public void testTryMinimizeExits_ExitNode() {
    Node exitNode = new Node(Token.RETURN);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(exitNode);
    
    minimizeExitPoints.tryMinimizeExits(exitNode, Token.RETURN, null);
    
    assertEquals(0, parent.getChildCount());
  }
  
  @Test
  public void testTryMinimizeExits_IfBlockOnly() {
    Node ifNode = new Node(Token.IF);
    Node trueBlock = new Node(Token.BLOCK);
    Node falseBlock = new Node(Token.BLOCK);
    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(falseBlock);
    
    minimizeExitPoints.tryMinimizeExits(ifNode, Token.BREAK, null);
    
    assertEquals(0, trueBlock.getChildCount());
    assertEquals(0, falseBlock.getChildCount());
  }
  
  @Test
  public void testTryMinimizeExits_LabelBlock() {
    Node labelNode = new Node(Token.LABEL);
    Node labelBlock = new Node(Token.BLOCK);
    labelNode.addChildToBack(labelBlock);
    
    minimizeExitPoints.tryMinimizeExits(labelNode, Token.BREAK, "label");
    
    assertEquals(0, labelBlock.getChildCount());
  }
  
  @Test
  public void testTryMinimizeExits_IfBlockWithSiblings() {
    Node ifNode = new Node(Token.IF);
    Node trueBlock = new Node(Token.BLOCK);
    Node falseBlock = new Node(Token.BLOCK);
    Node sibling1 = new Node(Token.RETURN);
    Node sibling2 = new Node(Token.RETURN);
    ifNode.addChildToBack(trueBlock);
    ifNode.addChildToBack(falseBlock);
    trueBlock.addChildToBack(sibling1);
    falseBlock.addChildToBack(sibling2);
    
    minimizeExitPoints.tryMinimizeExits(ifNode, Token.RETURN, null);
    
    assertEquals(0, trueBlock.getChildCount());
    assertEquals(0, sibling1.getParent().getChildCount());
    assertEquals(1, falseBlock.getChildCount());
    assertEquals(sibling2, falseBlock.getLastChild());
  }
  
  @Test
  public void testTryMinimizeIfBlockExits() {
    Node srcBlock = new Node(Token.BLOCK);
    Node destBlock = new Node(Token.BLOCK);
    Node ifNode = new Node(Token.IF);
    Node exitNode = new Node(Token.RETURN);
    srcBlock.addChildToBack(exitNode);
    
    minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, Token.RETURN, null);
    
    assertEquals(0, srcBlock.getChildCount());
    assertEquals(0, exitNode.getParent().getChildCount());
    assertEquals(0, destBlock.getChildCount());
  }
  
  @Test
  public void testMatchingExitNode_ReturnNodeWithoutExpression() {
    Node returnNode = new Node(Token.RETURN);
    
    boolean result = minimizeExitPoints.matchingExitNode(returnNode, Token.RETURN, null);
    
    assertTrue(result);
  }
  
  @Test
  public void testMatchingExitNode_ReturnNodeWithExpression() {
    Node returnNode = new Node(Token.RETURN);
    returnNode.addChildToBack(new Node(Token.NAME));
    
    boolean result = minimizeExitPoints.matchingExitNode(returnNode, Token.RETURN, null);
    
    assertFalse(result);
  }
  
  @Test
  public void testMatchingExitNode_BreakNode() {
    Node breakNode = new Node(Token.BREAK);
    
    boolean result = minimizeExitPoints.matchingExitNode(breakNode, Token.BREAK, null);
    
    assertTrue(result);
  }
  
  @Test
  public void testMatchingExitNode_BreakNodeWithoutLabel() {
    Node breakNode = new Node(Token.BREAK);
    breakNode.addChildToBack(new Node(Token.NAME));
    
    boolean result = minimizeExitPoints.matchingExitNode(breakNode, Token.BREAK, null);
    
    assertTrue(result);
  }
  
  @Test
  public void testMatchingExitNode_BreakNodeWithLabel() {
    Node breakNode = new Node(Token.BREAK);
    Node labelNode = new Node(Token.NAME);
    labelNode.setString("label");
    breakNode.addChildToBack(labelNode);
    
    boolean result = minimizeExitPoints.matchingExitNode(breakNode, Token.BREAK, "label");
    
    assertTrue(result);
  }
  
  @Test
  public void testMatchingExitNode_BreakNodeWithDifferentLabel() {
    Node breakNode = new Node(Token.BREAK);
    Node labelNode = new Node(Token.NAME);
    labelNode.setString("label");
    breakNode.addChildToBack(labelNode);
    
    boolean result = minimizeExitPoints.matchingExitNode(breakNode, Token.BREAK, "anotherLabel");
    
    assertFalse(result);
  }
  
  @Test
  public void testMoveAllFollowing() {
    Node start = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(start);
    Node sibling1 = new Node(Token.EXPR_RESULT);
    Node sibling2 = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(sibling1);
    parent.addChildToBack(sibling2);
    Node destParent = new Node(Token.BLOCK);
    
    minimizeExitPoints.moveAllFollowing(start, parent, destParent);
    
    assertEquals(0, parent.getChildCount());
    assertEquals(2, destParent.getChildCount());
    assertEquals(sibling1, destParent.getFirstChild());
    assertEquals(sibling2, destParent.getLastChild());
  }
}