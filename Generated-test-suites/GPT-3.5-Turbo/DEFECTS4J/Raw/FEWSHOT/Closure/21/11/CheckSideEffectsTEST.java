
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        checkSideEffects.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node scriptRoot = new Node(Token.SCRIPT);

        NodeTraversal.traverse(compiler, scriptRoot, checkSideEffects);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        NodeTraversal t = new NodeTraversal(compiler, checkSideEffects);
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.EXPR_RESULT);

        checkSideEffects.visit(t, n, parent);
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        List<Node> problemNodes = new ArrayList<>();

        Node node = new Node(Token.STRING);
        problemNodes.add(node);

        checkSideEffects.problemNodes.addAll(problemNodes);

        checkSideEffects.protectSideEffects();
    }

    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new Compiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;

        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        checkSideEffects.addExtern();
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new Compiler();

        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);

        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        stripProtection.process(externs, root);
    }

}
