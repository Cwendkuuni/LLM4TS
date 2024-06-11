import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(invalidNode));
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

    Node invalidNode = new Node(Token.CALL);
    assertFalse(NodeUtil.isStatement(invalidNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node funcNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    
    funcNode.addChildToBack(new Node(Token.EMPTY));
    scriptNode.addChildToBack(funcNode);
    
    assertTrue(NodeUtil.isFunctionDeclaration(funcNode));
    assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node funcNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    Node parentFuncNode = new Node(Token.FUNCTION);
    
    funcNode.addChildToBack(new Node(Token.EMPTY));
    scriptNode.addChildToBack(funcNode);
    parentFuncNode.addChildToBack(funcNode);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(funcNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(parentFuncNode));
  }
}