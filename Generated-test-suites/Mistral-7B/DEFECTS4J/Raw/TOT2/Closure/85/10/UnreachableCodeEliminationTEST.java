import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ExitScope;
import static com.google.javascript.jscomp.NodeTraversal.ScopedCallback.SCOPE_CHANGE;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    rootNode = Node.createScript("function test() {"
        + "if (true) {"
        + "  return;"
        + "  alert('hello');"
        + "}"
        + "alert('world');"
        + "}");
  }

  @Test
  public void testBasicFunctionality() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "if (true) {"
        + "  return;"
        + "}"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "if (true) {"
        + "  return;"
        + "}alert('world');", rootNode.toString());
  }

  @Test(expected = AssertionError.class)
  public void testExceptionThrown() {
    rootNode = Node.createScript("function test() {"
        + "throw new Error('test');"
        + "alert('world');"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();
  }

  @Test
  public void testDeadCodeInsideLoop() {
    rootNode = Node.createScript("function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) {"
        + "    return;"
        + "  }"
        + "  alert('i: ' + i);"
        + "}"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) {"
        + "    return;"
        + "  }"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) {"
        + "    return;"
        + "  }"
        + "  alert('i: ' + i);"
        + "}", rootNode.toString());
  }

  @Test
  public void testDeadCodeInsideIfElse() {
    rootNode = Node.createScript("function test() {"
        + "if (true) {"
        + "  alert('true');"
        + "}"
        + "else {"
        + "  alert('false');"
        + "}"
        + "alert('world');"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "if (true) {"
        + "  alert('true');"
        + "}"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "if (true) {"
        + "  alert('true');"
        + "}"
        + "else {"
        + "  alert('false');"
        + "}"
        + "alert('world');"
        + "}", rootNode.toString());
  }

  @Test
  public void testDeadCodeInsideSwitch() {
    rootNode = Node.createScript("function test() {"
        + "switch (1) {"
        + "  case 1:"
        + "    return;"
        + "  default:"
        + "    alert('default');"
        + "}"
        + "alert('world');"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "switch (1) {"
        + "  case 1:"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "switch (1) {"
        + "  case 1:"
        + "    return;"
        + "  default:"
        + "    alert('default');"
        + "}"
        + "alert('world');"
        + "}", rootNode.toString());
  }

  @Test
  public void testDeadCodeAtEndOfFunction() {
    rootNode = Node.createScript("function test() {"
        + "alert('world');"
        + "alert('hello');"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "alert('world');"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "alert('world');"
        + "alert('hello');"
        + "}", rootNode.toString());
  }

  @Test
  public void testDeadCodeInsideTryCatch() {
    rootNode = Node.createScript("function test() {"
        + "try {"
        + "  alert('try');"
        + "  throw new Error('test');"
        + "}"
        + "catch (e) {"
        + "  alert('catch');"
        + "}"
        + "alert('world');"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "try {"
        + "  alert('try');"
        + "  throw new Error('test');"
        + "}"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "try {"
        + "  alert('try');"
        + "  throw new Error('test');"
        + "}"
        + "catch (e) {"
        + "  alert('catch');"
        + "}"
        + "alert('world');"
        + "}", rootNode.toString());
  }

  @Test
  public void testDeadCodeInsideIfWithElseIf() {
    rootNode = Node.createScript("function test() {"
        + "if (true) {"
        + "  alert('if1');"
        + "}"
        + "else if (false) {"
        + "  alert('if2');"
        + "}"
        + "alert('world');"
        + "}");

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "if (true) {"
        + "  alert('if1');"
        + "}", rootNode.toString());

    elimination = new UnreachableCodeElimination(compiler, false);
    traversal = NodeTraversal.createTraverser(compiler, rootNode, elimination);
    traversal.traverse();

    assertEquals("function test() {"
        + "if (true) {"
        + "  alert('if1');"
        + "}"
        + "else if (false) {"
        + "  alert('if2');"
        + "}"
        + "alert('world');"
        + "}", rootNode.toString());
  }
}