import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));

    node.setType(Token.IF);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(SCRIPT);
    Node child = new Node(IF);
    node.addChild(child);

    assertTrue(NodeUtil.isStatement(child));

    node.setType(VAR);
    assertFalse(NodeUtil.isStatement(child));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(IF);
    Preconditions.checkState(node.getParent() != null);
    assertFalse(NodeUtil.isStatement(node));
    node.setParent(null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(FUNCTION);
    Node nameNode = new Node(IDENTIFIER, "funcName");
    Node bodyNode = new Node(BLOCK);

    functionNode.addChild(nameNode);
    functionNode.addChild(bodyNode);
    functionNode.setLineNumber(1);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    functionNode.setType(FUNCTION_EXPRESSION);
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(FUNCTION);
    Node nameNode = new Node(IDENTIFIER, "funcName");
    Node bodyNode = new Node(BLOCK);

    Node scriptNode = new Node(SCRIPT);
    scriptNode.addChild(functionNode);

    functionNode.addChild(nameNode);
    functionNode.addChild(bodyNode);
    functionNode.setLineNumber(1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node parentFunctionNode = new Node(FUNCTION);
    parentFunctionNode.addChild(functionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node functionNode = new Node(FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    functionNode.setParent(null);
  }
}