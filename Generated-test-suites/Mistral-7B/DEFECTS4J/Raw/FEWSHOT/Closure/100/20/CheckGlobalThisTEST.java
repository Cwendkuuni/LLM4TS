package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String sourceCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertTrue(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_EmptyFunction() {
    String sourceCode = "function() {}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertFalse(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String sourceCode = "var Obj = function() {}; Obj.prototype.foo = function() { this.bar = 42; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertFalse(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String sourceCode = "function Obj() { this.foo = 42; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertFalse(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String sourceCode = "/** @this {foo: number} */ function Obj() { this.foo = 42; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertFalse(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithoutThisAnnotation() {
    String sourceCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertTrue(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignments() {
    String sourceCode = "function evil() { this.useful = undefined; a = this; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertFalse(hasWarning);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithGetProperty() {
    String sourceCode = "function evil() { this.prototype.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker(Diagnostics diagnostics) {
        return new CheckerFactory().getCheckerByKind(CheckerFactory.CheckerKind.CHECK_GLOBAL_THIS, diagnostics);
      }
    };

    Parser parser = new Parser(new Diagnostics());
    Node ast = parser.parse(compiler, sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverseAllChildren();

    boolean hasWarning = false;
    for (Diagnostics.Message message : compiler.getDiagnostics()) {
      if (message.getSeverity() == Diagnostics.Warning) {
        hasWarning = true;
      }
    }

    assertFalse(hasWarning);
  }
}