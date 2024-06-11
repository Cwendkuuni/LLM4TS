import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNodeForTest("for (var i in array) { }");
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = createNodeForTest("for (var i = 0; i < 5; i++) { }");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInWithLessChildren() {
    Node node = createNodeForTest("for (var i in array) { }");
    node.setChildCount(2);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInWithMoreChildren() {
    Node node = createNodeForTest("for (var i in array) { statement; }");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isForIn(node));
  }

  // ... Repeat the test cases for isStatement, isFunctionDeclaration and isHoistedFunctionDeclaration

  @Test
  public void testIsStatement() {
    Node node = createNodeForTest("if (condition) { statement; }");
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node node = createNodeForTest("1 + 2;"); // Expression
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementWithNullParent() {
    Node node = createNodeForTest("if (condition) { statement; }");
    node.setParent(null);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementWithNonScriptParent() {
    Node node = createNodeForTest("if (condition) { statement; }");
    Node parent = createNodeForTest("return statement;");
    node.setParent(parent);
    assertFalse(NodeUtil.isStatement(node));
  }

  // ... Repeat the test cases for isFunctionDeclaration and isHoistedFunctionDeclaration

  // ... Add test cases for edge cases and exception handling scenarios for other methods

  private Node createNodeForTest(String javaScriptCode) {
    // Implement the logic to create Node object from given Java Script code
  }
}