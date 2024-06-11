
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node testNode = new Node(Token.FOR);
    Node child1 = new Node(Token.EMPTY);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);
    testNode.addChildToFront(child1);
    testNode.addChildToBack(child2);
    testNode.addChildToBack(child3);
    
    assertTrue(NodeUtil.isForIn(testNode));
    
    Node testNode2 = new Node(Token.WHILE);
    assertFalse(NodeUtil.isForIn(testNode2));
  }

  @Test
  public void testIsStatement() {
    Node testNode = new Node(Token.SCRIPT);
    Node child = new Node(Token.EMPTY);
    testNode.addChildToBack(child);
    
    assertTrue(NodeUtil.isStatement(child));
    
    Node testNode2 = new Node(Token.IF);
    assertFalse(NodeUtil.isStatement(testNode2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    Node child = new Node(Token.EMPTY);
    testNode.addChildToBack(child);
    
    assertTrue(NodeUtil.isFunctionDeclaration(testNode));

    Node testNode2 = new Node(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(testNode2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    Node child = new Node(Token.EMPTY);
    Node parent = new Node(Token.SCRIPT);
    testNode.addChildToBack(child);
    parent.addChildToBack(testNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(testNode));

    Node testNode2 = new Node(Token.IF);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(testNode2));
  }
}
