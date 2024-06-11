package com.google.javascript.jscomp.test.unit;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test_util.AbstractTest;
import com.google.javascript.jscomp.test_util.ThinNodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.io.IOException;
import java.util.Set;

public class VarCheckTest extends AbstractTest {

  private static final String CLASS_NAME = VarCheck.class.getName();

  @Override
  protected Compiler pass(Compiler compiler) {
    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionNames(true);
    options.setCheckTypes(false);
    options.setCreateSourceFileForEachOutputFile(false);
    options.setWarningLevel(JSError.make("", 0, 0, DiagnosticType.WARNING), CheckLevel.QUIRKS);
    options.setErrorLevel(JSError.make("", 0, 0, DiagnosticType.ERROR), CheckLevel.ERROR);

    return new VarCheck(compiler, true);
  }

  @org.junit.Test
  public void testEmptyExterns() throws IOException {
    String sourceCode = "/** @fileoverview */\n" +
        "var x;\n" +
        "// empty externs";

    Node ast = parse(sourceCode);
    Compiler compiler = new Compiler();
    assertCompilesTo(compiler, ast);

    NodeTraversal traversal = new ThinNodeTraversal(ast);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler).process(null, ast);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testUndefinedVariable() throws IOException {
    String sourceCode = "/** @fileoverview */\n" +
        "var x;\n" +
        "y = 5;\n";

    Node ast = parse(sourceCode);
    Compiler compiler = new Compiler();
    assertCompilesTo(compiler, ast);

    NodeTraversal traversal = new ThinNodeTraversal(ast);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler).process(null, ast);
    Node error = traversal.getLastError();
    assertEquals(DiagnosticType.ERROR, error.getType());
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getCode());
    assertEquals("variable y is undefined", error.getMessage());
  }

  @org.junit.Test
  public void testModuleDependency() throws IOException {
    String moduleA = "/** @fileoverview */\n" +
        "// module A\n" +
        "module 'a' {\n" +
        "  export function foo() {}\n" +
        "  export var x = 5;\n" +
        "}";

    String moduleB = "/** @fileoverview */\n" +
        "// module B\n" +
        "// depends on module A\n" +
        "import 'a' as a;\n" +
        "a.foo();\n" +
        "var y = a.x;";

    String sourceCode = "/** @fileoverview */\n" +
        "// module C\n" +
        "import 'b' as b;\n" +
        "b.foo();\n" +
        "var z = b.y;";

    Node astModuleA = parse(moduleA);
    Node astModuleB = parse(moduleB);
    Node ast = parse(sourceCode);

    Compiler compiler = new Compiler();
    assertCompilesTo(compiler, astModuleA, astModuleB, ast);

    NodeTraversal traversal = new ThinNodeTraversal(ast);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler).process(astModuleA, ast);
    Node error = traversal.getLastError();
    assertEquals(DiagnosticType.ERROR, error.getType());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", error.getCode());
    assertEquals("module C cannot reference a, defined in module A, since A loads after C", error.getMessage());
  }

  @org.junit.Test
  public void testMissingModuleDependency() throws IOException {
    String moduleA = "/** @fileoverview */\n" +
        "// module A\n" +
        "module 'a' {\n" +
        "  export function foo() {}\n" +
        "  export var x = 5;\n" +
        "}";

    String sourceCode = "/** @fileoverview */\n" +
        "// module C\n" +
        "import 'a' as a;\n" +
        "a.foo();\n" +
        "var z = a.x;";

    Node astModuleA = parse(moduleA);
    Node ast = parse(sourceCode);

    Compiler compiler = new Compiler();
    assertCompilesTo(compiler, astModuleA, ast);

    NodeTraversal traversal = new ThinNodeTraversal(ast);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler).process(astModuleA, ast);
    Node error = traversal.getLastError();
    assertEquals(DiagnosticType.WARNING, error.getType());
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", error.getCode());
    assertEquals("missing module dependency; module C should depend on module a because it references a", error.getMessage());
  }

  @org.junit.Test
  public void testStrictModuleDependency() throws IOException {
    String moduleA = "/** @fileoverview */\n" +
        "// module A\n" +
        "module 'a' {\n" +
        "  export function foo() {}\n" +
        "  export var x = 5;\n" +
        "}";

    String sourceCode = "/** @fileoverview */\n" +
        "// module C\n" +
        "// depends on module A\n" +
        "import 'a' as a;\n" +
        "a.foo();\n" +
        "var z = a.x;";

    Node astModuleA = parse(moduleA);
    Node ast = parse(sourceCode);

    Compiler compiler = new Compiler();
    compiler.setErrorLevel(UNDEFINED_EXTERN_VAR_ERROR, CheckLevel.ERROR);
    assertCompilesTo(compiler, astModuleA, ast);

    NodeTraversal traversal = new ThinNodeTraversal(ast);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler, true).process(astModuleA, ast);
    Node error = traversal.getLastError();
    assertEquals(DiagnosticType.DISABLED, error.getType());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", error.getCode());
    assertEquals("module C cannot reference a, defined in module A", error.getMessage());
  }

  @org.junit.Test
  public void testNameReferenceInExterns() throws IOException {
    String sourceCode = "/** @fileoverview */\n" +
        "var x = 5;\n" +
        "// empty externs";

    Node ast = parse(sourceCode);
    Compiler compiler = new Compiler();
    assertCompilesTo(compiler, ast);

    NodeTraversal traversal = new ThinNodeTraversal(ast);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler).process(null, ast);
    assertEquals(0, compiler.getDiagnostics().size());

    traversal.traverse();
    Node nameNode = traversal.getNode();
    assertEquals(Token.NAME, nameNode.getType());
    assertEquals("x", nameNode.getString());

    compiler.getOptions().setPrintErrors(true);
    new VarCheck(compiler).process(null, ast);
    Node error = traversal.getLastError();
    assertEquals(DiagnosticType.WARNING, error.getType());
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", error.getCode());
    assertEquals("accessing name x in externs has no effect", error.getMessage());
  }

  @org.junit.Test
  public void testDuplicateDeclaration() throws IOException {
    String sourceCode = "/** @fileoverview */\n" +
        "// file1.js\n" +
        "var x = 5;\n" +
        "// file2.js\n" +
        "var x = 10;\n";

    Node astFile1 = parse(sourceCode.substring(0, sourceCode.indexOf("\n// file2.js")) + "\n");
    Node astFile2 = parse(sourceCode.substring(sourceCode.indexOf("\n// file2.js") + "\n".length()));

    Compiler compiler = new Compiler();
    assertCompilesTo(compiler, astFile1, astFile2);

    NodeTraversal traversal = new ThinNodeTraversal(astFile1);
    compiler.getOptions().setPrintErrors(false);
    new VarCheck(compiler).process(astFile1, astFile2);
    Node error = traversal.getLastError();
    assertEquals(DiagnosticType.ERROR, error.getType());
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getCode());
    assertEquals("variable x is undefined", error.getMessage());

    traversal.traverse();
    Node duplicateDeclaration = traversal.getNode();
    assertEquals(Token.VAR, duplicateDeclaration.getType());
    assertEquals("x", duplicateDeclaration.getFirstChild().getString());

    compiler.getOptions().setPrintErrors(true);
    new VarCheck(compiler).process(astFile1, astFile2);
    error = traversal.getLastError();
    assertEquals(DiagnosticType.WARNING, error.getType());
    assertEquals("duplicate", error.getMessageSuppression());
  }
}