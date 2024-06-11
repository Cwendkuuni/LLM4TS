import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    Node nodeNotStatement = Node.newIdentifier("x");

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("func", new Node[]{}, Node.newBlock(null, null, Node.newName("x"), Node.newExpressionStatement(Node.newName("x").set("1"))), null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(Node.newName("func"), new Node[]{Node.newName("x")}, Node.newBlock(null, null, Node.newName("x").set("1"), null), null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = Node.newFunctionDeclaration("func", new Node[]{}, Node.newBlock(null, null, Node.newName("x"), Node.newExpressionStatement(Node.newName("x").set("1"))), null);
    Node nodeFunctionDeclarationInFunction = Node.newFunctionDeclaration("func", new Node[]{}, Node.newBlock(null, null, Node.newName("x"), Node.newExpressionStatement(Node.newName("x").set("1"))), Node.newName("outerFunc"));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationInBlock = Node.newFunctionDeclaration("func", new Node[]{}, Node.newBlock(Node.newName("outerBlock"), null, Node.newName("x"), Node.newExpressionStatement(Node.newName("x").set("1"))), null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }
}