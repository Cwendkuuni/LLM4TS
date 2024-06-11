package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private static AbstractCompiler compiler;
  private static CompilerInput input;
  private static CompilerPass pass;
  private static Node root;

  @org.junit.BeforeClass
  public static void setUp() {
    compiler = new TestCompiler();
    compiler.init();
    input = new CompilerInput(
        "TestSource.js",
        "sourceCodeHere",
        1,
        null,
        null,
        null);
    root = NodeFactory.createRoot();
    pass = new VarCheck(compiler);
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    input.setSourceFileName("TestSource.js");
    input.setSourceCode("var x; x = 5;");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Scope.Var var = scope.getVar("x");
    assertThat(var).isNull();
    Node node = traversal.getInput().getInputElementAt(0);
    assertThat(node).isNotNull();
    assertThat(node.getType()).isEqualTo(Token.ASSIGN);
    assertThat(node.getFirstChild()).isInstanceOf(Node.class);
    Node variableNode = node.getFirstChild();
    assertThat(variableNode.getType()).isEqualTo(Token.NAME);
    assertThat(variableNode.getString()).isEqualTo("x");

    Node error = traversal.getErrors().get(0);
    assertThat(error.getFirstChild().getType()).isEqualTo(Token.ERROR);
    assertThat(error.getFirstChild().getString()).isEqualTo("JSC_UNDEFINED_VARIABLE");
    assertThat(error.getFirstChild().getFirstChild().getString()).isEqualTo("x");
  }

  @org.junit.Test
  public void testUndefinedVariableInFunction() {
    input.setSourceFileName("TestSource.js");
    input.setSourceCode("function foo() { var x; x = 5; }");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Scope.Func func = scope.getFunc("foo");
    Scope funcScope = func.getScope();
    Scope.Var var = funcScope.getVar("x");
    assertThat(var).isNull();
    Node node = traversal.getInput().getInputElementAt(0);
    assertThat(node).isNotNull();
    assertThat(node.getType()).isEqualTo(Token.FUNCTION);
    Node body = node.getFirstChild();
    Node assignment = body.getLastChild();
    Node variableNode = assignment.getFirstChild();
    assertThat(variableNode.getType()).isEqualTo(Token.NAME);
    assertThat(variableNode.getString()).isEqualTo("x");

    Node error = traversal.getErrors().get(0);
    assertThat(error.getFirstChild().getType()).isEqualTo(Token.ERROR);
    assertThat(error.getFirstChild().getString()).isEqualTo("JSC_UNDEFINED_VARIABLE");
    assertThat(error.getFirstChild().getFirstChild().getString()).isEqualTo("x");
  }

  @org.junit.Test
  public void testUndefinedVariableInGlobalScope() {
    input.setSourceFileName("TestSource.js");
    input.setSourceCode("var x = 5;");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Scope.Var var = scope.getVar("x");
    assertThat(var).isNotNull();
    Node node = traversal.getInput().getInputElementAt(0);
    assertThat(node).isNotNull();
    assertThat(node.getType()).isEqualTo(Token.ASSIGN);
    Node variableNode = node.getFirstChild();
    assertThat(variableNode.getType()).isEqualTo(Token.NAME);
    assertThat(variableNode.getString()).isEqualTo("x");

    // No error should be reported since the variable is defined in the global scope
    assertThat(traversal.getErrors()).isEmpty();
  }

  @org.junit.Test
  public void testDuplicateVariableDeclaration() {
    input.setSourceFileName("TestSource.js");
    input.setSourceCode("var x; x = 5; var x;");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Scope.Var var = scope.getVar("x");
    Node error = traversal.getErrors().get(0);
    Node variableNode = traversal.getInput().getInputElementAt(0);

    // The first declaration should be valid
    assertThat(var).isNotNull();
    assertThat(variableNode.getType()).isEqualTo(Token.ASSIGN);
    assertThat(variableNode.getFirstChild()).isInstanceOf(Node.class);
    Node firstVariableNode = variableNode.getFirstChild();
    assertThat(firstVariableNode.getType()).isEqualTo(Token.NAME);
    assertThat(firstVariableNode.getString()).isEqualTo("x");

    // The second declaration should be ignored as a duplicate
    assertThat(var).isEqualTo(scope.getVar("x"));
    assertThat(error.getFirstChild().getType()).isEqualTo(Token.COMMENT);
    assertThat(error.getFirstChild().getString()).startsWith("duplicate");
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    input.setSourceFileName("DependentModule.js");
    input.setSourceCode("require('DependedModule'); var x = 5;");

    CompilerInput depInput = new CompilerInput("DependedModule.js",
        "// Empty source code for testing", 1, null, null, null);
    depInput.setModule(new JSModule("DependedModule", null, null, null));

    input.addDependency("DependedModule.js");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Node error = traversal.getErrors().get(0);

    assertThat(error.getFirstChild().getType()).isEqualTo(Token.ERROR);
    assertThat(error.getFirstChild().getString()).isEqualTo("JSC_MISSING_MODULE_DEPENDENCY");
    assertThat(error.getFirstChild().getFirstChild().getString()).isEqualTo("DependedModule");
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    input.setSourceFileName("ModuleA.js");
    input.setSourceCode("require('ModuleB'); var x = 5;");

    CompilerInput moduleBInput = new CompilerInput("ModuleB.js",
        "// Empty source code for testing", 1, null, null, null);
    moduleBInput.setModule(new JSModule("ModuleB", null, null, null));

    input.addDependency("ModuleB.js");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Node error = traversal.getErrors().get(0);

    assertThat(error.getFirstChild().getType()).isEqualTo(Token.DISABLED);
    assertThat(error.getFirstChild().getString()).isEqualTo("JSC_STRICT_MODULE_DEPENDENCY");
    assertThat(error.getFirstChild().getFirstChild().getType()).isEqualTo(Token.ERROR);
    assertThat(error.getFirstChild().getFirstChild().getString()).isEqualTo("JSC_VIOLATED_MODULE_DEPENDENCY");
  }

  @org.junit.Test
  public void testVariableAccessInAnotherFile() {
    input.setSourceFileName("FileA.js");
    input.setSourceCode("require('FileB'); var x = FileB.myVar;");

    CompilerInput fileBInput = new CompilerInput("FileB.js",
        "var myVar = 5;", 1, null, null, null);
    fileBInput.setModule(new JSModule("FileB", null, null, null));

    input.addDependency("FileB.js");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Node error = traversal.getErrors().get(0);

    assertThat(error.getFirstChild().getType()).isEqualTo(Token.ERROR);
    assertThat(error.getFirstChild().getString()).isEqualTo("JSC_VIOLATED_MODULE_DEPENDENCY");
  }

  @org.junit.Test
  public void testVariableAccessInSameFile() {
    input.setSourceFileName("FileA.js");
    input.setSourceCode("function foo() { var x = 5; } var x = 10;");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    pass.process(input, root);

    Scope scope = traversal.getScope();
    Scope.Var xVarInFunction = scope.getFunc("foo").getScope().getVar("x");
    Scope.Var xVarInGlobal = scope.getVar("x");

    assertThat(xVarInFunction).isNotNull();
    assertThat(xVarInFunction.getValue()).isInstanceOf(Number.class);
    assertThat(xVarInFunction.getValue().doubleValue()).isEqualTo(5.0);

    assertThat(xVarInGlobal).isNotNull();
    assertThat(xVarInGlobal.getValue()).isInstanceOf(Number.class);
    assertThat(xVarInGlobal.getValue().doubleValue()).isEqualTo(10.0);

    // No error should be reported since both variables are in the same file
    assertThat(traversal.getErrors()).isEmpty();
  }
}