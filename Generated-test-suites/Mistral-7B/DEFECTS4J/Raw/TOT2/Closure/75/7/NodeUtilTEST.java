import static org.junit.Assert.assertEquals;
import static com.google.javascript.rhino.NodeFactory.createNode;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, null, null, null, null, null);
    Node notForNode = createNode(Token.IF, null, null, null, null, null);
    assertEquals(true, NodeUtil.isForIn(forNode));
    assertEquals(false, NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.RETURN, null, null, null, null, null);
    Node expressionNode = createNode(Token.IF, null, null, null, null, null);
    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, "func", null, null, null, null);
    Node expressionFunctionNode = createNode(Token.FUNCTION, null, null, null, null, null);
    assertEquals(true, NodeUtil.isFunctionDeclaration(functionNode));
    assertEquals(false, NodeUtil.isFunctionDeclaration(expressionFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(Token.FUNCTION, "func", null, null, null, null);
    Node notHoistedFunctionNode = createNode(Token.FUNCTION, "func", null, null, null, null);
    Node scriptNode = createNode(Token.SCRIPT, null, null, null, null, null);
    Node functionInsideFunctionNode = createNode(Token.FUNCTION, "func2", null, null, null, null);
    scriptNode.setChild(0, hoistedFunctionNode);
    scriptNode.setChild(1, functionInsideFunctionNode);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionNode));
  }
}