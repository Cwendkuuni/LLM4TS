import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.test_util.AbstractTestCompiler;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private CompilerPass unreachableCodeElimination;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractTestCompiler(options);
    unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
  }

  @Test
  public void testDeadCodeAfterReturn() {
    String sourceCode = "function foo() {" +
        "if (true) {" +
        "return;" +
        "alert('unreachable');" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code after return statement has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isReturn());
  }

  @Test
  public void testDeadCodeNoSideEffects() {
    String sourceCode = "function foo() {" +
        "var a = {};" +
        "a.b.MyClass.prototype.propertyName;" +
        "return 42;" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code with no side effects has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isReturn());
  }

  @Test
  public void testDeadCodeWithSideEffects() {
    String sourceCode = "function foo() {" +
        "var a = {};" +
        "a.b.MyClass.prototype.propertyName = 42;" +
        "return 1;" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code with side effects remains in the AST.
    assertFalse(rootNode.isFunction() && rootNode.getLastChild().isReturn());
  }

  @Test
  public void testBreakContinue() {
    String sourceCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i === 3) {" +
        "break;" +
        "}" +
        "}" +
        "continue;" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that break and continue statements have been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testReturn() {
    String sourceCode = "function foo() {" +
        "return;" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that return statement remains in the AST.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isReturn());
  }

  @Test
  public void testFunctionWithNoBody() {
    String sourceCode = "function foo() {}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that empty function remains in the AST.
    assertTrue(rootNode.isFunction());
  }

  @Test
  public void testEmptyBlock() {
    String sourceCode = "function foo() {" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that empty block remains in the AST.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideFunction() {
    String sourceCode = "function foo(x) {" +
        "function bar() {" +
        "alert('unreachable');" +
        "}" +
        "return 42;" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a function has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isReturn());
  }

  @Test
  public void testDeadCodeWithCondition() {
    String sourceCode = "function foo() {" +
        "if (false) {" +
        "return;" +
        "}" +
        "alert('unreachable');" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code with a condition has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideSwitchStatement() {
    String sourceCode = "function foo() {" +
        "switch (true) {" +
        "case 0:" +
        "return;" +
        "default:" +
        "alert('unreachable');" +
        "}" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a switch statement has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideLoop() {
    String sourceCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i === 3) {" +
        "continue;" +
        "}" +
        "}" +
        "alert('unreachable');" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a loop has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideTryCatchBlock() {
    String sourceCode = "function foo() {" +
        "try {" +
        "alert('unreachable');" +
        "}" +
        "catch (e) {" +
        "}" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a try-catch block has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideTryFinallyBlock() {
    String sourceCode = "function foo() {" +
        "try {" +
        "alert('unreachable');" +
        "}" +
        "finally {" +
        "}" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a try-finally block has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideForInStatement() {
    String sourceCode = "function foo() {" +
        "for (var key in []) {" +
        "alert('unreachable');" +
        "}" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a for-in statement has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideConditionalExpression() {
    String sourceCode = "function foo() {" +
        "alert(true ? (1 + 1) : 1 + 1 + 1);" +
        "alert('unreachable');" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a conditional expression has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }

  @Test
  public void testDeadCodeInsideSwitchExpression() {
    String sourceCode = "function foo() {" +
        "alert(0 + (true ? 1 : 2) + 1);" +
        "alert('unreachable');" +
        "}";

    Node rootNode = NodeTraversal.parse(compiler, sourceCode);
    unreachableCodeElimination.process(null, rootNode);

    // Check that dead code inside a switch expression has been removed.
    assertTrue(rootNode.isFunction() && rootNode.getLastChild().isBlock());
  }
}