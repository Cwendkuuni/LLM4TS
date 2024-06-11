import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test_utils.CompileTestRunner;
import com.google.javascript.rhino.Token;

import java.util.Set;

/**
 * Tests for {@link VarCheck} class.
 */
public class VarCheckTest {

  private static CompilerInput createInputWithUndefinedVariable(String variableName) {
    return new CompilerInput(
        "test",
        "function foo() { " + variableName + "; }",
        null
    );
  }

  private static CompilerInput createInputWithDuplicateVariable(String variableName) {
    return new CompilerInput(
        "test",
        "function foo() { var x = " + variableName + "; }" +
        "function bar() { var x = " + variableName + "; }",
        null
    );
  }

  private static CompilerInput createInputWithUnresolvedDependencies(
      String currentModuleName, String dependedModuleName) {
    return new CompilerInput(
        currentModuleName,
        "function foo() {" +
        "  var x = require('" + dependedModuleName + "').y;" +
        "}",
        null
    );
  }

  private static CompilerInput createInputWithResolvedDependencies(
      String currentModuleName, String dependedModuleName) {
    CompilerInput dependedInput = new CompilerInput(
        dependedModuleName,
        "function y() { }",
        null
    );
    return new CompilerInput(
        currentModuleName,
        "function foo() {" +
        "  var x = require('" + dependedModuleName + "').y;" +
        "}",
        dependedInput
    );
  }

  private static CompilerInput createInputWithMissingDependencies(
      String currentModuleName, String dependedModuleName) {
    return new CompilerInput(
        currentModuleName,
        "function foo() {" +
        "  var x = require('" + dependedModuleName + "').y;" +
        "}",
        null
    );
  }

  private static CompilerInput createInputWithStrictDependencies(
      String currentModuleName, String dependedModuleName) {
    return new CompilerInput(
        currentModuleName,
        "function foo() {" +
        "  var x = require('" + dependedModuleName + "').y;" +
        "}",
        null,
        JSError.make("", 0, 0, DiagnosticType.disabled("JSC_STRICT_MODULE_DEPENDENCY",
            "module " + currentModuleName + " cannot reference " + dependedModuleName))
    );
  }

  private static CompilerInput createInputWithNameReferenceInExterns(String variableName) {
    return new CompilerInput(
        "test",
        "//<externs>\n" +
        "var " + variableName + ";" +
        "\n//</externs>\n" +
        "function foo() { }",
        null
    );
  }

  private static CompilerInput createInputWithUndefinedExternVariable(String variableName) {
    return new CompilerInput(
        "test",
        "//<externs>\n" +
        "var " + variableName + ";" +
        "\n//</externs>\n" +
        "function foo() { }",
        null,
        JSError.make("", 0, 0, DiagnosticType.warning("JSC_UNDEFINED_EXTERN_VAR_ERROR",
            "name " + variableName + " is not undefined in the externs."))
    );
  }

  private static void checkNoErrors(CompilerPass pass, CompilerInput input) {
    AbstractCompiler compiler = new TestCompiler();
    CompileTestRunner runner = new CompileTestRunner(compiler);
    Node root = runner.compile(input);
    assertNull(pass.getErrors().getDiagnostics());
  }

  private static void checkErrors(CompilerPass pass, CompilerInput input,
      DiagnosticType expectedDiagnosticType, String expectedMessage) {
    AbstractCompiler compiler = new TestCompiler();
    CompileTestRunner runner = new CompileTestRunner(compiler);
    Node root = runner.compile(input);
    Set<DiagnosticType> errors = Sets.newHashSet(pass.getErrors().getDiagnostics());

    assertEquals(1, errors.size());
    DiagnosticType error = errors.iterator().next();
    assertEquals(expectedDiagnosticType, error.getType());
    assertEquals(expectedMessage, error.getMessage());
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithUndefinedVariable("x");
    checkErrors(varCheck, input, UNDEFINED_VAR_ERROR, "variable x is undefined");
  }

