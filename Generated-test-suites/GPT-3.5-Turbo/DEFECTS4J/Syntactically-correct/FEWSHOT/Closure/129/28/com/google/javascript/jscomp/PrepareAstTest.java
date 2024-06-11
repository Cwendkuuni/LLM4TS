package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        PrepareAst pa = new PrepareAst(compiler);
        Node externs = new Node();
        Node root = new Node();
        pa.process(externs, root);
    }

    @Test
    public void testNormalizeNodeTypes() {
        AbstractCompiler compiler = new AbstractCompiler();
        PrepareAst pa = new PrepareAst(compiler);
        Node n = new Node();
        pa.normalizeNodeTypes(n);
    }

    @Test
    public void testNormalizeBlocks() {
        AbstractCompiler compiler = new AbstractCompiler();
        PrepareAst pa = new PrepareAst(compiler);
        Node n = new Node();
        pa.normalizeBlocks(n);
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations pa = new PrepareAst.PrepareAnnotations();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        pa.shouldTraverse(t, n, parent);
        pa.visit(t, n, parent);
    }
}