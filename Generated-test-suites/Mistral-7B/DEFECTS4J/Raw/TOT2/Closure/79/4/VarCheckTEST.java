// Test cases for VarCheck class
// Expert 1, Expert 2, and Expert 3 test files combined

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.VarCheck;

public class VarCheckTest {

  @Test
  public void testValidVariable() {
    // Given a valid Java class and input
    CompilerInput input = new CompilerInput("");
    Node root = Node.newBlock(Token.BLOCK);

    // When the VarCheck pass is executed
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal.traverseRoots(
        null, Lists.newArrayList(input.getAstRoot(null), root), varCheck);

    // Then no errors or warnings are reported
    assertEquals(0, varCheck.getNumDiagnostics());
  }

  @Test
  public void testUndefinedVariable() {
    // Given a Java class with an undefined variable
    CompilerInput input = new CompilerInput(
        "var x;",
        "function foo() { x++; }");
    Node root = Node.newBlock(Token.BLOCK);

    // When the VarCheck pass is executed
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal.traverseRoots(
        null, Lists.newArrayList(input.getAstRoot(null), root), varCheck);

    // Then a diagnostic is reported
    assertEquals(1, varCheck.getNumDiagnostics());
    DiagnosticType diagnostic = varCheck.getDiagnostics().get(0).getType();
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostic.getName());
  }

  @Test
  public void testStrictModuleDependency() {
    // Given two Java modules with a strict module dependency
    CompilerInput moduleAInput = new CompilerInput(
        "module A { export function foo() { var x; } }",
        "module B { function bar() { A.foo(); } }");
    Node moduleARoot = Node.newBlock(Token.BLOCK);
    Node moduleBRoot = Node.newBlock(Token.BLOCK);

    // When the VarCheck pass is executed in strict mode
    VarCheck varCheck = new VarCheck(null, true);
    NodeTraversal.traverseRoots(
        null, Lists.newArrayList(new CompilerInput("", moduleARoot),
            new CompilerInput("", moduleBRoot)), varCheck);

    // Then no diagnostic is reported
    assertEquals(0, varCheck.getNumDiagnostics());
  }

  @Test
  public void testMissingModuleDependency() {
    // Given a Java class with a missing module dependency
    CompilerInput input = new CompilerInput(
        "module A { export function foo() { var x; } }",
        "module B { function bar() { A.baz(); } }");
    Node root = Node.newBlock(Token.BLOCK);

    // When the VarCheck pass is executed
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal.traverseRoots(
        null, Lists.newArrayList(input.getAstRoot(null), root), varCheck);

    // Then a warning is reported
    assertEquals(1, varCheck.getNumDiagnostics());
    DiagnosticType diagnostic = varCheck.getDiagnostics().get(0).getType();
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", diagnostic.getName());
  }

  @Test
  public void testViolatedModuleDependency() {
    // Given two Java modules with a violated module dependency
    CompilerInput moduleAInput = new CompilerInput(
        "module A { export function foo() { var x; } }",
        "module B { function bar() { A.foo(); } }");
    Node moduleARoot = Node.newBlock(Token.BLOCK);
    Node moduleBRoot = Node.newBlock(Token.BLOCK);

    // When the VarCheck pass is executed
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal.traverseRoots(
        null, Lists.newArrayList(new CompilerInput("", moduleARoot),
            new CompilerInput("", moduleBRoot)), varCheck);

    // Then a diagnostic is reported
    assertEquals(1, varCheck.getNumDiagnostics());
    DiagnosticType diagnostic = varCheck.getDiagnostics().get(0).getType();
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", diagnostic.getName());
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    // Given a Java class with an invalid function declaration
    CompilerInput input = new CompilerInput(
        "function foo() {");
    Node root = Node.newBlock(Token.BLOCK);

    // When the VarCheck pass is executed
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal.traverseRoots(
        null, Lists.newArrayList(input.getAstRoot(null), root), varCheck);

    // Then a diagnostic is reported
    assertEquals(1, varCheck.getNumDiagnostics());
    DiagnosticType diagnostic = varCheck.getDiagnostics().get(0).getType();
    assertEquals("JSC_INVALID_FUNCTION_DECL", diagnostic.getName());
  }
}