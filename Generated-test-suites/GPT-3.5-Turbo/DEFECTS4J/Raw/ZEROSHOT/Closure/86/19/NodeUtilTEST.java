import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test for FOR-IN loop
    Node n1 = new Node(Token.FOR);
    Node n2 = new Node(Token.VAR);
    Node n3 = new Node(Token.IN);

    n1.addChildToFront(n2);
    n1.addChildToBack(n3);

    boolean result = NodeUtil.isForIn(n1);

    assertTrue(result);
  }

  @Test
  public void testIsNotForIn() {
    // Test for non-FOR-IN loop
    Node n1 = new Node(Token.FOR);
    Node n2 = new Node(Token.VAR);
    Node n3 = new Node(Token.BLOCK);

    n1.addChildToFront(n2);
    n1.addChildToBack(n3);

    boolean result = NodeUtil.isForIn(n1);

    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    // Test for statement node
    Node n1 = new Node(Token.SCRIPT);
    Node n2 = new Node(Token.BLOCK);

    n1.addChildToFront(n2);

    boolean result = NodeUtil.isStatement(n1);

    assertTrue(result);
  }

  @Test
  public void testIsNotStatement() {
    // Test for non-statement node
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.PARAM_LIST);

    n1.addChildToFront(n2);

    boolean result = NodeUtil.isStatement(n1);

    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test for function declaration node
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.PARAM_LIST);

    n1.addChildToFront(n2);

    boolean result = NodeUtil.isFunctionDeclaration(n1);

    assertTrue(result);
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    // Test for non-function declaration node
    Node n1 = new Node(Token.VAR);
    Node n2 = new Node(Token.INIT);

    n1.addChildToFront(n2);

    boolean result = NodeUtil.isFunctionDeclaration(n1);

    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test for hoisted function declaration
    Node n1 = new Node(Token.FUNCTION);
    Node n2 = new Node(Token.PARAM_LIST);
    Node n3 = new Node(Token.SCRIPT);

    n1.addChildToFront(n2);
    n3.addChildToFront(n1);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n1);

    assertTrue(result);

    result = NodeUtil.isHoistedFunctionDeclaration(n3);

    assertFalse(result);
  }

}