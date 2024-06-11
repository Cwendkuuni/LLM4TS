import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

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
  public void testIsForIn_NotForIn() {
    Node node = new Node(Token.EMPTY);
    
    Assert.assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));
    
    Assert.assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.EXPR_RESULT));
    
    Assert.assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));
    
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    Node grandParentNode = new Node(Token.EMPTY);
    grandParentNode.addChildToFront(scriptNode);
    scriptNode.addChildToFront(functionNode);
    
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentScriptNode = new Node(Token.SCRIPT);
    Node grandParentScriptNode = new Node(Token.SCRIPT);

    grandParentScriptNode.addChildToFront(parentScriptNode);

    parentScriptNode.addChildToFront(functionNode);

    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}