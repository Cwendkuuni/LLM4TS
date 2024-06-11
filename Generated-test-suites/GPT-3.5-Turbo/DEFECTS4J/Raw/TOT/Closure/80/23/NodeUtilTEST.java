import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case with FOR loop
    Node forNode = new Node(Token.FOR);
    forNode.addChildToFront(new Node(Token.EMPTY));
    forNode.addChildToFront(new Node(Token.EMPTY));
    forNode.addChildToFront(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(forNode));

    // Edge case with FOR loop with only two children
    Node forNode2 = new Node(Token.FOR);
    forNode2.addChildToFront(new Node(Token.EMPTY));
    forNode2.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(forNode2));

    // Edge case with FOR loop with more than three children
    Node forNode3 = new Node(Token.FOR);
    forNode3.addChildToFront(new Node(Token.EMPTY));
    forNode3.addChildToFront(new Node(Token.EMPTY));
    forNode3.addChildToFront(new Node(Token.EMPTY));
    forNode3.addChildToFront(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(forNode3));

    // Edge case with a non-FOR node
    Node emptyNode = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(emptyNode));
  }

  @Test
  public void testIsStatement() {
    // Typical use case with a SCRIPT parent
    Node scriptNode = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.EMPTY);
    scriptNode.addChildToFront(statementNode);
    assertTrue(NodeUtil.isStatement(statementNode));

    // Edge case with a BLOCK parent
    Node blockNode = new Node(Token.BLOCK);
    Node statementNode2 = new Node(Token.EMPTY);
    blockNode.addChildToFront(statementNode2);
    assertTrue(NodeUtil.isStatement(statementNode2));

    // Edge case with a LABEL parent
    Node labelNode = new Node(Token.LABEL);
    Node statementNode3 = new Node(Token.EMPTY);
    labelNode.addChildToFront(statementNode3);
    assertTrue(NodeUtil.isStatement(statementNode3));

    // Edge case with a non-statement parent
    Node emptyNode = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(emptyNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case with a FUNCTION parent and isStatement() is true
    Node functionNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    // Edge case with a FUNCTION parent and isStatement() is false
    Node functionNode2 = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode2);
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode2));

    // Edge case with a non-FUNCTION parent and isStatement() is true
    Node nonFunctionNode = new Node(Token.VAR);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));

    // Edge case with a non-FUNCTION parent and isStatement() is false
    Node emptyNode = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case with a FUNCTION parent and isFunctionDeclaration() and isStatement() are true
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    // Edge case with a FUNCTION parent, but isFunctionDeclaration() is false
    Node functionNode2 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));

    // Edge case with a SCRIPT parent, but isFunctionDeclaration() is true
    Node scriptNode2 = new Node(Token.SCRIPT);
    Node functionNode3 = new Node(Token.FUNCTION);
    scriptNode2.addChildToFront(functionNode3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode3));

    // Edge case with non-FUNCTION parent and isFunctionDeclaration() is true
    Node nonFunctionNode = new Node(Token.VAR);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonFunctionNode));

    // Edge case with non-FUNCTION parent and isFunctionDeclaration() is false
    Node emptyNode = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }
}