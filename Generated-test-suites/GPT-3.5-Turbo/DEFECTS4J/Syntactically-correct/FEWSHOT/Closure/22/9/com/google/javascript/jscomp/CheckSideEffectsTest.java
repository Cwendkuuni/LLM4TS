/* JUnit test cases for CheckSideEffects */
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node externs = new Node();
    Node root = new Node();
    checkSideEffects.process(externs, root);
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node scriptRoot = new Node();
    Node originalRoot = new Node();
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
  }

  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    checkSideEffects.visit(t, n, parent);
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    checkSideEffects.protectSideEffects();
  }

  @Test
  public void testAddExtern() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    checkSideEffects.addExtern();
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    Node externs = new Node();
    Node root = new Node();
    stripProtection.process(externs, root);
  }

}