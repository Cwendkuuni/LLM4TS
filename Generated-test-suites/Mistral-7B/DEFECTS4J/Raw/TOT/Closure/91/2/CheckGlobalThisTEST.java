import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    SourceFile source = new SourceFile("test.js", "var a = function() { this.x = 1; };");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GLOBAL_THIS, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(DiagnosticType.warning(JSC_USED_GLOBAL_THIS, "dangerous use of the global 'this' object")));
  }

  @org.junit.Test
  public void testCheckGlobalThisBeforeAssign() {
    SourceFile source = new SourceFile("test.js", "var a; a = function() { this.x = 1; };");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunction() {
    SourceFile source = new SourceFile("test.js", "function a() { this.x = 1; }");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisAnnotatedFunction() {
    SourceFile source = new SourceFile("test.js", "/** @constructor */ function a() { this.x = 1; }");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisPrototype() {
    SourceFile source = new SourceFile("test.js", "var obj = { x: function() { this.y = 1; } };");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionCall() {
    SourceFile source = new SourceFile("test.js", "function a() { this.x = 1; } a();");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionCallWithGlobal() {
    SourceFile source = new SourceFile("test.js", "var a = function() { this.x = 1; }; a();");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(DiagnosticType.warning(JSC_USED_GlobalThis, "dangerous use of the global 'this' object")));
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionCallWithPrototype() {
    SourceFile source = new SourceFile("test.js", "var obj = {}; function a() { this.x = 1; } obj.a = a; obj.a();");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisFunctionCallWithPrototypeAndGlobal() {
    SourceFile source = new SourceFile("test.js", "var obj = {}; function a() { this.x = 1; } obj.a = a; a();");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.equals(DiagnosticType.warning(JSC_USED_GlobalThis, "dangerous use of the global 'this' object")));
  }

  @org.junit.Test
  public void testCheckGlobalThisWithAnnotatedMethod() {
    SourceFile source = new SourceFile("test.js", "/** @constructor */ var obj = function() { /** @this {MyType} */ this.x = 1; };");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisWithAnnotatedMethodCall() {
    SourceFile source = new SourceFile("test.js", "var obj = new MyType(); obj.x = 1;");
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TYPEcheck, WARNING);
    options.setWarningLevel(JSC_USED_GlobalThis, ERROR);
    AbstractCompiler compiler = new TestCompiler(options);
    Checker checker = new Checker(compiler, new Parser());

    Node ast = checker.parse(source.getText());
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    new CheckGlobalThis(compiler, WARNING).visitAll(traversal);

    assertEquals(0, compiler.getDiagnostics().size());
  }
}