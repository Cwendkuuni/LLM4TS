import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilationTest.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.CompilerTestCase;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for ScopedAliases class.
 */
public class ScopedAliasesTest extends CompilerTestCase {

  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  goog.dom.createElement(DIV);\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<String> jsOutputs = getRewrittenSources(compiler);
    assertEquals(1, jsOutputs.size());

    String jsOutput = jsOutputs.get(0);
    assertTrue(jsOutput.contains("goog.dom.createElement(goog.dom.TagName.DIV)"));
  }

  public void testNonAliasedLocalVariable() {
    String input = "goog.scope(function() {\n" +
            "  var x = 0;\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);

    assertErrors(errors, "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", "x");
  }

  public void testAliasRedefined() {
    String input = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  var DIV = 'test';\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_ALIAS_REDEFINED", "DIV");
  }

  public void testScopeWithFunctionReturn() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    return 'hello';\n" +
            "  }\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_USES_RETURN");
  }

  public void testScopeWithThisKeyword() {
    String input = "goog.scope(function() {\n" +
            "  var scope = this;\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_REFERENCES_THIS");
  }

  public void testScopeWithThrowKeyword() {
    String input = "goog.scope(function() {\n" +
            "  throw new Error('test');\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_USES_THROW");
  }

  public void testScopeWithInvalidParameters() {
    String input = "goog.scope(function(a, b) {\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
  }

  public void testScopeWithFunctionArgument() {
    String input = "goog.scope(function(dom) {\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  goog.dom.createElement(DIV);\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<String> jsOutputs = getRewrittenSources(compiler);
    assertEquals(1, jsOutputs.size());

    String jsOutput = jsOutputs.get(0);
    assertTrue(jsOutput.contains("goog.dom.createElement(goog.dom.TagName.DIV)"));
  }

  public void testScopeWithFunctionReturnType() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    return 'hello';\n" +
            "  }\n" +
            "  return new foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_USES_RETURN");

    // Workaround since JUnit does not support checking the rewritten code
    String jsOutput = getOutputFromCompiler(compiler);
    assertFalse(jsOutput.contains("new foo()"));
  }

  public void testScopeWithFunctionAsVariable() {
    String input = "goog.scope(function() {\n" +
            "  var foo = function() {\n" +
            "    return 'hello';\n" +
            "  };\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_USES_RETURN");

    // Workaround since JUnit does not support checking the rewritten code
    String jsOutput = getOutputFromCompiler(compiler);
    assertFalse(jsOutput.contains("return foo()"));
  }

  public void testScopeWithFunctionVariableAsParameter() {
    String input = "goog.scope(function(foo) {\n" +
            "  return foo();\n" +
            "});\n" +
            "function foo() {\n" +
            "  return 'hello';\n" +
            "}";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<String> jsOutputs = getRewrittenSources(compiler);
    assertEquals(2, jsOutputs.size());

    String jsOutput1 = jsOutputs.get(0);
    assertTrue(jsOutput1.contains("return foo();"));

    String jsOutput2 = jsOutputs.get(1);
    assertTrue(jsOutput2.contains("return 'hello';"));
  }

  public void testScopeWithNestedFunction() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    function bar() {\n" +
            "      return 'hello';\n" +
            "    }\n" +
            "    return bar();\n" +
            "  }\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_USES_RETURN");

    // Workaround since JUnit does not support checking the rewritten code
    String jsOutput = getOutputFromCompiler(compiler);
    assertFalse(jsOutput.contains("return bar()"));
  }

  public void testScopeWithFunctionVariableAsLocal() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    return 'hello';\n" +
            "  }\n" +
            "  var foo = foo;\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_ALIAS_REDEFINED", "foo");

    // Workaround since JUnit does not support checking the rewritten code
    String jsOutput = getOutputFromCompiler(compiler);
    assertFalse(jsOutput.contains("return foo()"));
  }

  public void testScopeWithScopedFunctionAndVariable() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    return 'hello';\n" +
            "  }\n" +
            "  var bar = 0;\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<String> jsOutputs = getRewrittenSources(compiler);
    assertEquals(1, jsOutputs.size());

    String jsOutput = jsOutputs.get(0);
    assertTrue(jsOutput.contains("return 'hello';"));
    assertFalse(jsOutput.contains("var bar"));
  }

  public void testScopeWithScopedFunctionReturningScopedFunction() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    return function(x) {\n" +
            "      return x * 2;\n" +
            "    };\n" +
            "  }\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<String> jsOutputs = getRewrittenSources(compiler);
    assertEquals(1, jsOutputs.size());

    String jsOutput = jsOutputs.get(0);
    assertTrue(jsOutput.contains("function(x) { return x * 2; }"));
  }

  public void testScopeWithFunctionReturningFunctionWithArguments() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    function bar(x, y) {\n" +
            "      return x + y;\n" +
            "    }\n" +
            "    return bar;\n" +
            "  }\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<CompilationError> errors = getErrors(compiler);
    assertErrors(errors, "JSC_GOOG_SCOPE_USES_THROW");

    // Workaround since JUnit does not support checking the rewritten code
    String jsOutput = getOutputFromCompiler(compiler);
    assertFalse(jsOutput.contains("function bar(x, y) { return x + y; }"));
  }

  public void testScopeWithFunctionReturningFunctionWithThis() {
    String input = "goog.scope(function() {\n" +
            "  function foo() {\n" +
            "    function bar() {\n" +
            "      return this.x;\n" +
            "    }\n" +
            "    return bar;\n" +
            "  }\n" +
            "  this.x = 'hello';\n" +
            "  return foo();\n" +
            "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);

    Compiler compiler = newCompiler(options);
    compile(compiler, input);

    List<String> jsOutputs = getRewrittenSources(compiler);
    assertEquals(1, jsOutputs.size());

    String jsOutput = jsOutputs.get(0);
    assertTrue(jsOutput.contains("function bar() { return this.x; }"));
  }
}