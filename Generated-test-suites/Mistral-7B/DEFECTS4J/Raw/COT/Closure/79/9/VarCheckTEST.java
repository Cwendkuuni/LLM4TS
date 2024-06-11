import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new TestCompiler();
  }

  @org.junit.Test
  public void testProcessEmpty() {
    VarCheck varCheck = new VarCheck(compiler);
    Node root = Node.nullLiteral();
    Node externs = Node.nullLiteral();
    varCheck.process(externs, root);
  }

  @org.junit.Test
  public void testVisitUndefinedVariable() {
    VarCheck varCheck = new VarCheck(compiler);
    Node root = Node.newObjectLiteral();
    Node n = Node.newName("undefinedVar");
    root.addChildToBack(n);
    Node parent = Node.newName("parent");
    root.addChildToBack(parent);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    Scope scope = traverser.getScope();
    assertNull(scope.getVar("undefinedVar"));
  }

  @org.junit.Test
  public void testVisitDuplicateVariable() {
    VarCheck varCheck = new VarCheck(compiler);
    Node root = Node.newObjectLiteral();
    Node parent = Node.newName("parent");
    root.addChildToBack(parent);
    Node n1 = Node.newName("varName");
    parent.addChildToBack(n1);
    Node n2 = Node.newName("varName");
    parent.addChildToBack(n2);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    Scope scope = traverser.getScope();
    assertSame(scope.getVar("varName"), scope.getVar("varName"));
  }

  @org.junit.Test
  public void testVisitFunctionDeclarationWithEmptyName() {
    VarCheck varCheck = new VarCheck(compiler);
    Node root = Node.newFunctionDeclaration(
        Node.newName(""), Node.newName("foo"), Lists.newArrayList(),
        Node.newBlock(Node.newName("return 42")));
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    assertFalse(traverser.hasDiagnostics());
  }

  @org.junit.Test
  public void testVisitUndefinedVariableInGlobalScope() {
    VarCheck varCheck = new VarCheck(compiler);
    Node root = Node.newObjectLiteral();
    Node n = Node.newName("undefinedVar");
    root.addChildToBack(n);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    DiagnosticType diagnosticType = traverser.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertTrue(diagnosticType.getAggregateName().equals("JSC_UNDEFINED_VARIABLE"));
  }

  @org.junit.Test
  public void testVisitUndefinedVariableInFunctionScope() {
    VarCheck varCheck = new VarCheck(compiler);
    Node root = Node.newFunctionDeclaration(
        Node.newName("functionName"), Node.newList(),
        Node.newBlock(Node.newName("return 42")), null);
    Node parent = root.getFirstChild();
    Node n = Node.newName("undefinedVar");
    parent.addChildToBack(n);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    DiagnosticType diagnosticType = traverser.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertTrue(diagnosticType.getAggregateName().equals("JSC_UNDEFINED_VARIABLE"));
  }

  @org.junit.Test
  public void testVisitVariableWithDependentModule() {
    VarCheck varCheck = new VarCheck(compiler);
    CompilerInput input = new CompilerInput(
        "input.js",
        Node.newText("var x = require('module1').foo;"),
        Node.nullLiteral(),
        null);
    Node root = input.getAstRoot(compiler);
    JSModule module = new JSModule("input.js", null, null, null);
    input.setModule(module);
    JSModule module1 = new JSModule("module1.js", null, null, null);
    moduleGraph.addDependency(module, module1);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    DiagnosticType diagnosticType = traverser.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isWarning());
    assertTrue(diagnosticType.getAggregateName().equals("JSC_MISSING_MODULE_DEPENDENCY"));
  }

  @org.junit.Test
  public void testVisitVariableWithStrictModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler, true);
    CompilerInput input = new CompilerInput(
        "input.js",
        Node.newText("var x = require('module1').foo;"),
        Node.nullLiteral(),
        null);
    Node root = input.getAstRoot(compiler);
    JSModule module = new JSModule("input.js", null, null, null);
    input.setModule(module);
    JSModule module1 = new JSModule("module1.js", null, null, null);
    moduleGraph.addDependency(module1, module);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    DiagnosticType diagnosticType = traverser.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isDisabled());
  }

  @org.junit.Test
  public void testVisitVariableWithSameModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler);
    CompilerInput input = new CompilerInput(
        "input.js",
        Node.newText("var x = require('input.js').foo;"),
        Node.nullLiteral(),
        null);
    Node root = input.getAstRoot(compiler);
    JSModule module = new JSModule("input.js", null, null, null);
    input.setModule(module);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    assertFalse(traverser.hasDiagnostics());
  }

  @org.junit.Test
  public void testVisitVariableWithModuleDependencyCycle() {
    VarCheck varCheck = new VarCheck(compiler);
    CompilerInput input1 = new CompilerInput(
        "module1.js",
        Node.newText("var x = require('module2').foo;"),
        Node.nullLiteral(),
        null);
    Node root1 = input1.getAstRoot(compiler);
    JSModule module1 = new JSModule("module1.js", null, null, null);
    input1.setModule(module1);
    CompilerInput input2 = new CompilerInput(
        "module2.js",
        Node.newText("var x = require('module1').foo;"),
        Node.nullLiteral(),
        null);
    Node root2 = input2.getAstRoot(compiler);
    JSModule module2 = new JSModule("module2.js", null, null, null);
    input2.setModule(module2);
    moduleGraph.addDependency(module1, module2);
    moduleGraph.addDependency(module2, module1);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root1);
    traverser.traverse(varCheck);
    DiagnosticType diagnosticType = traverser.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertTrue(diagnosticType.getAggregateName().equals("JSC_VIOLATED_MODULE_DEPENDENCY"));
  }

  @org.junit.Test
  public void testVisitVariableWithMissingModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler);
    CompilerInput input = new CompilerInput(
        "input.js",
        Node.newText("var x = require('missingModule').foo;"),
        Node.nullLiteral(),
        null);
    Node root = input.getAstRoot(compiler);
    ParentChildTraverser traverser = new ParentChildTraverser(compiler, root);
    traverser.traverse(varCheck);
    DiagnosticType diagnosticType = traverser.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isWarning());
    assertTrue(diagnosticType.getAggregateName().equals("JSC_MISSING_MODULE_DEPENDENCY"));
  }
}