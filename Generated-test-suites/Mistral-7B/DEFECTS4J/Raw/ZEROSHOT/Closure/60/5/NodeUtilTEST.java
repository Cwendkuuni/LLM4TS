import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR);
    nodeForIn.setChild(0, new Node(VAR));
    nodeForIn.setChild(1, new Node(IDENTIFIER, "var"));
    nodeForIn.setChild(2, new Node(ASSIGN, ASSIGN));
    nodeForIn.setChild(3, new Node(IDENTIFIER, "item"));
    nodeForIn.setChild(4, new Node(IN, IN));
    nodeForIn.setChild(5, new Node(IDENTIFIER, "myArray"));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(SCRIPT);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeFor = new Node(FOR);
    nodeFor.setChild(0, new Node(VAR));
    nodeFor.setChild(1, new Node(IDENTIFIER, "var"));
    nodeFor.setChild(2, new Node(ASSIGN, ASSIGN));
    nodeFor.setChild(3, new Node(IDENTIFIER, "item"));
    nodeFor.setChild(4, new Node(SEMICOLON, SEMICOLON));
    nodeFor.setChild(5, new Node(ASSIGN, ASSIGN));
    nodeFor.setChild(6, new Node(IDENTIFIER, "sum"));
    nodeFor.setChild(7, new Node(IDENTIFIER, "i"));
    nodeFor.setChild(8, new Node(LEFT_PAREN, LEFT_PAREN));
    nodeFor.setChild(9, new Node(IDENTIFIER, "0"));
    nodeFor.setChild(10, new Node(RIGHT_PAREN, RIGHT_PAREN));
    nodeFor.setChild(11, new Node(IN, IN));
    nodeFor.setChild(12, new Node(IDENTIFIER, "myArray"));
    nodeFor.setChild(13, new Node(SEMICOLON, SEMICOLON));
    nodeFor.setChild(14, new Node(ASSIGN, ASSIGN));
    nodeFor.setChild(15, new Node(IDENTIFIER, "sum"));
    nodeFor.setChild(16, new Node(PLUS, PLUS));
    nodeFor.setChild(17, new Node(IDENTIFIER, "i"));
    nodeFor.setChild(18, new Node(SEMICOLON, SEMICOLON));
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(IF);
    nodeStatement.setChild(0, new Node(IDENTIFIER, "x"));
    nodeStatement.setChild(1, new Node(LESS, LESS));
    nodeStatement.setChild(2, new Node(NUMBER, 5));
    nodeStatement.setChild(3, new Node(BLOCK));
    nodeStatement.getChild(3).setChild(0, new Node(RETURN, RETURN));
    nodeStatement.getChild(3).setChild(1, new Node(IDENTIFIER, "100"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(ASSIGN, ASSIGN);
    nodeNotStatement.setChild(0, new Node(IDENTIFIER, "x"));
    nodeNotStatement.setChild(1, new Node(PLUS, PLUS));
    nodeNotStatement.setChild(2, new Node(NUMBER, 1));
    nodeNotStatement.setChild(3, new Node(IDENTIFIER, "y"));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    nodeFunctionDeclaration.setChild(0, new Node(IDENTIFIER, "myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION);
    nodeFunctionExpression.setChild(0, new Node(IDENTIFIER, "myFunction"));
    nodeFunctionExpression.setChild(1, new Node(ASSIGN, ASSIGN));
    nodeFunctionExpression.setChild(2, new Node(IDENTIFIER, "result"));
    nodeFunctionExpression.setChild(3, new Node(BLOCK));
    nodeFunctionExpression.getChild(3).setChild(0, new Node(RETURN, RETURN));
    nodeFunctionExpression.getChild(3).setChild(1, new Node(NUMBER, 42));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(SCRIPT);
    nodeHoistedFunctionDeclaration.setChild(0, new Node(FUNCTION));
    nodeHoistedFunctionDeclaration.getChild(0).setChild(0, new Node(IDENTIFIER, "myFunction"));
    nodeHoistedFunctionDeclaration.getChild(0).setChild(1, new Node(BLOCK));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration.getChild(0)));

    Node nodeNotHoistedFunctionDeclaration = new Node(FUNCTION);
    nodeNotHoistedFunctionDeclaration.setChild(0, new Node(IDENTIFIER, "myFunction"));
    nodeNotHoistedFunctionDeclaration.setChild(1, new Node(ASSIGN, ASSIGN));
    nodeNotHoistedFunctionDeclaration.getChild(1).setChild(0, new Node(IDENTIFIER, "result"));
    nodeNotHoistedFunctionDeclaration.getChild(1).setChild(1, new Node(BLOCK));
    nodeNotHoistedFunctionDeclaration.getChild(1).getChild(0).setChild(0, new Node(RETURN, RETURN));
    nodeNotHoistedFunctionDeclaration.getChild(1).getChild(0).setChild(1, new Node(NUMBER, 42));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}