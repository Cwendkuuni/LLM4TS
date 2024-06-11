import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "function evil() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);
    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(1));

    DiagnosticType warningType = warnings.getWarning(0).getType();
    assertThat(warningType, instanceOf(GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "function EvilConstructor() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);
    Node constructorNode = ast.getFirstChild();
    constructorNode.setJSDocInfo(new JSDocInfo(true, false, true, null, null));

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "var EvilPrototype = function() {}; EvilPrototype.prototype.evil = function() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);
    Node methodNode = ast.getLastChild();
    methodNode.setJSDocInfo(new JSDocInfo(false, true, null, null, null));

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotation() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "/** @constructor */ function EvilConstructor() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionNoAnnotation() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "function EvilFunction() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(1));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "var a = function() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithVariableDeclaration() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "var a = function() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);
    Node functionNode = ast.getLastChild();

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeProperty() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "var EvilPrototype = function() {}; EvilPrototype.prototype.evil = function() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);
    Node prototypeNode = ast.getFirstChild();
    prototypeNode.setType(Token.NAME);
    prototypeNode.setJSDocInfo(new JSDocInfo(true, false, true, null, null));
    prototypeNode.setQualifiedName("EvilPrototype.prototype");

    Node methodNode = ast.getLastChild();
    methodNode.setJSDocInfo(new JSDocInfo(false, true, null, null, null));

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeSubProperty() {
    CompilerOptions options = new CompilerOptions(null);
    Parser parser = new Parser(null);
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Checker checker = new Checker(options, parser, compiler);

    String sourceCode = "var EvilPrototype = function() {}; EvilPrototype.prototype.prototype.evil = function() { this.useful = undefined; }";
    Node ast = parser.parse(sourceCode, null, null);
    Node prototypeNode = ast.getFirstChild();
    prototypeNode.setType(Token.NAME);
    prototypeNode.setJSDocInfo(new JSDocInfo(true, false, true, null, null));
    prototypeNode.setQualifiedName("EvilPrototype.prototype");

    Node prototypePrototypeNode = parser.parse("prototype", null, prototypeNode).getLastChild();
    prototypePrototypeNode.setType(Token.GETPROP);
    prototypePrototypeNode.setString("prototype");
    prototypePrototypeNode.setJSDocInfo(new JSDocInfo(false, false, null, null, null));

    Node methodNode = parser.parse("function() { this.useful = undefined; }", null, prototypePrototypeNode).getLastChild();
    methodNode.setJSDocInfo(new JSDocInfo(false, true, null, null, null));

    ast.addChild(prototypePrototypeNode);
    ast.addChild(methodNode);

    checker.process(ast, null /* sourceFile */);

    Diagnostics diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics, instanceOf(Diagnostics.class));
    Diagnostics warnings = diagnostics.getWarnings();

    assertThat(warnings, instanceOf(Diagnostics.class));
    assertThat(warnings.getNumberOfErrors(), is(0));
    assertThat(warnings.getNumberOfWarnings(), is(0));
  }
}