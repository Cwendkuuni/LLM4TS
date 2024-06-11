package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ParserTestingUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();

    // Test basic functionality
    testFunctionality(compiler, "function foo() {}", CheckSideEffects.Level.WARNING);
    testFunctionality(compiler, "function foo() { return 1; }", CheckSideEffects.Level.WARNING);
    testFunctionality(compiler, "function foo() { var x = 1; }", CheckSideEffects.Level.WARNING);
    testFunctionality(compiler, "function foo() { return 1; var x = 1; }", CheckSideEffects.Level.WARNING);
    testFunctionality(compiler, "function foo() { if (true) { return 1; } }", CheckSideEffects.Level.WARNING);
    testFunctionality(compiler, "function foo() { if (false) { return 1; } }", CheckSideEffects.Level.WARNING);
    testFunctionality(compiler, "function foo() { while (true) {} }", CheckSideEffects.Level.WARNING);

    // Test edge cases and exception handling
    testEdgeCases(compiler, CheckSideEffects.Level.WARNING);
    testExceptions(compiler, CheckSideEffects.Level.WARNING);
  }

  private void testFunctionality(AbstractCompiler compiler, String code, CheckLevel level) {
    CompilerOptions options = ParserTestingUtil.createCompilerOptions();
    CompilerInput input = new CompilerInput(code, "Test", null, null, options);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(input, null);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertFalse("No problem nodes expected", !problemNodes.isEmpty());
    problemNodes.clear();

    // Ensure no problem nodes after compilation
    compiler.compile(input, null);
    assertFalse("No problem nodes expected after compilation", !problemNodes.isEmpty());
  }

  private void testEdgeCases(AbstractCompiler compiler, CheckLevel level) {
    String code = "function foo() { return (function() { var x = 1; })(); }";
    testFunctionalityWithEdgeCases(compiler, level, code);

    code = "function foo() { return (function(x) { var y = x; })(); }";
    testFunctionalityWithEdgeCases(compiler, level, code);

    code = "function foo() { return (function(x) { return x; })(); }";
    testFunctionalityWithEdgeCases(compiler, level, code);

    code = "function foo() { return eval('var x = 1'); }";
    testFunctionalityWithEdgeCases(compiler, level, code);

    code = "function foo() { return eval('var x = 1; return 1;'); }";
    testFunctionalityWithEdgeCases(compiler, level, code);

    code = "function foo() { return eval('function bar() {}; return 1;'); }";
    testFunctionalityWithEdgeCases(compiler, level, code);

    code = "function foo() { return eval('return 1;'); }";
    testFunctionalityWithEdgeCases(compiler, level, code);
  }

  private void testExceptions(AbstractCompiler compiler, CheckLevel level) {
    String code = "function foo() { try { throw new Error('Test error'); } catch (e) { return 1; } }";
    testFunctionalityWithExceptions(compiler, level, code);

    code = "function foo() { try { throw new Error('Test error'); } finally { return 1; } }";
    testFunctionalityWithExceptions(compiler, level, code);

    code = "function foo() { try { throw new Error('Test error'); } finally { throw new Error('Test error'); } }";
    testFunctionalityWithExceptions(compiler, level, code);

    code = "function foo() { throw new Error('Test error'); }";
    testFunctionalityWithExceptions(compiler, level, code);

    code = "function foo() { return throw new Error('Test error'); }";
    testFunctionalityWithExceptions(compiler, level, code);

    code = "function foo() { try { return 'test'; } catch (e) { throw new Error('Test error'); } }";
    testFunctionalityWithExceptions(compiler, level, code);
  }

  private void testFunctionalityWithEdgeCases(AbstractCompiler compiler, CheckLevel level, String code) {
    CompilerOptions options = ParserTestingUtil.createCompilerOptions();
    CompilerInput input = new CompilerInput(code, "Test", null, null, options);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(input, null);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertFalse("No problem nodes expected", !problemNodes.isEmpty());
    problemNodes.clear();

    // Ensure no problem nodes after compilation
    CompilerPass stripProtection = new CheckSideEffects.StripProtection(compiler);
    compiler.addPasses(stripProtection);
    compiler.compile(input, null);
    assertFalse("No problem nodes expected after compilation", !problemNodes.isEmpty());
  }

  private void testFunctionalityWithExceptions(AbstractCompiler compiler, CheckLevel level, String code) {
    CompilerOptions options = ParserTestingUtil.createCompilerOptions();
    CompilerInput input = new CompilerInput(code, "Test", null, null, options);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(input, null);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue("Problem nodes expected", !problemNodes.isEmpty());
    problemNodes.clear();

    // Ensure no problem nodes after compilation
    CompilerPass stripProtection = new CheckSideEffects.StripProtection(compiler);
    compiler.addPasses(stripProtection);
    compiler.compile(input, null);
    assertFalse("No problem nodes expected after compilation", !problemNodes.isEmpty());
  }

  static class Compiler extends AbstractCompiler {
    @Override
    protected NodeFactory getNodeFactory() {
      return super.getNodeFactory();
    }
  }
}