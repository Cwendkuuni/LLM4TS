import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    Assert.assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));
    
    Assert.assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parentNode = new Node(Token.SCRIPT);
    Node grandparentNode = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    
    node.setParent(grandparentNode);
    grandparentNode.setParent(parentNode);
    
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}