  @org.junit.Test
  public void testDuplicateVariable() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithDuplicateVariable("x");
    checkErrors(varCheck, input, UNDEFINED_VAR_ERROR, "variable x is undefined");
  }

  @org.junit.Test
  public void testUndefinedVariableWithDuplicateDeclaration() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithUndefinedVariable("x");
    CompilerInput inputWithDuplicateDeclaration =
        new CompilerInput("test", "function foo() { var x = 42; }", input);
    checkErrors(varCheck, inputWithDuplicateDeclaration, UNDEFINED_VAR_ERROR,
        "variable x is undefined");
  }

  @org.junit.Test
  public void testUnresolvedDependency() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithUnresolvedDependencies("module1", "module2");
    checkErrors(varCheck, input, VIOLATED_MODULE_DEP_ERROR,
        "module module1 cannot reference module2, defined in module module2, " +
        "since module module1 loads after module module2");
  }

  @org.junit.Test
  public void testResolvedDependency() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithResolvedDependencies("module1", "module2");
    checkNoErrors(varCheck, input);
  }

  @org.junit.Test
  public void testMissingDependency() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithMissingDependencies("module1", "module2");
    checkErrors(varCheck, input, MISSING_MODULE_DEP_ERROR,
        "missing module dependency; module module1 should depend " +
        "on module module2 because it references y");
  }

  @org.junit.Test
  public void testStrictDependency() {
    VarCheck varCheck = new VarCheck(new TestCompiler(), true);
    CompilerInput input = createInputWithStrictDependencies("module1", "module2");
    checkNoErrors(varCheck, input);
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithNameReferenceInExterns("x");
    checkErrors(varCheck, input, NAME_REFERENCE_IN_EXTERNS_ERROR,
        "accessing name x in externs has no effect");
  }

  @org.junit.Test
  public void testUndefinedExternVariable() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = createInputWithUndefinedExternVariable("x");
    checkErrors(varCheck, input, UNDEFINED_EXTERN_VAR_ERROR,
        "name x is not undefined in the externs.");
  }

  @org.junit.Test
  public void testFunctionDeclarationWithEmptyName() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = new CompilerInput(
        "test",
        "function foo() {}",
        null
    );
    Node node = traverse(new NodeTraversal(new TestCompiler()){
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == FUNCTION && n.getString().isEmpty()) {
          throw new IllegalStateException("Unexpected function declaration " + n);
        }
      }
    }, input.getAstRoot(new TestCompiler()), null).getFirstChild();
    checkErrors(varCheck, input, INVALID_FUNCTION_DECL,
        "Syntax error: function declaration must have a name");
  }

  @org.junit.Test
  public void testNameDeclarationWithEmptyName() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = new CompilerInput(
        "test",
        "var x = {};" +
        "function foo() {}",
        null
    );
    checkNoErrors(varCheck, input);
  }

  @org.junit.Test
  public void testEmptyNameInFunctionDeclaration() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = new CompilerInput(
        "test",
        "function x() {}",
        null
    );
    checkErrors(varCheck, input, INVALID_FUNCTION_DECL,
        "Syntax error: function declaration must have a name");
  }

  @org.junit.Test
  public void testEmptyNameInFunctionExpression() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = new CompilerInput(
        "test",
        "var x = function() {};",
        null
    );
    checkNoErrors(varCheck, input);
  }

  @org.junit.Test
  public void testEmptyNameInVariableDeclaration() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = new CompilerInput(
        "test",
        "var x = 42;" +
        "function foo() {}",
        null
    );
    checkNoErrors(varCheck, input);
  }

  @org.junit.Test
  public void testEmptyNameInVariableExpression() {
    VarCheck varCheck = new VarCheck(new TestCompiler());
    CompilerInput input = new CompilerInput(
        "test",
        "function foo() { var x = 42; }",
        null
    );
    checkNoErrors(varCheck, input);
  }
}