import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node("child1");
    Node child2 = new Node("child2");
    Node child3 = new Node("child3");
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));

    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node);

    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(node);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}