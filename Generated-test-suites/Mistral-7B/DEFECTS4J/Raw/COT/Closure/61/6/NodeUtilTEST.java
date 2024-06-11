import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i = 0; i < 10; i++) {}", FOR);
    assertTrue(NodeUtil.isForIn(node));

    Node invalidNode1 = new Node("if (true) {}", IF);
    assertFalse(NodeUtil.isForIn(invalidNode1));

    Node invalidNode2 = new Node("function myFunction() {}", FUNCTION);
    assertFalse(NodeUtil.isForIn(invalidNode2));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("if (true) {}", IF);
    assertFalse(NodeUtil.isStatement(node));

    Node statementNode = new Node("{}", BLOCK);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node functionStatementNode = new Node("function myFunction() {}", FUNCTION);
    assertTrue(NodeUtil.isStatement(functionStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node("function myFunction() {}", FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node("function myFunction() {}", FUNCTION_DECLARATION);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));

    Node ifStatementNode = new Node("if (true) {}", IF);
    assertFalse(NodeUtil.isFunctionDeclaration(ifStatementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node("function myFunction() {}", FUNCTION);
    Node hoistedFunctionDeclarationParentScriptNode = new Node("{}", SCRIPT);
    hoistedFunctionDeclarationNode.setParent(hoistedFunctionDeclarationParentScriptNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = new Node("function myFunction() {}", FUNCTION);
    Node functionDeclarationParentNode = new Node("if (true) {}", IF);
    functionDeclarationParentNode.addChild(nonHoistedFunctionDeclarationNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateThrowsExceptionWhenParentIsNull() {
    Node node = new Node("{}", BLOCK);
    NodeUtil.isStatement(node);
    Preconditions.checkState(node.getParent() != null);
  }
}