import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(FOR, new int[] {5, 6, 7});
    Node notForNode = new Node(IF, new int[] {1, 2, 3});

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(IF, new int[] {1, 2, 3});
    Node expressionNode = new Node(PLUS, new int[] {1, 2});

    Node parentStatement = new Node(BLOCK, new int[] {});
    parentStatement.addChildToEnd(statementNode);
    parentStatement.addChildToEnd(expressionNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));

    Node scriptNode = new Node(SCRIPT, new int[] {});
    scriptNode.addChildToEnd(parentStatement);

    // Since scriptNode is a statement, isStatement should return true for any
    // node in the script
    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(FUNCTION, new int[] {1, 2, 3, 4});
    functionDeclaration.addChildToEnd(new Node(ID, new int[] {5}));

    Node functionExpression = new Node(FUNCTION, new int[] {1, 2, 3});
    functionExpression.addChildToEnd(new Node(ID, new int[] {5}));

    // FunctionDeclaration has a name, FunctionExpression does not
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(FUNCTION, new int[] {1, 2, 3, 4});
    hoistedFunctionDeclaration.addChildToEnd(new Node(ID, new int[] {5}));

    Node functionDeclarationInFunction = new Node(FUNCTION, new int[] {1, 2, 3, 4});
    functionDeclarationInFunction.addChildToEnd(new Node(ID, new int[] {5}));

    Node functionDeclarationInScript = new Node(FUNCTION, new int[] {1, 2, 3, 4});
    functionDeclarationInScript.addChildToEnd(new Node(ID, new int[] {5}));

    Node functionInFunction = new Node(FUNCTION, new int[] {1, 2, 3});
    functionInFunction.addChildToEnd(new Node(ID, new int[] {5}));

    // HoistedFunctionDeclaration is in the scope root (Script)
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInFunction));
  }
}