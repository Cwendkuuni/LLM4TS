package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

public class VarCheckTest extends CompilerPassTestBase {

  @Test
  public void testVarCheck() throws Exception {
    String sourceCode =
        "function foo() {" +
            "var x;" +
            "var y = x;" +
            "return x;" +
        "}";

    CompilerPass pass = new VarCheck(getTestCompiler());
    CompilerPassManager manager = new CompilerPassManager(pass);
    Node ast = parse(sourceCode);

    testErrors(manager, ast, "JSC_UNDEFINED_VARIABLE", "x");

    // Test that the variable is actually declared in the output
    Node resultAst = manager.getOutput();
    Node xDeclaration = assertDeclarationExists(resultAst, "x");
    assertNotNull(xDeclaration);

    // Test that the variable is accessible in the function body
    Node xAssignment = assertAssignmentExists(resultAst, "x", "x");
    assertNotNull(xAssignment);

    // Test that the variable is accessible as a reference to another variable
    Node yAssignment = assertAssignmentExists(resultAst, "y", "y");
    assertNotNull(yAssignment);

    // Test that a function declaration is valid
    sourceCode =
        "function foo() {" +
            "function bar() {}" +
            "return bar();" +
        "}";

    assertErrors(manager, ast, INVALID_FUNCTION_DECL);

    // Test that a function expression is valid
    sourceCode =
        "function foo() {" +
            "return (function() {})();" +
        "}";

    assertErrors(manager, ast, INVALID_FUNCTION_DECL);

    // Test missing module dependency
    String sourceCodeMissingDependency =
        "module1.js:" +
        "function foo() {" +
        "  var x = module2.bar;" +
        "}" +
        "module2.js:" +
        "function bar() {}";

    assertErrors(manager, parse(sourceCodeMissingDependency),
        MISSING_MODULE_DEP_ERROR, "module1.js", "module2.js", "bar");

    // Test violated module dependency
    sourceCodeMissingDependency =
        "module1.js:" +
        "function foo() {" +
        "  var x = module2.bar;" +
        "}" +
        "module2.js:" +
        "function bar() {}" +
        "module1.js:load()" +
        "module2.js:load()";

    assertErrors(manager, parse(sourceCodeMissingDependency),
        VIOLATED_MODULE_DEP_ERROR, "module2.js", "module1.js", "bar", "foo");

    // Test strict module dependency error
    String sourceCodeStrictModuleDependency =
        "module1.js:" +
        "function foo() {" +
        "  var x = module2.bar;" +
        "}" +
        "module2.js:" +
        "function bar() {}";

    assertNoErrors(manager, parse(sourceCodeStrictModuleDependency),
        getTestCompiler().getErrorLevel(
            JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR)));

    // Test name reference in externs
    String sourceCodeNameReferenceInExterns =
        "externs.js:" +
        "var x = 1;" +
        "foo.js:" +
        "function foo() {" +
        "  var x = 2;" +
        "}" +
        "externs.js:load();" +
        "foo.js:load();";

    assertWarnings(manager, parse(sourceCodeNameReferenceInExterns),
        NAME_REFERENCE_IN_EXTERNS_ERROR, "x");

    // Test undefined extern variable
    sourceCodeNameReferenceInExterns =
        "externs.js:" +
        "var x;" +
        "foo.js:" +
        "function foo() {" +
        "  return x;" +
        "}";

    assertWarnings(manager, parse(sourceCodeNameReferenceInExterns),
        UNDEFINED_EXTERN_VAR_ERROR, "x");
  }
}