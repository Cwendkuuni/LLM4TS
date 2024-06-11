import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Result;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private CompilerInput input;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws Exception {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler() {
      @Override
      public CompilerInput getMainInput() {
        return input;
      }

      @Override
      public CompilerOptions getOptions() {
        return options;
      }
    };

    checkSideEffects = new CheckSideEffects(compiler, OFF, false);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testCheckSideEffects() throws Exception {
    String sourceCode = "function foo() {} var x = 1; x = foo();";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertEquals(1, warnings.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", warnings.get(0));
  }

  @Test
  public void testCheckSideEffectsWithEmptyStatement() throws Exception {
    String sourceCode = "function foo() {} var x = 1; ; x = foo();";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithStringConcatenation() throws Exception {
    String sourceCode = "function foo() {} var s = 'this string is '; s += 'continued on the next line but you forgot the +';";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithFunctionCall() throws Exception {
    String sourceCode = "function foo() {} var x = 1; x = foo();";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertEquals(1, warnings.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", warnings.get(0));
  }

  @Test
  public void testCheckSideEffectsWithFunctionCallAndAssignment() throws Exception {
    String sourceCode = "function foo() {} var x = 1; x = foo(); x = 2;";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithSimpleOperators() throws Exception {
    String sourceCode = "function foo() {} var x = 1; var y = 2; x += y;";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithExpressionResult() throws Exception {
    String sourceCode = "function foo() {} var x = (1 + 2);";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithBlockStatements() throws Exception {
    String sourceCode = "function foo() {} var x = 1;\nif (true) {\n\tvar y = 2;\n} x = foo();";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithForLoop() throws Exception {
    String sourceCode = "function foo() {} for (var i = 0; i < 10; i++) {} x = foo();";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithEvalCall() throws Exception {
    String sourceCode = "function foo() {} x = eval(foo);";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
    // The test should fail because of the eval call.
    assertEquals(1, compiler.getAllErrors().size());
  }

  @Test
  public void testCheckSideEffectsWithProtectedCode() throws Exception {
    String sourceCode = "function foo() {} JSCOMPILER_PRESERVE(function bar() { var x = 1; x = foo(); }); JSCOMPILER_PRESERVE(bar);";
    input = new TestSourceFile("TestFile.js", sourceCode);
    checkSideEffects.process(input, nodeFactory.createRootNode(sourceCode));
    List<String> warnings = compiler.getAllErrors();
    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithProtectedCodeAndDeadCodeRemoval() throws Exception {
    String sourceCode = "function foo() {} JSCOMPILER_PRESERVE(function bar() { var x = 1; x = foo(); }); JSCOMPILER_PRESERVE(bar); function baz() {}";
    input = new TestSourceFile("TestFile.js", sourceCode);
    compiler.addPasses(new CheckSideEffects(compiler, OFF, false), new CheckSideEffects.StripProtection(compiler));
    Result result = compiler.compileSingleFile(input, null);
    List<String> warnings = result.getErrors();
    assertTrue(warnings.isEmpty());
  }
}