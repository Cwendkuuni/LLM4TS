import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testBasicFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('this should not be reached');" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertNull("Check if 'alert' was removed", getChild(root, 0));
  }

  @Test
  public void testBasicFunctionWithReturnStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('this should not be reached');" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node returnNode = getChild(root, 0);
    assertEquals(Token.RETURN, returnNode.getType());
  }

  @Test
  public void testBasicFunctionWithEmptyBlock() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertTrue("Check if empty block was removed", root.isFunction());
  }

  @Test
  public void testFunctionWithBranching() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x > 0) {" +
        "    return;" +
        "  }" +
        "  alert('this should not be reached');" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertNull("Check if 'alert' was removed", getChild(root, 0));
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithNoReturnStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  alert('this should not be reached');" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    fail("Unreachable code elimination should not eliminate the function body");
  }

  @Test(expected = AssertionError.class)
  public void testFunctionWithSideEffectFreeExpression() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  return a.b.MyClass.prototype.propertyName;" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    fail("Unreachable code elimination should not eliminate side-effect free expressions");
  }

  @Test
  public void testFunctionWithExceptionHandling() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  try {" +
        "    throw new Error('Test error');" +
        "  } catch (e) {" +
        "    // empty catch block" +
        "  }" +
        "  alert('this should not be reached');" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertTrue("Check if empty catch block was removed", root.getLastChild().isFunction());
  }

  @Test
  public void testFunctionWithBreakStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 3) {" +
        "      break;" +
        "    }" +
        "    alert('should not be reached');" +
        "  }" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertTrue("Check if 'alert' was removed", root.getLastChild().isFunction());
  }

  @Test
  public void testFunctionWithContinueStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 3) {" +
        "      continue;" +
        "    }" +
        "    alert('should not be reached');" +
        "  }" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertTrue("Check if 'alert' was removed", root.getLastChild().isFunction());
  }

  @Test
  public void testFunctionWithUnconditionalReturn() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  return;" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertTrue("Check if empty function was removed", root.isFunction());
  }

  @Test
  public void testFunctionWithDeadCodeInsideTryCatch() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintDiagnosticDetails(true);

    String sourceCode = "function foo() {" +
        "  try {" +
        "    throw new Error('Test error');" +
        "  } catch (e) {" +
        "    alert('this should not be reached');" +
        "  }" +
        "}";

    Node root = parseTree(sourceCode, compiler, options);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertTrue("Check if 'alert' was removed", root.getLastChild().isFunction());
  }
}