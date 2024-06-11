import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

  private RenameLabels renameLabels;
  
  @Before
  public void setUp() {
    renameLabels = new RenameLabels(null);
  }

  @Test
  public void testProcess() {
    // Arrange
    
    // Act
    renameLabels.process(null, null);
    
    // Assert
    // Add assertions here to verify the correctness of the process() method
  }
  
  @Test
  public void testDefaultNameSupplier() {
    // Arrange
    RenameLabels.DefaultNameSupplier nameSupplier = renameLabels.new DefaultNameSupplier();
    
    // Act
    String name1 = nameSupplier.get();
    String name2 = nameSupplier.get();
    
    // Assert
    assertEquals("a", name1);
    assertEquals("b", name2);
  }
  
  @Test
  public void testProcessLabels_enterScope() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    
    // Act
    processLabels.enterScope(null);
    
    // Assert
    assertEquals(1, processLabels.namespaceStack.size());
  }
  
  @Test
  public void testProcessLabels_exitScope() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    
    // Act
    processLabels.exitScope(null);
    
    // Assert
    assertTrue(processLabels.namespaceStack.isEmpty());
  }
  
  @Test
  public void testProcessLabels_shouldTraverse_labelNode() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("labelName");
    node.addChildToFront(nameNode);
    
    // Act
    boolean result = processLabels.shouldTraverse(null, node, null);
    
    // Assert
    assertTrue(result);
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
  }
  
  @Test
  public void testProcessLabels_shouldTraverse_nonLabelNode() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node = new Node(Token.LET, new Node(Token.NAME));
    
    // Act
    boolean result = processLabels.shouldTraverse(null, node, null);
    
    // Assert
    assertTrue(result);
    assertTrue(processLabels.namespaceStack.isEmpty());
  }
  
  @Test
  public void testProcessLabels_shouldTraverse_existingLabelName() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node1 = new Node(Token.LABEL);
    Node nameNode1 = new Node(Token.NAME);
    nameNode1.setString("labelName");
    node1.addChildToFront(nameNode1);
    Node node2 = new Node(Token.LABEL);
    Node nameNode2 = new Node(Token.NAME);
    nameNode2.setString("labelName");
    node2.addChildToFront(nameNode2);
    
    // Act
    boolean result1 = processLabels.shouldTraverse(null, node1, null);
    boolean result2 = processLabels.shouldTraverse(null, node2, null);
    
    // Assert
    assertTrue(result1);
    assertFalse(result2);
  }
  
  @Test
  public void testProcessLabels_visit_labelNode_referencedLabel() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node = new Node(Token.LABEL);
    Node nameNode1 = new Node(Token.NAME);
    nameNode1.setString("labelName");
    node.addChildToFront(nameNode1);
    Node nameNode2 = new Node(Token.NAME);
    nameNode2.setString("newLabelName");
    node.addChildToBack(nameNode2);
    processLabels.namespaceStack.peek().renameMap.put("labelName", 
      renameLabels.new LabelInfo(1));
    
    // Act
    processLabels.visit(null, node, null);
    
    // Assert
    assertEquals("newLabelName", nameNode1.getString());
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }
  
  @Test
  public void testProcessLabels_visit_labelNode_unreferencedLabel() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("labelName");
    node.addChildToFront(nameNode);
    processLabels.namespaceStack.peek().renameMap.put("labelName", 
      renameLabels.new LabelInfo(1));
    
    // Act
    processLabels.visit(null, node, null);
    
    // Assert
    assertEquals(0, node.getChildCount());
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }
  
  @Test
  public void testProcessLabels_visit_breakOrContinueNode() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node = new Node(Token.BREAK);
    Node nameNode1 = new Node(Token.NAME);
    nameNode1.setString("labelName");
    node.addChildToFront(nameNode1);
    Node nameNode2 = new Node(Token.NAME);
    nameNode2.setString("newLabelName");
    node.addChildToBack(nameNode2);
    processLabels.namespaceStack.peek().renameMap.put("labelName", 
      renameLabels.new LabelInfo(1));
    
    // Act
    processLabels.visit(null, node, null);
    
    // Assert
    assertEquals("newLabelName", nameNode1.getString());
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }
  
  @Test
  public void testProcessLabels_visit_breakOrContinueNode_unnamed() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node node = new Node(Token.BREAK, new Node(Token.NAME));
    
    // Act
    processLabels.visit(null, node, null);
    
    // Assert
    assertEquals(0, node.getChildCount());
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }
  
  @Test
  public void testProcessLabels_getNameForId() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    processLabels.names.add("c");
    
    // Act
    String name1 = processLabels.getNameForId(1);
    String name2 = processLabels.getNameForId(2);
    String name3 = processLabels.getNameForId(3);
    
    // Assert
    assertEquals("a", name1);
    assertEquals("b", name2);
    assertEquals("c", name3);
  }
  
  @Test
  public void testProcessLabels_getLabelInfo() {
    // Arrange
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    processLabels.namespaceStack.peek().renameMap.put("labelName", 
      renameLabels.new LabelInfo(1));
    
    // Act
    RenameLabels.LabelInfo labelInfo = processLabels.getLabelInfo("labelName");
    
    // Assert
    assertEquals(1, labelInfo.id);
  }
}