import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNodeForTest("for (var i in arr) {}");
    assertTrue("IsForIn: Expected for in statement", NodeUtil.isForIn(nodeForIn));

    Node nodeFor = createNodeForTest("for (var i = 0; i < 10; i++) {}");
    assertFalse("IsForIn: Expected not for in statement", NodeUtil.isForIn(nodeFor));

    Node nodeIf = createNodeForTest("if (condition) {}");
    assertFalse("IsForIn: Expected not for in statement", NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = createNodeForTest("var x = 5;");
    assertTrue("IsStatement: Expected statement", NodeUtil.isStatement(nodeScript));

    Node nodeExpression = createNodeForTest("5 + 5");
    assertFalse("IsStatement: Expected not statement", NodeUtil.isStatement(nodeExpression));

    Node nodeIf = createNodeForTest("if (condition) {}");
    assertTrue("IsStatement: Expected statement", NodeUtil.isStatement(nodeIf));

    Node nodeLabel = createNodeForTest("label1: {}");
    assertTrue("IsStatement: Expected statement", NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = createNodeForTest("function foo() {}");
    assertTrue("IsFunctionDeclaration: Expected function declaration", NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = createNodeForTest("function foo() {}();");
    assertFalse(
        "IsFunctionDeclaration: Expected not function declaration, "
            + "function expression instead",
        NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeIf = createNodeForTest("if (condition) function foo() {}");
    assertFalse("IsFunctionDeclaration: Expected not function declaration", NodeUtil.isFunctionDeclaration(nodeIf));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeFunction = createNodeForTest("function foo() {}");
    Preconditions.checkState(nodeFunction.getParent() != null);
    NodeUtil.isHoistedFunctionDeclaration(nodeFunction);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = createNodeForTest("function foo() {}");
    Node scriptNode = nodeFunction.getParent();
    assertTrue("IsHoistedFunctionDeclaration: Expected hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInScript = createNodeForTest("script function foo() {}");
    assertTrue("IsHoistedFunctionDeclaration: Expected hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node nodeFunctionInFunction = createNodeForTest("function bar() { function foo() {} }");
    assertTrue("IsHoistedFunctionDeclaration: Expected hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  private Node createNodeForTest(String jsSource) {
    RhinoCompiler compiler = new RhinoCompiler();
    Node rootNode = compiler.compile(jsSource);
    return rootNode;
  }
}