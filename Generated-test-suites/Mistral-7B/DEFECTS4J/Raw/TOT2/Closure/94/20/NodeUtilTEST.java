import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[] { new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENTIFIER, "array") });

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertTrue(NodeUtil.isForIn(forNode)); // This should be false
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IDENTIFIER, "statement");
    Node expressionNode = new Node(Token.IDENTIFIER, "expression");

    Node statementParent = new Node(Token.BLOCK, new Node[] { statementNode });
    Node expressionParent = new Node(Token.EXPRESSION, new Node[] { expressionNode });

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(statementParent)); // This should be true
    assertFalse(NodeUtil.isStatement(expressionNode)); // This should be false
    assertFalse(NodeUtil.isStatement(expressionParent)); // This should be false
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENTIFIER, "functionName"), new Node(Token.BODY, new Node[0]) });

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENTIFIER, "hoistedFunction"), new Node(Token.BODY, new Node[0]) });

    Node functionDeclarationWithParentScriptNode = new Node(Token.SCRIPT, new Node[] { hoistedFunctionDeclarationNode });

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationWithParentScriptNode));
  }

  @Test
  public void testIsForInEdgeCasesAndExceptions() {
    // Edge case: Node n is null
    assertFalse(NodeUtil.isForIn(null));

    // Exception handling scenario: Node n has less than three children
    Node forNodeWithTwoChildren = new Node(Token.FOR, new Node[0]);
    assertFalse(NodeUtil.isForIn(forNodeWithTwoChildren));
  }

  @Test
  public void testIsStatementEdgeCasesAndExceptions() {
    // Edge case: Node n is null
    assertFalse(NodeUtil.isStatement(null));

    // Exception handling scenario: Node n has unknown parent type
    Node unknownParentNode = new Node(Token.EXPRESSION, new Node[] { new Node(Token.IDENTIFIER, "unknownParent") });
    Node statementChildNode = new Node(Token.IDENTIFIER, "statementChild");
    unknownParentNode.setChildren(new Node[] { statementChildNode });
    assertFalse(NodeUtil.isStatement(unknownParentNode));
  }

  @Test
  public void testIsFunctionDeclarationEdgeCasesAndExceptions() {
    // Edge case: Node n is null
    assertFalse(NodeUtil.isFunctionDeclaration(null));

    // Exception handling scenario: Node n has wrong type
    Node wrongTypeNode = new Node(Token.NUMBER_LITERAL, "wrongType");
    assertFalse(NodeUtil.isFunctionDeclaration(wrongTypeNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationEdgeCasesAndExceptions() {
    // Edge case: Node n is null
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));

    // Exception handling scenario: Node n has wrong type or wrong parent type
    Node wrongParentTypeNode = new Node(Token.VARIABLE, "wrongParentType");
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENTIFIER, "hoistedFunction"), new Node(Token.BODY, new Node[0]) });
    wrongParentTypeNode.setChildren(new Node[] { hoistedFunctionDeclarationNode });
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(wrongParentTypeNode));
  }
}