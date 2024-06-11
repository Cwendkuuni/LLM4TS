import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  // Test case for isForIn method
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node));
  }

  // Test case for isStatement method
  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(new Node(Token.EMPTY));

    Node node = new Node(Token.EMPTY);
    script.getFirstChild().addChildToBack(node);

    assertTrue(NodeUtil.isStatement(node));
  }

  // Test case for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(new Node(Token.EMPTY));
    script.getFirstChild().addChildToBack(function);

    assertTrue(NodeUtil.isFunctionDeclaration(function));
  }

  // Test case for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(new Node(Token.EMPTY));
    script.getFirstChild().addChildToBack(function);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }
}