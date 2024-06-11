import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.JSType;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.init();
    compiler.options = new CompilerOptions();
    compiler.options.showErrors = false;
    nodeFactory = compiler.factory;
  }

  @Test
  public void testMinimizeExitPoints() {
    String sourceCode = "function test() {\n" +
        "  if (true) return;\n" +
        "  if (false) return;\n" +
        "  if (true) break;\n" +
        "  if (false) break;\n" +
        "  if (false) continue;\n" +
        "}";

    Node root = nodeFactory.parse(sourceCode);

    MinimizeExitPoints minExitPoints = new MinimizeExitPoints(compiler);
    traverse(minExitPoints, root, null);
    compiler.process(NodeUtil.getExterns(sourceCode), root);

    // Check that there are no explicit returns or breaks.
    Node testFunction = NodeUtil.getFunctionNode("test", root);
    assertTrue(testFunction.getLastChild().isBlock());
    Node testBlock = testFunction.getLastChild();
    assertFalse(testBlock.hasChild(NodeUtil.getExitNode(JSType.RETURN, testBlock)));
    assertFalse(testBlock.hasChild(NodeUtil.getExitNode(JSType.BREAK, testBlock)));
    assertFalse(testBlock.hasChild(NodeUtil.getExitNode(JSType.CONTINUE, testBlock)));

    // Test that a single return is not removed.
    String sourceCodeWithSingleReturn = "function test() {\n" +
        "  return;\n" +
        "}";

    Node rootWithSingleReturn = nodeFactory.parse(sourceCodeWithSingleReturn);
    MinimizeExitPoints minExitPointsWithSingleReturn = new MinimizeExitPoints(compiler);
    traverse(minExitPointsWithSingleReturn, rootWithSingleReturn, null);
    compiler.process(NodeUtil.getExterns(sourceCodeWithSingleReturn), rootWithSingleReturn);

    Node testFunctionWithSingleReturn = NodeUtil.getFunctionNode("test", rootWithSingleReturn);
    assertTrue(testFunctionWithSingleReturn.getLastChild().isBlock());
    Node testBlockWithSingleReturn = testFunctionWithSingleReturn.getLastChild();
    assertTrue(testBlockWithSingleReturn.hasChild(NodeUtil.getExitNode(JSType.RETURN, testBlockWithSingleReturn)));

    // Test that multiple returns are merged into a single return.
    String sourceCodeWithMultipleReturns = "function test() {\n" +
        "  return;\n" +
        "  return;\n" +
        "}";

    Node rootWithMultipleReturns = nodeFactory.parse(sourceCodeWithMultipleReturns);
    MinimizeExitPoints minExitPointsWithMultipleReturns = new MinimizeExitPoints(compiler);
    traverse(minExitPointsWithMultipleReturns, rootWithMultipleReturns, null);
    compiler.process(NodeUtil.getExterns(sourceCodeWithMultipleReturns), rootWithMultipleReturns);

    Node testFunctionWithMultipleReturns = NodeUtil.getFunctionNode("test", rootWithMultipleReturns);
    assertTrue(testFunctionWithMultipleReturns.getLastChild().isBlock());
    Node testBlockWithMultipleReturns = testFunctionWithMultipleReturns.getLastChild();
    assertTrue(testBlockWithMultipleReturns.hasChild(NodeUtil.getExitNode(JSType.RETURN, testBlockWithMultipleReturns)));

    // Test that multiple breaks are merged into a single break.
    String sourceCodeWithMultipleBreaks = "function test() {\n" +
        "  if (true) break;\n" +
        "  if (false) break;\n" +
        "}";

    Node rootWithMultipleBreaks = nodeFactory.parse(sourceCodeWithMultipleBreaks);
    MinimizeExitPoints minExitPointsWithMultipleBreaks = new MinimizeExitPoints(compiler);
    traverse(minExitPointsWithMultipleBreaks, rootWithMultipleBreaks, null);
    compiler.process(NodeUtil.getExterns(sourceCodeWithMultipleBreaks), rootWithMultipleBreaks);

    Node testFunctionWithMultipleBreaks = NodeUtil.getFunctionNode("test", rootWithMultipleBreaks);
    assertTrue(testFunctionWithMultipleBreaks.getLastChild().isBlock());
    Node testBlockWithMultipleBreaks = testFunctionWithMultipleBreaks.getLastChild();
    assertTrue(testBlockWithMultipleReturns.hasChild(NodeUtil.getExitNode(JSType.BREAK, testBlockWithMultipleBreaks)));

    // Test that a continue is not removed within a loop.
    String sourceCodeWithContinue = "function test() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i % 2 == 0) continue;\n" +
        "    print(i);\n" +
        "  }\n" +
        "}";

    Node rootWithContinue = nodeFactory.parse(sourceCodeWithContinue);
    MinimizeExitPoints minExitPointsWithContinue = new MinimizeExitPoints(compiler);
    traverse(minExitPointsWithContinue, rootWithContinue, null);
    compiler.process(NodeUtil.getExterns(sourceCodeWithContinue), rootWithContinue);

    Node testFunctionWithContinue = NodeUtil.getFunctionNode("test", rootWithContinue);
    assertTrue(testFunctionWithContinue.getLastChild().isBlock());
    Node testBlockWithContinue = testFunctionWithContinue.getLastChild();

    // Test that a continue is removed from outside a loop.
    String sourceCodeWithContinueOutsideLoop = "function test() {\n" +
        "  if (true) continue;\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    print(i);\n" +
        "  }\n" +
        "}";

    Node rootWithContinueOutsideLoop = nodeFactory.parse(sourceCodeWithContinueOutsideLoop);
    MinimizeExitPoints minExitPointsWithContinueOutsideLoop = new MinimizeExitPoints(compiler);
    traverse(minExitPointsWithContinueOutsideLoop, rootWithContinueOutsideLoop, null);
    compiler.process(NodeUtil.getExterns(sourceCodeWithContinueOutsideLoop), rootWithContinueOutsideLoop);

    Node testFunctionWithContinueOutsideLoop = NodeUtil.getFunctionNode("test", rootWithContinueOutsideLoop);
    assertTrue(testFunctionWithContinueOutsideLoop.getLastChild().isBlock());
    Node testBlockWithContinueOutsideLoop = testFunctionWithContinueOutsideLoop.getLastChild();
    assertFalse(testBlockWithContinueOutsideLoop.hasChild(NodeUtil.getExitNode(JSType.CONTINUE, testBlockWithContinueOutsideLoop)));
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    String sourceCode = "function test() {\n" +
        "  outer:\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i % 2 == 0) continue outer;\n" +
        "    print(i);\n" +
        "  }\n" +
        "}";

    Node root = nodeFactory.parse(sourceCode);

    MinimizeExitPoints minExitPoints = new MinimizeExitPoints(compiler);
    traverse(minExitPoints, root, null);
    compiler.process(NodeUtil.getExterns(sourceCode), root);

    // Check that the continue within a label is not removed.
    Node testFunction = NodeUtil.getFunctionNode("test", root);
    assertTrue(testFunction.getLastChild().isBlock());
    Node testBlock = testFunction.getLastChild();
    assertTrue(testBlock.hasChild(NodeUtil.getLabelNode("outer", testBlock)));
    Node labelBlock = testBlock.getChildWithLabel("outer");
    assertTrue(labelBlock.hasChild(NodeUtil.getExitNode(JSType.CONTINUE, labelBlock)));
  }

  @Test
  public void testMinimizeExitPointsWithException() {
    String sourceCode = "function test() {\n" +
        "  try {\n" +
        "    throw new Error(\"Test Error\");\n" +
        "  } catch (e) {\n" +
        "    print(\"Caught error: \" + e.message);\n" +
        "  } finally {\n" +
        "    print(\"Finally block executed\");\n" +
        "  } \n" +
        "}";

    Node root = nodeFactory.parse(sourceCode);

    MinimizeExitPoints minExitPoints = new MinimizeExitPoints(compiler);
    traverse(minExitPoints, root, null);
    compiler.process(NodeUtil.getExterns(sourceCode), root);

    // Check that the try-catch-finally block is not modified.
    Node testFunction = NodeUtil.getFunctionNode("test", root);
    assertTrue(testFunction.getLastChild().isBlock());
    Node testBlock = testFunction.getLastChild();
    assertTrue(testBlock.hasChild(NodeUtil.getTryNode(testBlock)));
    Node tryBlock = testBlock.getChildWithType(Node.TRY);
    assertTrue(tryBlock.hasChild(NodeUtil.getExitNode(JSType.THROW, tryBlock)));
    assertTrue(tryBlock.hasChild(NodeUtil.getCatchBlock(tryBlock)));
    assertTrue(tryBlock.hasChild(NodeUtil.getFinallyBlock(tryBlock)));
  }
}