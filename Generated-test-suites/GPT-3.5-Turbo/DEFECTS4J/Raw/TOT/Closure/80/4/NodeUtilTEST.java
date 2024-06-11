import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_statement() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.VAR);
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_functionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setString("function");
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_hoistedFunctionDeclaration() {
    Node parent = new Node(Token.CALL);
    parent.addChildToBack(new Node(Token.STRING));

    Node node = new Node(Token.FUNCTION);
    node.setParent(parent);
    node.setString("function");
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node parent = new Node(Token.IF);
    parent.addChildToBack(new Node(Token.STRING));

    Node node = new Node(Token.FUNCTION);
    node.setString("function");
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}