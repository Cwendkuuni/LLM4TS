package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  @Test
  public void testCheckUndefinedVar() {
    VarCheck checker = new VarCheck(null);
    Node root = Node.newText("function foo() { bar(); }");
    checker.process(null, root);
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertNoErrors();
  }

  @Test
  public void testCheckUndefinedVarInFunction() {
    VarCheck checker = new VarCheck(null);
    Node root = Node.newText("function foo() { function bar() {} }");
    checker.process(null, root);
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertNoErrors();
  }

  @Test
  public void testCheckDuplicateVar() {
    VarCheck checker = new VarCheck(null);
    Node root = Node.newText("function foo() { var x = 1; var x = 2; }");
    checker.process(null, root);
    checker.visit(new NodeTraversal(null), Node.newName("x"), Node.newName("foo"));
    checker.visit(new NodeTraversal(null), Node.newName("x"), Node.newName("foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertNoErrors();
  }

  @Test
  public void testCheckUndefinedVarInExterns() {
    VarCheck checker = new VarCheck(null);
    Node externs = Node.newText("var x = 1;");
    Node root = Node.newText("function foo() { var x = 2; }");
    checker.process(externs, root);
    checker.visit(new NodeTraversal(null), Node.newName("x"), Node.newName("foo"));
    checker.assertErrors(1);
    checker.assertErrorMessage("JSC_UNDEFINED_EXTERN_VAR_ERROR", "x is not undefined in the externs.");
  }

  @Test
  public void testCheckUndefinedVarInExternsWithDuplicateDeclaration() {
    VarCheck checker = new VarCheck(null);
    Node externs = Node.newText("var x = 1; var x = 2;");
    Node root = Node.newText("function foo() { var x = 2; }");
    checker.process(externs, root);
    checker.visit(new NodeTraversal(null), Node.newName("x"), Node.newName("foo"));
    checker.assertErrors(1);
    checker.assertErrorMessage("JSC_UNDEFINED_EXTERN_VAR_ERROR", "x is not undefined in the externs.");
  }

  @Test
  public void testCheckModuleDependency() {
    VarCheck checker = new VarCheck(null);
    JSModule module1 = new JSModule("module1", Sets.newHashSet("foo"));
    JSModule module2 = new JSModule("module2", Sets.newHashSet("bar"));
    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(module1);
    moduleGraph.addModule(module2);
    moduleGraph.addDependency(module1, module2);

    Node externs1 = Node.newText("var foo = 1;");
    Node root1 = Node.newText("function module1Foo() { var bar = 2; }");
    Node externs2 = Node.newText("var bar = 1;");
    Node root2 = Node.newText("function module2Foo() { var bar = 2; }");

    checker.process(externs1, root1);
    checker.process(externs2, root2);
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module1Foo"));
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module2Foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertNoErrors();
  }

  @Test
  public void testCheckModuleDependencyViolation() {
    VarCheck checker = new VarCheck(null);
    JSModule module1 = new JSModule("module1", Sets.newHashSet("foo"));
    JSModule module2 = new JSModule("module2", Sets.newHashSet("bar"));
    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(module1);
    moduleGraph.addModule(module2);
    moduleGraph.addDependency(module2, module1);

    Node externs1 = Node.newText("var foo = 1;");
    Node root1 = Node.newText("function module1Foo() { var bar = 2; }");
    Node externs2 = Node.newText("var bar = 1;");
    Node root2 = Node.newText("function module2Foo() { var bar = 2; }");

    checker.process(externs1, root1);
    checker.process(externs2, root2);
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module1Foo"));
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module2Foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertErrors(1);
    checker.assertErrorMessage("JSC_VIOLATED_MODULE_DEPENDENCY",
        "module module1 cannot reference bar, defined in module module2, since module module2 loads after module module1");
  }

  @Test
  public void testCheckModuleDependencyViolationWithStrictChecking() {
    VarCheck checker = new VarCheck(null, true);
    JSModule module1 = new JSModule("module1", Sets.newHashSet("foo"));
    JSModule module2 = new JSModule("module2", Sets.newHashSet("bar"));
    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(module1);
    moduleGraph.addModule(module2);
    moduleGraph.addDependency(module2, module1);

    Node externs1 = Node.newText("var foo = 1;");
    Node root1 = Node.newText("function module1Foo() { var bar = 2; }");
    Node externs2 = Node.newText("var bar = 1;");
    Node root2 = Node.newText("function module2Foo() { var bar = 2; }");

    checker.process(externs1, root1);
    checker.process(externs2, root2);
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module1Foo"));
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module2Foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertErrors(1);
    checker.assertErrorMessage("JSC_STRICT_MODULE_DEPENDENCY_ERROR",
        "module module1 cannot reference bar, defined in module module2");
  }

  @Test
  public void testCheckModuleDependencyMissing() {
    VarCheck checker = new VarCheck(null);
    JSModule module1 = new JSModule("module1", Sets.newHashSet("foo"));
    JSModule module2 = new JSModule("module2", Sets.newHashSet("bar"));
    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(module1);
    moduleGraph.addModule(module2);

    Node externs1 = Node.newText("var foo = 1;");
    Node root1 = Node.newText("function module1Foo() { var bar = 2; }");
    Node externs2 = Node.newText("var bar = 1;");
    Node root2 = Node.newText("function module2Foo() { var bar = 2; }");

    checker.process(externs1, root1);
    checker.process(externs2, root2);
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module1Foo"));
    checker.visit(new NodeTraversal(null), Node.newName("bar"), Node.newName("module2Foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertErrors(1);
    checker.assertErrorMessage("JSC_MISSING_MODULE_DEPENDENCY",
        "missing module dependency; module module1 should depend on module module2 because it references bar");
  }

  @Test
  public void testCheckFunctionDeclaration() {
    VarCheck checker = new VarCheck(null);
    Node root = Node.newText("function foo() {}");
    checker.process(null, root);
    checker.visit(new NodeTraversal(null), Node.newName("foo"), null);
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertNoErrors();
  }

  @Test
  public void testCheckFunctionDeclarationEmptyName() {
    VarCheck checker = new VarCheck(null);
    Node root = Node.newText("function () {}");
    checker.process(null, root);
    checker.visit(new NodeTraversal(null), Node.newName(""), null);
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertErrors(1);
    checker.assertErrorMessage("JSC_INVALID_FUNCTION_DECL",
        "Syntax error: function declaration must have a name");
  }

  @Test
  public void testCheckFunctionDeclarationEmptyNameInExterns() {
    VarCheck checker = new VarCheck(null);
    Node externs = Node.newText("function () {}");
    Node root = Node.newText("function foo() {}");
    checker.process(externs, root);
    checker.visit(new NodeTraversal(null), Node.newName(""), Node.newName("foo"));
    assertNotNull(checker.synthesizedExternsRoot);
    checker.assertNoErrors();
  }
}