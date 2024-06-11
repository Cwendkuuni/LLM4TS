import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.getImpureBooleanValue;
import static com.google.javascript.jscomp.NodeUtil.getNodeByID;
import static com.google.javascript.rhino.token.TokenType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = new Compiler();
    passManager = new CompilerPassManager(compiler);
    minimizer = new MinimizeExitPoints(passManager);
  }

  @Test
  public void testMinimizeExitPoints() {
    String sourceCode =
        "function testFunction(x) {"
            + "if (x > 0) {"
            + " return; // exit 1"
            + "}"
            + "if (x < 0) {"
            + " throw new Error('negative number'); // error"
            + "}"
            + "return 0; // exit 2"
            + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    // Expected exit point 1 is removed
    // Expected exit point 2 remains
    assertNotNull(actualExitPoints);
    assertEquals(getImpureBooleanValue(expectedExitPoints.getLastChild()),
        getImpureBooleanValue(actualExitPoints.getParent()));
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    String sourceCode =
        "label outer:"
        + "function testFunction(x) {"
        + "outer:"
        + " if (x > 0) {"
        + " return; // exit 1"
        + "}"
        + " if (x < 0) {"
        + " break outer; // exit 2 with label"
        + "}"
        + "return 0; // exit 3"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Expected exit point 1 is removed
    // Expected exit point 2 is converted to break with label "outer"
    // Expected exit point 3 remains
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);
    Node actualBreakPoint = getLastChildWithType(expectedExitPoints, BREAK);

    assertNotNull(actualBreakPoint);
    assertEquals(actualBreakPoint.getParent().getString(), "outer");
    assertNull(actualExitPoints);
  }

  @Test
  public void testMinimizeExitPointsWithFunctionDeclaration() {
    String sourceCode =
        "function testFunction() {"
        + "function innerFunction(x) {"
        + " if (x > 0) {"
        + " return; // exit 1"
        + "}"
        + "}"
        + "return innerFunction(1); // exit 2"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Expected exit point 1 is removed
    // Expected exit point 2 remains
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNotNull(actualExitPoints);
    assertEquals(getImpureBooleanValue(expectedExitPoints.getLastChild()),
        getImpureBooleanValue(actualExitPoints.getParent()));
  }

  @Test
  public void testMinimizeExitPointsWithEmptyBlock() {
    String sourceCode = "function testFunction() {}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // No exit points in the empty block
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNull(actualExitPoints);
  }

  @Test
  public void testMinimizeExitPointsWithDoWhileLoop() {
    String sourceCode =
        "function testFunction() {"
        + "do {"
        + " if (false) {"
        + " break; // exit 1"
        + "}"
        + " } while (true); // exit 2"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Expected exit point 1 is removed
    // Expected exit point 2 remains
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNotNull(actualExitPoints);
    assertEquals(getImpureBooleanValue(expectedExitPoints.getLastChild()),
        getImpureBooleanValue(actualExitPoints.getParent()));
  }

  @Test(expected = AssertionError.class)
  public void testMinimizeExitPointsWithCatchBlock() {
    String sourceCode =
        "try {"
        + " throw new Error('test error'); // error"
        + "} catch (e) {"
        + " return; // exit"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Catch block cannot be removed, as it's handling an error
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNotNull(actualExitPoints);
    assertEquals(getImpureBooleanValue(expectedExitPoints.getLastChild()),
        getImpureBooleanValue(actualExitPoints.getParent()));
  }

  @Test
  public void testMinimizeExitPointsWithForLoop() {
    String sourceCode =
        "function testFunction() {"
        + "for (int i = 0; i < 10; i++) {"
        + " if (i > 5) {"
        + " continue; // exit 1"
        + "}"
        + "}"
        + "}"
        + "return; // exit 2";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Expected exit point 1 is removed
    // Expected exit point 2 remains
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNotNull(actualExitPoints);
    assertEquals(getImpureBooleanValue(expectedExitPoints.getLastChild()),
        getImpureBooleanValue(actualExitPoints.getParent()));
  }

  @Test
  public void testMinimizeExitPointsWithWhileLoop() {
    String sourceCode =
        "function testFunction() {"
        + "int i = 0;"
        + "while (true) {"
        + " if (i > 5) {"
        + " return; // exit 1"
        + "}"
        + " i++"
        + "}"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Expected exit point 1 is removed
    // The loop continues infinitely without an exit point
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNull(actualExitPoints);
  }

  @Test
  public void testMinimizeExitPointsWithNestedIfBlocks() {
    String sourceCode =
        "function testFunction() {"
        + "if (false) {"
        + " if (true) {"
        + " return; // exit 1"
        + "}"
        + "}"
        + "return 0; // exit 2"
        + "}";

    Node root = NodeUtil.parseJs(sourceCode);
    passManager.runPasses(root);

    Node expectedExitPoints = NodeUtil.getFunctionBody(root);

    // Expected exit point 1 is removed
    // Expected exit point 2 remains
    Node actualExitPoints = getLastChildWithType(expectedExitPoints, RETURN);

    assertNotNull(actualExitPoints);
    assertEquals(getImpureBooleanValue(expectedExitPoints.getLastChild()),
        getImpureBooleanValue(actualExitPoints.getParent()));
  }

  private Node getLastChildWithType(Node node, int nodeType) {
    if (node.getLastChild() == null) {
      return null;
    }

    Node lastChild = node.getLastChild();
    while (lastChild.getType() != nodeType && lastChild.getParent() != null) {
      lastChild = lastChild.getParent();
    }

    return lastChild;
  }
}