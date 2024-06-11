import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node();
    node.setType(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildAfter(new Node(), node.getFirstChild());
    boolean result = NodeUtil.isForIn(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node();
    node.setType(Token.SCRIPT);
    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.SCRIPT);
    Node grandParent = new Node();
    grandParent.setType(Token.FUNCTION);
    parent.addChildToFront(grandParent);
    grandParent.addChildToFront(node);
    node.setParent(grandParent);
    grandParent.setParent(parent);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
  }

}