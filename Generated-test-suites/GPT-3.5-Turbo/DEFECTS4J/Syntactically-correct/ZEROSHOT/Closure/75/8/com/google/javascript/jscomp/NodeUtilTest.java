import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.NUMBER));
    node2.addChildToBack(new Node(Token.STRING));
    node2.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node2));
    assertFalse(NodeUtil.isForIn(node1));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.LABEL);
    Node node3 = new Node(Token.NUMBER);
    node2.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertFalse(NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.NAME));
    Node node3 = new Node(Token.NUMBER);
    assertTrue(NodeUtil.isFunctionDeclaration(node2));
    assertFalse(NodeUtil.isFunctionDeclaration(node1));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToBack(new Node(Token.NAME));
    Node node3 = new Node(Token.NAME);
    Node node4 = new Node(Token.SCRIPT);
    Node node5 = new Node(Token.FUNCTION);
    node5.addChildToBack(new Node(Token.NAME));
    Node node6 = new Node(Token.FUNCTION);
    node4.addChildToBack(node5);
    node6.addChildToBack(new Node(Token.NAME));
    node4.addChildToBack(node6);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node5));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node6));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }

}