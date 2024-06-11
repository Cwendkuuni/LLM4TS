import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private Compiler compiler;
  private Node root;
  private RenameLabels renameLabels;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    root = NodeFactory.createProgram();
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testRenameTopLevelLabels() {
    Node labelNode = NodeFactory.createLabel("a");
    Node labelBody = NodeFactory.createBlock();
    root.addChild(labelNode);
    root.addChild(labelBody);

    renameLabels.process(NodeFactory.createScriptExtract(), root);

    assertEquals("a", labelNode.getFirstChild().getString());
    assertEquals("a", labelBody.getFirstChild().getName());
  }

  @org.junit.Test
  public void testRenameNestedLabels() {
    Node outerLabelNode = NodeFactory.createLabel("a");
    Node outerLabelBody = NodeFactory.createBlock();
    Node innerLabelNode = NodeFactory.createLabel("b");
    Node innerLabelBody = NodeFactory.createBlock();
    outerLabelBody.addChild(outerLabelNode);
    outerLabelBody.addChild(innerLabelNode);
    outerLabelBody.addChild(innerLabelBody);
    root.addChild(outerLabelBody);

    renameLabels.process(NodeFactory.createScriptExtract(), root);

    assertEquals("a", outerLabelNode.getFirstChild().getString());
    assertEquals("a_1", innerLabelNode.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithSameNameInDifferentScopes() {
    Node outerLabelNode1 = NodeFactory.createLabel("a");
    Node outerLabelBody1 = NodeFactory.createBlock();
    outerLabelBody1.addChild(outerLabelNode1);
    root.addChild(outerLabelBody1);

    Node outerLabelNode2 = NodeFactory.createLabel("a");
    Node outerLabelBody2 = NodeFactory.createBlock();
    outerLabelBody2.addChild(outerLabelNode2);
    root.addChild(outerLabelBody2);

    renameLabels.process(NodeFactory.createScriptExtract(), root);

    assertEquals("a", outerLabelNode1.getFirstChild().getString());
    assertEquals("a_1", outerLabelNode2.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRemoveUnusedLabels() {
    Node labelNode = NodeFactory.createLabel("a");
    Node labelBody = NodeFactory.createBlock();
    root.addChild(labelNode);
    root.addChild(labelBody);

    renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    renameLabels.process(NodeFactory.createScriptExtract(), root);

    // The label should be removed since it is not referenced.
    assertNull(root.getChild(0));
  }

  @org.junit.Test
  public void testRenameLabelsWithNamesConflictingWithKeywords() {
    Node labelNode1 = NodeFactory.createLabel("break");
    Node labelBody1 = NodeFactory.createBlock();
    Node labelNode2 = NodeFactory.createLabel("continue");
    Node labelBody2 = NodeFactory.createBlock();
    root.addChild(labelNode1);
    root.addChild(labelBody1);
    root.addChild(labelNode2);
    root.addChild(labelBody2);

    Map<String, LabelInfo> renameMap = new HashMap<>();
    ProcessLabels processLabels = new ProcessLabels() {
      @Override
      public void shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        super.shouldTraverse(nodeTraversal, node, parent);
        if (node.getType() == LABEL) {
          String name = node.getFirstChild().getString();
          if (name.equals("break") || name.equals("continue")) {
            LabelInfo li = new LabelInfo(1);
            renameMap.put(name, li);
          }
        }
      }
    };
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, processLabels);
    nodeTraversal.traverse();

    LabelInfo breakLabel = renameMap.get("break");
    LabelInfo continueLabel = renameMap.get("continue");

    // Check that the labels were renamed.
    assertEquals("break_1", labelNode1.getFirstChild().getString());
    assertEquals("continue_1", labelNode2.getFirstChild().getString());
  }
}