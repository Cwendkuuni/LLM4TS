import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

public class RenameLabelsTest {

  @SuppressWarnings("unused")
  private AbstractCompiler compiler;
  @SuppressWarnings("unused")
  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    compiler = new Compiler();
    renameLabels = new RenameLabels(compiler);
  }

  // Test for constructor
  @Test
  public void testRenameLabelsConstructor() {
    RenameLabels renameLabels = new RenameLabels(compiler);
    assertNotNull(renameLabels);
  }

  // Test for ProcessLabels class constructor
  @Test
  public void testProcessLabelsConstructor() {
    ProcessLabels processLabels = new ProcessLabels();
    assertNotNull(processLabels);
  }

  // Test for shouldTraverse method
  @Test
  public void testShouldTraverse() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: { b: break a; } }");
    processLabels.enterScope(nodeTraversal);
    nodeTraversal.traverse(processLabels, root, NodeTraversal.EMPTY);
    processLabels.exitScope(nodeTraversal);
    assertTrue(processLabels.namespaceStack.size() == 1);
  }

  // Test for visit method with labeled statement
  @Test
  public void testVisitLabeledStatement() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: { b: break a; } }");
    processLabels.enterScope(nodeTraversal);
    nodeTraversal.traverse(processLabels, root, NodeTraversal.EMPTY);
    processLabels.exitScope(nodeTraversal);
    Node labelNode = nodeTraversal.getRoot().getFirstChild();
    Node labelNameNode = labelNode.getFirstChild();
    assertEquals("a", labelNameNode.getString());
    processLabels = new ProcessLabels();
    nodeTraversal = NodeTraversal.create(compiler);
    root = NodeUtil.createNodeFromSource("function bar() { a: { b: break a; } }");
    processLabels.enterScope(nodeTraversal);
    nodeTraversal.traverse(processLabels, root, NodeTraversal.EMPTY);
    processLabels.exitScope(nodeTraversal);
    labelNode = nodeTraversal.getRoot().getFirstChild();
    labelNameNode = labelNode.getFirstChild();
    String newName = processLabels.getNameForId(1);
    assertEquals(newName, labelNameNode.getString());
  }

  // Test for visit method with break statement
  @Test
  public void testVisitBreakStatement() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: { b: break a; } }");
    processLabels.enterScope(nodeTraversal);
    nodeTraversal.traverse(processLabels, root, NodeTraversal.EMPTY);
    processLabels.exitScope(nodeTraversal);
    Node breakNode = null;
    for (Node child = nodeTraversal.getRoot().getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.BREAK) {
        breakNode = child;
        break;
      }
    }
    assertNotNull(breakNode);
    Node labelNode = breakNode.getPrevious();
    assertEquals("a", labelNode.getFirstChild().getString());
  }

  // Test for visit method with continue statement
  @Test
  public void testVisitContinueStatement() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: { b: continue a; } }");
    processLabels.enterScope(nodeTraversal);
    nodeTraversal.traverse(processLabels, root, NodeTraversal.EMPTY);
    processLabels.exitScope(nodeTraversal);
    Node continueNode = null;
    for (Node child = nodeTraversal.getRoot().getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CONTINUE) {
        continueNode = child;
        break;
      }
    }
    assertNotNull(continueNode);
    Node labelNode = continueNode.getPrevious();
    assertEquals("a", labelNode.getFirstChild().getString());
  }

  // Test for visit method with unreferenced label
  @Test
  public void testVisitUnreferencedLabel() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: {} b: {} }");
    processLabels.enterScope(nodeTraversal);
    nodeTraversal.traverse(processLabels, root, NodeTraversal.EMPTY);
    processLabels.exitScope(nodeTraversal);
    Node labelNode = null;
    for (Node child = nodeTraversal.getRoot().getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.LABEL) {
        labelNode = child;
        break;
      }
    }
    assertNotNull(labelNode);
    String name = labelNode.getFirstChild().getString();
    assertEquals("a", name);
    Node newChild = labelNode.getLastChild();
    NodeUtil.tryMergeBlock(newChild);
    nodeTraversal.getRoot().replaceChild(labelNode, newChild);
    compiler.reportCodeChange();
    assertFalse(nodeTraversal.getRoot().contains(labelNode));
  }

  // Test for process method
  @Test
  public void testProcess() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: { b: break a; } }");
    renameLabels.process(NodeUtil.EMPTY, root);
    assertTrue(nodeTraversal.getRoot().getFirstChild().getFirstChild().getString().equals("a")
        || nodeTraversal.getRoot().getFirstChild().getFirstChild().getString().equals(""));
  }

  // Test for edge case: empty function
  @Test
  public void testProcessEmptyFunction() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar(){}");
    renameLabels.process(NodeUtil.EMPTY, root);
    assertTrue(nodeTraversal.getRoot().getFirstChild() == null);
  }

  // Test for edge case: labeled statement without break or continue
  @Test
  public void testProcessLabeledStatementWithoutBreakOrContinue() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: {} }");
    renameLabels.process(NodeUtil.EMPTY, root);
    assertTrue(nodeTraversal.getRoot().getFirstChild().getFirstChild().getString().equals("a"));
  }

  // Test for exception handling: unreachable label
  @Test(expected = IllegalStateException.class)
  public void testProcessUnreachableLabel() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: {} b: break a; }");
    renameLabels.process(NodeUtil.EMPTY, root);
  }

  // Test for exception handling: non-label statement
  @Test(expected = IllegalArgumentException.class)
  public void testProcessNonLabelStatement() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: 5 }");
    renameLabels.process(NodeUtil.EMPTY, root);
  }

  // Test for exception handling: invalid label name
  @Test(expected = IllegalArgumentException.class)
  public void testProcessInvalidLabelName() {
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler);
    Node root = NodeUtil.createNodeFromSource("function bar() { a: { 1: {} } }");
    renameLabels.process(NodeUtil.EMPTY, root);
  }
}