import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.LABEL);
    scriptNode.addChildToFront(statementNode);
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode);
    parentFunctionNode.addChildToFront(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

}