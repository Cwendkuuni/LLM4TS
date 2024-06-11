import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeType.SCRIPT;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.FOR;
import static com.google.javascript.rhino.NodeType.FUNCTION;

import org.junit.Test;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node();
    node.setType(FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));

    node.setType(Token.IF);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node();
    Preconditions.checkState(node != null);

    node.setParent(new Node().setType(SCRIPT));
    assertTrue(NodeUtil.isStatement(node));

    node.setParent(new Node().setType(FUNCTION));
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node().setType(FUNCTION);
    node.setParent(new Node().setType(SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node().setType(FUNCTION);
    node.setParent(new Node().setType(BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node().setType(FUNCTION);
    node.setParent(new Node().setType(LABEL));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node().setType(FUNCTION);
    Node scriptNode = new Node().setType(SCRIPT);
    Node hoistedFunctionNode = new Node().setType(FUNCTION);

    functionNode.setParent(new Node().setType(BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    hoistedFunctionNode.setParent(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

    functionNode.setParent(scriptNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckState() {
    Node node = null;
    NodeUtil.isStatement(node);
  }
}