import static org.junit.Assert.*;
import org.junit.Test;

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
    Node scriptNode = new Node(Token.SCRIPT);
    Node labelNode = new Node(Token.LABEL);
    
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(new Node(Token.NAME)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    
    scriptNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}