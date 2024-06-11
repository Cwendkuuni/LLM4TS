import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options, null, null);
    checker = new Checker(new Parser(), new NodeTraversal(), compiler, options);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithoutJsDoc() {
    String code = "function evil() { this.useful = undefined; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }

  @Test
  public void testCheckGlobalThis_FunctionWithJsDoc() {
    String code = "/** @constructor */ function GoodConstructor() { this.useful = undefined; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }

  @Test
  public void testCheckGlobalThis_PropertyAccess() {
    String code = "var obj = {}; obj.prototype.evil = function() { this.useful = undefined; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported

    code = "var obj = {}; obj.prototype.evil = function() { this.prototype.useful = undefined; }";
    node = new Parser().parse(code, null, null);
    checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // Error reported for 'prototype' property access
    assertFalse(checker.errorsEmpty());
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototypeProperty() {
    String code = "var obj = {}; obj.prototype.evil = function() { this.useful = this; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }

  @Test
  public void testCheckGlobalThis_AssignmentToVariable() {
    String code = "var a = this;";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_AssignmentToThis() {
    String code = "this.useful = undefined;";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // Error reported
    checker.assertErrorsEmpty();
  }

  @Test
  public void testCheckGlobalThis_AssignmentToThisWithJsDoc() {
    String code = "/** @constructor */ function EvilConstructor() { this.useful = undefined; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }

  @Test
  public void testCheckGlobalThis_ConstructorWithJsDocAndThisAnnotation() {
    String code = "/** @constructor @this {Object} */ function SafeConstructor() { this.useful = undefined; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeMethod() {
    String code = "function EvilFunction() {}\n" +
        "EvilFunction.prototype.evil = function() { this.useful = undefined; }";
    Node node = new Parser().parse(code, null, null);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    checker.check(node, null, null);
    // No error reported
  }
}