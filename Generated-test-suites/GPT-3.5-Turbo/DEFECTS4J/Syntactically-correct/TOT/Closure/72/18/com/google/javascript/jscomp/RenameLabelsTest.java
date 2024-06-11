
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
  
  private RenameLabels renameLabels;
  
  @Before
  public void setUp() {
    renameLabels = new RenameLabels(new AbstractCompiler());
  }
  
  @Test
  public void testEnterScope() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    
    renameLabels.enterScope(nodeTraversal);
    
    assertEquals(2, renameLabels.namespaceStack.size());
  }
  
  @Test
  public void testExitScope() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    
    renameLabels.enterScope(nodeTraversal);
    renameLabels.exitScope(nodeTraversal);
    
    assertEquals(1, renameLabels.namespaceStack.size());
  }
  
  @Test
  public void testShouldTraverse_LabelNode() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    
    renameLabels.shouldTraverse(nodeTraversal, node, parent);
    
    assertEquals(1, renameLabels.namespaceStack.peek().renameMap.size());
    assertTrue(renameLabels.namespaceStack.peek().renameMap.containsKey("a"));
    assertFalse(renameLabels.namespaceStack.peek().renameMap.get("a").referenced);
    
    assertTrue(renameLabels.names.contains("a"));
    
    // Make sure the name is generated correctly
    String newName = renameLabels.getNameForId(1);
    assertEquals("a", newName);
  }
  
  @Test
  public void testShouldTraverse_LabelNode_ExistingLabel() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    
    renameLabels.shouldTraverse(nodeTraversal, node, parent);
    
    assertEquals(1, renameLabels.namespaceStack.peek().renameMap.size());
    assertTrue(renameLabels.namespaceStack.peek().renameMap.containsKey("a"));
    assertFalse(renameLabels.namespaceStack.peek().renameMap.get("a").referenced);
    
    assertTrue(renameLabels.names.contains("a"));
    assertEquals("a", renameLabels.getNameForId(1));
  }
  
  @Test
  public void testVisit_BreakNode() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.BREAK);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("a", nameNode.getString());
    assertTrue(renameLabels.namespaceStack.peek().renameMap.get("a").referenced);
  }
  
  @Test
  public void testVisit_BreakNode_NoLabelInfo() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.BREAK);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("a", nameNode.getString());
    assertFalse(renameLabels.namespaceStack.peek().renameMap.containsKey("a"));
  }
  
  @Test
  public void testVisit_ContinueNode() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.CONTINUE);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("a", nameNode.getString());
    assertTrue(renameLabels.namespaceStack.peek().renameMap.get("a").referenced);
  }
  
  @Test
  public void testVisit_ContinueNode_NoLabelInfo() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.CONTINUE);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("a", nameNode.getString());
    assertFalse(renameLabels.namespaceStack.peek().renameMap.containsKey("a"));
  }
  
  @Test
  public void testVisit_LabelNode_Referenced() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    renameLabels.namespaceStack.peek().renameMap.get("a").referenced = true;
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("a", nameNode.getString());
    assertEquals(0, renameLabels.namespaceStack.peek().renameMap.size());
  }
  
  @Test
  public void testVisit_LabelNode_Unreferenced() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    renameLabels.namespaceStack.peek().renameMap.get("a").referenced = false;
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("a", nameNode.getString());
    assertEquals(0, renameLabels.namespaceStack.peek().renameMap.size());
  }
  
  @Test
  public void testVisit_LabelNode_Referenced_NewName() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    renameLabels.namespaceStack.peek().renameMap.get("a").referenced = true;
    
    renameLabels.names.add("b");
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals("b", nameNode.getString());
    assertEquals(0, renameLabels.namespaceStack.peek().renameMap.size());
  }
  
  @Test
  public void testVisit_LabelNode_Unreferenced_Block() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    
    Node nameNode = new Node(Token.STRING, "a");
    node.addChildToBack(nameNode);
    
    Node newChild = new Node(Token.EMPTY);
    node.addChildToBack(newChild);
    
    renameLabels.namespaceStack.peek().renameMap.put("a", new LabelInfo(1));
    renameLabels.namespaceStack.peek().renameMap.get("a").referenced = false;
    
    renameLabels.visit(nodeTraversal, node, parent);
    
    assertEquals(Token.EMPTY, parent.getLastChild().getType());
    assertEquals(0, renameLabels.namespaceStack.peek().renameMap.size());
  }

}
