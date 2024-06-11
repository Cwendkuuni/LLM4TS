import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case
    Node node1 = new Node(Token.FOR, null);
    Node node2 = new Node(Token.FOR, null);
    Node node3 = new Node(Token.FOR, null);
    node1.addChildToBack(new Node(Token.STRING, "a"));
    node1.addChildToBack(new Node(Token.STRING, "b"));
    node1.addChildToBack(new Node(Token.STRING, "c"));
    node2.addChildToBack(new Node(Token.STRING, "d"));
    node2.addChildToBack(new Node(Token.STRING, "e"));
    node3.addChildToBack(new Node(Token.STRING, "f"));
    node3.addChildToBack(new Node(Token.STRING, "g"));
    node3.addChildToBack(new Node(Token.STRING, "h"));
    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
    assertFalse(NodeUtil.isForIn(node3));

    // Edge case
    Node node4 = new Node(Token.FOR, null);
    node4.addChildToBack(new Node(Token.STRING, "i"));
    node4.addChildToBack(new Node(Token.STRING, "j"));
    assertFalse(NodeUtil.isForIn(node4));
  }

  @Test
  public void testIsStatement() {
    // Typical use case
    Node node1 = new Node(Token.LABEL, null);
    Node node2 = new Node(Token.LABEL, null);
    Node node3 = new Node(Token.LABEL, null);
    node1.addChildToBack(new Node(Token.STRING, "a"));
    node2.addChildToBack(new Node(Token.STRING, "b"));
    node3.addChildToBack(new Node(Token.STRING, "c"));
    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertTrue(NodeUtil.isStatement(node3));

    // Edge case
    Node node4 = new Node(Token.EXPR_RESULT, null);
    node4.addChildToBack(new Node(Token.STRING, "d"));
    assertFalse(NodeUtil.isStatement(node4));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION, null);
    Node node2 = new Node(Token.FUNCTION, null);
    Node node3 = new Node(Token.FUNCTION, null);
    node1.addChildToBack(new Node(Token.STRING, "a"));
    node2.addChildToBack(new Node(Token.STRING, "b"));
    node3.addChildToBack(new Node(Token.STRING, "c"));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    assertTrue(NodeUtil.isFunctionDeclaration(node2));
    assertTrue(NodeUtil.isFunctionDeclaration(node3));

    // Edge case
    Node node4 = new Node(Token.RETURN, null);
    node4.addChildToBack(new Node(Token.STRING, "d"));
    assertFalse(NodeUtil.isFunctionDeclaration(node4));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node node1 = new Node(Token.FUNCTION, null);
    Node node2 = new Node(Token.FUNCTION, null);
    Node node3 = new Node(Token.FUNCTION, null);
    node1.addChildToBack(new Node(Token.STRING, "a"));
    node2.addChildToBack(new Node(Token.STRING, "b"));
    node3.addChildToBack(new Node(Token.STRING, "c"));
    Node scriptNode = new Node(Token.SCRIPT, null);
    scriptNode.addChildToBack(node1);
    scriptNode.addChildToBack(node2);
    scriptNode.addChildToBack(node3);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node3));

    // Edge case
    Node node4 = new Node(Token.FUNCTION, null);
    Node parentFunctionNode = new Node(Token.FUNCTION, null);
    Node grandParentFunctionNode = new Node(Token.FUNCTION, null);
    node4.addChildToBack(new Node(Token.STRING, "d"));
    parentFunctionNode.addChildToBack(node4);
    grandParentFunctionNode.addChildToBack(parentFunctionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }
}