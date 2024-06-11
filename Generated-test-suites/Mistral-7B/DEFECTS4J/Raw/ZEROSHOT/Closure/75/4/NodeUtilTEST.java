import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, new int[] { 1, 2, 3 });
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(IF, new int[] { 1, 2, 3 });
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(RETURN, new int[] { 1 });
    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = createNode(Identifier, new int[] { 1 }, "id");
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(FunctionDeclaration, new int[] { 1 }, "name", new int[] { 2, 3 }, null, null);
    Preconditions.checkState(functionDeclarationNode.getChildCount() > 0);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node notFunctionDeclarationNode = createNode(Identifier, new int[] { 1 }, "name");
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createNode(FunctionDeclaration, new int[] { 1 }, "name", new int[] { 2, 3 }, null, null);
    Preconditions.checkState(hoistedFunctionDeclarationNode.getChildCount() > 0);
    Node scriptNode = createNode(Script, new int[] { 1 });
    scriptNode.addChild(hoistedFunctionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = createNode(FunctionDeclaration, new int[] { 1 }, "name", new int[] { 2, 3 }, null, null);
    Preconditions.checkState(notHoistedFunctionDeclarationNode.getChildCount() > 0);
    Node functionNode = createNode(Function, new int[] { 1, 2 }, null, null);
    functionNode.addChild(notHoistedFunctionDeclarationNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}