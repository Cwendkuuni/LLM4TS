import static org.junit.Assert.*;
import static com.google.javascript.jscomp.compiler.TestCompiler.checkCompilationError;
import static com.google.javascript.jscomp.compiler.TestCompiler.checkCompilationWarning;
import static com.google.javascript.jscomp.nodetraversal.AbstractNodeVisitor.kindOf;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.compiler.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private Compiler compiler;
  private AbstractCompiler abstractCompiler;

  @Before
  public void setUp() {
    compiler = new Compiler(new CheckerFactory(), new CompilerOptions());
    abstractCompiler = new AbstractCompiler(compiler);
  }

  @Test
  public void testUndefinedVarError() {
    String sourceCode = "function foo() { var x; x++; }";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler);

    NodeTraversal traversal = new NodeTraversal(abstractCompiler, ast);
    VarCheck varCheck = new VarCheck(abstractCompiler);
    traversal.traverse(ast, varCheck);

    checkCompilationError(compiler, "JSC_UNDEFINED_VARIABLE", "x");
  }

  @Test
  public void testMissingModuleDepWarning() {
    String sourceCode = "var x = require('module1.js').foo;";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    NodeTraversal traversal = new NodeTraversal(abstractCompiler, ast);
    VarCheck varCheck = new VarCheck(abstractCompiler, true);
    traversal.traverse(ast, varCheck);

    checkCompilationWarning(compiler, "JSC_MISSING_MODULE_DEPENDENCY", "module1.js", "foo");
  }

  @Test
  public void testMissingModuleDepError() {
    String sourceCode = "var x = require('module2.js').foo;";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    JSModule module1 = TestCompiler.newModule("module1.js");
    JSModule module2 = TestCompiler.newModule("module2.js");
    CompilerOptions options = new CompilerOptions();
    options.addModuleDependency(module1, module2);

    Compiler compiledCompiler = new Compiler(new CheckerFactory(), options);
    AbstractCompiler compiledAbstractCompiler = new AbstractCompiler(compiledCompiler);

    NodeTraversal traversal = new NodeTraversal(compiledAbstractCompiler, ast);
    VarCheck varCheck = new VarCheck(compiledAbstractCompiler);
    traversal.traverse(ast, varCheck);

    checkCompilationError(compiledCompiler, "JSC_MISSING_MODULE_DEPENDENCY", "module1.js", "foo");
  }

  @Test
  public void testStrictModuleDepError() {
    String sourceCode = "var x = require('module2.js').foo;";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    JSModule module1 = TestCompiler.newModule("module1.js");
    JSModule module2 = TestCompiler.newModule("module2.js");
    CompilerOptions options = new CompilerOptions();
    options.addModuleDependency(module1, module2, false);

    Compiler compiledCompiler = new Compiler(new CheckerFactory(), options);
    AbstractCompiler compiledAbstractCompiler = new AbstractCompiler(compiledCompiler);

    NodeTraversal traversal = new NodeTraversal(compiledAbstractCompiler, ast);
    VarCheck varCheck = new VarCheck(compiledAbstractCompiler);
    traversal.traverse(ast, varCheck);

    checkCompilationWarning(compiledCompiler, "JSC_STRICT_MODULE_DEPENDENCY", "module1.js", "foo");
  }

  @Test
  public void testValidModuleDep() {
    String sourceCode = "var x = require('module1.js').foo;";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    JSModule module1 = TestCompiler.newModule("module1.js");
    JSModule module2 = TestCompiler.newModule("module2.js");
    CompilerOptions options = new CompilerOptions();
    options.addModuleDependency(module2, module1);

    Compiler compiledCompiler = new Compiler(new CheckerFactory(), options);
    AbstractCompiler compiledAbstractCompiler = new AbstractCompiler(compiledCompiler);

    NodeTraversal traversal = new NodeTraversal(compiledAbstractCompiler, ast);
    VarCheck varCheck = new VarCheck(compiledAbstractCompiler);
    traversal.traverse(ast, varCheck);

    assertTrue(compiledCompiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testNameReferenceInExternsWarning() {
    String sourceCode = "var x = {foo: 1};";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    NodeTraversal traversal = new NodeTraversal(abstractCompiler, ast);
    VarCheck varCheck = new VarCheck(abstractCompiler, true);
    traversal.traverse(ast, varCheck);

    checkCompilationWarning(compiler, "JSC_NAME_REFERENCE_IN_EXTERNS", "foo");
  }

  @Test
  public void testUndefinedExternVarWarning() {
    String sourceCode = "//externs: {foo: number}";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    NodeTraversal traversal = new NodeTraversal(abstractCompiler, ast);
    VarCheck varCheck = new VarCheck(abstractCompiler, true);
    traversal.traverse(ast, varCheck);

    checkCompilationWarning(compiler, "JSC_UNDEFINED_EXTERN_VAR_ERROR", "foo");
  }

  @Test
  public void testInvalidFunctionDeclError() {
    String sourceCode = "function() {}";
    Node ast = TestCompiler.compile(sourceCode, abstractCompiler).getAst();

    NodeTraversal traversal = new NodeTraversal(abstractCompiler, ast);
    VarCheck varCheck = new VarCheck(abstractCompiler);
    traversal.traverse(ast, varCheck);

    checkCompilationError(compiler, "JSC_INVALID_FUNCTION_DECL");
  }
}