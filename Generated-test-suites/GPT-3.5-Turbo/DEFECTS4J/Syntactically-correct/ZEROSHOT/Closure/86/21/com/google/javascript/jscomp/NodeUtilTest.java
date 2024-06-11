import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test case 1
    Node n1 = new Node(Token.FOR);
    n1.addChildToBack(new Node(Token.NAME));
    n1.addChildToBack(new Node(Token.NAME));
    n1.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(n1));

    // Test case 2
    Node n2 = new Node(Token.FOR);
    n2.addChildToBack(new Node(Token.NAME));
    n2.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n2));

    // Test case 3
    Node n3 = new Node(Token.FOR);
    n3.addChildToBack(new Node(Token.NAME));
    n3.addChildToBack(new Node(Token.NAME));
    n3.addChildToBack(new Node(Token.NAME));
    n3.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n3));
  }

  @Test
  public void testIsStatement() {
    // Test case 1
    Node n1 = new Node(Token.SCRIPT);
    Node n2 = new Node(Token.NAME);
    n1.addChildToBack(n2);
    assertTrue(NodeUtil.isStatement(n2));

    // Test case 2
    Node n3 = new Node(Token.BLOCK);
    Node n4 = new Node(Token.NAME);
    n3.addChildToBack(n4);
    assertTrue(NodeUtil.isStatement(n4));

    // Test case 3
    Node n5 = new Node(Token.LABEL);
    Node n6 = new Node(Token.NAME);
    n5.addChildToBack(n6);
    assertTrue(NodeUtil.isStatement(n6));

    // Test case 4
    Node n7 = new Node(Token.ADD);
    Node n8 = new Node(Token.NAME);
    n7.addChildToBack(n8);
    assertFalse(NodeUtil.isStatement(n8));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test case 1
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.ADD);
    n1.addChildToBack(n2);
    assertFalse(NodeUtil.isFunctionDeclaration(n1));

    // Test case 2
    Node n3 = new Node(Token.FUNCTION);
    n3.addChildToBack(new Node(Token.NAME));
    n3.addChildToBack(new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(n3));

    // Test case 3
    Node n4 = new Node(Token.SUB);
    Node n5 = new Node(Token.NAME);
    n4.addChildToBack(n5);
    assertFalse(NodeUtil.isFunctionDeclaration(n5));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test case 1
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.SCRIPT);
    n2.addChildToBack(n1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));

    // Test case 2
    Node n3 = new Node(Token.FUNCTION);
    Node n4 = new Node(Token.ADD);
    n4.addChildToBack(n3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));

    // Test case 3
    Node n5 = new Node(Token.FUNCTION);
    Node n6 = new Node(Token.FUNCTION);
    Node n7 = new Node(Token.SCRIPT);
    n7.addChildToBack(n6);
    n6.addChildToBack(n5);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n5));
  }

}