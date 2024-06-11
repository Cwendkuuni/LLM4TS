package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class RenameLabelsTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        Node externs = new Node(Token.BLANK);
        Node root = new Node(Token.SCRIPT);
        
        renameLabels.process(externs, root);
        
        // Add test assertions here
        
        compiler.checkForErrors();
    }
    
    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        
        renameLabels.enterScope(nodeTraversal);
        
        // Add test assertions here
    }
    
    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        
        renameLabels.exitScope(nodeTraversal);
        
        // Add test assertions here
    }
    
    @Test
    public void testShouldTraverse_withLabelNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node node = new Node(Token.LABEL);
        node.addChildToFront(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        
        boolean result = renameLabels.shouldTraverse(nodeTraversal, node, parent);
        
        // Add test assertions here
    }
    
    @Test
    public void testShouldTraverse_withoutLabelNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        
        boolean result = renameLabels.shouldTraverse(nodeTraversal, node, parent);
        
        // Add test assertions here
    }
    
    @Test
    public void testVisit_withLabelNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node node = new Node(Token.LABEL);
        node.addChildToFront(new Node(Token.NAME));
        Node parent = new Node(Token.SCRIPT);
        
        renameLabels.visit(nodeTraversal, node, parent);
        
        // Add test assertions here
    }
    
    @Test
    public void testVisit_withBreakNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node node = new Node(Token.BREAK);
        node.addChildToFront(new Node(Token.NAME));
        
        renameLabels.visit(nodeTraversal, node, null);
        
        // Add test assertions here
    }
    
    @Test
    public void testVisit_withContinueNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
        Node node = new Node(Token.CONTINUE);
        node.addChildToFront(new Node(Token.NAME));
        
        renameLabels.visit(nodeTraversal, node, null);
        
        // Add test assertions here
    }
    
    @Test
    public void testGetNameForId() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        
        String result1 = processLabels.getNameForId(1);
        String result2 = processLabels.getNameForId(2);
        
        assertEquals("a", result1);
        assertEquals("b", result2);
    }
    
    @Test
    public void testGetLabelInfo_withExistingName() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        LabelNamespace namespace = renameLabels.new LabelNamespace();
        processLabels.namespaceStack.push(namespace);
        namespace.renameMap.put("label1", new LabelInfo(1));
        
        LabelInfo result = processLabels.getLabelInfo("label1");
        
        assertSame(namespace.renameMap.get("label1"), result);
    }
    
    @Test
    public void testGetLabelInfo_withNonExistingName() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> supplier = new DefaultNameSupplier();
        RenameLabels renameLabels = new RenameLabels(compiler, supplier, true);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        LabelNamespace namespace = renameLabels.new LabelNamespace();
        processLabels.namespaceStack.push(namespace);
        namespace.renameMap.put("label1", new LabelInfo(1));
        
        LabelInfo result = processLabels.getLabelInfo("label2");
        
        assertEquals(null, result);
    }
    
    @Test
    public void testGetName() {
        RenameLabels.DefaultNameSupplier supplier = new RenameLabels.DefaultNameSupplier();
        String name1 = supplier.get();
        String name2 = supplier.get();
        
        assertEquals("a", name1);
        assertEquals("b", name2);
    }
}