import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Node.newNode(Token.FOR, new int[]{1, 2, 3});
    assertTrue(isForIn(node));

    Node invalidNode = Node.newNode(Token.IF, new int[]{1, 2, 3});
    assertFalse(isForIn(invalidNode));

    Node missingChildrenNode = Node.newNode(Token.FOR, new int[]{1});
    assertFalse(isForIn(missingChildrenNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[]{1, 2});
    assertTrue(isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.ADD, new int[]{1, 2, 3, 4});
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, new int[]{1, 2},
        Node.newNode("myFunction", new int[]{1, 2, 3}), Node.newNode(Token.BLOCK, new int[]{4}));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new int[]{1},
        Node.newNode("myFunction", new int[]{1, 2}), Node.newNode(Token.BLOCK, new int[]{4}));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(Token.FUNCTION, new int[]{1},
        Node.newNode("myFunction", new int[]{1, 2}), Node.newNode(Token.BLOCK, new int[]{4}));
    Node parentScriptNode = Node.newNode(Token.SCRIPT, new int[]{1, 2});
    Node grandParentFunctionNode = Node.newNode(Token.FUNCTION, new int[]{1}, null,
        Node.newNode(Token.BLOCK, new int[]{1}, parentScriptNode, Node.newNode(hoistedFunctionDeclarationNode.cloneTree(), null)));

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = hoistedFunctionDeclarationNode.cloneTree();
    notHoistedFunctionDeclarationNode.setParent(Node.newNode(Token.VAR, new int[]{1, 2}, Node.newNode("myVar"), null));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}