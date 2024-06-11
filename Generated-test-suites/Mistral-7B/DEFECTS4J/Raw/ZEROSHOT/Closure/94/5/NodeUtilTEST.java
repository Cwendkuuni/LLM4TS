import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{1, 2, 3});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3});
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.appendChild(nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3, 4});
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3});
    assertNull(nodeStatement.getParent());
    isStatement(nodeStatement);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"a"}, new int[]{1, 2, 3});
    nodeFunctionDeclaration.setProperty("id", "myFunction");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"a"}, new int[]{1, 2, 3});
    nodeHoistedFunctionDeclaration.setProperty("id", "myFunction");
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.appendChild(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"a"}, new int[]{1, 2, 3});
    nodeFunctionDeclarationInFunction.setProperty("id", "anInnerFunction");
    Node parentFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    parentFunction.setProperty("id", "myFunction");
    parentFunction.appendChild(nodeFunctionDeclarationInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    isFunctionDeclaration(null);
  }
}