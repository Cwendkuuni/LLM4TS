package com.google.javascript.jscomp;

import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();

        renameLabels.enterScope(nodeTraversal);
    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();

        renameLabels.exitScope(nodeTraversal);
    }

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();

        renameLabels.shouldTraverse(nodeTraversal, node, parent);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();

        renameLabels.visit(nodeTraversal, node, parent);
    }

    @Test
    public void testVisitBreakOrContinue() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        Node node = new Node();

        renameLabels.visitBreakOrContinue(node);
    }

    @Test
    public void testVisitLabel() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        Node node = new Node();
        Node parent = new Node();

        renameLabels.visitLabel(node, parent);
    }

    @Test
    public void testGetNameForId() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        int id = 1;
        renameLabels.getNameForId(id);
    }

    @Test
    public void testGetLabelInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String name = "label";
        renameLabels.getLabelInfo(name);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        Node externs = new Node();
        Node root = new Node();

        renameLabels.process(externs, root);
    }
}