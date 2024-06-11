package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.NodeOp.IS_CALL;
import static com.google.javascript.rhino.NodeOp.IS_GET_PROP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.TestUtils;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Tests for the DevirtualizePrototypeMethods compiler pass.
 */
public class DevirtualizePrototypeMethodsTest {
  private static abstract class TestCase {
    private final Compiler compiler;
    private final AbstractCompiler abstractCompiler;
    private final String code;
    private final String expectedOutput;
    private final String expectedErrorMessage;

    TestCase(String code, String expectedOutput, String expectedErrorMessage) {
      this.code = code;
      this.expectedOutput = expectedOutput;
      this.expectedErrorMessage = expectedErrorMessage;
    }

    TestCase(String code, String expectedOutput) {
      this(code, expectedOutput, null);
    }

    abstract void configureCompiler();

    void runTest() {
      compiler = TestUtils.createCompiler();
      configureCompiler();
      compiler.addPass(new DevirtualizePrototypeMethods(abstractCompiler));
      Node root = TestUtils.parse(compiler, code);
      Node externs = Node.ROOT_NODE;
      compiler.process(externs, root);
      String actualOutput = TestUtils.printTree(compiler, root);
      if (expectedOutput != null) {
        assertEquals("Expected:\n" + expectedOutput,
            "Actual:\n" + actualOutput);
      }
      if (expectedErrorMessage != null) {
        assertNotNull("Expected error message:\n" + expectedErrorMessage,
            compiler.getErrors().findMessageByType(ErrorType.ERROR));
      } else {
        assertNull("Expected no error messages",
            compiler.getErrors().findMessageByType(ErrorType.ERROR));
      }
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass correctly identifies
   * ineligible method definitions.
   */
  public static class IneligibleDefinitionTest extends TestCase {
    IneligibleDefinitionTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function(x) {\n" +
          "  this.qux = x;\n" +
          "}\n" +
          "Foo.prototype.qux = function() {\n" +
          "  return this.x;\n" +
          "}\n" +
          "Foo.prototype.x = 10;\n" +
          "var f = new Foo();\n" +
          "f.bar(11);",
          null,
          "Expected only one definition for property 'qux'");
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions()
          .withExterns(
              "window: {qux: number}"));
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass rewrites eligible
   * method definitions.
   */
  public static class EligibleDefinitionTest extends TestCase {
    EligibleDefinitionTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function(x) {\n" +
          "  this.qux += x;\n" +
          "}\n" +
          "var f = new Foo();",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass rewrites eligible
   * method definitions in a closure.
   */
  public static class EligibleDefinitionInClosureTest extends TestCase {
    EligibleDefinitionInClosureTest() {
      super("(function() {\n" +
          "  function Foo() {}\n" +
          "  Foo.prototype.bar = function(x) {\n" +
          "    this.qux += x;\n" +
          "  }\n" +
          "  var f = new Foo();\n" +
          "})();",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass rewrites eligible
   * method definitions defined on an object literal.
   */
  public static class EligibleDefinitionInObjectLiteralTest extends TestCase {
    EligibleDefinitionInObjectLiteralTest() {
      super("var foo = {\n" +
          "  bar: function(x) {\n" +
          "    this.qux += x;\n" +
          "  }\n" +
          "};\n" +
          "var f = foo;\n",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass preserves type information.
   */
  public static class PreservesTypeInformationTest extends TestCase {
    PreservesTypeInformationTest() {
      super("function Foo(x) {}\n" +
          "Foo.prototype.bar = function(y) {\n" +
          "  return this.x + y;\n" +
          "}\n" +
          "Foo.prototype.x = 10;\n" +
          "var f = new Foo(1);\n" +
          "var result = f.bar(2);",
          "function Foo(x) { var qux; return (function() {\n" +
          "  qux = this;\n" +
          "  return (function(y) {\n" +
          "    qux.x = x;\n" +
          "    return qux.bar(y);\n" +
          "  })(2);\n" +
          "})()\n" +
          "var result = result;\n",
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass rewrites method calls correctly.
   */
  public static class RewritesMethodCallsTest extends TestCase {
    RewritesMethodCallsTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function(x) {\n" +
          "  this.qux += x;\n" +
          "}\n" +
          "var f = new Foo();\n" +
          "f.bar(1);\n" +
          "var result = f.qux;\n",
          "var bar = function(self, x) {\n" +
          "  self.qux += x;\n" +
          "};\n" +
          "var f = new Foo();\n" +
          "bar(f, 1);\n" +
          "var result = f.qux;\n",
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass handles function calls in loops.
   */
  public static class FunctionCallsInLoopsTest extends TestCase {
    FunctionCallsInLoopsTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function() {\n" +
          "  this.qux++;\n" +
          "}\n" +
          "var f = new Foo();\n" +
          "for (var i = 0; i < 100000; i++) {\n" +
          "  f.bar();\n" +
          "}\n",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass handles method calls in conditionals.
   */
  public static class MethodCallsInConditionalsTest extends TestCase {
    MethodCallsInConditionalsTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function() {\n" +
          "  return this.qux > 0;\n" +
          "}\n" +
          "var f = new Foo();\n" +
          "if (f.bar()) {\n" +
          "  var result = 1;\n" +
          "} else {\n" +
          "  var result = 0;\n" +
          "}",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass handles method calls in functions.
   */
  public static class MethodCallsInFunctionsTest extends TestCase {
    MethodCallsInFunctionsTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function() {\n" +
          "  return this.qux + 1;\n" +
          "}\n" +
          "function Baz(x) {\n" +
          "  return Foo.prototype.bar.call(this, x);\n" +
          "}",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass handles method calls with arguments.
   */
  public static class MethodCallsWithArgumentsTest extends TestCase {
    MethodCallsWithArgumentsTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function(x) {\n" +
          "  return this.qux + x;\n" +
          "}\n" +
          "var f = new Foo();\n" +
          "var result = f.bar(10);",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  /**
   * Tests that the DevirtualizePrototypeMethods pass handles method calls with variable arguments.
   */
  public static class MethodCallsWithVariableArgumentsTest extends TestCase {
    MethodCallsWithVariableArgumentsTest() {
      super("function Foo() {}\n" +
          "Foo.prototype.bar = function() {\n" +
          "  var args = arguments;\n" +
          "  return this.qux + args.length;\n" +
          "}\n" +
          "var f = new Foo();\n" +
          "var result = f.bar(1, 2, 3);",
          null,
          null);
    }

    @Override
    void configureCompiler() {
      compiler.setOptions(CompilerOptions.defaultOptions());
    }
  }

  public static void main(String[] args) {
    List<TestCase> tests = Lists.newArrayList(
        new IneligibleDefinitionTest(),
        new EligibleDefinitionTest(),
        new EligibleDefinitionInClosureTest(),
        new EligibleDefinitionInObjectLiteralTest(),
        new PreservesTypeInformationTest(),
        new RewritesMethodCallsTest(),
        new FunctionCallsInLoopsTest(),
        new MethodCallsInConditionalsTest(),
        new MethodCallsWithArgumentsTest(),
        new MethodCallsWithVariableArgumentsTest());

    for (TestCase test : tests) {
      test.runTest();
    }
  }
}