
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToBack(new Node(Token.VAR));
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    
    assertTrue(NodeUtil.isStatement(node.getChildAtIndex(0)));
    assertTrue(!NodeUtil.isStatement(node.getChildAtIndex(1)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node.removeChild(node.getChildAtIndex(0));
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(node);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}
