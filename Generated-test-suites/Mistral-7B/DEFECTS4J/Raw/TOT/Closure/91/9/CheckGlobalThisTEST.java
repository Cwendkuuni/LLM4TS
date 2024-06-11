package com.google.javascript.jscomp.CheckGlobalThisTest;

import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.WARNING, DiagnosticType.JSC_USED_GLOBAL_THIS);
    compiler = new AbstractCompiler() {
      @Override
      protected CompilerOptions getOptions() {
        return options;
      }
    };
  }

  @Test
  public void testThisIsUsedInsideFunction() {
    String source = "function Foo() {"
        + "  this.foo = 'bar';"
        + "}";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    new CheckGlobalThis(compiler, WARNING).traverse(t, tree, null);

    // No warning expected
    assertThat(t.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testThisIsUsedOnLeftSideOfAssignment() {
    String source = "var a = { foo: function() { this.bar = 'baz'; } }";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    new CheckGlobalThis(compiler, WARNING).traverse(t, tree, null);

    // Expected warning: JSC_USED_GLOBAL_THIS
    DiagnosticType expectedDiagnostic = DiagnosticType.JSC_USED_GLOBAL_THIS;
    assertThat(t.getDiagnostics().get(0).getCode(), equalTo(expectedDiagnostic.getCode()));
  }

  @Test
  public void testThisIsUsedOnLeftSideOfPropertyAccess() {
    String source = "var a = { foo: { bar: function() { this.baz = 'qux'; } } }";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    new CheckGlobalThis(compiler, WARNING).traverse(t, tree, null);

    // Expected warning: JSC_USED_GLOBAL_THIS
    DiagnosticType expectedDiagnostic = DiagnosticType.JSC_USED_GLOBAL_THIS;
    assertThat(t.getDiagnostics().get(0).getCode(), equalTo(expectedDiagnostic.getCode()));
  }

  @Test
  public void testThisIsUsedInsidePrototypeMethod() {
    String source = "function Foo() {}"
        + "Foo.prototype.bar = function() {"
        + "  this.baz = 'qux';"
        + "}";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    new CheckGlobalThis(compiler, WARNING).traverse(t, tree, null);

    // No warning expected
    assertThat(t.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testThisIsUsedInsideConstructor() {
    String source = "function Foo() {"
        + "  this.foo = 'bar';"
        + "}"
        + "new Foo();";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    new CheckGlobalThis(compiler, WARNING).traverse(t, tree, null);

    // No warning expected
    assertThat(t.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testThisIsUsedInsideFunctionAnnotatedWithThis() {
    String source = "function @this Foo() {"
        + "  this.foo = 'bar';"
        + "}"
        + "new Foo();";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    new CheckGlobalThis(compiler, WARNING).traverse(t, tree, null);

    // No warning expected
    assertThat(t.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testThisIsUsedInsideFunctionWhereNoAnnotationIsPresented() {
    String source = "function Foo() {"
        + "  this.foo = 'bar';"
        + "}"
        + "new Foo();";

    TestSourceFile sf = new TestSourceFile("test.js", source);
    Node tree = compiler.parse(sf, null);
    NodeTraversal t = new NodeTraversal(tree, compiler);

    // Set the check level to error, since there's no this annotation
    CompilerOptions errorOptions = new CompilerOptions();
    errorOptions.setErrorLevel(CheckLevel.ERROR, DiagnosticType.JSC_USED_GLOBAL_THIS);
    AbstractCompiler errorCompiler = new AbstractCompiler() {
      @Override
      protected CompilerOptions getOptions() {
        return errorOptions;
      }
    };
    new CheckGlobalThis(errorCompiler, CHECK_LEVEL_ERROR).traverse(t, tree, null);

    // Expected error: JSC_USED_GLOBAL_THIS
    DiagnosticType expectedDiagnostic = DiagnosticType.JSC_USED_GLOBAL_THIS;
    assertThat(t.getDiagnostics().get(0).getCode(), equalTo(expectedDiagnostic.getCode()));
  }
}