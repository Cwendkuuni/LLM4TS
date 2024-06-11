import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node testNode = new Node(Token.FOR);
    testNode.addChildToBack(new Node(Token.EMPTY));
    testNode.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(testNode));
  }

  @Test
  public void testIsStatement() {
    Node testNode = new Node(Token.SCRIPT);
    testNode.addChildToBack(new Node(Token.VAR));
    assertTrue(NodeUtil.isStatement(testNode.getChildAtIndex(0)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node testNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(testNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(new Node(Token.SCRIPT, functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = NullPointerException.class)
  public void testIsStatementNullParent() {
    Node testNode = new Node(Token.SCRIPT);
    assertFalse(NodeUtil.isStatement(testNode));
  }

}