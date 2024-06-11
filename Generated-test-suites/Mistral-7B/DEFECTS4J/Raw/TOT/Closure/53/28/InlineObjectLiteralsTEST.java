import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    compiler = new Compiler(options);
    passManager = new CompilerPassManager(compiler);
  }

  /**
   * Test case for inlining simple object literals
   */
  @Test
  public void testInlineSimpleObject() {
    String input = "function foo() {" +
        "  var obj = {a: 1, b: 2};" +
        "  var c = obj.a;" +
        "  var d = obj.b;" +
        "  return c + d;" +
        "}";

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new InlineObjectLiterals(compiler, compiler.getSafeNameIdSupplier())
            .process(externs, root);
      }
    });

    passManager.runPasses(new Node(Token.PROGRAM, null, Arrays.asList(
        Node.newString(Token.SQUOTE, input)
    )));

    String expectedOutput = "function foo() {" +
        "  var a = JSCompiler_object_inline_a_0;" +
        "  var b = JSCompiler_object_inline_b_0;" +
        "  return a + b;" +
        "}";

    verifyCompilerOutput(expectedOutput);
  }

  /**
   * Test case for inlining complex object literals with nested properties
   */
  @Test
  public void testInlineNestedObject() {
    String input = "function foo() {" +
        "  var obj = {a: {b: 1, c: 2}};" +
        "  var d = obj.a.b;" +
        "  return d;" +
        "}";

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new InlineObjectLiterals(compiler, compiler.getSafeNameIdSupplier())
            .process(externs, root);
      }
    });

    passManager.runPasses(new Node(Token.PROGRAM, null, Arrays.asList(
        Node.newString(Token.SQUOTE, input)
    )));

    String expectedOutput = "function foo() {" +
        "  var b_0 = JSCompiler_object_inline_b_1_0;" +
        "  return b_0;" +
        "}";

    verifyCompilerOutput(expectedOutput);
  }

  /**
   * Test case for inlining object literals containing arrays
   */
  @Test
  public void testInlineObjectWithArrays() {
    String input = "function foo() {" +
        "  var obj = {a: [1, 2], b: 3};" +
        "  return obj.a[0] + obj.b;" +
        "}";

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new InlineObjectLiterals(compiler, compiler.getSafeNameIdSupplier())
            .process(externs, root);
      }
    });

    passManager.runPasses(new Node(Token.PROGRAM, null, Arrays.asList(
        Node.newString(Token.SQUOTE, input)
    )));

    String expectedOutput = "function foo() {" +
        "  var a_0_0 = JSCompiler_object_inline_a_0_0_0;" +
        "  var b = JSCompiler_object_inline_b_0;" +
        "  return a_0_0 + b;" +
        "}";

    verifyCompilerOutput(expectedOutput);
  }

  /**
   * Test case for inlining object literals with cyclic references
   */
  @Test(expected = AssertionError.class)
  public void testInlineCyclicObject() {
    String input = "function foo() {" +
        "  var obj1 = {a: obj2};" +
        "  var obj2 = {a: obj1};" +
        "}";

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new InlineObjectLiterals(compiler, compiler.getSafeNameIdSupplier())
            .process(externs, root);
      }
    });

    try {
      passManager.runPasses(new Node(Token.PROGRAM, null, Arrays.asList(
          Node.newString(Token.SQUOTE, input)
      )));
      fail("Expected compilation error but succeeded instead.");
    } catch (AssertionError e) {
      // Expected
    }
  }

  /**
   * Test case for inlining object literals within a function expression
   */
  @Test
  public void testInlineObjectInFunctionExpression() {
    String input = "function foo() {" +
        "  var obj = function(x) {" +
        "    return {a: x, b: 2};" +
        "  }();" +
        "  return obj.a;" +
        "}";

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new InlineObjectLiterals(compiler, compiler.getSafeNameIdSupplier())
            .process(externs, root);
      }
    });

    passManager.runPasses(new Node(Token.PROGRAM, null, Arrays.asList(
        Node.newString(Token.SQUOTE, input)
    )));

    String expectedOutput = "function foo() {" +
        "  var a = JSCompiler_object_inline_a_1_0;" +
        "  return a;" +
        "}";

    verifyCompilerOutput(expectedOutput);
  }

  /**
   * Test case for inlining object literals within a for loop
   */
  @Test
  public void testInlineObjectInForLoop() {
    String input = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var obj = {a: i};" +
        "  }" +
        "  return 0;" +
        "}";

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new InlineObjectLiterals(compiler, compiler.getSafeNameIdSupplier())
            .process(externs, root);
      }
    });

    passManager.runPasses(new Node(Token.PROGRAM, null, Arrays.asList(
        Node.newString(Token.SQUOTE, input)
    )));

    String expectedOutput = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var a = JSCompiler_object_inline_a_2_i;" +
        "  }" +
        "  return 0;" +
        "}";

    verifyCompilerOutput(expectedOutput);
  }

  private void verifyCompilerOutput(String expectedOutput) {
    List<String> messages = compiler.getErrors();
    assert messages.isEmpty();

    String actualOutput = compiler.toString();
    assertEquals(expectedOutput, actualOutput);
  }
}