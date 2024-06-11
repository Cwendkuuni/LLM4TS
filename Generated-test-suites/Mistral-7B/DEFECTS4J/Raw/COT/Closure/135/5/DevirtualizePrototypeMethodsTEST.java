import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import java.util.Collection;
import java.util.List;

/**
 * Test for DevirtualizePrototypeMethods class.
 */
public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    CompilerContext context = new CompilerContext();
    compiler = new AbstractCompiler(context) {
      @Override
      public CompilerPass getProcessor(String passName, List<String> options) {
        if (passName.equals("devirtualizePrototypeMethods")) {
          return new DevirtualizePrototypeMethods(this);
        }
        return null;
      }
    };
  }

  @Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    String input =
        "function Foo() {}\n" +
            "Foo.prototype.bar = function(a) { return a + 1; }\n" +
            "var foo = new Foo();\n" +
            "var result = foo.bar(2);\n";

    String expectedOutput =
        "var bar = function(self, a) { return a + 1; }\n" +
            "var result = bar(foo, 2);\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithTwoArgs() throws Exception {
    String input =
        "function Foo() {}\n" +
            "Foo.prototype.bar = function(a, b) { return a + b; }\n" +
            "var foo = new Foo();\n" +
            "var result = foo.bar(1, 2);\n";

    String expectedOutput =
        "var bar = function(self, a, b) { return a + b; }\n" +
            "var result = bar(foo, 1, 2);\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithVarArgs() throws Exception {
    String input =
        "function Foo() {}\n" +
            "Foo.prototype.bar = function() { var args = arguments; return args.length; }\n" +
            "var foo = new Foo();\n" +
            "var result = foo.bar(1, 2, 3, 4);\n";

    String expectedOutput =
        "var bar = function(self) { return arguments.length; }\n" +
            "var result = bar(foo);\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithCyclicDependency() throws Exception {
    String input =
        "function A() {}\n" +
            "A.prototype.foo = function() { return this.bar(); }\n" +
            "A.prototype.bar = function() { return this.foo(); }\n" +
            "function B() {}\n" +
            "B.prototype.foo = function() { return this.bar(); }\n" +
            "B.prototype.bar = function() { return new A(); }\n" +
            "var a = new A();\n" +
            "var b = new B();\n" +
            "var result = a.foo();\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals("", output); // should have thrown an error
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithNestedFunction() throws Exception {
    String input =
        "function Foo() {}\n" +
            "Foo.prototype.bar = function() {\n" +
            "  return function() {\n" +
            "    return this.baz();\n" +
            "  };\n" +
            "}\n" +
            "Foo.prototype.baz = function() {\n" +
            "  return 42;\n" +
            "}\n" +
            "var foo = new Foo();\n" +
            "var result = foo.bar()();\n";

    String expectedOutput =
        "var bar = function(self) {\n" +
            "  return function() {\n" +
            "    return self.baz();\n" +
            "  };\n" +
            "}\n" +
            "var baz = function(self) {\n" +
            "  return 42;\n" +
            "}\n" +
            "var result = bar(foo)();\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithCtor() throws Exception {
    String input =
        "function Foo() {\n" +
            "  this.bar = function() { return 42; };\n" +
            "}\n" +
            "var foo = new Foo();\n" +
            "var result = foo.bar();\n";

    String expectedOutput =
        "function Foo() {\n" +
            "  this.bar = function(self) {\n" +
            "    return 42;\n" +
            "  };\n" +
            "}\n" +
            "var result = Foo.prototype.bar(foo);\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithUndefinedPrototype() throws Exception {
    String input =
        "function Foo() {\n" +
            "  this.bar = function() { return 42; };\n" +
            "}\n" +
            "var foo = new Foo();\n" +
            "delete Foo.prototype.bar;\n" +
            "var result = foo.bar();\n";

    String expectedOutput =
        "function Foo() {\n" +
            "  this.bar = function(self) {\n" +
            "    return 42;\n" +
            "  };\n" +
            "}\n" +
            "var result = Foo.prototype.bar(foo);\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionType() throws Exception {
    String input =
        "function Foo() {\n" +
            "  this.bar = function() { return 42; };\n" +
            "}\n" +
            "Foo.prototype.bar.prototype = Object.create(\n" +
            "  Object.getPrototypeOf(\n" +
            "    new FunctionType(null, [\"this\"])\n" +
            "  ).prototype\n" +
            ");\n" +
            "Foo.prototype.bar.prototype.baz = function() {\n" +
            "  return 43;\n" +
            "}\n" +
            "var foo = new Foo();\n" +
            "var result = foo.bar().baz();\n";

    String expectedOutput =
        "function Foo() {\n" +
            "  this.bar = function(self) {\n" +
            "    return new FunctionType(null, [\"this\"]).call(self, self);\n" +
            "  };\n" +
            "}\n" +
            "Foo.prototype.bar.prototype = Object.create(\n" +
            "  Object.getPrototypeOf(\n" +
            "    new FunctionType(ObjectType.getTypeOf(FunctionType.class), [\n" +
            "      ObjectType.getTypeOf(FunctionType.class), \"this\"\n" +
            "    ])\n" +
            "  ).prototype\n" +
            ");\n" +
            "Foo.prototype.bar.prototype.baz = function(self) {\n" +
            "  return 43;\n" +
            "}\n" +
            "var result = Foo.prototype.bar(foo).baz();\n";

    String output = CompilationTestHelper.testInnerJs(compiler, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithTypeError() throws Exception {
    String input =
        "function Foo() {\n" +
            "  this.bar = function() { throw new TypeError(\"foo\"); };\n" +
            "}\n" +
            "var foo = new Foo();\n" +
            "foo.bar();\n";

    CompilationTestHelper.expectErrors(compiler, input, "DEVIRTUALIZE_PROTOTYPE_METHODS_ERROR");
  }
}