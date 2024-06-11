import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.Kind.VAR;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ContextualRenamer;
import com.google.javascript.jscomp.ContextualRenamer.ContextualRenameInverter;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link MakeDeclaredNamesUnique} class.
 */
public class MakeDeclaredNamesUniqueTest {

  public static class TestData {
    Node node;
    AbstractCompiler compiler;
    Scope expectedScope;

    TestData(Node node, AbstractCompiler compiler, Scope expectedScope) {
      this.node = node;
      this.compiler = compiler;
      this.expectedScope = expectedScope;
    }
  }

  private static TestData createTestData(Node node, AbstractCompiler compiler) {
    Scope expectedScope = new Scope();
    NodeTraversal.traverse(compiler, node, new AbstractPostOrderCallback<Scope>() {
      @Override
      public void enterScope(NodeTraversal t) {
        expectedScope = t.getScope();
      }
    });
    return new TestData(node, compiler, expectedScope);
  }

  private static void testUniqueNames(String jsCode, String uniqueIdPrefix) {
    TestingCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Preconditions.checkArgument(compiler.compile(jsCode, options), "Compilation failed");

    ErrorReporter errorReporter = compiler.getErrors();
    if (!errorReporter.isEmpty()) {
      System.out.println("Compilation errors: " + errorReporter);
      throw new RuntimeException("Compilation failed");
    }

    AbstractNodeTraversal traverser = new AbstractNodeTraversal(compiler) {
      @Override
      public CompilerPass getPass(String passName) {
        if (ContextualRenameInverter.class.getName().equals(passName)) {
          return new ContextualRenameInverter(compiler);
        }
        if (MakeDeclaredNamesUnique.class.getName().equals(passName)) {
          return new MakeDeclaredNamesUnique(new ContextualRenamer(compiler.getErrorReporter()));
        }
        return null;
      }
    };

    Node node = NodeFactory.compile(compiler.getFactory(), jsCode);
    TestData testData = createTestData(node, compiler);
    traverser.traverse(NodeTraversal.ROOT, node);
    assertEquals("Expected scope: \n" + testData.expectedScope,
        testData.expectedScope.toString(),
        compiler.getErrorReporter().getScope());
  }

  @org.junit.Test
  public void testContextualRenamer() {
    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  var a = 3;"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  function foo() {}"
            + "  var a = 3;"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  function foo() {"
            + "    var b = 1;"
            + "    var a = 2;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  function foo() {"
            + "    var a = 3;"
            + "  }"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "    var b = 2;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  function foo() {"
            + "    var a = 3;"
            + "  }"
            + "  function bar() {}"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {}"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  function bar() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__1_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__2_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__3_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function qux() {}"
            + "  var a = 3;"
            + "}",
        "a__4_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function qux() {"
            + "    function quux() {}"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__5_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      function quux() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function qux() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__6_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      function quux() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function qux() {"
            + "    function quuz() {}"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__7_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      function quux() {}"
            + "      function quuz() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function qux() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__8_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      function baz() {}"
            + "      function quux() {}"
            + "      function quuz() {}"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function qux() {"
            + "    function quuz() {"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__9_");
  }

  @org.junit.Test
  public void testInlineRenamer() {
    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  var a = 3;"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  function foo() {"
            + "    var a = 3;"
            + "  }"
            + "}",
        "a_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  function foo() {"
            + "    var a = 3;"
            + "  }"
            + "  var b = 2;"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "  function bar() {}"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  function foo() {"
            + "    var a = 3;"
            + "  }"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  var a = 1;"
            + "  var b = 2;"
            + "  function foo() {"
            + "    var a = 3;"
            + "  }"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__1_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  function bar() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__2_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  function bar() {"
            + "    var a = 1;"
            + "  }"
            + "  function baz() {}"
            + "  var a = 3;"
            + "}",
        "a__3_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "  }"
            + "  function bar() {"
            + "    var a = 1;"
            + "  }"
            + "  function baz() {}"
            + "  function qux() {}"
            + "  var a = 3;"
            + "}",
        "a__4_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "    function bar() {}"
            + "  }"
            + "  function baz() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__0_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {}"
            + "    var a = 1;"
            + "  }"
            + "  function baz() {"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__1_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    var a = 1;"
            + "    function bar() {}"
            + "  }"
            + "  function baz() {"
            + "    function qux() {}"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__2_");

    testUniqueNames(
        "function test() {"
            + "  function foo() {"
            + "    function bar() {"
            + "      var a = 1;"
            + "    }"
            + "  }"
            + "  function baz() {"
            + "    function qux() {}"
            + "    var a = 1;"
            + "  }"
            + "  var a = 3;"
            + "}",
        "a__3_");
  }
}