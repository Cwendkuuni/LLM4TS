
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Supplier;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
    private AbstractCompiler compiler;
    private RenameLabels.ProcessLabels processLabels;
    
    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        Supplier<String> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn("a", "b");
        processLabels = new RenameLabels(compiler, supplier, true).new ProcessLabels();
    }
    
    @Test 
    public void testProcessLabelsEnterScope() {
        processLabels.enterScope(mock(NodeTraversal.class));
        assertEquals(2, processLabels.namespaceStack.size());
    }
    
    @Test
    public void testProcessLabelsExitScope() {
        processLabels.enterScope(mock(NodeTraversal.class));
        processLabels.exitScope(mock(NodeTraversal.class));
        assertEquals(1, processLabels.namespaceStack.size());
    }
    
    @Test
    public void testProcessLabelsShouldTraverse() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.SCRIPT);
        node.addChildToFront(new Node(Token.NAME, "label"));
        processLabels.shouldTraverse(nodeTraversal, node, parent);
        assertEquals(1, processLabels.namespaceStack.size());
        assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
        assertEquals("a", processLabels.namespaceStack.peek().renameMap.get("label").newName);
        verify(compiler).addToDebugLog("label renamed: label => a");
    }
    
    @Test
    public void testProcessLabelsVisitLabelReferenced() {
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.SCRIPT);
        LabelInfo labelInfo = new LabelInfo(1);
        labelInfo.referenced = true;
        processLabels.namespaceStack.peek().renameMap.put("label", labelInfo);
        
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        node.addChildToFront(new Node(Token.NAME, "label"));
        processLabels.visit(nodeTraversal, node, parent);
        assertEquals("a", node.getFirstChild().getString());
        verify(compiler).reportCodeChange();
    }
    
    @Test
    public void testProcessLabelsVisitLabelUnreferenced() {
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.SCRIPT);
        LabelInfo labelInfo = new LabelInfo(1);
        labelInfo.referenced = false;
        processLabels.namespaceStack.peek().renameMap.put("label", labelInfo);
        
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        node.addChildToFront(new Node(Token.NAME, "label"));
        processLabels.visit(nodeTraversal, node, parent);
        assertNull(parent.getFirstChild());
        verify(compiler).reportCodeChange();
    }
    
    @Test
    public void testProcessLabelsVisitBreakOrContinue() {
        Node breakNode = new Node(Token.BREAK);
        Node continueNode = new Node(Token.CONTINUE);
        Node parent = new Node(Token.SCRIPT);
        LabelInfo labelInfo = new LabelInfo(1);
        labelInfo.referenced = true;
        processLabels.namespaceStack.peek().renameMap.put("label", labelInfo);
        
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        
        breakNode.addChildToFront(new Node(Token.NAME, "label"));
        continueNode.addChildToFront(new Node(Token.NAME, "label"));
        
        processLabels.visit(nodeTraversal, breakNode, parent);
        processLabels.visit(nodeTraversal, continueNode, parent);
        
        assertEquals("a", breakNode.getFirstChild().getString());
        assertEquals("a", continueNode.getFirstChild().getString());
        verify(compiler, times(2)).reportCodeChange();
    }
    
    @Test 
    public void testGetNameForId() {
        assertEquals("a", processLabels.getNameForId(1));
        assertEquals("b", processLabels.getNameForId(2));
    }
    
    @Test
    public void testGetLabelInfo() {
        LabelInfo labelInfo = new LabelInfo(1);
        processLabels.namespaceStack.peek().renameMap.put("label", labelInfo);
        assertEquals(labelInfo, processLabels.getLabelInfo("label"));
    }
}

