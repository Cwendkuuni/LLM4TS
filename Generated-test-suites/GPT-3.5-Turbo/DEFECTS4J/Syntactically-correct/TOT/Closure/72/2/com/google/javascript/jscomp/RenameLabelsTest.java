import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  private RenameLabels renameLabels;

  @Before
  public void setup() {
    AbstractCompiler compiler = new AbstractCompiler();
    renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testEnterScope() {
    // Test that enterScope() creates a new namespace for label names
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    assertEquals(1, processLabels.namespaceStack.size());

    processLabels.enterScope(nodeTraversal);

    assertEquals(2, processLabels.namespaceStack.size());
  }

  @Test
  public void testExitScope() {
    // Test that exitScope() removes the top namespace from the stack
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    processLabels.enterScope(nodeTraversal);
    processLabels.enterScope(nodeTraversal);

    assertEquals(3, processLabels.namespaceStack.size());

    processLabels.exitScope(nodeTraversal);

    assertEquals(2, processLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverse_LabelNode() {
    // Test that shouldTraverse() correctly processes a LABEL node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node parentNode = new Node(Token.SCRIPT);
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "myLabel");

    labelNode.addChildToBack(nameNode);
    parentNode.addChildToBack(labelNode);

    assertTrue(processLabels.shouldTraverse(nodeTraversal, labelNode, parentNode));
    assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("myLabel"));
    assertFalse(processLabels.namespaceStack.peek().renameMap.get("myLabel").referenced);
  }

  @Test
  public void testShouldTraverse_NonLabelNode() {
    // Test that shouldTraverse() correctly processes a non-LABEL node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node parentNode = new Node(Token.SCRIPT);
    Node nameNode = new Node(Token.NAME, "myLabel");

    parentNode.addChildToBack(nameNode);

    assertTrue(processLabels.shouldTraverse(nodeTraversal, nameNode, parentNode));
    assertEquals(0, processLabels.namespaceStack.size());
  }

  @Test
  public void testVisit_LabelNode() {
    // Test that visit() correctly processes a LABEL node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node parentNode = new Node(Token.SCRIPT);
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "myLabel");

    labelNode.addChildToBack(nameNode);
    parentNode.addChildToBack(labelNode);

    processLabels.shouldTraverse(nodeTraversal, labelNode, parentNode);

    assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("myLabel"));

    processLabels.visit(nodeTraversal, labelNode, parentNode);

    assertFalse(processLabels.namespaceStack.peek().renameMap.containsKey("myLabel"));
  }

  @Test
  public void testVisit_LabelNode_Unreferenced() {
    // Test that visit() correctly processes an unreferenced LABEL node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node parentNode = new Node(Token.SCRIPT);
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "unreferencedLabel");
    Node statementNode = new Node(Token.EMPTY);

    labelNode.addChildToBack(nameNode);
    labelNode.addChildToBack(statementNode);
    parentNode.addChildToBack(labelNode);

    processLabels.shouldTraverse(nodeTraversal, labelNode, parentNode);

    assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("unreferencedLabel"));

    processLabels.visit(nodeTraversal, labelNode, parentNode);

    assertFalse(processLabels.namespaceStack.peek().renameMap.containsKey("unreferencedLabel"));
    assertEquals(statementNode, parentNode.getFirstChild());
    assertEquals(labelNode, parentNode.getSecondChild());
  }

  @Test
  public void testVisit_LabelNode_Referenced() {
    // Test that visit() correctly processes a referenced LABEL node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node parentNode = new Node(Token.SCRIPT);
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "referencedLabel");
    Node statementNode = new Node(Token.EMPTY);

    labelNode.addChildToBack(nameNode);
    labelNode.addChildToBack(statementNode);
    parentNode.addChildToBack(labelNode);

    processLabels.shouldTraverse(nodeTraversal, labelNode, parentNode);
    processLabels.visit(nodeTraversal, labelNode, parentNode);

    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("referencedLabel"));
    assertTrue(processLabels.namespaceStack.peek().renameMap.get("referencedLabel").referenced);
    assertEquals(statementNode, parentNode.getFirstChild());
    assertEquals(labelNode, parentNode.getSecondChild());

    Node newParentNode = new Node(Token.SCRIPT);
    Node newLabelNode = new Node(Token.LABEL);
    Node newNameNode = new Node(Token.NAME, "newLabel");

    newLabelNode.addChildToBack(newNameNode);
    newParentNode.addChildToBack(newLabelNode);

    processLabels.shouldTraverse(nodeTraversal, newLabelNode, newParentNode);
    processLabels.visit(nodeTraversal, newLabelNode, newParentNode);

    assertFalse(processLabels.namespaceStack.peek().renameMap.containsKey("newLabel"));
  }

  @Test
  public void testVisit_BreakOrContinueNode_Unnamed() {
    // Test that visit() correctly processes an unnamed BREAK or CONTINUE node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node breakNode = new Node(Token.BREAK);

    processLabels.visitBreakOrContinue(breakNode);

    assertNull(breakNode.getFirstChild());
  }

  @Test
  public void testVisit_BreakOrContinueNode_Named_Unreferenced() {
    // Test that visit() correctly processes an unreferenced named BREAK or CONTINUE node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "unreferencedLabel");
    breakNode.addChildToBack(nameNode);

    processLabels.shouldTraverse(nodeTraversal, breakNode, null);
    processLabels.visitBreakOrContinue(breakNode);

    assertFalse(processLabels.namespaceStack.peek().renameMap.containsKey("unreferencedLabel"));
    assertNull(breakNode.getFirstChild());
  }

  @Test
  public void testVisit_BreakOrContinueNode_Named_Referenced() {
    // Test that visit() correctly processes a referenced named BREAK or CONTINUE node
    NodeTraversal nodeTraversal = new NodeTraversal();
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "referencedLabel");
    breakNode.addChildToBack(nameNode);

    processLabels.shouldTraverse(nodeTraversal, breakNode, null);
    processLabels.visitBreakOrContinue(breakNode);

    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("referencedLabel"));
    assertTrue(processLabels.namespaceStack.peek().renameMap.get("referencedLabel").referenced);
    assertNotEquals("referencedLabel", nameNode.getString());
  }

  @Test
  public void testGetANameForId() {
    // Test that getNameForId() returns the correct short name for a given id
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    assertEquals("a", processLabels.getNameForId(1));
    assertEquals("b", processLabels.getNameForId(2));
    assertEquals("c", processLabels.getNameForId(3));
  }

  @Test
  public void testGetLabelInfo() {
    // Test that getLabelInfo() returns the correct LabelInfo object for a given label name
    renameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    assertNull(processLabels.getLabelInfo("myLabel"));

    processLabels.shouldTraverse(null, createLabelNode("myLabel"), null);

    assertNotNull(processLabels.getLabelInfo("myLabel"));
    assertFalse(processLabels.getLabelInfo("myLabel").referenced);
  }

  private Node createLabelNode(String label) {
    Node labelNode = new Node(Token.LABEL);
    labelNode.addChildToBack(new Node(Token.NAME, label));
    return labelNode;
  }
}