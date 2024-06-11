import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
    
    private AbstractCompiler compiler;
    private RenameLabels renameLabels;
    
    @Before
    public void setUp() {
        compiler = new Compiler();
        renameLabels = new RenameLabels(compiler);
    }
    
    @Test
    public void testEnterScope() {
        // Test that a new namespace is created when entering a scope
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        Assert.assertTrue(processLabels.namespaceStack.isEmpty());
        
        processLabels.enterScope(null);
        Assert.assertFalse(processLabels.namespaceStack.isEmpty());
    }
    
    @Test
    public void testExitScope() {
        // Test that the current namespace is popped when exiting a scope
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope(null);
        Assert.assertFalse(processLabels.namespaceStack.isEmpty());
        
        processLabels.exitScope(null);
        Assert.assertTrue(processLabels.namespaceStack.isEmpty());
    }
    
    @Test
    public void testShouldTraverse_ExistingLabel() {
        // Test that a new name is generated and stored for an existing label
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope(null);
        
        Node labelNode = new Node(Token.LABEL);
        Node labelName = new Node(Token.NAME, "myLabel");
        labelNode.addChildToFront(labelName);
        processLabels.shouldTraverse(null, labelNode, null);
        
        Assert.assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("myLabel"));
    }
    
    @Test
    public void testShouldTraverse_NewLabel() {
        // Test that a new name is generated and stored for a new label
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        
        Node labelNode = new Node(Token.LABEL);
        Node labelName = new Node(Token.NAME, "newLabel");
        labelNode.addChildToFront(labelName);
        processLabels.shouldTraverse(null, labelNode, null);

        Assert.assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("newLabel"));
        Assert.assertEquals("a", processLabels.namespaceStack.peek().renameMap.get("newLabel").newName);
    }
    
    @Test
    public void testVisitBreakOrContinue_NamedBreak() {
        // Test that a named break is renamed correctly
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope(null);
        processLabels.names.add("a");
        
        Node breakNode = new Node(Token.BREAK);
        Node labelName = new Node(Token.NAME, "myLabel");
        breakNode.addChildToFront(labelName);
        processLabels.visitBreakOrContinue(breakNode);
        
        Assert.assertEquals("a", breakNode.getFirstChild().getString());
    }
    
    @Test
    public void testVisitBreakOrContinue_UnnamedBreak() {
        // Test that an unnamed break is not renamed
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        
        Node breakNode = new Node(Token.BREAK);
        processLabels.visitBreakOrContinue(breakNode);
        
        Assert.assertTrue(breakNode.getFirstChild().isNull());
    }
    
    @Test
    public void testVisitBreakOrContinue_NamedContinue() {
        // Test that a named continue is renamed correctly
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope(null);
        processLabels.names.add("b");
        
        Node continueNode = new Node(Token.CONTINUE);
        Node labelName = new Node(Token.NAME, "otherLabel");
        continueNode.addChildToFront(labelName);
        processLabels.visitBreakOrContinue(continueNode);
        
        Assert.assertEquals("b", continueNode.getFirstChild().getString());
    }
    
    @Test
    public void testVisitBreakOrContinue_UnnamedContinue() {
        // Test that an unnamed continue is not renamed
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        
        Node continueNode = new Node(Token.CONTINUE);
        processLabels.visitBreakOrContinue(continueNode);
        
        Assert.assertTrue(continueNode.getFirstChild().isNull());
    }
    
    @Test
    public void testVisitLabel_ReferencedLabel() {
        // Test that a referenced label is renamed correctly
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        
        Node labelNode = new Node(Token.LABEL);
        Node labelName = new Node(Token.NAME, "myLabel");
        labelNode.addChildToFront(labelName);
        processLabels.namespaceStack.peek().renameMap.put("myLabel", true);
        processLabels.visitLabel(labelNode, null);
        
        Assert.assertEquals("a", labelNode.getFirstChild().getString());
    }
    
    @Test
    public void testVisitLabel_UnreferencedLabel() {
        // Test that an unreferenced label is removed
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        
        Node labelNode = new Node(Token.LABEL);
        Node labelName = new Node(Token.NAME, "unusedLabel");
        labelNode.addChildToFront(labelName);
        processLabels.visitLabel(labelNode, null);
        
        Assert.assertEquals(labelNode.getLastChild(), labelNode.getParent());
    }
}