import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest extends CompilationTestBase {
  private AliasTransformationHandler handler = new AliasTransformationHandler() {
    @Override
    public AliasTransformation getNextAliasTransformation() {
      return null;
    }
  };

  @Test
  public void testBasicUsage() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "goog.dom.createElement(goog.dom.TagName.DIV);";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCallWithArguments() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function(foo) {" +
        "  goog.dom.createElement(goog.dom.TagName.DIV, foo);" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "goog.dom.createElement(goog.dom.TagName.DIV, goog.dom.TagName.DIV);";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test(expected = AssertionError.class)
  public void testCallWithMultipleStatements() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  goog.dom.createTextNode('Hello World');" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    throw new AssertionError("Expected an error but none was thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testCallWithReturnStatement() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    throw new AssertionError("Expected an error but none was thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testCallWithThrowStatement() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  throw new Error('Test Error');" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    throw new AssertionError("Expected an error but none was thrown.");
  }

  @Test(expected = AssertionError.class)
  public void testCallWithThisKeyword() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  this.foo = 'Test';" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    throw new AssertionError("Expected an error but none was thrown.");
  }

  @Test
  public void testAliasRedefinition() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  DIV = 42;" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "goog.dom.TagName.DIV = 42;";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
    // The alias dom should not be overwritten.
    Node domAlias = getFirstChild(getRoot(compiler), 1);
    assertEquals("goog.dom", domAlias.getString());
  }

  @Test
  public void testInvalidScopeCall() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() { goog.scope(); });";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "JSC_GOOG_SCOPE_USED_IMPROPERLY: The call to goog.scope must be alone " +
        "in a single statement.";
    Node actual = getFirstChild(getRoot(compiler), 1);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testScopeCallWithArguments() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function(foo) {" +
        "  goog.scope(function(bar) {" +
        "  });" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only " +
        "a single parameter.  It must be an anonymous function that itself takes " +
        "no parameters.";
    Node actual = getFirstChild(getRoot(compiler), 1);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testAliasUsageWithInheritedScope() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  var foo = function() { return goog.dom.createElement(goog.dom.TagName.DIV); };" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    assertEquals(dom, goog.dom);" +
        "    assertEquals(dom.createElement, foo);" +
        "  });" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "goog.dom = function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "};" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  assertEquals(dom, goog.dom);" +
        "  assertEquals(dom.createElement, function() {" +
        "    return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "});";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testAliasUsageWithInheritedScopeAndAliases() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  var foo = goog.dom;" +
        "  goog.scope(function() {" +
        "    var bar = goog.dom;" +
        "    assertEquals(foo, bar);" +
        "  });" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "goog.scope(function() {" +
        "  var foo = goog.dom;" +
        "  goog.scope(function() {" +
        "    var bar = goog.dom;" +
        "    assertEquals(foo, bar);" +
        "  });" +
        "});" +
        "goog.dom = goog.dom;" +
        "goog.scope(function() {" +
        "  var foo = goog.dom;" +
        "  var bar = goog.dom;" +
        "  assertEquals(foo, bar);" +
        "});";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testAliasUsageWithGlobalScope() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "goog.scope(function() {" +
        "  var foo = goog.dom;" +
        "  goog.scope(function() {" +
        "    window.bar = goog.dom;" +
        "  });" +
        "});" +
        "goog.scope(function() {" +
        "  assertEquals(foo, window.bar);" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "goog.scope(function() {" +
        "  var foo = goog.dom;" +
        "  goog.scope(function() {" +
        "    window.bar = goog.dom;" +
        "  });" +
        "});" +
        "goog.scope(function() {" +
        "  assertEquals(foo, window.bar);" +
        "});" +
        "goog.dom = goog.dom;" +
        "window.bar = goog.dom";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testAliasUsageWithVariableScope() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "var foo = goog.dom;" +
        "goog.scope(function() {" +
        "  var bar = goog.dom;" +
        "  assertEquals(foo, bar);" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "var foo = goog.dom;" +
        "goog.scope(function() {" +
        "  var bar = goog.dom;" +
        "  assertEquals(foo, bar);" +
        "});" +
        "goog.dom = goog.dom";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testAliasUsageWithFunctionScope() {
    Compiler compiler = createCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, handler);
    String input =
        "function foo() {" +
        "  goog.scope(function() {" +
        "    var bar = goog.dom;" +
        "    assertEquals(foo.bar, bar);" +
        "  });" +
        "  var bar = goog.dom;" +
        "}" +
        "goog.scope(function() {" +
        "  assertEquals(foo.bar, goog.dom);" +
        "});";
    Node root = parseTree(compiler, input);
    scopedAliases.process(null, root);
    Node expected =
        "function foo() {" +
        "  goog.scope(function() {" +
        "    var bar = goog.dom;" +
        "    assertEquals(foo.bar, bar);" +
        "  });" +
        "  var bar = goog.dom;" +
        "}" +
        "goog.scope(function() {" +
        "  assertEquals(foo.bar, goog.dom);" +
        "});" +
        "goog.dom = goog.dom";
    Node actual = getRoot(compiler).getLastChild();
    assertEquals(expected.toString(), actual.toString());
  }

  private Node getFirstChild(Node parent, int index) {
    Node node = parent;
    for (int i = 0; i < index; i++) {
      node = node.getFirstChild();
    }
    return node;
  }

  private Node getRoot(Compiler compiler) {
    return compiler.getRootNode();
  }
}