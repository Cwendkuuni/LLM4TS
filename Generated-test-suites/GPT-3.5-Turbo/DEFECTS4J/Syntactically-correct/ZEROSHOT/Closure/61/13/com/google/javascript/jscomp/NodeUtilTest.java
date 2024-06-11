import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
    boolean result = NodeUtil.isForIn(node);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.IF, new Node(), new Node());
    boolean result = NodeUtil.isForIn(node);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    boolean result = NodeUtil.isStatement(node);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.EXPR_RESULT);
    boolean result = NodeUtil.isStatement(node);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.LAMBDA);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    parent.addChildToFront(node);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    Assert.assertFalse(result);
  }
}