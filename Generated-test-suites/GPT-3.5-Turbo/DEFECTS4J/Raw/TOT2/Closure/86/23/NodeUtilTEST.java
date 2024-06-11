import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);

    Node n1 = new Node(Token.FUNCTION);
    n1.setParent(scriptNode);

    Node n2 = new Node(Token.NUMBER);
    n2.setParent(blockNode);

    Node n3 = new Node(Token.STRING);
    n3.setParent(labelNode);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(n1));
    assertFalse(NodeUtil.isStatement(n2));
    assertFalse(NodeUtil.isStatement(n3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.PARAM_LIST));
    functionNode.addChildToBack(new Node(Token.BLOCK));

    Node scriptNode = new Node(Token.SCRIPT);
    functionNode.setParent(scriptNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.PARAM_LIST));
    functionNode.addChildToBack(new Node(Token.BLOCK));

    Node scriptNode = new Node(Token.SCRIPT);
    functionNode.setParent(scriptNode);

    Node parentFunctionNode = new Node(Token.FUNCTION);
    Node grandparentFunctionNode = new Node(Token.FUNCTION);
    grandparentFunctionNode.addChildToBack(functionNode);
    parentFunctionNode.addChildToBack(grandparentFunctionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(parentFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
  }
}