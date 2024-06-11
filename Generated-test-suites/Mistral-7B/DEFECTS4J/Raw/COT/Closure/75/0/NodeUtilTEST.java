import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression("1 + 1");; // Replace with an invalid node
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression("1 + 1"); // Replace with a valid statement node
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNumber(1.0); // Replace with an invalid statement node
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = Node.newExpression("1 + 1"); // Replace with a node without a parent
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("foo");
    nodeFunctionDeclaration.setBody(Node.newBlock(Node.newExpression("1 + 1")));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = Node.newExpression("1 + 1");; // Replace with an invalid function node
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction("foo");
    Node scriptNode = Node.newScript("function bar() {}", nodeFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction("foo"); // Replace with an invalid hoisted function node
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}