import static org.junit.Assert.*;
import org.junit.Test;

public class RenameLabelsTest {
  
  // Test that the label name is renamed correctly
  @Test
  public void testLabelRename() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Create test node with label
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "label");
    labelNode.addChildToFront(nameNode);
    
    // Call visitLabel to rename the label
    renameLabels.visitLabel(labelNode, null);
    
    // Check that the label name is renamed correctly
    assertEquals("a", labelNode.getFirstChild().getString());
  }
  
  // Test that label names are correctly removed when not referenced
  @Test
  public void testRemoveLabel() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Create test node with label
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "label");
    labelNode.addChildToFront(nameNode);
    
    // Call visitLabel to remove the label
    renameLabels.visitLabel(labelNode, null);
    
    // Check that the label node is removed
    assertNull(labelNode.getParent());
  }
  
  // Test that named breaks are renamed correctly
  @Test
  public void testNamedBreakRename() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Create test node with named break
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "label");
    breakNode.addChildToFront(nameNode);
    
    // Add label information to renameLabels namespace
    renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());
    renameLabels.namespaceStack.peek().renameMap.put("label", renameLabels.new LabelInfo(1));
    
    // Call visitBreakOrContinue to rename the named break
    renameLabels.visitBreakOrContinue(breakNode);
    
    // Check that the named break is renamed correctly
    assertEquals("a", breakNode.getFirstChild().getString());
  }
  
  // Test that named continues are renamed correctly
  @Test
  public void testNamedContinueRename() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Create test node with named continue
    Node continueNode = new Node(Token.CONTINUE);
    Node nameNode = new Node(Token.NAME, "label");
    continueNode.addChildToFront(nameNode);
    
    // Add label information to renameLabels namespace
    renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());
    renameLabels.namespaceStack.peek().renameMap.put("label", renameLabels.new LabelInfo(1));
    
    // Call visitBreakOrContinue to rename the named continue
    renameLabels.visitBreakOrContinue(continueNode);
    
    // Check that the named continue is renamed correctly
    assertEquals("a", continueNode.getFirstChild().getString());
  }
  
  // Test that label references are correctly marked as referenced
  @Test
  public void testMarkLabelAsReferenced() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Create test node with label
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "label");
    labelNode.addChildToFront(nameNode);
    
    // Add label information to renameLabels namespace
    renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());
    renameLabels.namespaceStack.peek().renameMap.put("label", renameLabels.new LabelInfo(1));
    
    // Call visitLabel to mark the label as referenced
    renameLabels.visitLabel(labelNode, null);
    
    // Check that the label is marked as referenced
    assertTrue(renameLabels.getLabelInfo("label").referenced);
  }
  
  // Test that indices in getNameForId are correct
  @Test
  public void testGetNameForId() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Add label names to renameLabels names list
    renameLabels.names.add("a");
    renameLabels.names.add("b");
    renameLabels.names.add("c");
    
    // Check indices for different ids
    assertEquals("a", renameLabels.getNameForId(1));
    assertEquals("b", renameLabels.getNameForId(2));
    assertEquals("c", renameLabels.getNameForId(3));
  }
  
  // Test that label info is correctly retrieved for a given name
  @Test
  public void testGetLabelInfo() {
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    
    // Create label info and add to renameLabels namespace
    renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());
    LabelInfo labelInfo = renameLabels.new LabelInfo(1);
    renameLabels.namespaceStack.peek().renameMap.put("label", labelInfo);
    
    // Check that the correct label info is retrieved
    assertEquals(labelInfo, renameLabels.getLabelInfo("label"));
  }

}