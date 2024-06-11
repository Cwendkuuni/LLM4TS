import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourceFile;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis_NormalCase() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "function Foo() { this.x = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "function Foo() { this.constructor = Foo; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getErrors().size());
    assertEquals(diagnosticType, checker.getErrors().get(0).getType());
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "function Foo() {}\nFoo.prototype.bar = function() { this.x = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_MethodWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "/** @this {number} */ function Foo() { this.x = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "var a = function() { this.x = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithVariableDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "var a = function a() { this.x = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithParameterDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "function a(x) { this.x = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_AssignmentToProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "var a = {}; a.x = function() { this.y = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getErrors().size());
    assertEquals(diagnosticType, checker.getErrors().get(0).getType());
  }

  @org.junit.Test
  public void testCheckGlobalThis_AssignmentToPrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "var a = {}; a.__proto__.x = function() { this.y = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_AssignmentToPrototypeSubProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "var a = {}; a.__proto__.x.y = function() { this.z = 1; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getErrors().size());
    assertEquals(diagnosticType, checker.getErrors().get(0).getType());
  }

  @org.junit.Test
  public void testCheckGlobalThis_AssignmentToFunctionProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "function Foo() { this.x = function() {}; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getErrors().size());
    assertEquals(diagnosticType, checker.getErrors().get(0).getType());
  }

  @org.junit.Test
  public void testCheckGlobalThis_AssignmentToFunctionPropertyWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js", null), null);
    Checker checker = new Checker(compiler, options);

    String jsCode = "function Foo() { /** @this {number} */ this.x = function() {}; }";
    Node rootNode = Parser.parse(jsCode, null, null);
    checker.process(rootNode, null);

    assertEquals(0, checker.getErrors().size());
  }
}