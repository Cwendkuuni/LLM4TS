import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode =
        "function unsafeGlobalThis() { this.prop = 1; } "
        + "var globalThisVar = this; "
        + "function prototypeFunction() { this.prop = 2; } "
        + "function constructorFunction() { this.prop = 3; } "
        + "function normalFunction() { var that = this; this.prop = 4; } ";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertTrue(checker.hasDiagnostics());
    assertFalse(checker.hasDiagnostics(DiagnosticType.ERROR));
    checker.clearDiagnostics();

    sourceCode =
        "function safePrototypeFunction() { this.prop = 2; } "
        + "function safeConstructorFunction() { this.prop = 3; } "
        + "function safeNormalFunction() { var that = this; this.prop = 4; } ";

    source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    ast = compiler.compile(source, options).getAst();

    traversal = new NodeTraversal(ast, null);
    checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisAssignment() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "var a = this; a.prop = 1;";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionCall() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "unsafeGlobalThis();";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertTrue(checker.hasDiagnostics());
    checker.clearDiagnostics();

    sourceCode = "safePrototypeFunction();";

    source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    ast = compiler.compile(source, options).getAst();

    traversal = new NodeTraversal(ast, null);
    checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisObjectLiteral() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "var obj = { prop: this };";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisObjectLiteralNested() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "var obj = { prop: { prop: this } };";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionAnnotatedWithConstructor() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "@constructor\nfunction ConstructorFunction() {}";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "@this\nfunction ThisFunction() {}";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionAnnotatedWithOverride() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "class Base {\n  foo() {}\n}\n\n@override\nclass Derived extends Base {\n  foo() {}\n}";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionAnnotatedWithInterface() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCheckGlobalThis(true);
    options.setWarningLevel(WARNING);

    String sourceCode = "interface Interface {\n  foo(): void;\n}\n\nfunction Implementation() {\n  foo() {}\n}";

    TestSourceFile source = new TestSourceFile("CheckGlobalThisTest.js", sourceCode);
    Node ast = compiler.compile(source, options).getAst();

    NodeTraversal traversal = new NodeTraversal(ast, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, options);
    traversal.traverse(checker);

    assertFalse(checker.hasDiagnostics());
  }
}