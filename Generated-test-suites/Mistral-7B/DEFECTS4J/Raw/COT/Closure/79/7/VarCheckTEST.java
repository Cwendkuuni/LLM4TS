import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTest.assertNotify;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testUndefinedVarError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.newSourceFile("Test.js", "var x;", null);
    VarCheck varCheck = new VarCheck(compiler);
    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    Node varNode = root.getFirstChild();
    assertEquals("x", varNode.getString());
    assertEquals(NAME, varNode.getType());

    assertNotify(compiler, 1, UNDEFINED_VAR_ERROR, varNode, "x");
  }

  @Test
  public void testDuplicateVarDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.newSourceFile("Test.js", "var x; var x;", null);
    VarCheck varCheck = new VarCheck(compiler);
    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    Node firstVarNode = root.getFirstChild();
    Node secondVarNode = firstVarNode.getNextSibling();
    assertEquals("x", firstVarNode.getString());
    assertEquals(NAME, firstVarNode.getType());
    assertEquals("x", secondVarNode.getString());
    assertEquals(NAME, secondVarNode.getType());

    // The first declaration should not be reported as an error.
    assertFalse(firstVarNode.hasSuppression("duplicate"));

    // The second declaration should be reported as a duplicate and suppressed.
    assertTrue(secondVarNode.hasSuppression("duplicate"));
  }

  @Test
  public void testUndefinedVarSanityCheck() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new VarCheck(compiler, true);
    CompilerInput input = compiler.newSourceFile("Test.js", "var x;", null);
    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getExterns(), root), pass);

    Node varNode = root.getFirstChild();
    assertEquals("x", varNode.getString());
    assertEquals(NAME, varNode.getType());

    // A sanity check should not report an error for an undefined variable.
    assertFalse(varNode.hasError());
  }

  @Test
  public void testModuleDependencyError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput moduleAInput = compiler.newSourceFile("ModuleA.js", "", null);
    CompilerInput moduleBInput = compiler.newSourceFile("ModuleB.js", "var x = require('ModuleA');", null);
    VarCheck varCheck = new VarCheck(compiler);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(moduleAInput.getExterns(), moduleBInput.getExterns(), moduleAInput.getAstRoot(compiler), moduleBInput.getAstRoot(compiler)), varCheck);

    Node moduleBVarNode = moduleBInput.getAstRoot(compiler).getFirstChild().getFirstChild();
    assertEquals("x", moduleBVarNode.getString());
    assertEquals(GETPROP, moduleBVarNode.getType());

    Scope moduleBScope = varCheck.getScope();
    Scope moduleAScope = ((Node) moduleBVarNode.getFirstChild()).getParentScope();
    assertEquals(moduleAScope, moduleBScope);

    assertNotify(compiler, 1, VIOLATED_MODULE_DEP_ERROR, moduleBVarNode, "ModuleB.js", "ModuleA.js", "x");
  }

  @Test
  public void testMissingModuleDependencyWarning() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput moduleAInput = compiler.newSourceFile("ModuleA.js", "exports.x = 1;", null);
    CompilerInput moduleBInput = compiler.newSourceFile("ModuleB.js", "var x = require('ModuleA').x;", null);
    VarCheck varCheck = new VarCheck(compiler);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(moduleAInput.getExterns(), moduleBInput.getExterns(), moduleAInput.getAstRoot(compiler), moduleBInput.getAstRoot(compiler)), varCheck);

    Node moduleBVarNode = moduleBInput.getAstRoot(compiler).getFirstChild().getFirstChild();
    assertEquals("x", moduleBVarNode.getString());
    assertEquals(GETPROP, moduleBVarNode.getType());

    Scope moduleBScope = varCheck.getScope();
    Scope moduleAScope = ((Node) moduleBVarNode.getFirstChild()).getParentScope();
    assertEquals(moduleAScope, moduleBScope);

    // Since we don't have a dependency graph, we can't check that ModuleB
    // should load before ModuleA. Instead, we just check that a warning is
    // emitted.
    assertNotify(compiler, 1, MISSING_MODULE_DEP_ERROR, moduleBVarNode, "ModuleB.js", "ModuleA.js", "x");
  }

  @Test
  public void testStrictModuleDependencyError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput moduleAInput = compiler.newSourceFile("ModuleA.js", "exports.x = 1;", null);
    CompilerInput moduleBInput = compiler.newSourceFile("ModuleB.js", "var x = require('ModuleA').x;", null);
    VarCheck varCheck = new VarCheck(compiler, false, true);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(moduleAInput.getExterns(), moduleBInput.getExterns(), moduleAInput.getAstRoot(compiler), moduleBInput.getAstRoot(compiler)), varCheck);

    Node moduleBVarNode = moduleBInput.getAstRoot(compiler).getFirstChild().getFirstChild();
    assertEquals("x", moduleBVarNode.getString());
    assertEquals(GETPROP, moduleBVarNode.getType());

    Scope moduleBScope = varCheck.getScope();
    Scope moduleAScope = ((Node) moduleBVarNode.getFirstChild()).getParentScope();
    assertEquals(moduleAScope, moduleBScope);

    // Since ModuleB loads after ModuleA, this should not be an error.
    assertFalse(varCheck.strictExternCheck);
    assertFalse(moduleBVarNode.hasError());
  }

  @Test
  public void testEmptyFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.newSourceFile("Test.js", "function() {}", null);
    VarCheck varCheck = new VarCheck(compiler);
    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    Node functionNode = root.getFirstChild();
    assertEquals(FUNCTION_DECL, functionNode.getType());
    assertEquals(0, functionNode.getChildCount());

    // An empty function declaration is supposed to be a syntax error.
    assertEquals(1, functionNode.getErrorCount());
    assertEquals(INVALID_FUNCTION_DECL, functionNode.getErrors().get(0).getType());
  }

  @Test
  public void testFunctionDeclarationWithName() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.newSourceFile("Test.js", "function foo() {}", null);
    VarCheck varCheck = new VarCheck(compiler);
    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    Node functionNode = root.getFirstChild();
    assertEquals(FUNCTION_DECL, functionNode.getType());
    assertEquals("foo", functionNode.getFirstChild().getString());
    assertEquals(0, functionNode.getChildCount() - 1);

    // A function declaration with a name should not be reported as an error.
    assertFalse(functionNode.hasError());
  }

  @Test
  public void testVariableIsFilePrivate() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = compiler.newSourceFile("TestFile1.js", "var x = 1;", null);
    VarCheck varCheck = new VarCheck(compiler);
    Node root = input.getAstRoot(compiler);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input.getExterns(), root), varCheck);

    Node varNode = root.getFirstChild();
    assertEquals("x", varNode.getString());
    assertEquals(NAME, varNode.getType());

    CompilerInput input2 = compiler.newSourceFile("TestFile2.js", "var x = 2;", null);
    Node root2 = input2.getAstRoot(compiler);

    assertNotNull(varCheck.getScope());
    Scope scope = varCheck.getScope();
    Scope globalScope = scope.getGlobalScope();

    // The variable is file-private, so it should only be accessible in the
    // defining file.
    assertFalse(globalScope.getVar("x") != null);
    assertFalse(root2.getFirstChild().hasError());
  }

  @Test
  public void testVariableIsAccessibleAcrossModuleBoundariesWithProperDependency() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput moduleAInput = compiler.newSourceFile("ModuleA.js", "exports.x = 1;", null);
    CompilerInput moduleBInput = compiler.newSourceFile("ModuleB.js", "var x = require('ModuleA').x;", null);
    VarCheck varCheck = new VarCheck(compiler);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(moduleAInput.getExterns(), moduleBInput.getExterns(), moduleAInput.getAstRoot(compiler), moduleBInput.getAstRoot(compiler)), varCheck);

    Node moduleBVarNode = moduleBInput.getAstRoot(compiler).getFirstChild().getFirstChild();
    assertEquals("x", moduleBVarNode.getString());
    assertEquals(GETPROP, moduleBVarNode.getType());

    Scope moduleBScope = varCheck.getScope();
    Scope moduleAScope = ((Node) moduleBVarNode.getFirstChild()).getParentScope();
    assertEquals(moduleAScope, moduleBScope);

    // According to the module system, ModuleB should be able to access x, since
    // it depends on ModuleA.
    assertFalse(moduleBVarNode.hasError());
  }

  @Test
  public void testVariableIsNotAccessibleAcrossModuleBoundariesWithoutProperDependency() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput moduleAInput = compiler.newSourceFile("ModuleA.js", "exports.x = 1;", null);
    CompilerInput moduleBInput = compiler.newSourceFile("ModuleB.js", "var x = 1;", null);
    VarCheck varCheck = new VarCheck(compiler);

    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(moduleAInput.getExterns(), moduleBInput.getExterns(), moduleAInput.getAstRoot(compiler), moduleBInput.getAstRoot(compiler)), varCheck);

    Node moduleBVarNode = moduleBInput.getAstRoot(compiler).getFirstChild();
    assertEquals("x", moduleBVarNode.getString());
    assertEquals(NAME, moduleBVarNode.getType());

    Scope moduleBScope = varCheck.getScope();
    Scope moduleAScope = ((Node) moduleBVarNode.getFirstChild()).getParentScope();
    assertEquals(moduleAScope, moduleBScope);

    // According to the module system, ModuleB should not be able to access x,
    // since it does not have a dependency on ModuleA.
    assertTrue(moduleBVarNode.hasError());
    assertEquals(1, moduleBVarNode.getErrorCount());
    assertEquals(VIOLATED_MODULE_DEP_ERROR, moduleBVarNode.getErrors().get(0).getType());
  }
}