package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticMessageCategory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class VarCheckTest {

  @Test
  public void testVarCheck() throws Exception {
    String input = "function foo() {\n" +
                   "  var x;\n" +
                   "  var y = 5;\n" +
                   "  var z;\n" +
                   "  var a = 7;\n" +
                   "}\n" +
                   "\n" +
                   "function bar() {\n" +
                   "  x = 1;\n" +
                   "  y = 2;\n" +
                   "  z = 3;\n" +
                   "  a = 8;\n" +
                   "}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    CompilerInput inputCompiled = compiler.compile(input, "test.js", null);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    VarCheck check = new VarCheck(compiler);
    check.process(inputCompiled.getExterns(), root);

    // Check that all variables are declared.
    Scope globalScope = traversal.getScope();
    for (String varName : new String[]{"x", "y", "z", "a"}) {
      String errorMessage = "Undefined variable: " + varName;
      Scope.Var var = globalScope.getVar(varName);
      assertNull("Variable " + varName + " should be undefined", var);
      assertTrue("Expected error message for variable " + varName,
          check.hasError(errorMessage));
    }

    // Check that file-private variables are only accessed in the file.
    String file1Input = "function foo() {\n" +
                       "  var x = 5;\n" +
                       "}\n" +
                       "\n" +
                       "function bar() {\n" +
                       "  x = 10;\n" +
                       "}";
    CompilerInput file1Compiled = compiler.compile(file1Input, "file1.js", null);
    Node file1Root = file1Compiled.getAstRoot(compiler);

    NodeTraversal file1Traversal = new NodeTraversal(compiler, file1Root);
    check = new VarCheck(compiler);
    check.process(file1Compiled.getExterns(), file1Root);

    Scope file1GlobalScope = file1Traversal.getScope();
    assertTrue("Expected error message for file-private variable x",
        check.hasError("Module 'file1.js' cannot reference 'foo', " +
                      "defined in 'file1.js', since 'file1.js' loads after 'file1.js'"));

    // Check that module dependencies are respected.
    String file2Input = "var x = 5;\n" +
                       "\n" +
                       "function foo() {\n" +
                       "  require('file1');\n" +
                       "  x = 10;\n" +
                       "}";
    CompilerInput file2Compiled = compiler.compile(file2Input, "file2.js", null);
    Node file2Root = file2Compiled.getAstRoot(compiler);

    NodeTraversal file2Traversal = new NodeTraversal(compiler, file2Root);
    check = new VarCheck(compiler);
    check.process(file2Compiled.getExterns(), file2Root);

    Scope file2GlobalScope = file2Traversal.getScope();
    assertTrue("Expected warning message for missing module dependency",
        check.hasWarning("missing module dependency; module 'file2.js' " +
                         "should depend on 'file1.js' because it " +
                         "references 'x'"));

    // Check that strict module dependencies are respected.
    String file3Input = "define(['file1'], function(x) {\n" +
                       "  x = 10;\n" +
                       "}";
    CompilerInput file3Compiled = compiler.compile(file3Input, "file3.js", null);
    Node file3Root = file3Compiled.getAstRoot(compiler);

    NodeTraversal file3Traversal = new NodeTraversal(compiler, file3Root);
    check = new VarCheck(compiler, true);
    check.process(file3Compiled.getExterns(), file3Root);

    Scope file3GlobalScope = file3Traversal.getScope();
    assertTrue("Expected disabled error message for strict module dependency",
        check.hasDiagnosticMessage(
            DiagnosticMessageCategory.ERROR,
            "JSC_STRICT_MODULE_DEPENDENCY",
            "module 'file3.js' " +
            "cannot reference 'file1', defined in 'file1.js'"));
  }

  boolean hasError(VarCheck check, String message) {
    for (DiagnosticType type : new DiagnosticType[]{
        UNDEFINED_VAR_ERROR,
        VIOLATED_MODULE_DEP_ERROR,
        MISSING_MODULE_DEP_ERROR,
        NAME_REFERENCE_IN_EXTERNS_ERROR,
        STRICT_MODULE_DEP_ERROR}) {
      for (DiagnosticInfo info : check.getDiagnostics()) {
        if (info.getType() == type && info.getMessage().equals(message)) {
          return true;
        }
      }
    }
    return false;
  }
}