package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.List;

/**
 * Tests {@link DevirtualizePrototypeMethods} class.
 */
public class DevirtualizePrototypeMethodsTest {

  private static final String SOURCE =
      "(" +
      "  function A() {" +
      "    this.total = 0;" +
      "  }" +
      "  A.prototype.accumulate = function(value) {" +
      "    this.total += value; return this.total" +
      "  }" +
      "  var a = new A();" +
      "  var total = a.accumulate(2);" +
      ")" +
      "(" +
      "  function B() {" +
      "    this.x = 0;" +
      "  }" +
      "  B.prototype.incrementX = function() {" +
      "    this.x += 1;" +
      "  }" +
      "  var b = new B();" +
      "  b.incrementX();" +
      ")";

  @ org.junit. BeforeClass
  public static void setUpBeforeClass() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.compile(new SourceFile("Test.java", SOURCE), options);
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} correctly
   * transforms an accumulate method.
   */
  @ org.junit. Test
  public void testAccumulate() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    Node root = new SourceFile("Test.java", SOURCE).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, root);

    // Check that the accumulate method has been transformed.
    Node accumulateNode = findNode("function JSCompiler_StaticMethods_accumulate", root);
    assertEquals("Unexpected accumulate method implementation.",
        "function(self, value) { self.total += value; return self.total }",
        accumulateNode.toString());

    // Check that the total variable is assigned the result of the transformed method.
    Node totalAssignmentNode = findNode("total = JSCompiler_StaticMethods_accumulate(a, 2)", root);
    assertEquals("Unexpected total assignment.", "total = accumulate(a, 2)", totalAssignmentNode.toString());
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} correctly
   * transforms an incrementX method.
   */
  @ org.junit. Test
  public void testIncrementX() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    Node root = new SourceFile("Test.java", SOURCE).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, root);

    // Check that the incrementX method has been transformed.
    Node incrementXNode = findNode("function JSCompiler_StaticMethods_incrementX", root);
    assertEquals("Unexpected incrementX method implementation.",
        "function(self) { self.x += 1 }",
        incrementXNode.toString());

    // Check that the incrementX method is called correctly.
    Node incrementXCallNode = findNode("b.incrementX();", root);
    assertEquals("Unexpected incrementX call.", "incrementX(b)", incrementXCallNode.toString());
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} does not
   * transform a non-prototype method.
   */
  @ org.junit. Test
  public void testNonPrototypeMethod() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    Node root = new SourceFile("Test.java", SOURCE).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, root);

    // The totalAssignmentNode should not have been transformed.
    Node totalAssignmentNode = findNode("total = JSCompiler_StaticMethods_accumulate(a, 2)", root);
    assertNull("totalAssignmentNode should not be transformed.", totalAssignmentNode);
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} does not
   * transform a method defined in an object literal.
   */
  @ org.junit. Test
  public void testObjectLiteralMethod() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    String source =
        "(" +
        "  var obj = {" +
        "    accumulate: function(value) {" +
        "      this.total += value; return this.total" +
        "    }" +
        "  }" +
        ")";

    Node root = new SourceFile("Test.java", source).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, root);

    // The accumulate method should not have been transformed.
    Node accumulateNode = findNode("function(value) { this.total += value; return this.total }", root);
    assertNotNull("accumulateNode should exist.", accumulateNode);
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} does not
   * transform a non-eligible method.
   */
  @ org.junit. Test
  public void testNonEligibleMethod() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    String source =
        "(" +
        "  function A() {" +
        "    this.total = 0;" +
        "  }" +
        "  A.prototype.accumulate = function(value) {" +
        "    this.total += value; return this.total" +
        "  }" +
        "  var a = new A();" +
        "  // The following statement exports the accumulate method." +
        "  export function accumulate(value) {" +
        "    return a.accumulate(value);" +
        "  }" +
        ")";

    Node root = new SourceFile("Test.java", source).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, root);

    // The accumulate method should not have been transformed.
    Node accumulateNode = findNode("function(value) { this.total += value; return this.total }", root);
    assertNotNull("accumulateNode should exist.", accumulateNode);
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} does not
   * transform a method used before its definition.
   */
  @ org.junit. Test
  public void testMethodUsedBeforeDefinition() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    String source =
        "(" +
        "  function A() {" +
        "    this.total = 0;" +
        "  }" +
        "  A.prototype.accumulate = function(value) {" +
        "    this.total += value; return this.total" +
        "  }" +
        "  var a = new A();" +
        "  JSCompiler_StaticMethods_accumulate(a, 2);" +
        ")";

    Node root = new SourceFile("Test.java", source).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    try {
      pass.process(null, root);
      fail("Expected a CompilationError.");
    } catch (AssertionError e) {
      // Expected.
    }
  }

  /**
   * Checks that {@link DevirtualizePrototypeMethods} does not
   * transform a method defined after its first call.
   */
  @ org.junit. Test
  public void testMethodDefinedAfterFirstCall() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    String source =
        "(" +
        "  function A() {" +
        "    this.total = 0;" +
        "  }" +
        "  var a = new A();" +
        "  JSCompiler_StaticMethods_accumulate = function(value) {" +
        "    this.total += value; return this.total" +
        "  }" +
        "  JSCompiler_StaticMethods_accumulate(a, 2);" +
        ")";

    Node root = new SourceFile("Test.java", source).getBody();
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    try {
      pass.process(null, root);
      fail("Expected a CompilationError.");
    } catch (AssertionError e) {
      // Expected.
    }
  }

  private Node findNode(String expression, Node node) {
    for (Node child = node; child != null; child = child.getNextSibling()) {
      if (child.getTag() == Token.EXPR && child.toString().equals(expression)) {
        return child;
      }
    }
    return null;
  }
}