import org.junit.Before;
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
    
    node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
    Node ifNode = new Node(Token.IF);
    
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(ifNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignmentNode = new Node(Token.ASSIGN);
    
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(assignmentNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.addChildToBack(new Node(Token.NAME));
    parentFunctionNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(parentFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
  }

}