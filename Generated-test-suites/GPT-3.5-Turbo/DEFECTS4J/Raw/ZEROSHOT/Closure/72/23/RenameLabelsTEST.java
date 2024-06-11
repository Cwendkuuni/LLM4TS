import org.junit.*;
import static org.junit.Assert.*;

public class RenameLabelsTest {
  
  @Test
  public void testProcessLabels_enterScope() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    // Test entering a scope adds a new namespace to the stack
    pl.enterScope(null);
    assertEquals(1, pl.namespaceStack.size());
  }
  
  @Test
  public void testProcessLabels_exitScope() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    // Test exiting a scope removes the top namespace from the stack
    pl.exitScope(null);
    assertEquals(0, pl.namespaceStack.size());
  }
  
  @Test
  public void testProcessLabels_shouldTraverse_label() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.STRING, "labelName"));
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    NodeTraversal nodeTraversal = null;
    Node parent = null;
    // Test adding a label to the rename map and generating a new name
    pl.shouldTraverse(nodeTraversal, node, parent);
    assertEquals(1, pl.namespaceStack.peek().renameMap.size());
    assertNotNull(pl.namespaceStack.peek().renameMap.get("labelName"));
    assertEquals(1, pl.names.size());
  }
  
  @Test
  public void testProcessLabels_visit_breakOrContinue() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    Node node = new Node(Token.CONTINUE);
    node.addChildToBack(new Node(Token.STRING, "labelName"));
    Node oldNameNode = node.getFirstChild();
    Node nameNode = node.getFirstChild().cloneNode();
    node.replaceChild(oldNameNode, nameNode);
    // Test renaming a break or continue label
    pl.visitBreakOrContinue(node);
    String newName = pl.namespaceStack.peek().renameMap.values().iterator().next().newName;
    assertEquals(newName, nameNode.getString());
    assertNotEquals(oldNameNode.getString(), nameNode.getString());
  }
  
  @Test
  public void testProcessLabels_visitLabel_referenced() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.STRING, "labelName"));
    node.addChildToBack(new Node(Token.BLOCK));
    Node nameNode = node.getFirstChild();
    pl.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));
    // Test renaming a referenced label
    pl.visitLabel(node, null);
    String newName = pl.namespaceStack.peek().renameMap.get("labelName").newName;
    assertEquals(newName, nameNode.getString());
  }
  
  @Test
  public void testProcessLabels_visitLabel_notReferenced() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.STRING, "labelName"));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    //Test removing an unreferenced label
    pl.visitLabel(node, parent);
    assertEquals(0, pl.namespaceStack.peek().renameMap.size());
    assertEquals(node.getLastChild(), parent.getFirstChild());
  }
  
  @Test
  public void testProcessLabels_getNameForId() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    pl.names.add("a");
    pl.names.add("b");
    // Test getting name for id
    assertEquals("a", pl.getNameForId(1));
    assertEquals("b", pl.getNameForId(2));
  }
  
  @Test
  public void testProcessLabels_getLabelInfo() {
    RenameLabels.ProcessLabels pl = new RenameLabels.ProcessLabels();
    pl.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));
    // Test getting label info
    assertNotNull(pl.getLabelInfo("labelName"));
  }
  
  @Test
  public void testDefaultNameSupplier_get() {
    RenameLabels.DefaultNameSupplier dns = new RenameLabels.DefaultNameSupplier();
    // Test getting a new name
    assertNotNull(dns.get());
  }
  
  @Test
  public void testProcess_renameLabel() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node script = new Node(Token.SCRIPT);
    Node label = new Node(Token.LABEL);
    label.addChildToBack(new Node(Token.STRING, "labelName"));
    script.addChildToBack(label);
    renameLabels.process(null, script);
    // Test renaming a label
    assertEquals("a", label.getFirstChild().getString());
  }
  
  @Test
  public void testProcess_removeLabel() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node script = new Node(Token.SCRIPT);
    Node label = new Node(Token.LABEL);
    label.addChildToBack(new Node(Token.STRING, "labelName"));
    label.addChildToBack(new Node(Token.BLOCK));
    script.addChildToBack(label);
    renameLabels.process(null, script);
    // Test removing an unreferenced label
    assertEquals(0, script.getChildCount());
  }
  
  @Test
  public void testLabelInfo_constructor() {
    RenameLabels.LabelInfo li = new RenameLabels.LabelInfo(1);
    // Test creating LabelInfo object
    assertEquals(1, li.id);
  }
  
  @Test
  public void testLabelNamespace_constructor() {
    RenameLabels.LabelNamespace ln = new RenameLabels.LabelNamespace();
    // Test creating LabelNamespace object
    assertNotNull(ln);
  }
}