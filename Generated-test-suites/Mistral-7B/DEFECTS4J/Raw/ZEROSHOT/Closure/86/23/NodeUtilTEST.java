import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(1, null, null, null, null, null);
    assertTrue(isForIn(forNode));

    Node notForNode = Node.newExpressionStatement(Node.newName("foo"));
    assertFalse(isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newName("foo"));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newIdent("foo");
    assertFalse(isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(0, null, null, null);
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction(0, null, null, null));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newFunction(0, null, null, null);
    hoistedFunctionDeclarationNode.setParent(Node.newScript());
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = Node.newFunction(0, null, null, null);
    notHoistedFunctionDeclarationNode.setParent(Node.newExpressionStatement(Node.newName("foo")));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }

  @Test
  public void testPreconditionsCheckState() {
    Node node = Node.newExpressionStatement(Node.newName("foo"));
    Node parent = Node.newBlock();
    parent.setChildren(node);

    Preconditions.checkState(node.getParent() == null, "Node has a parent.");
    Preconditions.checkState(parent != null, "Node's parent is null.");
  }
}