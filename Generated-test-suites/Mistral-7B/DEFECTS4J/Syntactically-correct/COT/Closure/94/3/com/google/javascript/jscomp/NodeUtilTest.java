import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[]{1, 2, 3});
    Node parentOfStatement = Node.newNode(Token.BLOCK, new int[]{1});
    parentOfStatement.addChildToEnd(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.ASSIGN, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node node = Node.newNode(Token.BLOCK, new int[]{1});
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 2}, "myFunction");
    Node parentOfFunctionDeclaration = Node.newNode(Token.SCRIPT, new int[]{1});
    parentOfFunctionDeclaration.addChildToEnd(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 2});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 2}, "myFunction");
    Node parentOfHoistedFunctionDeclaration = Node.newNode(Token.SCRIPT, new int[]{1});
    parentOfHoistedFunctionDeclaration.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{1, 2}, "myFunction");
    Node parentOfNotHoistedFunctionDeclaration = Node.newNode(Token.BLOCK, new int[]{1});
    parentOfNotHoistedFunctionDeclaration.addChildToEnd(nodeNotHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}