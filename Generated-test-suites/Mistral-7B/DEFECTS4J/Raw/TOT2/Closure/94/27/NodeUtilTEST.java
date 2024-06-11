import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node node;

  @Before
  public void setUp() {
    // Initialize node for testing
    // ...
  }

  @Test
  public void testIsForIn() {
    node = createNodeForTest("for (var i in array) { ... }");
    assertTrue(NodeUtil.isForIn(node));

    node = createNodeForTest("for (var i = 0; i < array.length; i++) { ... }");
    assertFalse(NodeUtil.isForIn(node));

    node = createNodeForTest("function forInFunction() { for (var i in array) { ... } }");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    node = createNodeForTest("1 + 1;");
    assertTrue(NodeUtil.isStatement(node));

    node = createNodeForTest("function statementFunction() { return 1; }");
    assertTrue(NodeUtil.isStatement(node));

    node = createNodeForTest("if (true) { 1 + 1; }");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    node = createNodeForTest("function functionDeclaration(arg) { return arg; }");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = createNodeForTest("function () { return 1; }");
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    node = createNodeForTest("var functionDeclaration = function(arg) { return arg; }");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    node = createNodeForTest("function hoistedFunctionDeclaration() { return 1; }");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = createNodeForTest("function scriptFunction() { function hoistedFunctionDeclaration() { return 1; } }");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  private Node createNodeForTest(String jsCode) {
    // Create and return a Node instance for the given JS code
    // ...
  }
}