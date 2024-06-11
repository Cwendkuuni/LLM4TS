import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null, 0, 0);
    Node nodeFor = new Node(FOR, null, 0, 0);
    Node nodeIf = new Node(IF, null, 0, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, null, 0, 0);
    Node nodeBlock = new Node(BLOCK, null, 0, 0);
    Node nodeLabel = new Node(IDENT, "label", 0, 0);
    Node nodeFunction = new Node(FUNCTION, null, 0, 0);
    Node nodeIf = new Node(IF, null, 0, 0);
    Node nodeExpression = new Node(IDENT, "expression", 0, 0);

    Node parentScript = new Node(SCRIPT, null, 0, 0);
    Node parentBlock = new Node(BLOCK, null, 0, 0);
    Node parentLabel = new Node(LABEL, null, 0, 0);
    Node parentFunction = new Node(FUNCTION, null, 0, 0);
    Node parentIf = new Node(IF, null, 0, 0);
    Node parentExpression = new Node(EXPR_STATEMENT, null, 0, 0);

    nodeScript.setParent(parentScript);
    nodeBlock.setParent(parentBlock);
    nodeLabel.setParent(parentLabel);
    nodeFunction.setParent(parentFunction);
    nodeIf.setParent(parentIf);
    nodeExpression.setParent(parentExpression);

    parentScript.setParent(null);
    parentBlock.setParent(null);
    parentLabel.setParent(null);
    parentFunction.setParent(null);
    parentIf.setParent(null);
    parentExpression.setParent(null);

    // Test isStatement with script, block, and label nodes
    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));

    // Test isStatement with function and if nodes
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));

    // Test isStatement with expression statement
    assertTrue(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, 0, 0);
    Node nodeFunctionExpression = new Node(FUNCTION, null, 0, 0);
    Node nodeIf = new Node(IF, null, 0, 0);
    Node nodeScript = new Node(SCRIPT, null, 0, 0);

    Node parentFunctionDeclaration = new Node(BLOCK, null, 0, 0);
    Node parentFunctionExpression = new Node(EXPR_STATEMENT, null, 0, 0);
    Node parentIf = new Node(BLOCK, null, 0, 0);
    Node parentScript = new Node(SCRIPT, null, 0, 0);

    nodeFunctionDeclaration.setParent(parentFunctionDeclaration);
    nodeFunctionExpression.setParent(parentFunctionExpression);
    nodeIf.setParent(parentIf);
    nodeScript.setParent(parentScript);

    parentFunctionDeclaration.setParent(null);
    parentFunctionExpression.setParent(null);
    parentIf.setParent(null);
    parentScript.setParent(null);

    // Test isFunctionDeclaration
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    // Test isFunctionDeclaration with if statement
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIf));

    // Test isFunctionDeclaration with script
    assertFalse(NodeUtil.isFunctionDeclaration(nodeScript));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, 0, 0);
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, null, 0, 0);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, null, 0, 0);

    Node parentScript = new Node(SCRIPT, null, 0, 0);
    Node parentFunction = new Node(FUNCTION, null, 0, 0);

    nodeFunctionDeclaration.setParent(parentFunction);
    nodeFunctionDeclarationInScript.setParent(parentScript);
    nodeFunctionDeclarationInFunction.setParent(parentFunction);

    parentFunction.setParent(null);
    parentScript.setParent(null);

    // Test isHoistedFunctionDeclaration
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}