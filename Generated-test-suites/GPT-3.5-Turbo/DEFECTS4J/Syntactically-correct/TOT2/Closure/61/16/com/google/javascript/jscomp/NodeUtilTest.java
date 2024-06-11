import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node funcNode = new Node(Token.FUNCTION);
    
    assertTrue(NodeUtil.isFunctionDeclaration(funcNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node funcNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    
    scriptNode.addChildToBack(funcNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(funcNode));
  }
}