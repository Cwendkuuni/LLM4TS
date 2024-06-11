import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for in statement");
    node.setType(Token.FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_False() {
    Node node = new Node("non-for-in statement");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test(expected = NullPointerException.class)
  public void testIsForIn_NullNode() {
    Node node = null;
    // This should throw an NPE
    NodeUtil.isForIn(node);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("statement node");
    node.setType(Token.IF);
    Node parent = new Node("script node");
    parent.setType(Token.SCRIPT);
    parent.addChild(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_False() {
    Node node = new Node("expression node");
    node.setType(Token.ADD);
    Node parent = new Node("script node");
    parent.setType(Token.SCRIPT);
    parent.addChild(node);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test(expected = NullPointerException.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    // This should throw an NPE
    NodeUtil.isStatement(node);
  }
}