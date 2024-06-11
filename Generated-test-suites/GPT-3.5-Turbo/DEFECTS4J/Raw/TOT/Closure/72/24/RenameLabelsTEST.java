import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.RenameLabels.DefaultNameSupplier;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testEnterScope() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node labelNode = new Node(Token.LABEL, fooNode);
    script.addChildToBack(labelNode);

    nodeTraversal.traverseNode(script);

    // Assert that a new namespace is created for label names
    assertEquals(2, processLabels.namespaceStack.size());
  }

  @Test
  public void testExitScope() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node labelNode = new Node(Token.LABEL, fooNode);
    script.addChildToBack(labelNode);

    Scope scope = new Scope(null, script);
    scope.addReference(labelNode);

    nodeTraversal.traverseNode(script);

    // Assert that the namespace is popped after exiting the scope
    assertEquals(0, processLabels.namespaceStack.size());
  }

  @Test
  public void testShouldTraverse_LabelNode() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node labelNode = new Node(Token.LABEL, fooNode);
    script.addChildToBack(labelNode);

    nodeTraversal.traverseNode(script);

    // Assert that the label name is stored in the rename map
    assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("foo"));
  }

  @Test
  public void testShouldTraverse_LabelNodeWithExistingName() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node labelNode1 = new Node(Token.LABEL, fooNode);
    Node labelNode2 = new Node(Token.LABEL, fooNode);
    script.addChildToBack(labelNode1);
    script.addChildToBack(labelNode2);

    nodeTraversal.traverseNode(script);

    // Assert that the existing label name is not overwritten
    assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
  }

  @Test
  public void testShouldTraverse_ReturnValue() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node labelNode = new Node(Token.LABEL, fooNode);
    script.addChildToBack(labelNode);

    boolean result = nodeTraversal.traverseNode(script);

    // Assert that the shouldTraverse method returns true
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_NoneLabelNode() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node statementNode = new Node(Token.EXPR_RESULT);
    blockNode.addChildToBack(statementNode);
    script.addChildToBack(blockNode);

    boolean result = nodeTraversal.traverseNode(script);

    // Assert that the shouldTraverse method returns true
    assertTrue(result);
  }

  @Test
  public void testVisit_BreakNode() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node breakNode = new Node(Token.BREAK, fooNode);
    script.addChildToBack(breakNode);

    processLabels.namespaceStack.peek().renameMap.put("foo", new RenameLabels.LabelInfo(1));

    nodeTraversal.traverseNode(script);

    // Assert that the label name is renamed
    assertEquals("a", breakNode.getFirstChild().getString());
  }

  @Test
  public void testVisit_ContinueNode() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node continueNode = new Node(Token.CONTINUE, fooNode);
    script.addChildToBack(continueNode);

    processLabels.namespaceStack.peek().renameMap.put("foo", new RenameLabels.LabelInfo(1));

    nodeTraversal.traverseNode(script);

    // Assert that the label name is renamed
    assertEquals("a", continueNode.getFirstChild().getString());
  }

  @Test
  public void testVisit_UnreferencedLabelNode() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, processLabels);

    Node script = new Node(Token.SCRIPT);
    Node fooNode = new Node(Token.NAME, "foo");
    Node labelNode = new Node(Token.LABEL, fooNode);
    script.addChildToBack(labelNode);

    processLabels.namespaceStack.peek().renameMap.put("foo", new RenameLabels.LabelInfo(1));

    nodeTraversal.traverseNode(script);

    // Assert that the label node is removed
    assertEquals(Token.EMPTY, script.getFirstChild().getType());
  }

  @Test
  public void testGetNameForId() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    processLabels.names.add("a");
    processLabels.names.add("b");
    processLabels.names.add("c");

    String result = processLabels.getNameForId(2);

    // Assert that the correct short name is returned
    assertEquals("b", result);
  }

  @Test
  public void testGetLabelInfo() {
    RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

    processLabels.namespaceStack.peek().renameMap.put("foo", new RenameLabels.LabelInfo(1));

    RenameLabels.LabelInfo result = processLabels.getLabelInfo("foo");

    // Assert that the correct label info is returned
    assertEquals(1, result.id);
  }
}