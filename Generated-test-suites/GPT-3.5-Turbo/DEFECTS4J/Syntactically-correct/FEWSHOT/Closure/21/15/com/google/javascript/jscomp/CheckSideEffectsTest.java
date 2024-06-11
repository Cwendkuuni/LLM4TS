package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node externs = IR.root();
        Node root = IR.root();
        checkSideEffects.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node scriptRoot = IR.root();
        Node originalRoot = IR.root();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        NodeTraversal t = new NodeTraversal();
        Node n = IR.root();
        Node parent = IR.root();
        checkSideEffects.visit(t, n, parent);
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        List<Node> problemNodes = Lists.newArrayList();
        checkSideEffects.protectSideEffects(problemNodes);
    }

    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        checkSideEffects.addExtern();
    }

    @Test
    public void testStripProtectionProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);

        Node externs = IR.root();
        Node root = IR.root();
        stripProtection.process(externs, root);
    }

    @Test
    public void testStripProtectionVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);

        NodeTraversal t = new NodeTraversal();
        Node n = IR.root();
        Node parent = IR.root();
        stripProtection.visit(t, n, parent);
    }
}