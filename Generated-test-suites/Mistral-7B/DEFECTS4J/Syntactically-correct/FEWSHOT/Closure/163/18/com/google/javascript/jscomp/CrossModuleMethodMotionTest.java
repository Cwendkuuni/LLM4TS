package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessedSourceFile;
import com.google.javascript.rhino.IR;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethods() {
    // Create a simple compiler for testing
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, true);
    abstractCompiler.addPass(crossModuleMethodMotion);

    // Create a simple test case with two modules
    String source0 = "module A {\n" +
        "  function foo() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";

    String source1 = "module B {\n" +
        "  import foo from 'A';\n" +
        "  function bar() {\n" +
        "    return foo();\n" +
        "  }\n" +
        "}";

    ProcessedSourceFile processedSourceFile0 = compiler.processString(source0, "A.js");
    ProcessedSourceFile processedSourceFile1 = compiler.processString(source1, "B.js");

    // Check that the method is not moved without the pass
    Node root0 = processedSourceFile0.getRootNode();
    Node root1 = processedSourceFile1.getRootNode();

    List<Node> fooDeclarations0 = findDeclarations(root0, "foo");
    List<Node> fooDeclarations1 = findDeclarations(root1, "foo");

    assertTrue(fooDeclarations0.size() > 0);
    assertTrue(fooDeclarations1.size() > 0);
    assertFalse(fooDeclarations0.get(0).getParent().equals(fooDeclarations1.get(0).getParent()));

    // Run the pass and check that the method is moved
    crossModuleMethodMotion.process(null, null);
    Node root0New = processedSourceFile0.getRootNode();
    Node root1New = processedSourceFile1.getRootNode();

    List<Node> fooDeclarations0New = findDeclarations(root0New, "foo");
    List<Node> fooDeclarations1New = findDeclarations(root1New, "foo");

    assertTrue(fooDeclarations0New.size() > 0);
    assertTrue(fooDeclarations1New.size() > 0);
    assertTrue(fooDeclarations0New.get(0).getParent().equals(fooDeclarations1New.get(0).getParent()));
  }

  @Test
  public void testMoveMethodsWithStubs() {
    // Create a simple compiler for testing
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, true);
    abstractCompiler.addPass(crossModuleMethodMotion);

    // Create a simple test case with two modules and a stubbed method
    String source0 = "module A {\n" +
        "  function foo() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";

    String source1 = "module B {\n" +
        "  import foo from 'A';\n" +
        "  function bar() {\n" +
        "    return foo();\n" +
        "  }\n" +
        "  JSCompiler_unstubMethod(1, function() {\n" +
        "    return foo();\n" +
        "  });\n" +
        "}";

    ProcessedSourceFile processedSourceFile0 = compiler.processString(source0, "A.js");
    ProcessedSourceFile processedSourceFile1 = compiler.processString(source1, "B.js");

    // Check that the method is not moved without the pass
    Node root0 = processedSourceFile0.getRootNode();
    Node root1 = processedSourceFile1.getRootNode();

    List<Node> fooDeclarations0 = findDeclarations(root0, "foo");
    List<Node> fooDeclarations1 = findDeclarations(root1, "foo");

    assertTrue(fooDeclarations0.size() > 0);
    assertTrue(fooDeclarations1.size() > 0);
    assertFalse(fooDeclarations0.get(0).getParent().equals(fooDeclarations1.get(0).getParent()));

    // Run the pass and check that the method is moved and stubbed
    crossModuleMethodMotion.process(null, null);
    Node root0New = processedSourceFile0.getRootNode();
    Node root1New = processedSourceFile1.getRootNode();

    List<Node> fooDeclarations0New = findDeclarations(root0New, "foo");
    List<Node> fooDeclarations1New = findDeclarations(root1New, "foo");

    assertTrue(fooDeclarations0New.size() > 0);
    assertTrue(fooDeclarations1New.size() > 0);
    assertTrue(fooDeclarations0New.get(0).getParent().equals(fooDeclarations1New.get(0).getParent()));

    List<Node> stubCalls = findDeclarations(root1New, STUB_METHOD_NAME);
    assertTrue(stubCalls.size() > 0);
    List<Node> unstubCalls = findDeclarations(root0New, UNSTUB_METHOD_NAME);
    assertTrue(unstubCalls.size() > 0);
  }

  @Test
  public void testNoMovements() {
    // Create a simple compiler for testing
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, true);
    abstractCompiler.addPass(crossModuleMethodMotion);

    // Create a simple test case with two modules that cannot be moved
    String source0 = "module A {\n" +
        "  function foo() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";

    String source1 = "module B {\n" +
        "  import foo from 'A';\n" +
        "  function bar() {\n" +
        "    return foo();\n" +
        "  }\n" +
        "  function baz() {\n" +
        "    return foo();\n" +
        "  }\n" +
        "}";

    ProcessedSourceFile processedSourceFile0 = compiler.processString(source0, "A.js");
    ProcessedSourceFile processedSourceFile1 = compiler.processString(source1, "B.js");

    // Run the pass and check that no methods are moved
    crossModuleMethodMotion.process(null, null);
    Node root0New = processedSourceFile0.getRootNode();
    Node root1New = processedSourceFile1.getRootNode();

    List<Node> fooDeclarations0 = findDeclarations(root0New, "foo");
    List<Node> fooDeclarations1 = findDeclarations(root1New, "foo");

    assertTrue(fooDeclarations0.size() > 0);
    assertTrue(fooDeclarations1.size() > 0);
    assertFalse(fooDeclarations0.get(0).getParent().equals(fooDeclarations1.get(0).getParent()));

    List<Node> barDeclarations0 = findDeclarations(root0New, "bar");
    List<Node> barDeclarations1 = findDeclarations(root1New, "bar");

    assertTrue(barDeclarations0.size() > 0);
    assertTrue(barDeclarations1.size() > 0);
    assertFalse(barDeclarations0.get(0).getParent().equals(barDeclarations1.get(0).getParent()));

    List<Node> bazDeclarations0 = findDeclarations(root0New, "baz");
    List<Node> bazDeclarations1 = findDeclarations(root1New, "baz");

    assertTrue(bazDeclarations0.size() > 0);
    assertTrue(bazDeclarations1.size() > 0);
    assertFalse(bazDeclarations0.get(0).getParent().equals(bazDeclarations1.get(0).getParent()));
  }

  @Test
  public void testNullDeepestCommonModuleRef() {
    // Create a simple compiler for testing
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, true);
    abstractCompiler.addPass(crossModuleMethodMotion);

    // Create a simple test case with a null deepest common module reference
    String source = "module A {\n" +
        "  function foo() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";

    ProcessedSourceFile processedSourceFile = compiler.processString(source, "A.js");

    // Run the pass and check that an error is reported
    crossModuleMethodMotion.process(null, null);
    List<JSError> errors = compiler.getErrors();

    assertTrue(errors.size() > 0);
    assertNull(processedSourceFile.getRootNode());
  }

  @Test
  public void testNoMovementsWhenCanModifyExternsIsFalse() {
    // Create a simple compiler for testing
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, false);
    abstractCompiler.addPass(crossModuleMethodMotion);

    // Create a simple test case with two modules that cannot be moved
    String source0 = "module A {\n" +
        "  export function foo() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";

    String source1 = "module B {\n" +
        "  import foo from 'A';\n" +
        "  function bar() {\n" +
        "    return foo();\n" +
        "  }\n" +
        "}";

    ProcessedSourceFile processedSourceFile0 = compiler.processString(source0, "A.js");
    ProcessedSourceFile processedSourceFile1 = compiler.processString(source1, "B.js");

    // Run the pass and check that no methods are moved
    crossModuleMethodMotion.process(null, null);
    Node root0New = processedSourceFile0.getRootNode();
    Node root1New = processedSourceFile1.getRootNode();

    List<Node> fooDeclarations0 = findDeclarations(root0New, "foo");
    List<Node> fooDeclarations1 = findDeclarations(root1New, "foo");

    assertTrue(fooDeclarations0.size() > 0);
    assertTrue(fooDeclarations1.size() > 0);
    assertFalse(fooDeclarations0.get(0).getParent().equals(fooDeclarations1.get(0).getParent()));

    List<Node> barDeclarations0 = findDeclarations(root0New, "bar");
    List<Node> barDeclarations1 = findDeclarations(root1New, "bar");

    assertTrue(barDeclarations0.size() > 0);
    assertTrue(barDeclarations1.size() > 0);
    assertFalse(barDeclarations0.get(0).getParent().equals(barDeclarations1.get(0).getParent()));
  }

  private List<Node> findDeclarations(Node root, String name) {
    List<Node> result = new ArrayList<>();
    traverse(root, node -> result.add(node), n -> n.isFunctionDeclaration() && n.getName().equals(name));
    return result;
  }

  private void traverse(Node node, Consumer<Node> consumer, Predicate<Node> predicate) {
    if (predicate.test(node)) {
      consumer.accept(node);
    }
    for (int i = 0; i < node.getNumChildren(); i++) {
      traverse(node.getChild(i), consumer, predicate);
    }
  }

  @FunctionalInterface
  private interface Consumer<T> {
    void accept(T t);
  }

  @FunctionalInterface
  private interface Predicate<T> {
    boolean test(T t);
  }
}