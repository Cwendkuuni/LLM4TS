package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
    }

    @Test
    public void testConstructor() {
        RenameLabels renameLabels = new RenameLabels(compiler);
        assertTrue(renameLabels instanceof RenameLabels);
    }

    @Test
    public void testProcess() {
        RenameLabels renameLabels = new RenameLabels(compiler);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        renameLabels.process(externs, root);
    }

    @Test
    public void testProcessWithSupplier() {
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        renameLabels.process(externs, root);
    }

    @Test
    public void testProcessWithNameSupplier() {
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        renameLabels.process(externs, root);
    }

    @Test
    public void testGetNameForId() {
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        assertEquals("a", renameLabels.new ProcessLabels().getNameForId(1));
        assertEquals("b", renameLabels.new ProcessLabels().getNameForId(2));
        assertEquals("c", renameLabels.new ProcessLabels().getNameForId(3));
    }

    @Test
    public void testGetLabelInfo() {
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        renameLabels.new ProcessLabels().getLabelInfo("name");
    }

    @Test
    public void testVisitBreakOrContinue() {
        Node node = new Node(Token.BREAK);
        node.addChildToBack(new Node(Token.NAME, "name"));
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        renameLabels.new ProcessLabels().visitBreakOrContinue(node);
    }

    @Test
    public void testVisitLabel() {
        Node parent = new Node(Token.ROOT);
        Node node = new Node(Token.LABEL);
        node.addChildToBack(new Node(Token.NAME, "name"));
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        renameLabels.new ProcessLabels().visitLabel(node, parent);
    }
}