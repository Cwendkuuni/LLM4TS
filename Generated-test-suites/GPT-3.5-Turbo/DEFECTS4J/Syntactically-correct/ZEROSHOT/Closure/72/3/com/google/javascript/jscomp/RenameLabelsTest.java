import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    renameLabels = new RenameLabels(new AbstractCompiler());
  }

  @Test
  public void testProcess() {
    // Test that process method doesn't throw any errors
    renameLabels.process(new Node(), new Node());
  }

  @Test
  public void testDefaultNameSupplier() {
    RenameLabels.DefaultNameSupplier supplier = new RenameLabels.DefaultNameSupplier();
    String name1 = supplier.get();
    String name2 = supplier.get();
    assertNotEquals(name1, name2);
  }

  @Test
  public void testGetNameForId() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    processLabels.names.add("a");
    processLabels.names.add("b");
    String name = processLabels.getNameForId(1);
    assertEquals("a", name);
  }

  @Test
  public void testGetLabelInfo() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    RenameLabels.LabelNamespace namespace = processLabels.namespaceStack.peek();
    String labelName = "testLabel";
    RenameLabels.LabelInfo labelInfo = new RenameLabels.LabelInfo(1);
    namespace.renameMap.put(labelName, labelInfo);
    assertEquals(labelInfo, processLabels.getLabelInfo(labelName));
  }

  @Test
  public void testProcessLabelsEnterScope() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    int initialStackSize = processLabels.namespaceStack.size();

    processLabels.enterScope(new NodeTraversal());

    int finalStackSize = processLabels.namespaceStack.size();
    assertEquals(initialStackSize + 1, finalStackSize);
  }

  @Test
  public void testProcessLabelsExitScope() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    processLabels.enterScope(new NodeTraversal());
    int initialStackSize = processLabels.namespaceStack.size();

    processLabels.exitScope(new NodeTraversal());

    int finalStackSize = processLabels.namespaceStack.size();
    assertEquals(initialStackSize - 1, finalStackSize);
  }

  @Test
  public void testProcessLabelsShouldTraverse() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node node = new Node(Token.LABEL);
    Node parent = new Node();
    boolean result = processLabels.shouldTraverse(nodeTraversal, node, parent);
    assertTrue(result);
  }

  @Test
  public void testProcessLabelsVisit() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node labelNode = new Node(Token.LABEL);
    Node parent = new Node();
    processLabels.shouldTraverse(nodeTraversal, labelNode, parent);
    
    processLabels.visit(nodeTraversal, labelNode, parent);
  }

  @Test
  public void testProcessLabelsVisitBreakOrContinue() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    Node breakNode = new Node(Token.BREAK);
    Node continueNode = new Node(Token.CONTINUE);
    processLabels.visitBreakOrContinue(breakNode);
    processLabels.visitBreakOrContinue(continueNode);
  }

  @Test
  public void testProcessLabelsVisitLabel() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node labelNode = new Node(Token.LABEL);
    Node parent = new Node();
    processLabels.shouldTraverse(nodeTraversal, labelNode, parent);

    processLabels.visitLabel(labelNode, parent);
  }
}