import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node parentNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    node.setParent(parentNode);
    assertTrue(NodeUtil.isStatement(node));

    Node nonStatementNode = new Node(Token.FUNCTION);
    nonStatementNode.setParent(parentNode);
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    Node nonFunctionDeclarationNode = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parentNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setParent(parentNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node nonHoistedFunctionNode = new Node(Token.FUNCTION);
    nonHoistedFunctionNode.setParent(new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));
  }

}