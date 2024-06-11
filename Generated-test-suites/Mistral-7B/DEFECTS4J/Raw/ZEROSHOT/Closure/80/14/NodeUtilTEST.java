import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR);
    nodeForIn.setLinearFrom(1);
    nodeForIn.setLinearTo(3);
    nodeForIn.setChildren(new Node[3]);

    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(IF);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT);
    nodeScript.setChildren(new Node[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = new Node(IDENTIFIER, "foo");
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeLabel = new Node(IDENTIFIER, "label");
    nodeLabel.setType(LABEL);
    assertTrue(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    nodeFunctionDeclaration.setChildren(new Node[2]);
    nodeFunctionDeclaration.getChildren()[0].setName("foo");
    nodeFunctionDeclaration.getChildren()[1].setChildren(new Node[0]);
    nodeFunctionDeclaration.getChildren()[1].setType(BLOCK);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION);
    nodeFunctionExpression.setChildren(new Node[1]);
    nodeFunctionExpression.getChildren()[0].setName("foo");
    nodeFunctionExpression.getChildren()[0].setType(EXPR);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(SCRIPT);
    nodeScript.setChildren(new Node[1]);
    Node nodeFunctionDeclaration = new Node(FUNCTION);
    nodeFunctionDeclaration.setChildren(new Node[2]);
    nodeFunctionDeclaration.getChildren()[0].setName("foo");
    nodeFunctionDeclaration.getChildren()[1].setChildren(new Node[0]);
    nodeFunctionDeclaration.getChildren()[1].setType(BLOCK);

    nodeScript.getChildren()[0] = nodeFunctionDeclaration;

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInBlock = new Node(FUNCTION);
    nodeFunctionDeclarationInBlock.setChildren(new Node[2]);
    nodeFunctionDeclarationInBlock.getChildren()[0].setName("foo");
    nodeFunctionDeclarationInBlock.getChildren()[1].setChildren(new Node[0]);
    nodeFunctionDeclarationInBlock.getChildren()[1].setType(BLOCK);

    Node nodeBlock = new Node(BLOCK);
    nodeBlock.setChildren(new Node[1]);
    nodeBlock.getChildren()[0] = nodeFunctionDeclarationInBlock;

    nodeScript.getChildren()[0] = nodeBlock;

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }
}