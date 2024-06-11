import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  // Test cases for isForIn() method

  @Test
  public void testIsForIn_WithValidForIn_ReturnsTrue() {
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(forInNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_WithInvalidNode_ReturnsFalse() {
    Node invalidNode = new Node(Token.WHILE);

    boolean result = NodeUtil.isForIn(invalidNode);

    Assert.assertFalse(result);
  }

  // Test cases for isStatement() method

  @Test
  public void testIsStatement_WithStatementParent_ReturnsTrue() {
    Node parent = new Node(Token.BLOCK);
    Node child = new Node(Token.EMPTY);
    parent.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_WithNonStatementParent_ReturnsFalse() {
    Node parent = new Node(Token.EXPR_RESULT);
    Node child = new Node(Token.EMPTY);
    parent.addChildToBack(child);

    boolean result = NodeUtil.isStatement(child);

    Assert.assertFalse(result);
  }

  // Test cases for isFunctionDeclaration() method

  @Test
  public void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnsTrue() {
    Node functionDeclaration = new Node(Token.FUNCTION);

    boolean result = NodeUtil.isFunctionDeclaration(functionDeclaration);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_WithNonFunctionDeclaration_ReturnsFalse() {
    Node nonFunctionDeclaration = new Node(Token.VAR);

    boolean result = NodeUtil.isFunctionDeclaration(nonFunctionDeclaration);

    Assert.assertFalse(result);
  }

  // Test cases for isHoistedFunctionDeclaration() method

  @Test
  public void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclaration_ReturnsTrue() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToBack(hoistedFunctionDeclaration);
    parent2.addChildToBack(parent1);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclaration_ReturnsFalse() {
    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.BLOCK);
    Node parent2 = new Node(Token.SWITCH);
    parent1.addChildToBack(nonHoistedFunctionDeclaration);
    parent2.addChildToBack(parent1);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration);

    Assert.assertFalse(result);
  }

}