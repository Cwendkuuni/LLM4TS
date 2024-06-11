import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node("for (var i in array) {}");
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node("for (var i = 0; i < 5; i++) {}");
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node("var x = 5;");
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node("5 + 3;");
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node node = new Node("");
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node("function foo() {}");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node("var foo = function() {}");
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = new Node("function foo() {}");
    Preconditions.checkState(nodeFunctionDeclaration.getParent() != null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node("function foo() {}");
    Node scriptNode = new Node("function bar() {}");
    Node functionNode = new Node("function foo() {}");
    Node hoistedFunctionDeclaration = new Node("function foo() {}", scriptNode);
    Node functionDeclarationInFunction = new Node("function bar() { function foo() {} }");

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionNode));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeHoistedFunctionDeclaration = new Node("function foo() {}", null);
    Preconditions.checkState(nodeHoistedFunctionDeclaration.getParent() != null);
  }
}