import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private Compiler compiler;
  private Node rootNode;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    rootNode = NodeFactory.createRoot();
  }

  @org.junit.Test
  public void testRenameLabels() {
    // Given a simple label
    Node labelNode = NodeFactory.createLabel("a");
    Node labelBlock = NodeFactory.createBlock();
    labelBlock.addChild(NodeFactory.createBreak("a"));
    rootNode.addChild(labelNode);
    rootNode.addChild(labelBlock);

    // When the RenameLabels pass is applied
    new RenameLabels(compiler).process(NodeFactory.createRoot(), rootNode);

    // Then the label should be renamed to a short name
    LabelInfo labelInfo = getLabelInfo("a");
    assertNotNull(labelInfo);
    assertEquals("a$_0", labelInfo.id);

    // And the break statement should use the new label name
    Node renamedBreak = rootNode.getChild(1).getLastChild();
    assertEquals("a$_0", renamedBreak.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameNestedLabels() {
    // Given a nested label
    Node labelNode1 = NodeFactory.createLabel("a");
    Node labelBlock1 = NodeFactory.createBlock();
    Node labelNode2 = NodeFactory.createLabel("b");
    Node labelBlock2 = NodeFactory.createBlock();
    Node labelNode3 = NodeFactory.createLabel("c");
    Node labelBlock3 = NodeFactory.createBlock();
    Node breakNode = NodeFactory.createBreak("b");
    labelBlock1.addChild(labelNode1);
    labelBlock1.addChild(labelBlock2);
    labelBlock2.addChild(labelNode2);
    labelBlock2.addChild(labelNode3);
    labelBlock2.addChild(breakNode);
    rootNode.addChild(labelNode1);
    rootNode.addChild(labelBlock1);

    // When the RenameLabels pass is applied
    new RenameLabels(compiler).process(NodeFactory.createRoot(), rootNode);

    // Then the labels should be renamed to short names
    LabelInfo labelInfo1 = getLabelInfo("a");
    assertNotNull(labelInfo1);
    assertEquals("a$_0", labelInfo1.id);

    LabelInfo labelInfo2 = getLabelInfo("b");
    assertNotNull(labelInfo2);
    assertEquals("b$_1", labelInfo2.id);

    LabelInfo labelInfo3 = getLabelInfo("c");
    assertNotNull(labelInfo3);
    assertEquals("c$_2", labelInfo3.id);

    // And the break statement should use the new label name
    Node renamedBreak = rootNode.getChild(1).getLastChild().getLastChild();
    assertEquals("b$_1", renamedBreak.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRemoveUnusedLabels() {
    // Given a label that is not referenced
    Node labelNode = NodeFactory.createLabel("a");
    Node labelBlock = NodeFactory.createBlock();
    rootNode.addChild(labelNode);
    rootNode.addChild(labelBlock);

    // When the RenameLabels pass is applied with removeUnused = true
    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(
        NodeFactory.createRoot(), rootNode);

    // Then the label should be removed
    Node label = rootNode.getChild(0);
    assertNull(label);
  }

  @org.junit.Test
  public void testRenameKeywordLabel() {
    // Given a label with a keyword name
    Node labelNode = NodeFactory.createLabel("break");
    Node labelBlock = NodeFactory.createBlock();
    rootNode.addChild(labelNode);
    rootNode.addChild(labelBlock);

    // When the RenameLabels pass is applied
    new RenameLabels(compiler).process(NodeFactory.createRoot(), rootNode);

    // Then the label should be renamed to a valid name
    LabelInfo labelInfo = getLabelInfo("break");
    assertNotNull(labelInfo);
    assertEquals("break$_0", labelInfo.id);

    // And the code should not contain the keyword name
    assertFalse(rootNode.getChild(0).getString().contains("break"));
  }

  @org.junit.Test
  public void testRenameLabelWithSameNameInDifferentScopes() {
    // Given two labels with the same name in different scopes
    Node labelNode1 = NodeFactory.createLabel("a");
    Node labelBlock1 = NodeFactory.createBlock();
    Node labelNode2 = NodeFactory.createLabel("a");
    Node labelBlock2 = NodeFactory.createBlock();
    rootNode.addChild(labelNode1);
    rootNode.addChild(labelBlock1);
    rootNode.addChild(labelNode2);
    rootNode.addChild(labelBlock2);

    // When the RenameLabels pass is applied
    new RenameLabels(compiler).process(NodeFactory.createRoot(), rootNode);

    // Then the labels should be renamed to different short names
    LabelInfo labelInfo1 = getLabelInfo("a");
    assertNotNull(labelInfo1);
    assertEquals("a$_0", labelInfo1.id);

    LabelInfo labelInfo2 = getLabelInfo("a");
    assertNotNull(labelInfo2);
    assertEquals("a$_1", labelInfo2.id);

    // And the code should not contain the original label names
    assertFalse(rootNode.getChild(0).getString().contains("a"));
    assertFalse(rootNode.getChild(1).getString().contains("a"));
  }

  private LabelInfo getLabelInfo(String name) {
    LabelNamespace labelNamespace = (LabelNamespace) namespaceStack.peek();
    return labelNamespace.renameMap.get(name);
  }
}