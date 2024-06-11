import static org.junit.Assert.*;
import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testProcessLabels() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    assertNotNull(processLabels);
  }

  @Test
  public void testEnterScope() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    processLabels.enterScope(nodeTraversal);
    assertFalse(processLabels.namespaceStack.isEmpty());
  }

  @Test
  public void testExitScope() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    processLabels.enterScope(nodeTraversal);
    assertFalse(processLabels.namespaceStack.isEmpty());
    processLabels.exitScope(nodeTraversal);
    assertTrue(processLabels.namespaceStack.isEmpty());
  }

  @Test
  public void testShouldTraverseLabel() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    Node node = new Node(Token.LABEL);
    node.addChildToFront(new Node(Token.NAME, "label1"));
    boolean traverse = processLabels.shouldTraverse(null, node, null);
    assertTrue(traverse);
  }

  @Test
  public void testShouldTraverseNotLabel() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    Node node = new Node(Token.ASSIGN);
    boolean traverse = processLabels.shouldTraverse(null, node, null);
    assertFalse(traverse);
  }

  @Test
  public void testVisitLabel() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    Node node = new Node(Token.LABEL);
    node.addChildToFront(new Node(Token.NAME, "label1"));
    Node parent = new Node(Token.SCRIPT);
    nodeTraversal.visit(null, node, parent);
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }

  @Test
  public void testVisitBreakOrContinue() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    Node node = new Node(Token.BREAK);
    node.addChildToFront(new Node(Token.NAME, "label1"));
    processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());
    processLabels.namespaceStack.peek().renameMap.put("label1", new RenameLabels.LabelInfo(1));
    processLabels.visitBreakOrContinue(node);
    assertTrue(processLabels.namespaceStack.peek().renameMap.get("label1").referenced);
  }

  @Test
  public void testGetNameForId() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    String name = processLabels.getNameForId(1);
    assertEquals("a", name);
  }

  @Test
  public void testGetLabelInfo() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();
    processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());
    processLabels.namespaceStack.peek().renameMap.put("label1", new RenameLabels.LabelInfo(1));
    RenameLabels.LabelInfo labelInfo = processLabels.getLabelInfo("label1");
    assertNotNull(labelInfo);
    assertEquals(1, labelInfo.id);
  }

}