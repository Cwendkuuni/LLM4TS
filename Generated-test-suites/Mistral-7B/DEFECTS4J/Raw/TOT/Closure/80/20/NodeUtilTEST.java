import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(0, null, null, null, null);
    assertTrue(isForIn(forNode));

    Node ifNode = Node.newIfStatement(null, null, null, null);
    assertFalse(isForIn(ifNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(isStatement(statementNode));

    Node expressionNode = Node.newExpression(Node.newNumberLiteral(1));
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunction(Node.newName("funcName"), null, Node.SCRIPT_BLOCK);
    assertTrue(isFunctionDeclaration(functionNode));

    Node functionExpressionNode = Node.newExpressionFunction(Node.newName("funcName"), null, null);
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(new Node[]{
        Node.newFunction(Node.newName("funcName"), null, Node.SCRIPT_BLOCK)
    });
    Node functionNode = scriptNode.getFirstChild();
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node blockNode = Node.newBlock(new Node[]{functionNode});
    Node functionInBlockNode = blockNode.getFirstChild().getFirstChild();
    assertFalse(isHoistedFunctionDeclaration(functionInBlockNode));
  }
}