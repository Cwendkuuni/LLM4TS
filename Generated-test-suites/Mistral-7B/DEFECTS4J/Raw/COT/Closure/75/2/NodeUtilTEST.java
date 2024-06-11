import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null, null, null);
    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null);
    Node nodeExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null);
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertTrue(isStatement(nodeScript));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentStateFailure() {
    Node node = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null);
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, Node.newNode(Token.RETURN, null, null, null, null, null, null, null, null, null));
    Node nodeIfStatement = Node.newNode(Token.IF, null, null, null, null, null, null, null, null, null);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isFunctionDeclaration(nodeIfStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null, null, null);
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, Node.newNode(Token.FUNCTION, "outerFunction", null, null, null, null, null, null, null, null));

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInScriptWithParameters() {
    Node nodeFunctionDeclarationInScriptWithParameters = Node.newNode(Token.FUNCTION, "myFunction", Node.newNode("param1"), Node.newNode("param2"), null, null, null, null, null, null);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScriptWithParameters));
  }

  @Test
  public void testIsHoistedFunctionDeclarationInFunctionWithParameters() {
    Node nodeFunctionDeclarationInFunctionWithParameters = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, null, Node.newNode(Token.FUNCTION, "outerFunction", Node.newNode("param1"), Node.newNode("param2"), null, null, null, null, null, null));
    Node functionNode = nodeFunctionDeclarationInFunctionWithParameters.getFirstChild();
    assertTrue(isHoistedFunctionDeclaration(functionNode));
  }
}