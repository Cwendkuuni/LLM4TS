import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) break;" +
        "    console.log(i);" +
        "  }" +
        "}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that there is no break statement after the loop.
    Node expectedOutput = IR.parseStatementText("function test() { for (var i = 0; i < 10; i++) { if (i % 2 === 0) {} console.log(i); } }");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isBreakStatement()));

    input = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) continue;" +
        "    console.log(i);" +
        "  }" +
        "}";

    node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that there is no continue statement after the loop.
    expectedOutput = IR.parseStatementText("function test() { for (var i = 0; i < 10; i++) { if (i % 2 === 0) {} console.log(i); } }");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isContinueStatement()));

    input = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) return;" +
        "    console.log(i);" +
        "  }" +
        "  console.log('Should not reach here');" +
        "}";

    node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the 'Should not reach here' statement is removed.
    expectedOutput = IR.parseStatementText("function test() { for (var i = 0; i < 10; i++) { if (i % 2 === 0) return; console.log(i); } }");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("console.log('Should not reach here')")));
  }

  @Test
  public void testMinimizeExitPointsNestedIf() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "function test() {" +
        "  if (true) {" +
        "    if (true) break;" +
        "  }" +
        "  if (true) console.log('Reached second if');" +
        "}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the second if statement and its console.log statement are removed.
    Node expectedOutput = IR.parseStatementText("function test() { if (true) { if (true) {} } }");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("if (true) console.log('Reached second if')")));
  }

  @Test
  public void testMinimizeExitPointsLabeledBreak() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "label1: function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) break label1;" +
        "    console.log(i);" +
        "  }" +
        "}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the break statement is removed and the 'console.log' statement remains.
    Node expectedOutput = IR.parseStatementText("function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) {}" +
        "    console.log(i);" +
        "  }" +
        "}");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertTrue(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("console.log(i)")));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("break label1")));
  }

  @Test
  public void testMinimizeExitPointsLabeledContinue() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "label1: function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) continue label1;" +
        "    console.log(i);" +
        "  }" +
        "}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the continue statement is removed and the 'console.log' statement remains.
    Node expectedOutput = IR.parseStatementText("function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 === 0) {}" +
        "    console.log(i);" +
        "  }" +
        "}");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertTrue(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("console.log(i)")));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("continue label1")));
  }

  @Test
  public void testMinimizeExitPointsUnreachableCode() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "function test() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  console.log('Reached unreachable code');" +
        "}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the unreachable console.log statement is removed.
    Node expectedOutput = IR.parseStatementText("function test() { if (true) return; }");
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("console.log('Reached unreachable code')")));
  }

  @Test
  public void testMinimizeExitPointsUnknownControlFlowStatement() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "function test() {" +
        "  if (true) x:" +
        "  console.log('Reached unknown control flow statement');" +
        "}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the unknown control flow statement is left unchanged.
    Node expectedOutput = IR.parseLabeledStatement(input, "test");
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isLabeledStatement(actualOutput, "test"));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertTrue(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isStatementText("console.log('Reached unknown control flow statement')")));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild(), NodeUtil.isLabelStatement()));
  }

  @Test
  public void testMinimizeExitPointsEmptyBlock() {
    Compiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    String input = "function test() {}";

    Node node = IR.parseLabeledStatement(input, "test");
    minimizeExitPoints.process(null, node);

    // Check that the empty block remains unchanged.
    Node expectedOutput = IR.parseStatementText(input);
    Node actualOutput = node;
    for (Node n = node.getFirstChild(); n != null; n = n.getNext()) {
      actualOutput = n;
    }
    assertTrue(NodeUtil.isFunctionDeclaration(expectedOutput.getLastChild()));
    assertTrue(NodeUtil.isFunctionDeclaration(actualOutput));
    assertTrue(NodeUtil.isBlock(actualOutput.getLastChild()));
    assertFalse(NodeUtil.hasStatement(actualOutput.getLastChild()));
  }
}