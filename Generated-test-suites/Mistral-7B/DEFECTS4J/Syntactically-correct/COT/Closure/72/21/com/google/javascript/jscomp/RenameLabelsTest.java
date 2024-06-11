import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.util.Map;

/**
 * Test class for RenameLabels.
 */
public class RenameLabelsTest {

  /**
   * Test method for RenameLabels.process() with no labels.
   */
  @Test
  public void testProcessNoLabels() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node labelNode = assertLabelNodeAbsent(root, "a");
    Node labelNode2 = assertLabelNodeAbsent(root, "b");
  }

  /**
   * Test method for RenameLabels.process() with a single label.
   */
  @Test
  public void testProcessSingleLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNode = NodeFactory.createLabel("a");
    root.addChild(labelNode);

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNode = assertLabelNodePresent(root, "a");
    assertEquals("a", newLabelNode.getString());
  }

  /**
   * Test method for RenameLabels.process() with multiple labels.
   */
  @Test
  public void testProcessMultipleLabels() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    Node labelNodeB = NodeFactory.createLabel("b");
    root.addChild(labelNodeA);
    root.addChild(labelNodeB);

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNodeA = assertLabelNodePresent(root, "a");
    assertEquals("a", newLabelNodeA.getString());

    Node newLabelNodeB = assertLabelNodePresent(root, "b");
    assertEquals("b", newLabelNodeB.getString());
  }

  /**
   * Test method for RenameLabels.process() with multiple labels with the same name.
   */
  @Test
  public void testProcessMultipleLabelsWithSameName() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNodeA1 = NodeFactory.createLabel("a");
    Node labelNodeA2 = NodeFactory.createLabel("a");
    root.addChild(labelNodeA1);
    root.addChild(labelNodeA2);

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNodeA1 = assertLabelNodePresent(root, "a0");
    assertEquals("a0", newLabelNodeA1.getString());

    Node newLabelNodeA2 = assertLabelNodePresent(root, "a1");
    assertEquals("a1", newLabelNodeA2.getString());
  }

  /**
   * Test method for RenameLabels.process() with removeUnused set to true.
   */
  @Test
  public void testProcessRemoveUnused() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler, null, true);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    Node labelNodeB = NodeFactory.createLabel("b");
    root.addChild(labelNodeA);
    root.addChild(labelNodeB);

    renames.process(TestSourceFile.EMPTY_EXterns, root);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node labelNodeABeforeRemoval = assertLabelNodePresent(root, "a");
    Node labelNodeBAfterRemoval = assertLabelNodeAbsent(root, "a");
    Node labelNodeB = assertLabelNodePresent(root, "b");
  }

  /**
   * Test method for RenameLabels.process() with a break statement referencing a label.
   */
  @Test
  public void testProcessBreakStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    Node breakNode = NodeFactory.createBreak();
    Node breakNodeLabelRef = NodeFactory.createName("a");
    breakNode.addChild(breakNodeLabelRef);
    root.addChild(labelNodeA);
    root.addChild(breakNode);

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNodeA = assertLabelNodePresent(root, "a");
    assertEquals("a", newLabelNodeA.getString());

    Node newBreakNodeLabelRef = assertLabelReferencePresent(root, "a");
    assertEquals("a", newBreakNodeLabelRef.getString());
  }

  /**
   * Test method for RenameLabels.process() with a continue statement referencing a label.
   */
  @Test
  public void testProcessContinueStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    Node continueNode = NodeFactory.createContinue();
    Node continueNodeLabelRef = NodeFactory.createName("a");
    continueNode.addChild(continueNodeLabelRef);
    root.addChild(labelNodeA);
    root.addChild(continueNode);

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNodeA = assertLabelNodePresent(root, "a");
    assertEquals("a", newLabelNodeA.getString());

    Node newContinueNodeLabelRef = assertLabelReferencePresent(root, "a");
    assertEquals("a", newContinueNodeLabelRef.getString());
  }

  /**
   * Test method for RenameLabels.process() with a nested label.
   */
  @Test
  public void testProcessNestedLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    Node labelNodeB = NodeFactory.createLabel("b");
    root.addChild(labelNodeA);
    root.addChild(NodeFactory.createBlock(new Node[] { labelNodeA }));
    root.addChild(NodeFactory.createBlock(new Node[] {
        NodeFactory.createLabel("b"),
        NodeFactory.createBreak(),
        NodeFactory.createName("a")
    }));

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNodeA = assertLabelNodePresent(root, "a");
    assertEquals("a", newLabelNodeA.getString());

    Node newLabelNodeB = assertLabelNodePresent(root, "a0");
    assertEquals("a0", newLabelNodeB.getString());
  }

  /**
   * Test method for RenameLabels.process() with a nested label and removeUnused set to true.
   */
  @Test
  public void testProcessNestedLabelRemoveUnused() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler, null, true);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    Node labelNodeB = NodeFactory.createLabel("b");
    root.addChild(labelNodeA);
    root.addChild(NodeFactory.createBlock(new Node[] { labelNodeA }));
    root.addChild(NodeFactory.createBlock(new Node[] {
        NodeFactory.createLabel("b"),
        NodeFactory.createBreak(),
        NodeFactory.createName("a")
    }));

    renames.process(TestSourceFile.EMPTY_EXterns, root);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node labelNodeABeforeRemoval = assertLabelNodePresent(root, "a");
    Node labelNodeBAfterRemoval = assertLabelNodeAbsent(root, "a");
    Node labelNodeB = assertLabelNodePresent(root, "b");
  }

  /**
   * Test method for RenameLabels.process() with a label with a duplicate name at
   * a deeper nesting level.
   */
  @Test
  public void testProcessLabelWithDuplicateNameAtDeeperNestingLevel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameLabels renames = new RenameLabels(compiler);

    Node root = NodeFactory.createProgram();
    Node labelNodeA = NodeFactory.createLabel("a");
    root.addChild(labelNodeA);
    root.addChild(
        NodeFactory.createBlock(new Node[] {
            NodeFactory.createLabel("a"),
            NodeFactory.createBreak(),
            NodeFactory.createName("a")
        })
    );

    renames.process(TestSourceFile.EMPTY_EXterns, root);
    assertFalse(renames.removeUnused);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.traverse(new ProcessLabels());

    Node newLabelNodeA1 = assertLabelNodePresent(root, "a0");
    assertEquals("a0", newLabelNodeA1.getString());

    Node newLabelNodeA2 = assertLabelNodePresent(root, "a1");
    assertEquals("a1", newLabelNodeA2.getString());
  }

  /**
   * Helper method to check if a label node is present in the given root node.
   * @param root The root node.
   * @param name The name of the label node to search for.
   * @return The label node if found, null otherwise.
   */
  private Node assertLabelNodePresent(Node root, String name) {
    Node labelNode = null;
    for (int i = 0; labelNode == null && i < root.getNumChildren(); i++) {
      Node child = root.getChild(i);
      if (child.getType() == Token.LABEL && child.getString().equals(name)) {
        labelNode = child;
      } else {
        labelNode = assertLabelNodePresent(child, name);
      }
    }
    assertNotNull(labelNode);
    return labelNode;
  }

  /**
   * Helper method to check if a label node is absent in the given root node.
   * @param root The root node.
   * @param name The name of the label node to search for.
   * @return The label node if found, null otherwise.
   */
  private Node assertLabelNodeAbsent(Node root, String name) {
    Node labelNode = null;
    for (int i = 0; labelNode == null && i < root.getNumChildren(); i++) {
      Node child = root.getChild(i);
      if (child.getType() == Token.LABEL && child.getString().equals(name)) {
        fail("Found unexpected label node with name " + name);
      } else {
        labelNode = assertLabelNodeAbsent(child, name);
      }
    }
    assertNull(labelNode);
    return labelNode;
  }

  /**
   * Helper method to check if a label reference node is present in the given root node.
   * @param root The root node.
   * @param name The name of the label reference node to search for.
   * @return The label reference node if found, null otherwise.
   */
  private Node assertLabelReferencePresent(Node root, String name) {
    for (int i = 0; i < root.getNumChildren(); i++) {
      Node child = root.getChild(i);
      if (child.getType() == Token.NAME && child.getString().equals(name)) {
        Node parent = root.getChild(root.getChildIndex(child) - 1);
        if (parent.getType() == Token.BREAK || parent.getType() == Token.CONTINUE) {
          return child;
        }
      }
    }
    fail("Found no label reference node with name " + name);
    return null;
  }
}