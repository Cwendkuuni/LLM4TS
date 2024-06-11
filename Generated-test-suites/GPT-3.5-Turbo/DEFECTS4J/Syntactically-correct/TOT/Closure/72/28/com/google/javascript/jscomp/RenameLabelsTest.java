import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  @Test
  public void testEnterScope() {
    // Test when enterScope is called, a new Namespace is pushed onto the namespaceStack
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    int initialStackSize = processLabels.namespaceStack.size();
    NodeTraversal nodeTraversal = new NodeTraversal();
    processLabels.enterScope(nodeTraversal);
    int newStackSize = processLabels.namespaceStack.size();
    assertEquals(initialStackSize + 1, newStackSize);
  }

  @Test
  public void testExitScope() {
    // Test when exitScope is called, a Namespace is popped from the namespaceStack
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal();
    processLabels.enterScope(nodeTraversal);
    int initialStackSize = processLabels.namespaceStack.size();
    processLabels.exitScope(nodeTraversal);
    int newStackSize = processLabels.namespaceStack.size();
    assertEquals(initialStackSize - 1, newStackSize);
  }

  @Test
  public void testShouldTraverse_LabelNode() {
    // Test when shouldTraverse is called with a Label node, the renameMap is updated correctly
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    int initialRenameMapSize = processLabels.namespaceStack.peek().renameMap.size();
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "labelName");
    labelNode.addChildToFront(nameNode);
    Node parent = new Node();
    processLabels.shouldTraverse(nodeTraversal, labelNode, parent);
    int newRenameMapSize = processLabels.namespaceStack.peek().renameMap.size();
    assertTrue(newRenameMapSize > initialRenameMapSize);
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
  }

  @Test
  public void testShouldTraverse_NotLabelNode() {
    // Test when shouldTraverse is called with a non-Label node, the renameMap is not updated
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    int initialRenameMapSize = processLabels.namespaceStack.peek().renameMap.size();
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.GETPROP);
    Node parent = new Node();
    processLabels.shouldTraverse(nodeTraversal, node, parent);
    int newRenameMapSize = processLabels.namespaceStack.peek().renameMap.size();
    assertEquals(initialRenameMapSize, newRenameMapSize);
  }

  @Test
  public void testVisit_BreakNodeWithReferencedLabel() {
    // Test when visit is called with a Break node that has a referenced label, the label name is renamed
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    processLabels.names.add("c");
    processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(2));
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node breakNode = new Node(Token.BREAK);
    Node labelNameNode = new Node(Token.NAME, "labelName");
    breakNode.addChildToFront(labelNameNode);
    processLabels.visit(nodeTraversal, breakNode, null);
    assertEquals("b", labelNameNode.getString());
  }

  @Test
  public void testVisit_BreakNodeWithUnreferencedLabel() {
    // Test when visit is called with a Break node that has an unreferenced label, the label is removed
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node breakNode = new Node(Token.BREAK);
    Node labelNameNode = new Node(Token.NAME, "labelName");
    breakNode.addChildToFront(labelNameNode);
    processLabels.visit(nodeTraversal, breakNode, null);
    assertNull(breakNode.getFirstChild());
  }

  @Test
  public void testVisit_ContinueNodeWithReferencedLabel() {
    // Test when visit is called with a Continue node that has a referenced label, the label name is renamed
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    processLabels.names.add("c");
    processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(3));
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node continueNode = new Node(Token.CONTINUE);
    Node labelNameNode = new Node(Token.NAME, "labelName");
    continueNode.addChildToFront(labelNameNode);
    processLabels.visit(nodeTraversal, continueNode, null);
    assertEquals("c", labelNameNode.getString());
  }

  @Test
  public void testVisit_ContinueNodeWithUnreferencedLabel() {
    // Test when visit is called with a Continue node that has an unreferenced label, the label is removed
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node continueNode = new Node(Token.CONTINUE);
    Node labelNameNode = new Node(Token.NAME, "labelName");
    continueNode.addChildToFront(labelNameNode);
    processLabels.visit(nodeTraversal, continueNode, null);
    assertNull(continueNode.getFirstChild());
  }

  @Test
  public void testVisitLabel_ReferencedLabel() {
    // Test when visitLabel is called with a labeled statement that is referenced, the label name is renamed
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    processLabels.names.add("c");
    processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(2));
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "labelName");
    labelNode.addChildToFront(nameNode);
    Node statementNode = new Node(Token.LET);
    labelNode.addChildToBack(statementNode);
    processLabels.visitLabel(labelNode, null);
    assertEquals("b", nameNode.getString());
  }

  @Test
  public void testVisitLabel_UnreferencedLabel() {
    // Test when visitLabel is called with a labeled statement that is not referenced, the label is removed
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "labelName");
    labelNode.addChildToFront(nameNode);
    Node statementNode = new Node(Token.LET);
    labelNode.addChildToBack(statementNode);
    processLabels.visitLabel(labelNode, null);
    assertNull(labelNode.getFirstChild());
  }

  @Test
  public void testGetNameForId() {
    // Test that getNameForId returns the correct short name for a given id
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    processLabels.names.add("c");
    assertEquals("a", processLabels.getNameForId(1));
    assertEquals("b", processLabels.getNameForId(2));
    assertEquals("c", processLabels.getNameForId(3));
  }

  @Test
  public void testGetLabelInfo() {
    // Test that getLabelInfo returns the correct LabelInfo for a given label name
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    RenameLabels.LabelInfo labelInfo = new RenameLabels.LabelInfo(1);
    processLabels.namespaceStack.peek().renameMap.put("labelName", labelInfo);
    assertEquals(labelInfo, processLabels.getLabelInfo("labelName"));
  }
}