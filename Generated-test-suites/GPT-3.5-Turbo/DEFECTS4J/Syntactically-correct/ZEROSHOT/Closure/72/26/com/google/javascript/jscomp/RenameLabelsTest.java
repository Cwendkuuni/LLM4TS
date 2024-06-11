import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testEnterScope() {
    // Test if enterScope method works correctly
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Call the enterScope method
    renameLabels.enterScope(nodeTraversal);
    // Verify if the namespaceStack has been updated correctly
    assertEquals(2, renameLabels.namespaceStack.size());
  }

  @Test
  public void testExitScope() {
    // Test if exitScope method works correctly
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Call the enterScope method to add a namespace to the stack
    renameLabels.enterScope(nodeTraversal);
    // Call the exitScope method
    renameLabels.exitScope(nodeTraversal);
    // Verify if the namespaceStack has been updated correctly
    assertEquals(1, renameLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverse_withLabelNode() {
    // Test if shouldTraverse method works correctly with a label node
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Create a label node
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("label");
    labelNode.addChildToBack(nameNode);
    // Call the shouldTraverse method
    boolean result = renameLabels.shouldTraverse(nodeTraversal, labelNode, null);
    // Verify if the shouldTraverse method returns true
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_withoutLabelNode() {
    // Test if shouldTraverse method works correctly without a label node
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Create a non-label node
    Node node = new Node(Token.EXPR_RESULT);
    // Call the shouldTraverse method
    boolean result = renameLabels.shouldTraverse(nodeTraversal, node, null);
    // Verify if the shouldTraverse method returns true
    assertTrue(result);
  }

  @Test
  public void testVisitLabel_withReferencedLabel() {
    // Test if visitLabel method works correctly with a referenced label
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Create a label node
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("label");
    labelNode.addChildToBack(nameNode);
    // Add the label to the renameMap
    LabelNamespace current = renameLabels.namespaceStack.peek();
    LabelInfo labelInfo = new LabelInfo(1);
    labelInfo.referenced = true;
    current.renameMap.put("label", labelInfo);
    // Call the visitLabel method
    renameLabels.visitLabel(labelNode, null);
    // Verify if the label node's name has been updated
    assertEquals("a", nameNode.getString());
  }

  @Test
  public void testVisitLabel_withUnreferencedLabel() {
    // Test if visitLabel method works correctly with an unreferenced label
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Create a label node
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("label");
    labelNode.addChildToBack(nameNode);
    // Add the label to the renameMap
    LabelNamespace current = renameLabels.namespaceStack.peek();
    LabelInfo labelInfo = new LabelInfo(1);
    labelInfo.referenced = false;
    current.renameMap.put("label", labelInfo);
    // Call the visitLabel method
    renameLabels.visitLabel(labelNode, null);
    // Verify if the label node has been removed
    assertFalse(labelNode.hasChildren());
  }

  @Test
  public void testVisitBreakOrContinue_withReferencedLabel() {
    // Test if visitBreakOrContinue method works correctly with a referenced label
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Create a break node
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("label");
    breakNode.addChildToFront(nameNode);
    // Add the label to the renameMap
    LabelNamespace current = renameLabels.namespaceStack.peek();
    LabelInfo labelInfo = new LabelInfo(1);
    labelInfo.referenced = true;
    current.renameMap.put("label", labelInfo);
    // Call the visitBreakOrContinue method
    renameLabels.visitBreakOrContinue(breakNode);
    // Verify if the break node's name has been updated
    assertEquals("a", nameNode.getString());
  }

  @Test
  public void testVisitBreakOrContinue_withUnreferencedLabel() {
    // Test if visitBreakOrContinue method works correctly with an unreferenced label
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a NodeTraversal object
    NodeTraversal nodeTraversal = new NodeTraversal();
    // Create a break node
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("label");
    breakNode.addChildToFront(nameNode);
    // Add the label to the renameMap
    LabelNamespace current = renameLabels.namespaceStack.peek();
    LabelInfo labelInfo = new LabelInfo(1);
    labelInfo.referenced = false;
    current.renameMap.put("label", labelInfo);
    // Call the visitBreakOrContinue method
    renameLabels.visitBreakOrContinue(breakNode);
    // Verify if the break node's name has not been updated
    assertEquals("label", nameNode.getString());
  }

  @Test
  public void testGetNameForId() {
    // Test if getNameForId method returns the correct name for the given id
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Add a name to the names list
    renameLabels.names.add("a");
    // Call the getNameForId method with id=1
    String result = renameLabels.getNameForId(1);
    // Verify if the result is "a"
    assertEquals("a", result);
  }

  @Test
  public void testGetLabelInfo_withExistingLabel() {
    // Test if getLabelInfo method returns the correct LabelInfo for an existing label
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Create a label node
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("label");
    labelNode.addChildToBack(nameNode);
    // Add the label to the renameMap
    LabelNamespace current = renameLabels.namespaceStack.peek();
    LabelInfo labelInfo = new LabelInfo(1);
    labelInfo.referenced = true;
    current.renameMap.put("label", labelInfo);
    // Call the getLabelInfo method
    LabelInfo result = renameLabels.getLabelInfo("label");
    // Verify if the result is the same as the labelInfo object
    assertEquals(labelInfo, result);
  }

  @Test
  public void testGetLabelInfo_withNonexistingLabel() {
    // Test if getLabelInfo method returns null for a nonexisting label
    // Create a RenameLabels object
    RenameLabels renameLabels = new RenameLabels(new AbstractCompiler());
    // Call the getLabelInfo method
    LabelInfo result = renameLabels.getLabelInfo("label");
    // Verify if the result is null
    assertNull(result);
  }

}