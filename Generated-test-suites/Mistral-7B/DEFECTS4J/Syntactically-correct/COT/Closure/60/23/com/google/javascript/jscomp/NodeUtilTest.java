import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null);
    Node nodeForInWithThreeChildren = new Node(FOR, new Object[]{NULL, NULL, NULL});
    Node nodeNotForIn = new Node(IF, null);

    assertTrue(NodeUtil.isForIn(nodeForInWithThreeChildren));
    assertFalse(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(IF, null);
    Node nodeParentScript = new Node(SCRIPT, null);
    Node nodeParentBlock = new Node(BLOCK, null);
    Node nodeParentLabel = new Node(LABEL, "label");
    Node nodeFunction = new Node(FUNCTION, null);
    Node nodeExpression = new Node(IDENT, "identifier");

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeParentScript));
    assertTrue(NodeUtil.isStatement(nodeParentBlock));
    assertFalse(NodeUtil.isStatement(nodeParentLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new Object[]{"functionName", NULL, NULL});
    Node nodeFunctionExpression = new Node(IDENT, "functionName");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentNullError() {
    Node node = new Node(IDENT, "identifier");
    Node nullParent = null;
    Node parent = nullParent;

    NodeUtil.isStatement(node);
    Preconditions.checkState(parent != null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(SCRIPT, null);
    Node nodeFunctionDeclaration = new Node(FUNCTION, new Object[]{"functionName", NULL, NULL});
    Node nodeFunction = new Node(FUNCTION, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInScript = new Node(FUNCTION, new Object[]{"functionName", NULL, NULL}, nodeScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  @Test
  public void testIsFunctionDeclarationWithParentFunction() {
    Node nodeParentFunction = new Node(FUNCTION, null);
    Node nodeFunctionDeclaration = new Node(FUNCTION, new Object[]{"functionName", NULL, NULL}, nodeParentFunction);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}