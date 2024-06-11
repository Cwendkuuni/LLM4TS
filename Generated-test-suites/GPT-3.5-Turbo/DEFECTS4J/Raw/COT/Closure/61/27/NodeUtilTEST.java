import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_notForLoop() {
    Node node = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_notEnoughChildren() {
    Node node = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.PLUS);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_statement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.PLUS);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.PLUS);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_functionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionDeclarationInScript() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionDeclarationInFunction() {
    Node parent = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    grandParent.addChildToBack(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}