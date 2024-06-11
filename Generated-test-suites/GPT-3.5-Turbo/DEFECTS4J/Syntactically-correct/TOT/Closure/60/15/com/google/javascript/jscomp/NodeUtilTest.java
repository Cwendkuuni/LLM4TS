import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    // typical use case: n represents a FOR-IN loop
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.VAR));
    n.addChildToFront(new Node(Token.VAR));
    n.addChildToFront(new Node(Token.VAR));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement_typical() {
    // typical use case: n is used as a statement
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToFront(n);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_notStatement() {
    // error scenario: n is not part of the AST, cannot determine if it's a statement
    Node n = new Node(Token.STRING);

    try {
      NodeUtil.isStatement(n);
      fail("Expected Preconditions.checkState() to throw an exception");
    } catch (IllegalStateException e) {
      // success
    }
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    // typical use case: n is a function declaration
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToFront(n);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    // typical use case: n is not a function declaration
    Node parent = new Node(Token.BLOCK);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToFront(n);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    // typical use case: n is a hoisted function declaration
    Node scriptParent = new Node(Token.SCRIPT);
    Node functionParent = new Node(Token.FUNCTION);
    Node n = new Node(Token.FUNCTION);
    scriptParent.addChildToFront(functionParent);
    functionParent.addChildToFront(n);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoisted() {
    // typical use case: n is not a hoisted function declaration
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION);
    parent.addChildToFront(n);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    // typical use case: n is not a function declaration
    Node scriptParent = new Node(Token.SCRIPT);
    Node functionParent = new Node(Token.FUNCTION);
    Node variableParent = new Node(Token.VAR);
    Node n = new Node(Token.FUNCTION);
    scriptParent.addChildToFront(functionParent);
    functionParent.addChildToFront(variableParent);
    variableParent.addChildToFront(n);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}