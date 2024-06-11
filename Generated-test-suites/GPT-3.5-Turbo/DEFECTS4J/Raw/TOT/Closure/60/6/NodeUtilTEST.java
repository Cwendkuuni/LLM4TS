import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node inputNode = new Node(Token.FOR);
    inputNode.addChildToFront(new Node(Token.VAR));
    inputNode.addChildToBack(new Node(Token.NAME));
    inputNode.addChildToBack(new Node(Token.IN));
    assertTrue(NodeUtil.isForIn(inputNode));
  }
  
  @Test
  public void testIsForIn_notForIn() {
    Node inputNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(inputNode));
  }
  
  @Test
  public void testIsForIn_incompleteForIn() {
    Node inputNode = new Node(Token.FOR);
    inputNode.addChildToFront(new Node(Token.VAR));
    inputNode.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(inputNode));
  }
  
  @Test
  public void testIsStatement_typicalCase() {
    Node inputNode = new Node(Token.BLOCK);
    inputNode.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isStatement(inputNode));
  }
  
  @Test
  public void testIsStatement_notStatement() {
    Node inputNode = new Node(Token.PROPERTY);
    assertFalse(NodeUtil.isStatement(inputNode));
  }
  
  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node inputNode = new Node(Token.FUNCTION);
    inputNode.addChildToFront(new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(inputNode));
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node inputNode = new Node(Token.IF);
    assertFalse(NodeUtil.isFunctionDeclaration(inputNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node inputNode = new Node(Token.FUNCTION);
    inputNode.addChildToFront(new Node(Token.NAME));
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToFront(inputNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(inputNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node inputNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(inputNode));
  }

}