package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    @Test
    public void testProcess() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(new AbstractCompiler());
        Node externs = null;
        Node root = null;
        collapse.process(externs, root);
    }
    
    @Test
    public void testGatherCollapses() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(new AbstractCompiler());
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        collapse.new GatherCollapses().visit(t, n, parent);
    }
    
    @Test
    public void testBlacklistStubVars() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(new AbstractCompiler());
        NodeTraversal t = null;
        Node varNode = null;
        collapse.new GatherCollapses().blacklistStubVars(t, varNode);
    }
    
    @Test
    public void testCanBeRedeclared() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(new AbstractCompiler());
        Node n = null;
        Scope s = null;
        collapse.new GatherCollapses().canBeRedeclared(n, s);
    }
    
    @Test
    public void testApplyCollapses() {
        CollapseVariableDeclarations collapse = new CollapseVariableDeclarations(new AbstractCompiler());
        collapse.applyCollapses();
    }
}