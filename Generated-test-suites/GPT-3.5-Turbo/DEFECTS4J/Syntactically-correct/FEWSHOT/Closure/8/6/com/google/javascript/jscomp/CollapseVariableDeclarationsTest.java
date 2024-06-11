package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        cvd.process(externs, root);
        // Add assertions here
    }

    @Test
    public void testGatherCollapses_visit() {
        AbstractCompiler compiler = new Compiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.ROOT);
        cvd.new GatherCollapses().visit(t, n, parent);
        // Add assertions here
    }

    @Test
    public void testGatherCollapses_blacklistStubVars() {
        AbstractCompiler compiler = new Compiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node varNode = new Node(Token.VAR);
        cvd.new GatherCollapses().blacklistStubVars(t, varNode);
        // Add assertions here
    }

    @Test
    public void testGatherCollapses_canBeRedeclared() {
        AbstractCompiler compiler = new Compiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.VAR);
        Scope s = new Scope();
        assertFalse(cvd.new GatherCollapses().canBeRedeclared(n, s));
    }

    @Test
    public void testApplyCollapses() {
        AbstractCompiler compiler = new Compiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        cvd.applyCollapses();
        // Add assertions here
    }

}