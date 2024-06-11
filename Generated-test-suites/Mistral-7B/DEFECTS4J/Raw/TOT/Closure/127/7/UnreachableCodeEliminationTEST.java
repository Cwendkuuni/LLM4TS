import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarnMissingReturn(false);
    compiler.setOptions(options);
    rootNode = NodePrinter.printToNode("function foo() {" +
        "  bar();" +
        "  return;" +
        "  alert('hello');" +
        "}", null);
  }

  @After
  public void tearDown() {
    if (rootNode != null) {
      compiler.dispose();
    }
  }

  @Test
  public void testEmptyFunction() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node result = pass.process(null, rootNode);
    assertEquals("function foo() {}", result.toString());
  }

  @Test
  public void testUnreachableCodeAfterReturn() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node result = pass.process(null, rootNode);
    assertEquals("function foo() { return; }", result.toString());
  }

  @Test
  public void testReturnWithStatement() {
    rootNode = NodePrinter.printToNode("function foo() {" +
        "  return a;" +
        "}", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node result = pass.process(null, rootNode);
    assertEquals("function foo() { return a; }", result.toString());
  }

  @Test
  public void testRemoveNoOpStatements() {
    rootNode = NodePrinter.printToNode("function foo() {" +
        "  a.b.MyClass.prototype.propertyName;" +
        "}", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    Node result = pass.process(null, rootNode);
    assertEquals("function foo() { }", result.toString());
  }

  @Test
  public void testNestedFunctions() {
    rootNode = NodePrinter.printToNode("function foo() {" +
        "  function bar() {" +
        "    return;" +
        "  }" +
        "}", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node result = pass.process(null, rootNode);
    assertEquals("function foo() { function bar() { } }", result.toString());
  }

  @Test(expected = RuntimeException.class)
  public void testException() {
    rootNode = NodePrinter.printToNode("function foo() {" +
        "  throw new Error('test');" +
        "}", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node result = pass.process(null, rootNode);
    fail("Expected an exception but got: " + result.toString());
  }
}