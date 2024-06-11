import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));

    Node invalidNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(invalidNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(scriptNode));

    Node invalidNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.EMPTY));
    Node scriptNode = new Node(Token.SCRIPT, functionNode);
    Node parentNode = new Node(Token.FUNCTION, scriptNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(parentNode));

    Node invalidNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
  }
}