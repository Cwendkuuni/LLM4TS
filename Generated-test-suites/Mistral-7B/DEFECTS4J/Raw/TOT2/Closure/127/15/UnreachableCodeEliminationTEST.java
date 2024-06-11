import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeSnippetTestHelper.newNode;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal.AbstractCallback;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {
  @Test
  public void testBasicFunction() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  var x = 1;" +
        "  return;" +
        "  alert('unreachable');" +
        "}"
    );

    pass.process(null, root);

    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testFunctionWithBody() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  var x = 1;" +
        "  alert('Hello World');" +
        "}"
    );

    pass.process(null, root);

    assertTrue(root.getFirstChild().isFunction());
  }

  @Test
  public void testRemoveBlock() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  {" +
        "    alert('Hello World');" +
        "  }" +
        "  return;" +
        "}"
    );

    pass.process(null, root);

    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testRemoveReturn() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  return;" +
        "}"
    );

    pass.process(null, root);

    assertTrue(root.isFunction());
    assertTrue(root.getLastChild().isReturnStatement());
  }

  @Test
  public void testRemoveBreak() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i > 5) {" +
        "      break;" +
        "    }" +
        "  }" +
        "}"
    );

    pass.process(null, root);

    // The loop should have been removed
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testRemoveContinue() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 == 0) {" +
        "      continue;" +
        "    }" +
        "    alert(i);" +
        "  }" +
        "}"
    );

    pass.process(null, root);

    // The even numbers should have been filtered out
    Node functionNode = root.getFirstChild();
    Node body = functionNode.getLastChild();
    int count = 0;
    for (Node statement = body.getFirstChild(); statement != null;
        statement = statement.getNext()) {
      if (statement.isExpressionStatement()) {
        Node expression = statement.getFirstChild();
        if (expression.isNumberLiteral()) {
          int number = expression.getInt();
          assertTrue(number % 2 != 0);
          count++;
        }
      }
    }
    assertEquals(5, count);
  }

  @Test
  public void testRemoveEmptyBlock() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode("{}");
    pass.process(null, root);

    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testRemoveEmptyFunction() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode("function foo() {}");
    pass.process(null, root);

    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testRemoveFunctionWithEmptyBody() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode("function foo() {}");
    pass.process(null, root);

    assertTrue(root.isFunction());
  }

  @Test
  public void testRemoveFunctionWithEmptyBodyAndName() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode("function bar() {}");
    pass.process(null, root);

    assertTrue(root.isFunction());
    assertEquals("bar", root.getName());
  }

  @Test
  public void testRemoveNoOpStatement() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);

    Node root = newNode(
        "function foo() {" +
        "  alert('Hello World');" +
        "  true;" +
        "}"
    );

    pass.process(null, root);

    assertTrue(root.getFirstChild().isFunction());
    assertTrue(root.getFirstChild().getLastChild().isReturnStatement());
  }

  @Test
  public void testRemoveFunctionWithNoOpStatement() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);

    Node root = newNode(
        "function foo() {" +
        "  function bar() {" +
        "    alert('Hello World');" +
        "    true;" +
        "  }" +
        "  bar();" +
        "}"
    );

    pass.process(null, root);

    assertTrue(root.isFunction());
    assertTrue(root.getLastChild().isReturnStatement());
  }

  @Test
  public void testRemoveFunctionWithNoOpStatementAndName() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);

    Node root = newNode(
        "function foo() {" +
        "  function bar() {" +
        "    alert('Hello World');" +
        "    true;" +
        "  }" +
        "  bar();" +
        "}"
    );

    pass.process(null, root);

    assertTrue(root.isFunction());
    assertEquals("foo", root.getName());
    assertTrue(root.getLastChild().isReturnStatement());
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeWithException() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = newNode(
        "function foo() {" +
        "  try {" +
        "    throw new Error('Test');" +
        "  } catch (e) {" +
        "    alert('Caught ' + e);" +
        "    return;" +
        "  }" +
        "}"
    );

    pass.process(null, root);

    fail("Expected exception not thrown.");
  }
}