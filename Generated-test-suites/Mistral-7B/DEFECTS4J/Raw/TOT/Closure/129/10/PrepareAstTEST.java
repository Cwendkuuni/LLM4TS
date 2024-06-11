import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrepareAstTest {

  @Test
  public void testPrepareAstNormalizesNodeTypes() {
    // Set up the test environment
    PrepareAst prepareAst = new PrepareAst(null, false);

    // Create a sample AST
    Node root = IR.program();
    Node objectLit = IR.objectLit();
    IR.setProperty(objectLit, "a", IR.numberLiteral(1));
    IR.setProperty(objectLit, "b", IR.stringLiteral("hello"));
    root.addChild(objectLit);

    // Perform the test
    prepareAst.process(null, root);

    // Check the results
    Node expectedResult = IR.program();
    Node expectedObjectLit = IR.block().srcref(objectLit);
    expectedObjectLit.addChild(IR.objectLit());
    IR.setProperty(expectedObjectLit, "a", IR.numberLiteral(1));
    IR.setProperty(expectedObjectLit, "b", IR.stringLiteral("hello"));
    expectedResult.addChild(expectedObjectLit);

    // Assert the results are equal
    assertTrue(compareNodes(root, expectedResult));
  }

  @Test
  public void testPrepareAstNormalizesControlStructures() {
    // Set up the test environment
    PrepareAst prepareAst = new PrepareAst(null, false);

    // Create a sample AST
    Node root = IR.program();
    Node ifStatement = IR.ifStatement();
    Node condition = IR.literal(true);
    Node statement = IR.printString("This is an if statement.");
    ifStatement.addChild(condition);
    ifStatement.addChild(statement);
    root.addChild(ifStatement);

    // Perform the test
    prepareAst.process(null, root);

    // Check the results
    Node expectedResult = IR.program();
    Node expectedIfStatement = IR.ifStatement();
    Node expectedCondition = IR.booleanLiteral(true);
    Node expectedStatement = IR.expressionStatement(IR.callMethod(
        IR.stringLiteral("print"), IR.stringLiteral("This is an if statement.")));
    expectedIfStatement.addChild(expectedCondition);
    expectedIfStatement.addChild(expectedStatement);
    expectedResult.addChild(expectedIfStatement);

    // Assert the results are equal
    assertTrue(compareNodes(root, expectedResult));
  }

  @Test
  public void testPrepareAstDoesNotChangeNodesDuringChecks() {
    // Set up the test environment
    PrepareAst prepareAst = new PrepareAst(null, true);

    // Create a sample AST
    Node root = IR.program();
    Node objectLit = IR.objectLit();
    IR.setProperty(objectLit, "a", IR.numberLiteral(1));
    IR.setProperty(objectLit, "b", IR.stringLiteral("hello"));
    root.addChild(objectLit);

    // Attempt to perform the test
    prepareAst.process(null, root);

    // Assert the test fails due to the normalizeNodeTypes() method's
    // violation of the checkOnly constraint
    assertFalse(true);
  }

  /**
   * Helper method to compare two nodes for equality, recursively.
   */
  private static boolean compareNodes(Node a, Node b) {
    if (a == b) {
      return true;
    }

    if (a == null || b == null) {
      return false;
    }

    if (a.getType() != b.getType()) {
      return false;
    }

    switch (a.getType()) {
      case Token.PROGRAM:
      case Token.BLOCK:
        return compareNodes((Node) a.getFirstChild(), (Node) b.getFirstChild())
            && compareNodes((Node) a.getLastChild(), (Node) b.getLastChild());
      case Token.OBJECT_LIT:
        return compareNodes((Node) a.getFirstChild(), (Node) b.getFirstChild());
      case Token.STRING_LITERAL:
        return a.getString().equals(b.getString());
      case Token.NUMBER_LITERAL:
        return a.getNumber() == b.getNumber();
      case Token.BOOLEAN_LITERAL:
        return a.getBooleanValue() == b.getBooleanValue();
      case Token.NULL_LITERAL:
        return a.isNullLiteral() == b.isNullLiteral();
      case Token.CALL:
        return compareNodes(a.getFirstChild(), b.getFirstChild())
            && compareNodes(a.getLastChild(), b.getLastChild());
      case Token.NAME:
        return a.getString().equals(b.getString());
      case Token.KEYWORD:
        return a.getString().equals(b.getString());
      case Token.PUNCTUATION:
        return a.getType() == b.getType();
      case Token.ASSIGN:
        return compareNodes(a.getFirstChild(), b.getFirstChild())
            && compareNodes(a.getLastChild(), b.getLastChild());
      case Token.LPAREN:
      case Token.RPAREN:
        return true;
      case Token.LBRACE:
      case Token.RBRACE:
        return true;
      case Token.SEMICOLON:
        return true;
      case Token.COMMA:
        return true;
      case Token.DIVIDE:
      case Token.MODULO:
      case Token.ADD:
      case Token.SUBTRACT:
      case Token.MULTIPLY:
      case Token.NEGATION:
      case Token.NOT:
      case Token.LT:
      case Token.LE:
      case Token.GT:
      case Token.GE:
      case Token.EQ:
      case Token.NE:
        return true;
      case Token.AND:
      case Token.OR:
        return compareNodes(a.getFirstChild(), b.getFirstChild())
            && compareNodes(a.getLastChild(), b.getLastChild());
      case Token.THIS:
      case Token.NEW:
      case Token.VAR:
      case Token.IF:
      case Token.ELSE:
      case Token.ELSEIF:
      case Token.WHILE:
      case Token.DO:
      case Token.FOR:
      case Token.SWITCH:
      case Token.CASE:
      case Token.DEFAULT:
      case Token.TRY:
      case Token.CATCH:
      case Token.FINALLY:
      case Token.THROW:
      case Token.WITH:
      case Token.BREAK:
      case Token.CONTINUE:
      case Token.RETURN:
        return true;
      case Token.LABEL:
        return a.getString().equals(b.getString());
      case Token.TYPEOF:
      case Token.INSTANCEOF:
        return true;
      case Token.NEW_ARRAY:
      case Token.ARRAY_LITERAL:
        return compareNodes(a.getFirstChild(), b.getFirstChild());
      case Token.ASSIGN_ADD:
      case Token.ASSIGN_SUB:
      case Token.ASSIGN_MUL:
      case Token.ASSIGN_DIV:
      case Token.ASSIGN_MOD:
      case Token.ASSIGN_AND:
      case Token.ASSIGN_OR:
      case Token.BITWISE_AND:
      case Token.BITWISE_XOR:
      case Token.BITWISE_OR:
      case Token.LEFT_SHIFT:
      case Token.RIGHT_SHIFT:
        return true;
      case Token.SQUARE_BRACKET:
        return true;
      case Token.DOT:
        return compareNodes(a.getFirstChild(), b.getFirstChild());
      case Token.OBJECT_PROPERTY_ACCESS:
        return compareNodes(a.getFirstChild(), b.getFirstChild())
            && compareNodes(a.getLastChild(), b.getLastChild());
      case Token.METHOD_CALL:
        return compareNodes(a.getFirstChild(), b.getFirstChild())
            && compareNodes(a.getLastChild(), b.getLastChild());
      case Token.PROPERTY_ACCESS:
        return compareNodes(a.getFirstChild(), b.getFirstChild());
      case Token.QUALIFIED_NAME:
        return compareNodes(a.getFirstChild(), b.getFirstChild());
      default:
        throw new IllegalArgumentException("Unknown node type: " + a.getType());
    }
  }
}