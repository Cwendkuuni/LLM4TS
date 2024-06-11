import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private Node root;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = new Compiler();
    root = (Node) NodeFactory.createScope();
  }

  @Test
  public void testRenameLabels() {
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    Node labelNode = createLabelNode("a");
    Node labelReferenceNode = createLabelReferenceNode("a");
    Node labelBlockNode = createLabelBlockNode("a", createStatementNode("print('Label A')"));

    root.addChild(labelNode);
    root.addChild(labelReferenceNode);
    root.addChild(labelBlockNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    new ProcessLabels().process(nodeTraversal);

    assertEquals("Expected label 'a' to be renamed to 'a0'.", "a0", labelNode.getFirstChild().getString());
    assertEquals("Expected label reference 'a' to be renamed to 'a0'.", "a0", labelReferenceNode.getFirstChild().getString());
    assertEquals(
        "Expected label block 'a' to be renamed to 'a0' and its statement block to remain unchanged.",
        "a0 { print('Label A') }", labelBlockNode.toString());
  }

  @Test
  public void testRenameLabelsNested() {
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    Node labelNodeA = createLabelNode("a");
    Node labelNodeB = createLabelNode("b");
    Node labelReferenceNodeA = createLabelReferenceNode("a");
    Node labelReferenceNodeB = createLabelReferenceNode("b");
    Node labelBlockNodeAA = createLabelBlockNode("a", createStatementNode("print('Label A')"));
    Node labelBlockNodeAB = createLabelBlockNode("a", createLabelBlockNode("b", createStatementNode("print('Label B')")));

    root.addChild(labelNodeA);
    root.addChild(labelReferenceNodeA);
    root.addChild(labelBlockNodeAA);
    root.addChild(labelNodeB);
    root.addChild(labelReferenceNodeB);
    root.addChild(labelBlockNodeAB);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    new ProcessLabels().process(nodeTraversal);

    assertEquals("Expected label 'a' to be renamed to 'a0'.", "a0", labelNodeA.getFirstChild().getString());
    assertEquals(
        "Expected label 'b' to be renamed to 'b0' and its parent label 'a' to be renamed to 'a0'.",
        "b0 { a0 { print('Label B') } }", labelBlockNodeAB.toString());
    assertEquals("Expected label reference 'a' to be renamed to 'a0'.", "a0", labelReferenceNodeA.getFirstChild().getString());
    assertEquals("Expected label reference 'b' to be renamed to 'b0'.", "b0", labelReferenceNodeB.getFirstChild().getString());
  }

  @Test
  public void testRenameLabelsSameName() {
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    Node labelNodeA = createLabelNode("a");
    Node labelNodeA1 = createLabelNode("a");
    Node labelReferenceNodeA = createLabelReferenceNode("a");
    Node labelReferenceNodeA1 = createLabelReferenceNode("a");
    Node labelBlockNodeAA = createLabelBlockNode("a", createStatementNode("print('Label A')"));
    Node labelBlockNodeAA1 = createLabelBlockNode("a", createStatementNode("print('Label A')"));

    root.addChild(labelNodeA);
    root.addChild(labelNodeA1);
    root.addChild(labelReferenceNodeA);
    root.addChild(labelReferenceNodeA1);
    root.addChild(labelBlockNodeAA);
    root.addChild(labelBlockNodeAA1);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    new ProcessLabels().process(nodeTraversal);

    assertEquals(
        "Expected labels 'a' and 'a' to be renamed to 'a0' and 'a1' respectively.",
        "a0 { a1 { print('Label A') } }", labelBlockNodeAA.toString());
    assertEquals("Expected label 'a' reference to be renamed to 'a0'.", "a0", labelReferenceNodeA.getFirstChild().getString());
    assertEquals("Expected label 'a' reference to be renamed to 'a1'.", "a1", labelReferenceNodeA1.getFirstChild().getString());
  }

  @Test
  public void testRenameLabelsRemoveUnused() {
    RenameLabels renameLabels = new RenameLabels(compiler, new RemoveUnusedNameSupplier(), true);
    renameLabels.process(null, root);

    Node labelNodeA = createLabelNode("a");
    Node labelReferenceNodeA = createLabelReferenceNode("a");
    Node labelBlockNodeAA = createLabelBlockNode("a", createStatementNode("print('Label A')"));

    root.addChild(labelNodeA);
    root.addChild(labelReferenceNodeA);
    root.addChild(labelBlockNodeAA);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    new ProcessLabels().process(nodeTraversal);

    // The label 'a' is not used, so it should be removed.
    assertTrue("Expected label 'a' to be removed.", root.getChildCount() == 2);
    assertFalse("Expected label 'a' to be present.", root.hasChild(labelNodeA));
    assertFalse("Expected label reference 'a' to be present.", root.hasChild(labelReferenceNodeA));
    assertFalse("Expected label block 'a' to be present.", root.hasChild(labelBlockNodeAA));
  }

  @Test
  public void testRenameLabelsRemoveUnusedWithReferencedLabel() {
    RenameLabels renameLabels = new RenameLabels(compiler, new RemoveUnusedNameSupplier(), true);
    renameLabels.process(null, root);

    Node labelNodeA = createLabelNode("a");
    Node labelReferenceNodeA = createLabelReferenceNode("a");
    Node labelBlockNodeAA = createLabelBlockNode("a", createStatementNode("print('Label A')"));
    Node statementNode = createStatementNode(labelReferenceNodeA);

    root.addChild(labelNodeA);
    root.addChild(labelReferenceNodeA);
    root.addChild(labelBlockNodeAA);
    root.addChild(statementNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    new ProcessLabels().process(nodeTraversal);

    // The label 'a' is not used, but it is referenced, so it should not be removed.
    assertTrue("Expected label 'a' to be present.", root.hasChild(labelNodeA));
    assertTrue("Expected label reference 'a' to be present.", root.hasChild(labelReferenceNodeA));
    assertTrue("Expected label block 'a' to be present.", root.hasChild(labelBlockNodeAA));
  }

  @Test(expected = AssertionError.class)
  public void testRenameLabelsInvalidLabel() {
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    Node labelNodeKeyword = createLabelNode(Token.KEYWORD_WITH_COLON.toString());

    root.addChild(labelNodeKeyword);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    new ProcessLabels().process(nodeTraversal);

    fail("Expected an AssertionError due to a keyword label not being renamed.");
  }

  private Node createLabelNode(String name) {
    Node labelNode = NodeFactory.createLabel();
    labelNode.setFirstChild(NodeFactory.createString(name));
    return labelNode;
  }

  private Node createLabelReferenceNode(String name) {
    Node labelReferenceNode = NodeFactory.createStatement(Token.BREAK);
    labelReferenceNode.setFirstChild(NodeFactory.createLabel(name));
    return labelReferenceNode;
  }

  private Node createLabelBlockNode(String name, Node statementBlock) {
    Node labelBlockNode = NodeFactory.createBlock();
    labelBlockNode.addChild(createLabelNode(name));
    labelBlockNode.addChild(statementBlock);
    return labelBlockNode;
  }

  private Node createStatementNode(Node node) {
    Node statementNode = NodeFactory.createStatement(Token.PRINT);
    statementNode.addChild(NodeFactory.createString("print statement"));
    statementNode.addChild(node);
    return statementNode;
  }

  private static class RemoveUnusedNameSupplier implements NameGenerator {
    @Override
    public String generateNextName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setUsedNames(Map<String, Object> usedNames) {
      usedNames.put("a", null);
      usedNames.put("b", null);
    }
  }

  private static interface NameGenerator {
    String generateNextName();
    void setUsedNames(Map<String, Object> usedNames);
  }
}