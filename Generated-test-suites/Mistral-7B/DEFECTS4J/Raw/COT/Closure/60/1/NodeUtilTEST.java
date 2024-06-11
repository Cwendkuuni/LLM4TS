import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR);
    nodeForIn.setChild(0, new Node(IDENTIFIER, "for"));
    nodeForIn.setChild(1, new Node(ASSIGN, null, new Node(VAR, null, new Node(IDENTIFIER, "i")), new Node(NUM, 0)));
    nodeForIn.setChild(2, new Node(BLOCK, null, new Node(STMT, null, new Node(PRINT, null, new Node(IDENTIFIER, "i")))));

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(ASSIGN);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(IDENTIFIER, "statement");

    Node parentScript = new Node(SCRIPT, null);
    parentScript.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(ASSIGN);

    Node parentExpression = new Node(ASSIGN);
    parentExpression.setChild(0, nodeNotStatement);

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, new Node(IDENTIFIER, "functionDeclaration"), new Node(BLOCK, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(ASSIGN);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, new Node(IDENTIFIER, "functionDeclaration"), new Node(BLOCK, null));
    Preconditions.checkState(nodeFunctionDeclaration.getParent() != null);

    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(FUNCTION, null, new Node(IDENTIFIER, "hoistedFunctionDeclaration"), new Node(BLOCK, null));
    Node parentScript = new Node(SCRIPT, null);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node(FUNCTION, null, new Node(IDENTIFIER, "notHoistedFunctionDeclaration"), new Node(BLOCK, null));
    Node parentFunction = new Node(FUNCTION, null, null, null);
    parentFunction.setChild(0, nodeNotHoistedFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeHoistedFunctionDeclaration = new Node(FUNCTION, null, new Node(IDENTIFIER, "hoistedFunctionDeclaration"), new Node(BLOCK, null));

    NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration);
  }
}