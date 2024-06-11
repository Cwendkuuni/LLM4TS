import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.INIT));
    node.addChildToBack(new Node(Token.CONDITION));
    node.addChildToBack(new Node(Token.INCR));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_missingChildNodes() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.INIT));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsForIn_differentTokenType() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node(Token.INIT));
    node.addChildToBack(new Node(Token.CONDITION));
    node.addChildToBack(new Node(Token.INCR));
    
    boolean result = NodeUtil.isForIn(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_typicalCase() {
    Node node = new Node(Token.BLOCK);
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_differentTokenType() {
    Node node = new Node(Token.CALL);
    
    boolean result = NodeUtil.isStatement(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_typicalCase() {
    Node node = new Node(Token.FUNCTION);
    node.setIsStatement(true);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_differentTokenType() {
    Node node = new Node(Token.VAR);
    node.setIsStatement(true);
    
    boolean result = NodeUtil.isFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalCase() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parentNode.addChildToBack(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_differentTokenType() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.LET);
    parentNode.addChildToBack(node);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notInScopeRoot() {
    Node parentNode = new Node(Token.BLOCK);
    Node grandParentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parentNode.addChildToBack(node);
    grandParentNode.addChildToBack(parentNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    
    Assert.assertFalse(result);
  }
}