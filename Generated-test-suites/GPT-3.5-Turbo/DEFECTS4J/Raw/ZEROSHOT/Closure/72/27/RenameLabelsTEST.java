import org.junit.*;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  @Test
  public void testEnterScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.SCRIPT);
    nodeTraversal.enterScope(node);
    assertEquals(1, processLabels.namespaceStack.size());
  }

  @Test
  public void testExitScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.SCRIPT);
    nodeTraversal.exitScope(node);
    assertEquals(0, processLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverseLabel() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.NAME, "label"));

    Node parent = new Node(Token.SCRIPT);
    assertTrue(processLabels.shouldTraverse(nodeTraversal, node, parent));

    assertEquals(1, processLabels.namespaceStack.size());
    assertEquals(0, processLabels.names.size());
    assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
    assertEquals(0, processLabels.namespaceStack.peek().renameMap.get("label").id);
  }

  @Test
  public void testShouldTraverseNonLabel() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.VAR);

    Node parent = new Node(Token.SCRIPT);
    assertTrue(processLabels.shouldTraverse(nodeTraversal, node, parent));

    assertEquals(0, processLabels.namespaceStack.size());
    assertEquals(0, processLabels.names.size());
  }

  @Test
  public void testVisitLabelReferenced() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.NAME, "label"));
    node.addChildToBack(new Node(Token.BLOCK));

    Node parent = new Node(Token.SCRIPT);
    processLabels.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(0));
    nodeTraversal.visit(node, parent);
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }

  @Test
  public void testVisitLabelNotReferenced() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.LABEL);
    node.addChildToBack(new Node(Token.NAME, "label"));
    node.addChildToBack(new Node(Token.BLOCK));

    Node parent = new Node(Token.SCRIPT);
    processLabels.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(0));
    processLabels.namespaceStack.peek().renameMap.get("label").referenced = false;
    nodeTraversal.visit(node, parent);
    assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
  }

  @Test
  public void testVisitBreakOrContinue() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.BREAK);
    node.addChildToBack(new Node(Token.NAME, "label"));

    Node parent = new Node(Token.SCRIPT);
    processLabels.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(0));
    processLabels.namespaceStack.peek().renameMap.get("label").referenced = true;
    nodeTraversal.visit(node, parent);
    assertEquals("a", node.getFirstChild().getString());
  }

  @Test
  public void testVisitBreakOrContinueNotReferenced() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node node = new Node(Token.BREAK);
    node.addChildToBack(new Node(Token.NAME, "label"));

    Node parent = new Node(Token.SCRIPT);
    processLabels.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(0));
    processLabels.namespaceStack.peek().renameMap.get("label").referenced = false;
    nodeTraversal.visit(node, parent);
    assertEquals("label", node.getFirstChild().getString());
  }

  @Test
  public void testGetNameForId() {
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
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    processLabels.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(0));

    assertEquals(0, processLabels.getLabelInfo("label").id);
    assertNull(processLabels.getLabelInfo("missing"));
  }

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    renameLabels.process(externs, root);

    // Add test assertions here
  }
}