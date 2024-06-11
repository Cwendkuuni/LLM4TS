import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JSEmitter.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeType.*;
import static com.google.javascript.jscomp.Token.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSEmitter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.SourceFile;

public class VarCheckTest {

  private static Compiler createCompiler(CompilerOptions options) {
    return new Compiler(options);
  }

  private static void testVarCheck(Compiler compiler, String input,
      String expectedOutput) {
    SourceFile source = new SourceFile("test.js", null);
    Node root = parseTree(source, input);

    CompilerPass varCheck = new VarCheck(compiler);
    varCheck.process(null, root);

    JSEmitter emitter = new JSEmitter(compiler.getOptions());
    emit(emitter, root);
    String output = emitter.getCompiledSourceForTesting();

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSimple() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String input =
        "function foo() {" +
        "  var x;" +
        "  x = 42;" +
        "}";

    String expectedOutput =
        "function foo() {}\n" +
        "x = 42;";

    testVarCheck(compiler, input, expectedOutput);
  }

  @Test
  public void testUndefinedVariable() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String input = "function foo() { y = 42; }";

    String expectedOutput =
        "function foo() {" +
        "  // TypeError: y is undefined\n" +
        "}";

    testVarCheck(compiler, input, expectedOutput);
  }

  @Test
  public void testModuleDependency() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String fooInput =
        "// foo.js\n" +
        "function foo() {" +
        "  x = 42;" +
        "}";

    String barInput =
        "// bar.js\n" +
        "import foo from './foo.js';\n" +
        "function bar() {" +
        "  x = 5;" +
        "}";

    String expectedFooOutput = "function foo() {}";
    String expectedBarOutput = "function bar() { x = 5; }";

    testVarCheck(compiler, fooInput, expectedFooOutput);
    testVarCheck(compiler, barInput, expectedBarOutput);

    String input = "// test.js\n" +
        "import bar from './bar.js';\n" +
        "function test() {" +
        "  bar();" +
        "}";

    String expectedOutput = "function test() {" +
        "  bar();" +
        "}";

    testVarCheck(compiler, input, expectedOutput);
  }

  @Test
  public void testDuplicateDeclaration() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String input = "function foo() {" +
        "  var x = 1;" +
        "  var x = 2;" +
        "}";

    String expectedOutput = "function foo() {" +
        "  var x = 1;" +
        "}";

    testVarCheck(compiler, input, expectedOutput);
  }

  @Test
  public void testEmptyFunctionName() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String input = "function() {}";

    try {
      testVarCheck(compiler, input, "");
      fail();
    } catch (AssertionError e) {
      // Expected INVALID_FUNCTION_DECL error.
    }
  }

  @Test
  public void testMissingModuleDependency() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String fooInput = "function foo() {}";
    String barInput = "import foo from './foo.js'; function bar() {}";

    testVarCheck(compiler, fooInput, "function foo() {}");

    try {
      testVarCheck(compiler, barInput, "");
      fail();
    } catch (AssertionError e) {
      // Expected MISSING_MODULE_DEPENDENCY warning.
    }

    CompilerInput foo = compiler.getInput(0);
    CompilerInput bar = compiler.getInput(1);
    assertEquals(ImmutableSet.of(foo), bar.getRequiredModules());

    String expectedOutput = "import foo from './foo.js'; function bar() {}";

    testVarCheck(compiler, expectedOutput, expectedOutput);
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options, ImmutableSet.of("strict"));

    String fooInput = "// foo.js\n" +
        "function foo() {}";

    String barInput = "import foo from './foo.js'; function bar() {}";

    testVarCheck(compiler, fooInput, "function foo() {}");

    try {
      testVarCheck(compiler, barInput, "");
      fail();
    } catch (AssertionError e) {
      // Expected STRICT_MODULE_DEPENDENCY error.
    }
  }

  @Test
  public void testNameReferenceInExterns() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    String input = "// test.js\n" +
        "import * as foo from './foo.js';\n" +
        "foo.x = 42;";

    CompilerInput fooInput = new CompilerInput("foo.js", null);
    fooInput.setAstRoot(Node.newText(""));

    testVarCheck(compiler, input, "");

    testVarCheck(compiler, fooInput.getInput(), "");
  }

  @Test
  public void testUndefinedExternVariable() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = createCompiler(options);

    CompilerInput input = new CompilerInput("test.js", null);
    input.setAstRoot(Node.newText(""));

    testVarCheck(compiler, input.getInput(), "");

    String expectedOutput =
        "import * as externals from './externals.js';\n" +
        "externals.x = 42;";

    testVarCheck(compiler, expectedOutput, expectedOutput);
  }
}