import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest extends CompilationTestBase {

  private AbstractCompiler compiler;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler();
  }

  @Test
  public void testSimpleUnreachableCode() {
    String input = "function f() {" +
        "  if (true) {" +
        "    return;" +
        "  }else {" +
        "    alert('unreachable');" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {}\n", output);
  }

  @Test
  public void testRemoveNoOpStatements() {
    String input = "function f() {" +
        "  false;" +
        "  return;" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {\n}", output);
  }

  @Test
  public void testPrototypePropertyWithNoInitialization() {
    String input = "function MyClass() {}" +
        "MyClass.prototype.prop = function() {}\n" +
        "function f() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  MyClass.prototype.prop();" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals(input, output);
  }

  @Test
  public void testComplexControlFlow() {
    String input = "function f() {" +
        "  if (x) {" +
        "    if (y) {" +
        "      return;" +
        "    }else {" +
        "      return;" +
        "    }" +
        "    return;" +
        "  }else {" +
        "    if (z) {" +
        "      return;" +
        "    }else {" +
        "      return;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals(input, output);
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    String input = "function f() {" +
        "  if (true) {" +
        "    if (true) {" +
        "      break;" +
        "    }else {" +
        "      break;" +
        "    }" +
        "  }else {" +
        "    if (true) {" +
        "      break;" +
        "    }else {" +
        "      break;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {}\n", output);
  }

  @Test
  public void testDeadCodeInsideLoop() {
    String input = "function f() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "    i++;" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {\n  for (var i = 0; i < 10; i++) {\n    i++;\n  }\n}", output);
  }

  @Test
  public void testDeadCodeInsideConditional() {
    String input = "function f() {" +
        "  if (true) {" +
        "    if (true) {" +
        "      return;" +
        "    }else {" +
        "      return;" +
        "    }" +
        "  }else {" +
        "    if (x) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals(input, output);
  }

  @Test
  public void testDeadCodeInsideSwitch() {
    String input = "function f() {" +
        "  switch (x) {" +
        "    case 1:" +
        "      return;" +
        "    case 2:" +
        "      return;" +
        "    case 3:" +
        "      return;" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {\n}", output);
  }

  @Test
  public void testDeadCodeInsideTryCatch() {
    String input = "function f() {" +
        "  try {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "    throw new Error();" +
        "  } catch (e) {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {\n  try {\n  } catch (e) {\n  }\n}", output);
  }

  @Test
  public void testDeadCodeInsideFinally() {
    String input = "function f() {" +
        "  try {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "    throw new Error();" +
        "  } finally {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {\n  try {\n  } finally {\n  }\n}", output);
  }

  @Test
  public void testDeadCodeInsideWhile() {
    String input = "function f() {" +
        "  while (true) {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals(input, output);
  }

  @Test
  public void testDeadCodeInsideFor() {
    String input = "function f() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (true) {" +
        "      return;" +
        "    }" +
        "  }" +
        "}";

    Node root = parse(input);
    NodeTraversal t = new NodeTraversal(root);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, root);
    NodeTraversal.traverse(compiler, root, eliminator);
    String output = printToSource(root);
    assertEquals("function f() {\n  for (var i = 0; i < 10; i++) {\n  }\n}", output);
  }
}