import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.setChildCount(3);
    assertTrue(isForIn(node));

    node.setType(Token.IF);
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    assertFalse(isStatement(node));

    node.setParent(new Node(Token.BLOCK));
    assertTrue(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node statementNode = new Node(Token.IF);

    functionNode.setName("testFunction");
    statementNode.setType(Token.IF);

    Preconditions.checkState(functionNode.getParent() == null);
    Preconditions.checkState(statementNode.getParent() != null);

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(statementNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setName("testFunction");
    functionNode.setParent(null);
    isFunctionDeclaration(functionNode);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setName("testFunction");
    functionNode.setParent(scriptNode);

    Node functionInFunctionNode = new Node(Token.FUNCTION);
    functionInFunctionNode.setName("testFunctionInFunction");
    functionInFunctionNode.setParent(functionNode);

    assertTrue(isHoistedFunctionDeclaration(functionNode));
    assertFalse(isHoistedFunctionDeclaration(functionInFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNoParent() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setName("testFunction");
    isHoistedFunctionDeclaration(functionNode);
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentFunction() {
    Node parentFunctionNode = new Node(Token.FUNCTION);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setName("testFunction");
    functionNode.setParent(parentFunctionNode);

    assertTrue(isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithParentScript() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setName("testFunction");
    functionNode.setParent(scriptNode);

    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }
}