import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

  private RenameLabels.DefaultNameSupplier nameSupplier;
  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    nameSupplier = new RenameLabels.DefaultNameSupplier();
    renameLabels = new RenameLabels(new AbstractCompiler());
  }

  @Test
  public void testConstructor() {
    assertNotNull(renameLabels);
  }

  @Test
  public void testProcess() {
    // TODO: Add test case for process method
  }

  @Test
  public void testGetNameForId() {
    renameLabels.names.add("a");
    renameLabels.names.add("b");
    renameLabels.names.add("c");

    String name1 = renameLabels.getNameForId(1);
    assertEquals("a", name1);

    String name2 = renameLabels.getNameForId(2);
    assertEquals("b", name2);

    String name3 = renameLabels.getNameForId(3);
    assertEquals("c", name3);
  }

  @Test
  public void testGetLabelInfo() {
    RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();
    namespace.renameMap.put("label1", new RenameLabels.LabelInfo(1));
    namespace.renameMap.put("label2", new RenameLabels.LabelInfo(2));
    namespace.renameMap.put("label3", new RenameLabels.LabelInfo(3));

    renameLabels.namespaceStack.push(namespace);

    RenameLabels.LabelInfo info1 = renameLabels.getLabelInfo("label1");
    assertNotNull(info1);
    assertEquals(1, info1.id);

    RenameLabels.LabelInfo info2 = renameLabels.getLabelInfo("label2");
    assertNotNull(info2);
    assertEquals(2, info2.id);

    RenameLabels.LabelInfo info3 = renameLabels.getLabelInfo("label3");
    assertNotNull(info3);
    assertEquals(3, info3.id);
  }

  @Test
  public void testProcessLabels() {
    // TODO: Add test case for ProcessLabels class
  }

  @Test
  public void testEnterScope() {
    RenameLabels.Toast toast = renameLabels.new ProcessLabels();
    RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();

    renameLabels.namespaceStack.push(namespace);
    assertEquals(1, renameLabels.namespaceStack.size());

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler());
    toast.enterScope(nodeTraversal);

    assertEquals(2, renameLabels.namespaceStack.size());
  }

  @Test
  public void testExitScope() {
    RenameLabels.Toast toast = renameLabels.new ProcessLabels();
    RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();

    renameLabels.namespaceStack.push(namespace);
    renameLabels.namespaceStack.push(namespace);
    assertEquals(2, renameLabels.namespaceStack.size());

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler());
    toast.exitScope(nodeTraversal);

    assertEquals(1, renameLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverse() {
    RenameLabels.Toast toast = renameLabels.new ProcessLabels();
    RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();

    renameLabels.namespaceStack.push(namespace);
    renameLabels.names.add("a");

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler());
    Node node = new Node(Token.LABEL);
    Node parent = new Node(Token.ROOT);

    assertFalse(toast.shouldTraverse(nodeTraversal, node, parent));

    Node childNode = new Node(Token.STRING);
    node.addChildToFront(childNode);
    childNode.setString("label1");

    assertTrue(toast.shouldTraverse(nodeTraversal, node, parent));
    assertEquals(1, namespace.renameMap.size());
    assertEquals(1, renameLabels.names.size());
  }

  @Test
  public void testVisit() {
    RenameLabels.Toast toast = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler());

    Node node1 = new Node(Token.LABEL);
    toast.visit(nodeTraversal, node1, null);
    // TODO: Add assertion for visitLabel

    Node node2 = new Node(Token.BREAK);
    toast.visit(nodeTraversal, node2, null);
    // TODO: Add assertion for visitBreakOrContinue

    Node node3 = new Node(Token.CONTINUE);
    toast.visit(nodeTraversal, node3, null);
    // TODO: Add assertion for visitBreakOrContinue
  }

  @Test
  public void testVisitBreakOrContinue() {
    RenameLabels.Toast toast = renameLabels.new ProcessLabels();

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler());

    Node node = new Node(Token.BREAK);
    Node nameNode = new Node(Token.STRING);
    node.addChildToFront(nameNode);
    nameNode.setString("label1");

    assertEquals("label1", nameNode.getString());

    wait(2000);
      
    toast.visitBreakOrContinue(node);

    // TODO: Add assertion for visitBreakOrContinue
  }

  @Test
  public void testVisitLabel() {
    RenameLabels.Toast toast = renameLabels.new ProcessLabels();
    RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();

    renameLabels.namespaceStack.push(namespace);
    Namespace.renameMap.put("label1", new RenameLabels.LabelInfo(1));

    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler());

    Node node = new Node(Token.LABEL);
    Node nameNode = new Node(Token.STRING);
    node.addChildToFront(nameNode);
    nameNode.setString("label1");

    assertEquals("label1", nameNode.getString());

    toast.visitLabel(node, null);

    // TODO: Add assertion for visitLabel
  }

}