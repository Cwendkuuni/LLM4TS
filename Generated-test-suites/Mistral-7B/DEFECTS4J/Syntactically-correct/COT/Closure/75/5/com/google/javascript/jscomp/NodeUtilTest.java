import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChildCount(3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new Node("message"));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, new Node(Token.TRUE));
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node("functionName"), new NodeList(), new NodeList());
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(Token.VAR, new Node("functionName"), new NodeList(), new NodeList());
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node("functionName"), new NodeList(), new NodeList());
    nodeScript.setFirstChild(nodeFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new Node("outerFunctionName"), new NodeList(), new NodeList());
    Node nodeInnerFunctionDeclaration = new Node(Token.FUNCTION, new Node("innerFunctionName"), new NodeList(), new NodeList());
    nodeFunctionDeclarationInFunction.setLastChild(nodeInnerFunctionDeclaration);
    assertFalse(isHoistedFunctionDeclaration(nodeInnerFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInNullNode() {
    Node nodeNull = null;
    isForIn(nodeNull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullNode() {
    Node nodeNull = null;
    isStatement(nodeNull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    Node nodeNull = null;
    isFunctionDeclaration(nodeNull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    Node nodeNull = null;
    isHoistedFunctionDeclaration(nodeNull);
  }
}