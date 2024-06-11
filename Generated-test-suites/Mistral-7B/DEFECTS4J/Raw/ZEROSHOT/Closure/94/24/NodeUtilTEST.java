import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.testing.TestUtils;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = TestUtils.createNodeFromString("for (var i in array) {}");
    Node nodeFor = TestUtils.createNodeFromString("for (var i = 0; i < array.length; i++) {}");
    Node nodeIf = TestUtils.createNodeFromString("if (condition) {}");

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeFor));
    assertFalse(isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = TestUtils.createNodeFromString("var x = 1;");
    Node nodeFunction = TestUtils.createNodeFromString("function foo() {}");
    Node nodeIf = TestUtils.createNodeFromString("if (condition) {}", TestUtils.createNodeFromString("// comment"));

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeFunction));
    assertFalse(isStatement(nodeIf.getChild(1)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = TestUtils.createNodeFromString("function foo() {}");
    Node nodeFunctionExpression = TestUtils.createNodeFromString("var foo = function() {}");

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = TestUtils.createNodeFromString("function foo() {}", null, null, null, 0);
    Node nodeFunction = TestUtils.createNodeFromString("function foo() {}", null, null, null, 1);
    Node nodeFunctionDeclarationInFunction = TestUtils.createNodeFromString("function bar() { function foo() {} }", null, null, null, 0);

    assertTrue(isHoistedFunctionDeclaration(nodeScript));
    assertTrue(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}