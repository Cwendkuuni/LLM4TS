import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

  @ org.junit.Test
  public void testEmptyFunction() {
    String jsCode = "function foo() {}";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Empty function should be removed", compiler.getSource().isEmpty());
  }

  @ org.junit.Test
  public void testReturnStatementWithDeadCode() {
    String jsCode = "function foo() { return; alert('dead code'); }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("return;"));
    assertFalse("Alert statement should be removed", compiler.getSource().toString().contains("alert"));
  }

  @ org.junit.Test
  public void testPrototypePropertyAccessNoInitialization() {
    String jsCode = "function Foo() {} Foo.prototype.x = 'prototype property';";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Prototype property access should be preserved", compiler.getSource().toString().contains("Foo.prototype.x"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideLoop() {
    String jsCode = "for (var i = 0; i < 5; i++) { alert('dead code: ' + i); }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("for (var i = 0; i < 5; i++) {"));
    assertFalse("Alert statements should be removed", compiler.getSource().toString().contains("alert"));
  }

  @ org.junit.Test
  public void testUnreachableCodeInsideConditional() {
    String jsCode = "if (false) { alert('dead code'); }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("if (false) {"));
    assertFalse("Alert statement should be removed", compiler.getSource().toString().contains("alert"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideSwitchStatement() {
    String jsCode = "switch (1) { case 1: alert('dead code'); break; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("switch (1) { case 1:"));
    assertFalse("Alert statement should be removed", compiler.getSource().toString().contains("alert"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideTryCatchStatement() {
    String jsCode = "try { alert('dead code'); } catch(e) {}";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("try { alert"));
    assertFalse("Alert statement should be removed", compiler.getSource().toString().contains("alert"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideFunctionDefinition() {
    String jsCode = "function foo() { alert('dead code'); } function bar() {}";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("function foo() { alert"));
    assertFalse("Alert statement should be removed", compiler.getSource().toString().contains("alert"));
    assertTrue("Function bar should not be removed", compiler.getSource().toString().contains("function bar() {}"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideIfStatementWithDeadElse() {
    String jsCode = "if (true) { alert('dead code in if'); } else { alert('dead code in else'); }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code in if statement should be removed", compiler.getSource().toString().contains("if (true) { alert"));
    assertFalse("Dead code in else statement should be removed", compiler.getSource().toString().contains("else { alert"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideIfElseIfStatement() {
    String jsCode = "if (false) { alert('dead code in if'); } else if (true) { alert('dead code in elseif'); }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertFalse("Dead code in if statement should be removed", compiler.getSource().toString().contains("if (false) { alert"));
    assertFalse("Dead code in elseif statement should be removed", compiler.getSource().toString().contains("else if (true) { alert"));
  }

  @ org.junit.Test
  public void testDeadCodeInsideForInStatement() {
    String jsCode = "for (var p in obj) { alert('dead code'); }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Dead code should be removed", compiler.getSource().toString().contains("for (var p in obj) {"));
    assertFalse("Alert statement should be removed", compiler.getSource().toString().contains("alert"));
  }

  @ org.junit.Test
  public void testKeepNoOpStatement() {
    String jsCode = "function foo() { alert('test'); x = x; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertFalse("NoOp statement should not be removed", compiler.getSource().toString().contains("x = x;"));
  }

  @ org.junit.Test
  public void testKeepConditionalStatement() {
    String jsCode = "if (x > 0) { y = 10; } else { y = 20; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Conditional statement should not be removed", compiler.getSource().toString().contains("if (x > 0) { y = 10; } else { y = 20; }"));
  }

  @ org.junit.Test
  public void testKeepSwitchStatement() {
    String jsCode = "switch (x) { case 1: y = 10; break; case 2: y = 20; break; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPass pass = new UnreachableCodeElimination(compiler, false);

    Node root = Parser.parse(Parser.parseJsConfig(jsCode), null);
    traverse(compiler, root, pass);

    assertTrue("Switch statement should not be removed", compiler.getSource().toString().contains("switch (x) { case 1: y = 10; break; case 2: y = 20; break; }"));
  }
}