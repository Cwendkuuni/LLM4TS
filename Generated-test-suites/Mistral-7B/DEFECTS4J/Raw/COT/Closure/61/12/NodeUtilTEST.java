import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    Node nodeWithThreeChildren = new Node(Token.IF, new Node[3]);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeWithThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null);
    Node nodeBlock = new Node(Token.BLOCK, null);
    Node nodeLabel = new Node(Token.LABEL, "label");
    Node nodeFunction = new Node(Token.FUNCTION, null);
    Node nodeExpression = new Node(Token.IDENT, "ident");

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertFalse(isStatement(nodeFunction));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new Node[0]);
    Node nodeFunctionWithName = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENT, "name")});

    assertFalse(isFunctionDeclaration(nodeFunction));
    assertTrue(isFunctionDeclaration(nodeFunctionWithName));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new Node[0]);
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new Node[0], new Node(Token.SCRIPT, null));
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new Node[0], new Node(Token.FUNCTION, null));

    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateFailure() {
    Node node = new Node(Token.SCRIPT, null);
    Preconditions.checkState(node.getParent() == null);
  }

  @Test
  public void testIsForInWithNullNode() {
    assertFalse(isForIn(null));
  }

  @Test
  public void testIsStatementWithNullNode() {
    Node node = new Node(Token.SCRIPT, null);
    Preconditions.checkState(node != null);

    Node parent = new Node(Token.SCRIPT, null);
    node.setParent(parent);

    assertTrue(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = new Node(Token.FUNCTION, null);
    Preconditions.checkState(node != null);

    Node parent = new Node(Token.SCRIPT, null);
    node.setParent(parent);

    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithTwoChildren() {
    Node node = new Node(Token.FUNCTION, new Node[2]);
    Node parent = new Node(Token.SCRIPT, null);
    node.setParent(parent);

    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithThreeChildren() {
    Node node = new Node(Token.FUNCTION, new Node[3]);
    Node parent = new Node(Token.SCRIPT, null);
    node.setParent(parent);

    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node node = new Node(Token.FUNCTION, null);
    Preconditions.checkState(node != null);

    assertTrue(isHoistedFunctionDeclaration(node)); // Should not happen
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNonScriptParent() {
    Node node = new Node(Token.FUNCTION, null);
    Node parent = new Node(Token.IF, null);
    node.setParent(parent);

    assertFalse(isHoistedFunctionDeclaration(node));
  }
}