import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.setChildCount(3);
    assertEquals(true, NodeUtil.isForIn(node));

    Node nodeWithTwoChildren = new Node(Token.FOR);
    nodeWithTwoChildren.setChildCount(2);
    assertEquals(false, NodeUtil.isForIn(nodeWithTwoChildren));

    Node nodeWithInvalidType = new Node(Token.VAR);
    assertEquals(false, NodeUtil.isForIn(nodeWithInvalidType));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.IF);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    assertEquals(true, NodeUtil.isStatement(node));

    Node nodeWithInvalidType = new Node(Token.NUMBER);
    assertEquals(false, NodeUtil.isStatement(nodeWithInvalidType));

    Node nodeWithoutParent = new Node(Token.IF);
    assertEquals(false, NodeUtil.isStatement(nodeWithoutParent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setName("myFunction");
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    assertEquals(true, NodeUtil.isFunctionDeclaration(node));

    Node nodeWithArguments = new Node(Token.FUNCTION);
    nodeWithArguments.setName("myFunction");
    nodeWithArguments.setChild(0, new Node(Token.IDENTIFIER, "arg1"));
    nodeWithArguments.setChild(1, new Node(Token.IDENTIFIER, "arg2"));
    nodeWithArguments.setParent(parent);
    assertEquals(true, NodeUtil.isFunctionDeclaration(nodeWithArguments));

    Node nodeWithoutName = new Node(Token.FUNCTION);
    nodeWithoutName.setParent(parent);
    assertEquals(false, NodeUtil.isFunctionDeclaration(nodeWithoutName));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setName("myFunction");
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);
    Node grandParent = new Node(Token.SCRIPT);
    parent.setParent(grandParent);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));

    Node nodeInsideFunction = new Node(Token.FUNCTION);
    nodeInsideFunction.setName("myFunction");
    nodeInsideFunction.setParent(new Node(Token.FUNCTION));
    Node parentFunction = new Node(Token.SCRIPT);
    nodeInsideFunction.setParent(parentFunction);
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(nodeInsideFunction));

    Node nodeInsideLoop = new Node(Token.FUNCTION);
    nodeInsideLoop.setName("myFunction");
    Node parentLoop = new Node(Token.WHILE);
    nodeInsideLoop.setParent(parentLoop);
    Node parentScript = new Node(Token.SCRIPT);
    parentLoop.setParent(parentScript);
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(nodeInsideLoop));
  }
}