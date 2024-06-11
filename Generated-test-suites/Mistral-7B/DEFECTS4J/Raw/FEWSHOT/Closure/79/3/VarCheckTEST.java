package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SemanticCheck;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.NodeUtil;

import java.util.Set;

/**
 * Test cases for {@link VarCheck}.
 */
public class VarCheckTest {
  @Test
  public void testVarCheck() {
    // Set up test environment
    CompilerPass passthrough = new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        // No-op
      }
    };
    Checker checker = new Checker();
    checker.addPass(passthrough);
    checker.addPass(new SemanticCheck());
    checker.addPass(new VarCheck(checker));

    // Create input code
    CompilerInput input = new CompilerInput(
        "test.js",
        "function a() {}",
        "function b() {\n" +
        "  var x;\n" +
        "  x = a();\n" +
        "}"
    );
    Node root = input.getAstRoot(checker);

    // Run test
    checker.check(input, DiagnosticType.warning, null);

    // Check results
    Set<String> declaredVars = Sets.newHashSet("a");
    for (Scope scope = NodeUtil.firstParentOfType(root, Scope.class);
         scope != null;
         scope = scope.getParent()) {
      for (Scope.Var var : scope.getVars()) {
        declaredVars.add(var.name);
      }
    }
    assertEquals(declaredVars, Sets.newHashSet("a"));

    NodeTraversal traversal = NodeTraversal.traverse(checker, root, new TestVisitor());

    assertFalse(traversal.hasError());
    assertNull(traversal.getErrorMessage());

    // Test variable references between modules
    input = new CompilerInput(
        "module1.js",
        "function foo() {}",
        ""
    );
    Node module1Root = input.getAstRoot(checker);

    input = new CompilerInput(
        "module2.js",
        "import foo from 'module1.js';\n" +
        "function bar() {\n" +
        "  foo();\n" +
        "}"
    );
    Node module2Root = input.getAstRoot(checker);

    NodeTraversal traverse1 = NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(module1Root, module2Root), new TestVisitor());

    assertFalse(traversal.hasError());
    assertNull(traversal.getErrorMessage());

    NodeTraversal traverse2 = NodeTraversal.traverse(
        checker, module2Root, new TestVisitor());

    assertFalse(traversal.hasError());
    assertNull(traversal.getErrorMessage());

    // Test undefined variable
    input = new CompilerInput(
        "test.js",
        "function a() {}\n" +
        "function b() {\n" +
        "  var x;\n" +
        "  x = a().y;\n" +
        "}"
    );
    root = input.getAstRoot(checker);

    checker.check(input, DiagnosticType.warning, null);

    traversal = NodeTraversal.traverse(checker, root, new TestVisitor());

    assertTrue(traversal.hasError());
    assertEquals(1, traversal.getErrors().size());
    assertEquals("Variable 'y' is undefined", traversal.getErrors().get(0).getMessage());

    // Test undeclared variable in function declaration
    input = new CompilerInput(
        "test.js",
        "function a(x) {}\n" +
        "function b() {\n" +
        "  var y = x;\n" +
        "}"
    );
    root = input.getAstRoot(checker);

    checker.check(input, DiagnosticType.warning, null);

    traversal = NodeTraversal.traverse(checker, root, new TestVisitor());

    assertTrue(traversal.hasError());
    assertEquals(1, traversal.getErrors().size());
    assertEquals("Function declaration must have a name", traversal.getErrors().get(0).getMessage());

    // Test duplicate variable declaration
    input = new CompilerInput(
        "test.js",
        "function a() {}\n" +
        "function b() {\n" +
        "  var x;\n" +
        "  var x;\n" +
        "}"
    );
    root = input.getAstRoot(checker);

    checker.check(input, DiagnosticType.warning, null);

    traversal = NodeTraversal.traverse(checker, root, new TestVisitor());

    assertTrue(traversal.hasError());
    assertEquals(1, traversal.getErrors().size());
    assertEquals("Duplicate variable declaration", traversal.getErrors().get(0).getMessage());

    // Test strict module dependency check
    input = new CompilerInput(
        "module1.js",
        "function foo() {}",
        ""
    );
    Node module1RootStrict = input.getAstRoot(checker);

    input = new CompilerInput(
        "module2.js",
        "import foo from 'module1.js';\n" +
        "function bar() {\n" +
        "  foo();\n" +
        "}",
        new CompilerOptions().setStrictMode(true)
    );
    Node module2RootStrict = input.getAstRoot(checker);

    NodeTraversal traverseStrict1 = NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(module1RootStrict, module2RootStrict), new TestVisitor());

    assertFalse(traversal.hasError());
    assertNull(traversal.getErrorMessage());

    NodeTraversal traverseStrict2 = NodeTraversal.traverse(
        checker, module2RootStrict, new TestVisitor());

    assertTrue(traversal.hasError());
    assertEquals(1, traversal.getErrors().size());
    assertEquals("module1.js cannot reference foo, defined in module2.js", traversal.getErrors().get(0).getMessage());

    // Test missing module dependency check
    input = new CompilerInput(
        "module1.js",
        "function foo() {}",
        ""
    );
    Node module1RootMissing = input.getAstRoot(checker);

    input = new CompilerInput(
        "module2.js",
        "import foo from 'module1.js';\n" +
        "function bar() {\n" +
        "  foo();\n" +
        "}"
    );
    Node module2RootMissing = input.getAstRoot(checker);

    NodeTraversal traverseMissing1 = NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(module1RootMissing, module2RootMissing), new TestVisitor());

    assertFalse(traversal.hasError());
    assertNull(traversal.getErrorMessage());

    NodeTraversal traverseMissing2 = NodeTraversal.traverse(
        checker, module2RootMissing, new TestVisitor());

    assertFalse(traversal.hasError());
    assertNull(traversal.getErrorMessage());

    // Test name reference in externs check
    input = new CompilerInput(
        "test.js",
        "/** @extern */\n" +
        "function foo() {}",
        "function bar() {\n" +
        "  foo();\n" +
        "}"
    );
    root = input.getAstRoot(checker);

    checker.check(input, DiagnosticType.warning, null);

    traversal = NodeTraversal.traverse(checker, root, new TestVisitor());

    assertTrue(traversal.hasError());
    assertEquals(1, traversal.getErrors().size());
    assertEquals("accessing name foo in externs has no effect", traversal.getErrors().get(0).getMessage());

    // Test undefined extern variable
    input = new CompilerInput(
        "test.js",
        "/** @extern */\n" +
        "function foo() {}",
        "function bar() {\n" +
        "  foo();\n" +
        "}",
        new CompilerOptions().setUndeclaredVars(Checker.ErrorLevel.ERROR)
    );
    root = input.getAstRoot(checker);

    checker.check(input, DiagnosticType.warning, null);

    traversal = NodeTraversal.traverse(checker, root, new TestVisitor());

    assertTrue(traversal.hasError());
    assertEquals(1, traversal.getErrors().size());
    assertEquals("name foo is not undefined in the externs.", traversal.getErrors().get(0).getMessage());
  }

  /**
   * Visitor to test VarCheck.
   */
  private static class TestVisitor extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.NAME) {
        if (parent.getType() == Token.VAR || parent.getType() == Token.FUNCTION) {
          Scope scope = t.getScope();
          Scope.Var var = scope.getVar(n.getString());
          if (var == null) {
            // This should be reported as an error or a warning depending
            // on the configuration.
            if (t.getInput().isExtern()) {
              if (parent.getType() == Token.GETPROP && n == parent.getFirstChild()) {
                // This is a getprop of a variable in the externs.
                // This check used to prevent a variable from getting
                // renamed, but no longer has any effect.
                return;
              }
              t.report(n, UNDEFINED_VAR_ERROR, n.getString());
              return;
            }
            if (t.compiler.getErrorLevel(UNDEFINED_VAR_ERROR) == Checker.ErrorLevel.ERROR) {
              t.report(n, UNDEFINED_VAR_ERROR, n.getString());
              return;
            }
            // In sanity check mode, throw an exception if we encounter
            // an undefined variable.
            if (t.sanityCheck) {
              throw new IllegalStateException(n.getString() + " is undefined");
            }
            // In other cases, create a synthesized extern var.
            t.getVarCheck().createSynthesizedExternVar(n.getString());
            scope.getGlobalScope().declare(n.getString(), n, null, t.getVarCheck().getSynthesizedExternsInput());
          }
        }

        // Check module dependencies.
        CompilerInput currInput = t.getInput();
        CompilerInput varInput = var.input;
        if (varInput != currInput && varInput != null && currInput != null) {
          JSModule currModule = currInput.getModule();
          JSModule varModule = varInput.getModule();
          JSModuleGraph moduleGraph = t.compiler.getModuleGraph();
          if (varModule != currModule) {
            if (moduleGraph.dependsOn(currModule, varModule)) {
              // The module dependency was properly declared.
            } else {
              // The variable reference violates a declared module dependency.
              t.report(n, VIOLATED_MODULE_DEP_ERROR,
                       currModule.getName(), varModule.getName(), n.getString());
            }
          }
        }
      }
    }
  }
}