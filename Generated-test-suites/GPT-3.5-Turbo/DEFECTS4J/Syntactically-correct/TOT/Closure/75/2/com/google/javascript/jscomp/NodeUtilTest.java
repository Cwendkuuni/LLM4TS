import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.TRUE));
    node.addChildToFront(new Node(Token.NUMBER));
    node.addChildToFront(new Node(Token.STRING));
    
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_empty() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_script() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.TRUE));
    
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_block() {
    Node node = new Node(Token.BLOCK);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.FALSE));
    
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_label() {
    Node node = new Node(Token.LABEL);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.NULL));
    
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.PLUS);
    node.addChildToFront(new Node(Token.ONE));
    node.addChildToFront(new Node(Token.TWO));
    
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.PLUS);
    node.addChildToFront(new Node(Token.THREE));
    node.addChildToFront(new Node(Token.FOUR));
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME));

    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withinFunction() {
    Node functionNode1 = new Node(Token.FUNCTION);
    functionNode1.addChildToFront(new Node(Token.NAME));

    Node functionNode2 = new Node(Token.FUNCTION);
    functionNode2.addChildToFront(functionNode1);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    Node node = new Node(Token.PLUS);
    node.addChildToFront(new Node(Token.FIVE));
    node.addChildToFront(new Node(Token.SIX));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}