import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.FOR_IN);
    Node child2 = new Node(Token.NUMBER);
    Node child3 = new Node(Token.STRING);
    
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_notForInLoop() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NUMBER);
    Node child2 = new Node(Token.STRING);
    Node child3 = new Node(Token.FALSE);
    
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_typicalCase() {
    Node node = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(node);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(node);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}