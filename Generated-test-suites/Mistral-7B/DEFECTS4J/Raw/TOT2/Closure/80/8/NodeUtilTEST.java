import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for in");
    assertTrue(NodeUtil.isForIn(node));

    Node nodeInvalid = new Node("not for in");
    assertFalse(NodeUtil.isForIn(nodeInvalid));

    Node nodeWithInvalidChildCount = new Node("for with wrong child count");
    nodeWithInvalidChildCount.setChildCount(2);
    assertFalse(NodeUtil.isForIn(nodeWithInvalidChildCount));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("statement");
    assertTrue(NodeUtil.isStatement(node));

    Node nodePartOfExpression = new Node("expression");
    assertFalse(NodeUtil.isStatement(nodePartOfExpression));

    Node nodeWithInvalidParent = new Node("statement with invalid parent");
    nodeWithInvalidParent.setParent(null);
    assertFalse(NodeUtil.isStatement(nodeWithInvalidParent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function declaration");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    Node nodeFunctionExpression = new Node("function expression");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeWithInvalidParent = new Node("function declaration with invalid parent");
    nodeWithInvalidParent.setParent(null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeWithInvalidParent));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function declaration in scope root");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    Node nodeFunctionInFunction = new Node("function declaration within a function");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));

    Node nodeWithInvalidParent = new Node("hoisted function declaration with invalid parent");
    nodeWithInvalidParent.setParent(new Node("not a script or function"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeWithInvalidParent));
  }
}