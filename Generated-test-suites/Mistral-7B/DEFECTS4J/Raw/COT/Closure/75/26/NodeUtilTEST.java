import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node validForInNode = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(isForIn(validForInNode));

    Node invalidForInNode = new Node(Token.ASSIGN, new int[]{}, new int[0]);
    assertFalse(isForIn(invalidForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[2]);
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.ASSIGN, new int[]{}, new int[1]);
    assertFalse(isStatement(expressionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParentNode() {
    Node node = new Node(Token.IF, new int[]{}, new int[2]);
    Preconditions.checkState(node.getParent() != null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node validFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    validFunctionDeclarationNode.setProperty("id", "testFunction");
    assertTrue(isFunctionDeclaration(validFunctionDeclarationNode));

    Node invalidFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    assertFalse(isFunctionDeclaration(invalidFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[0]));
    hoistedFunctionDeclarationNode.setProperty("id", "testFunction");
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    notHoistedFunctionDeclarationNode.setParent(new Node(Token.BLOCK, new int[]{}, new int[0]));
    notHoistedFunctionDeclarationNode.setProperty("id", "testFunction");
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNonFunctionParent() {
    Node node = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    node.setParent(new Node(Token.NUMBER, new int[]{}, new int[0]));
    assertFalse(isHoistedFunctionDeclaration(node));
  }
}