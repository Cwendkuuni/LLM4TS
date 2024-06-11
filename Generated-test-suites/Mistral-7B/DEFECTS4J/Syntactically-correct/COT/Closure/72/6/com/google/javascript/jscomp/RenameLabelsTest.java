import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private Node createNode(int type, String value) {
    Node node = new Node();
    node.setType(type);
    node.setString(value);
    return node;
  }

  @SuppressWarnings("unchecked")
  private Node createLabelNode(String name) {
    Node labelNode = new Node();
    labelNode.setType(Token.LABEL);
    Node labelNameNode = new Node();
    labelNameNode.setType(Token.IDENT);
    labelNameNode.setString(name);
    labelNode.addChild(labelNameNode);
    return labelNode;
  }

  @SuppressWarnings("unchecked")
  private Node createBreakOrContinueNode(String name) {
    Node breakOrContinueNode = new Node();
    breakOrContinueNode.setType(Token.BREAK);
    Node breakOrContinueNameNode = new Node();
    breakOrContinueNameNode.setType(Token.IDENT);
    breakOrContinueNameNode.setString(name);
    breakOrContinueNode.addChild(breakOrContinueNameNode);
    return breakOrContinueNode;
  }

  @SuppressWarnings("unchecked")
  private Node createBlockNode() {
    Node blockNode = new Node();
    blockNode.setType(Token.BLOCK);
    return blockNode;
  }

  @SuppressWarnings("unchecked")
  private AbstractCompiler createCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.addToDebugLog(anyString())).thenReturn(null);
    when(compiler.reportCodeChange()).thenReturn(null);
    return compiler;
  }

  @SuppressWarnings("unchecked")
  private CompilerPass createRenameLabels(AbstractCompiler compiler) {
    return new RenameLabels(compiler);
  }

  @SuppressWarnings("unchecked")
  private NodeTraversal createNodeTraversal(NodeTraversal.ScopedCallback callback) {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getRoot()).thenReturn(new Node());
    when(nodeTraversal.enterScope(any())).thenAnswer(i -> {
      callback.enterScope(nodeTraversal);
      return null;
    });
    when(nodeTraversal.exitScope(any())).thenAnswer(i -> {
      callback.exitScope(nodeTraversal);
      return null;
    });
    when(nodeTraversal.traverse(any(), any(), any(), any())).thenAnswer(i -> null);
    when(nodeTraversal.visit(any(), any(), any())).thenAnswer(i -> null);
    when(nodeTraversal.shouldTraverse(any(), any(), any())).thenAnswer(i -> true);
    return nodeTraversal;
  }

  @SuppressWarnings("unchecked")
  private CompilerPassManager createCompilerPassManager() {
    CompilerPassManager compilerPassManager = mock(CompilerPassManager.class);
    return compilerPassManager;
  }

  @org.junit.Test
  public void testRenameLabels() {
    // Test basic functionality
    AbstractCompiler compiler = createCompiler();
    CompilerPass renameLabels = createRenameLabels(compiler);
    CompilerPassManager compilerPassManager = createCompilerPassManager();
    Node root = new Node();
    Node externs = new Node();
    NodeTraversal nodeTraversal = createNodeTraversal(new RenameLabels.ProcessLabels());

    compilerPassManager.addPasses(new CompilerPass[] {renameLabels});
    compilerPassManager.runPasses(externs, root);

    assertNotNull(compiler);
    assertNotNull(renameLabels);
    assertNotNull(compilerPassManager);

    // Test label renaming
    Node labelNode = createLabelNode("label1");
    Node parent = new Node();
    parent.addChild(labelNode);
    NodeTraversal.traverse(compiler, root, nodeTraversal);
    String newLabelName = renameLabels.getNameForId(1);
    assertNotNull(newLabelName);
    assertFalse(labelNode.getString().equals(newLabelName));

    // Test label removal
    Node unreferencedLabelNode = createLabelNode("label2");
    Node unreferencedParent = new Node();
    unreferencedParent.addChild(unreferencedLabelNode);
    NodeTraversal.traverse(compiler, root, nodeTraversal);
    assertNull(unreferencedLabelNode.getParentNode());

    // Test label referencing
    Node labelRefNode = createBreakOrContinueNode("label1");
    Node refParent = new Node();
    refParent.addChild(labelRefNode);
    NodeTraversal.traverse(compiler, root, nodeTraversal);
    Node referencedLabelNode = nodeTraversal.getRoot().lookupField("labelRenamed1");
    assertNotNull(referencedLabelNode);
    assertFalse(referencedLabelNode.getString().equals("labelRenamed1"));
    assertEquals(labelNode.getString(), referencedLabelNode.getString());

    // Test multiple labels
    Node multipleLabelsRoot = new Node();
    Node multipleLabelsExterns = new Node();
    Node multipleLabelsParent = new Node();
    multipleLabelsParent.setType(Token.BLOCK);
    multipleLabelsExterns.addChild(multipleLabelsParent);
    multipleLabelsRoot.setType(Token.BLOCK);
    multipleLabelsRoot.addChild(multipleLabelsExterns);

    Node label1Node = createLabelNode("label1");
    Node label2Node = createLabelNode("label2");
    Node label1RefNode = createBreakOrContinueNode("label1");
    Node label2RefNode = createBreakOrContinueNode("label2");

    multipleLabelsParent.addChild(label1Node);
    multipleLabelsParent.addChild(label2Node);
    multipleLabelsParent.addChild(label1RefNode);
    multipleLabelsParent.addChild(label2RefNode);

    NodeTraversal nodeTraversalMultipleLabels = createNodeTraversal(new RenameLabels.ProcessLabels());
    NodeTraversal.traverse(compiler, multipleLabelsRoot, nodeTraversalMultipleLabels);

    String newLabel1Name = renameLabels.getNameForId(1);
    String newLabel2Name = renameLabels.getNameForId(2);

    assertNotNull(newLabel1Name);
    assertFalse(label1Node.getString().equals(newLabel1Name));
    assertNotNull(newLabel2Name);
    assertFalse(label2Node.getString().equals(newLabel2Name));

    Node referencedLabel1Node = nodeTraversalMultipleLabels.getRoot().lookupField("labelRenamed1");
    Node referencedLabel2Node = nodeTraversalMultipleLabels.getRoot().lookupField("labelRenamed2");
    assertNotNull(referencedLabel1Node);
    assertFalse(referencedLabel1Node.getString().equals("labelRenamed1"));
    assertEquals(label1Node.getString(), referencedLabel1Node.getString());

    assertNotNull(referencedLabel2Node);
    assertFalse(referencedLabel2Node.getString().equals("labelRenamed2"));
    assertEquals(label2Node.getString(), referencedLabel2Node.getString());

    // Test removeUnused flag
    RenameLabels renameLabelsWithRemoveUnused = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    CompilerPassManager compilerPassManagerWithRemoveUnused = createCompilerPassManager();
    Node rootWithRemoveUnused = new Node();
    Node externsWithRemoveUnused = new Node();
    NodeTraversal nodeTraversalWithRemoveUnused = createNodeTraversal(new RenameLabels.ProcessLabels());

    compilerPassManagerWithRemoveUnused.addPasses(new CompilerPass[] {renameLabelsWithRemoveUnused});
    compilerPassManagerWithRemoveUnused.runPasses(externsWithRemoveUnused, rootWithRemoveUnused);

    Node unreferencedLabelNodeWithRemoveUnused = createLabelNode("label3");
    Node unreferencedParentWithRemoveUnused = new Node();
    unreferencedParentWithRemoveUnused.addChild(unreferencedLabelNodeWithRemoveUnused);
    NodeTraversal.traverse(compiler, rootWithRemoveUnused, nodeTraversalWithRemoveUnused);

    assertNull(unreferencedLabelNodeWithRemoveUnused.getParentNode());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testLabelStackUnderflow() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass renameLabels = createRenameLabels(compiler);
    CompilerPassManager compilerPassManager = createCompilerPassManager();
    Node root = new Node();
    Node externs = new Node();
    Node labelNode = createLabelNode("label1");
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    parent.addChild(labelNode);
    parent.addChild(createLabelNode("label2"));
    parent.addChild(createLabelNode("label3"));
    externs.addChild(parent);
    root.setType(Token.BLOCK);
    root.addChild(externs);

    compilerPassManager.addPasses(new CompilerPass[] {renameLabels});
    compilerPassManager.runPasses(externs, root);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testLabelStackOverflow() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass renameLabels = createRenameLabels(compiler);
    CompilerPassManager compilerPassManager = createCompilerPassManager();
    Node root = new Node();
    Node externs = new Node();

    // Create a loop with 10000 labels
    Deque<LabelNamespace> labelNamespaces = new java.util.LinkedList<>();
    ArrayList<String> names = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      LabelNamespace labelNamespace = new LabelNamespace();
      labelNamespaces.push(labelNamespace);
      String name = names.get(i % names.size());
      names.add(newNameForLabel(name));
      LabelInfo labelInfo = new LabelInfo(i);
      labelNamespace.renameMap.put(name, labelInfo);
    }

    Node parent = new Node();
    parent.setType(Token.BLOCK);
    for (int i = 0; i < 10000; i++) {
      Node labelNode = createLabelNode(names.get(i % names.size()));
      parent.addChild(labelNode);
    }

    externs.addChild(parent);
    root.setType(Token.BLOCK);
    root.addChild(externs);

    compilerPassManager.addPasses(new CompilerPass[] {renameLabels});
    compilerPassManager.runPasses(externs, root);
  }

  private String newNameForLabel(String name) {
    if (name.isEmpty()) {
      throw new AssertionError("Label name cannot be empty.");
    }

    return name;
  }
}