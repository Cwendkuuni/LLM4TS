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
    
    scriptNode.addChildToBack(new Node(Token.EMPTY));
    blockNode.addChildToBack(new Node(Token.EMPTY));
    labelNode.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));

    assertFalse(NodeUtil.isStatement(new Node(Token.EMPTY)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDecl = new Node(Token.FUNCTION);
    functionDecl.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDecl));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionDecl = new Node(Token.FUNCTION);
    Node parentFunction = new Node(Token.FUNCTION);
    
    scriptNode.addChildToBack(functionDecl);
    parentFunction.addChildToBack(functionDecl);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDecl));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentFunction));
  }
}