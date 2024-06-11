import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerUtils;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;

public class DevirtualizePrototypeMethodsTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    nodeFactory = compiler.getNodeFactory();
  }

  @Test
  public void testDevirtualizePrototypeMethods_SimpleCase() {
    String jsCode = "function A() {} A.prototype.foo = function(x) { return x; } " +
        "var a = new A(); var y = a.foo(10); assertEquals(10, y);";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    String expectedCode = "var foo = function(self, x) { return x; } " +
        "var y = foo(a, 10); assertEquals(10, y);";

    assertEquals(expectedCode, CompilerUtils.toSource(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethods_MultipleCalls() {
    String jsCode = "function A() {} A.prototype.foo = function(x) { return x; } " +
        "var a = new A(); var x = 10; var y = a.foo(x); var z = a.foo(x); " +
        "assertEquals(x, y); assertEquals(x, z);";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    String expectedCode = "var foo = function(self, x) { return x; } " +
        "var y = foo(a, x); var z = foo(a, x); assertEquals(x, y); assertEquals(x, z);";

    assertEquals(expectedCode, CompilerUtils.toSource(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethods_RecursiveFunction() {
    String jsCode = "function A() {} A.prototype.foo = function(x) {" +
        "if (x > 0) {" +
        "  return x - 1; " +
        "} else {" +
        "  return x + 1; " +
        "}" +
        "} ";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    String expectedCode = "var foo = function(self, x) {" +
        "if (x > 0) {" +
        "  return x - 1; " +
        "} else {" +
        "  return x + 1; " +
        "}" +
        "}; " +
        "function JSCompiler_StaticMethods__foo(self, x) {" +
        "if (self === undefined) {" +
        "  self = this; " +
        "}" +
        "return self.foo(x); " +
        "}; " +
        "var x = 5; var result = JSCompiler_StaticMethods__foo(a, x); " +
        "assertEquals(4, result);";

    assertEquals(expectedCode, CompilerUtils.toSource(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethods_FunctionWithVarArgs() {
    String jsCode = "function A() {} A.prototype.foo = function() {" +
        "var args = arguments; return args.length; " +
        "}";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    String expectedCode = "function A() {} " +
        "A.prototype.foo = function(self, args) {" +
        "return args.length; " +
        "}; " +
        "var args = [1, 2, 3]; " +
        "var result = JSCompiler_StaticMethods__foo(a, args); " +
        "assertEquals(3, result);";

    assertEquals(expectedCode, CompilerUtils.toSource(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethods_PropertyAccessInsideCall() {
    String jsCode = "function A() {} A.prototype.foo = function() {" +
        "return this.x; " +
        "} " +
        "var a = new A(); a.x = 10; " +
        "var result = a.foo(); " +
        "assertEquals(10, result);";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    String expectedCode = "function A() {" +
        "this.x = 0; " +
        "} " +
        "A.prototype.foo = function(self) {" +
        "return self.x; " +
        "}; " +
        "var a = new A(); a.x = 10; " +
        "var result = JSCompiler_StaticMethods__foo(a); " +
        "assertEquals(10, result);";

    assertEquals(expectedCode, CompilerUtils.toSource(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethods_CallSiteInsideLoop() {
    String jsCode = "function A() {} A.prototype.foo = function() {" +
        "return 1; " +
        "} " +
        "function bar() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  var result = a.foo(); " +
        "}" +
        "}";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    String expectedCode = "function A() {} " +
        "A.prototype.foo = function(self) {" +
        "return 1; " +
        "}; " +
        "function bar() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  var result = JSCompiler_StaticMethods__foo(a); " +
        "}" +
        "}";

    assertEquals(expectedCode, CompilerUtils.toSource(rewrittenCode));
  }

  @Test(expected = AssertionError.class)
  public void testDevirtualizePrototypeMethods_TwoDefinitions() {
    String jsCode = "function A() {} A.prototype.foo = function() {" +
        "return 1; " +
        "}" +
        "A.prototype.foo = function() {" +
        "return 2; " +
        "} " +
        "var result = a.foo();";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    Node rewrittenCode = CompilerUtils.getCompiledCode(root);
    // Assertion error expected since the code is ambiguous.
  }

  @Test(expected = AssertionError.class)
  public void testDevirtualizePrototypeMethods_CallSiteBeforeDefinition() {
    String jsCode = "var a = {}; a.foo = function() {" +
        "return 1; " +
        "} " +
        "function A() {} A.prototype.foo = function() {" +
        "return 2; " +
        "} " +
        "var result = a.foo();";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    // Assertion error expected since the call site is defined before the function definition.
  }

  @Test(expected = AssertionError.class)
  public void testDevirtualizePrototypeMethods_Cycle() {
    String jsCode = "function A() {" +
        "this.foo = A; " +
        "}" +
        "A.prototype.foo = function() {" +
        "return 1; " +
        "}";

    Node root = compiler.parseJavascript(jsCode, null, options);

    CompilerPass devitationalizer = new DevirtualizePrototypeMethods(compiler);
    devitationalizer.process(null, root);

    // Assertion error expected since there is a cycle in the prototype chain.
  }
}