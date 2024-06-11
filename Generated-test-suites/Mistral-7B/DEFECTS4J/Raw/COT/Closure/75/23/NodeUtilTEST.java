import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression("1+2");
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression("var x = 1;");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newExpression("1+2");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression("function myFunction() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0]));
    Node parentOfHoistedFunctionDeclaration = Node.newScript(new Node[0]);
    parentOfHoistedFunctionDeclaration.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction("outerFunction", new Node[0], Node.newBlock(Node.newExpression("return myFunction;"), nodeHoistedFunctionDeclaration));
    Node parentOfFunctionInFunction = Node.newFunction("myFunction", new Node[0], Node.newBlock(new Node[0]));
    Node parentOfFunction = Node.newScript(new Node[0]);
    parentOfFunction.addChildToEnd(nodeFunctionInFunction);
    parentOfFunction.addChildToEnd(parentOfFunctionInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateNotSatisfied() {
    Node node = Node.newExpression("1+2");
    NodeUtil.isStatement(node);
  }
}