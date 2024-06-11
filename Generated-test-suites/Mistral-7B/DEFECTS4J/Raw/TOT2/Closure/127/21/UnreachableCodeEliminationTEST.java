import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testSimpleMethod() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String sourceCode = "function foo() {}";
    Node root = NodeFactory.compile(sourceCode, options).ast;

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(options.getExterns(), root);

    Node foo = root.getFirstChild();
    assertFalse("foo should not be removed", pass.eliminationPass.removeDeadExprStatementSafely(foo));
  }

  @Test
  public void testMethodWithUnreachableCodeAfterReturn() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String sourceCode = "function foo() { return; alert('unreachable'); }";
    Node root = NodeFactory.compile(sourceCode, options).ast;

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(options.getExterns(), root);

    Node foo = root.getFirstChild();
    assertNull("alert should be removed", foo.getLastChild());
  }

  @Test
  public void testMethodWithUnreachableCodeAsLastStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String sourceCode = "function foo() { alert('unreachable'); }";
    Node root = NodeFactory.compile(sourceCode, options).ast;

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(options.getExterns(), root);

    Node foo = root.getFirstChild();
    assertNull("alert should be removed", foo.getLastChild());
  }

  @Test
  public void testMethodWithNoOpStatements() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String sourceCode = "function foo() { a.b.MyClass.prototype.propertyName; }";
    Node root = NodeFactory.compile(sourceCode, options).ast;

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(options.getExterns(), root);

    Node foo = root.getFirstChild();
    assertFalse("no-op statement should not be removed",
        pass.eliminationPass.removeDeadExprStatementSafely(foo.getFirstChild()));
  }

  @Test
  public void testMethodWithDeadCodeInSwitchStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String sourceCode = "function foo(x) {\n" +
        "  switch (x) {\n" +
        "    case 0:\n" +
        "      return;\n" +
        "    case 1:\n" +
        "      alert('unreachable');\n" +
        "      break;\n" +
        "    default:\n" +
        "      return 1;\n" +
        "  }\n" +
        "}";
    Node root = NodeFactory.compile(sourceCode, options).ast;

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(options.getExterns(), root);

    Node foo = root.getFirstChild();
    Node switchStatement = foo.getFirstChild();
    Node unreachableCode = switchStatement.getChildAt(1);
    assertNull("unreachable alert should be removed", unreachableCode);
  }

  @Test
  public void testMethodWithDeadCodeInCatchBlock() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String sourceCode = "function foo() {\n" +
        "  try {\n" +
        "    throw 'error';\n" +
        "  } catch (e) {\n" +
        "    alert('unreachable');\n" +
        "  }\n" +
        "}";
    Node root = NodeFactory.compile(sourceCode, options).ast;

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(options.getExterns(), root);

    Node foo = root.getFirstChild();
    Node catchBlock = foo.getLastChild();
    Node unreachableCode = catchBlock.getLastChild();
    assertNull("unreachable alert should be removed", unreachableCode);
  }
}