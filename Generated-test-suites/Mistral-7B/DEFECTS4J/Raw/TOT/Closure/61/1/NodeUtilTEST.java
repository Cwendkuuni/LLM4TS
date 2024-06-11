import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(Token.FOR, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isForIn(node));

    node = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode(Token.ADD, new int[]{}, new Node[]{}, null);
    Node parent = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    node.setParent(parent);
    assertTrue(NodeUtil.isStatement(node));

    node = Node.newNode(Token.ADD, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node statementNode = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    Node parent = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    statementNode.setParent(parent);
    functionNode.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(statementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    functionNode.setParent(parentFunction);
    parentFunction.setParent(parentScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node functionNode2 = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node parentStatement = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    functionNode2.setParent(parentStatement);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
  }

  @Test
  public void testIsForInvalidNodeType() {
    Node node = Node.newNode(Token.IDENT, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatementInvalidParentType() {
    Node node = Node.newNode(Token.ADD, new int[]{}, new Node[]{}, null);
    Node parent = Node.newNode(Token.RETURN, new int[]{}, new Node[]{}, null);
    node.setParent(parent);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclarationInvalidParentType() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node parent = Node.newNode(Token.ASSIGN, new int[]{}, new Node[]{}, null);
    functionNode.setParent(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }
}