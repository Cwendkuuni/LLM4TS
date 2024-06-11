import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
    
    Node nodeInvalid = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(nodeInvalid));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
    
    Node nodeInvalid = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(nodeInvalid));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    Node nodeInvalid = new Node(Token.SCRIPT);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeInvalid));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT, node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node nodeInvalid1 = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInvalid1));
    
    Node nodeInvalid2 = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInvalid2));
  }
}