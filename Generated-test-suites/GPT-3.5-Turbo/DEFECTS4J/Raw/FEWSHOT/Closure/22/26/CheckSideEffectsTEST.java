package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node externs = IR.root();
        Node root = IR.root();
        checker.process(externs, root);
        // TODO: Add assertions
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node scriptRoot = IR.script();
        Node originalRoot = IR.root();
        checker.hotSwapScript(scriptRoot, originalRoot);
        // TODO: Add assertions
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        NodeTraversal t = new NodeTraversal(compiler, new StripProtection(compiler));
        Node n = IR.empty();
        Node parent = IR.empty();
        checker.visit(t, n, parent);
        // TODO: Add assertions
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        checker.protectSideEffects();
        // TODO: Add assertions
    }

    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        checker.addExtern();
        // TODO: Add assertions
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);

        Node externs = IR.root();
        Node root = IR.root();
        NodeTraversal.traverse(compiler, root, stripper);
        // TODO: Add assertions
    }
}