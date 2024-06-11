import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

  Compiler compiler;
  NodeFactory nodeFactory;
  RenameLabels renameLabels;
  Scope scope;
  Node root;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    nodeFactory = new NodeFactory();
    scope = new Scope(null, null, null);
    root = nodeFactory.createRootNode();
    renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testProcess_EmptyCode() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);
    renameLabels.process(nodeFactory.createNodeList(), root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testProcess_SingleLabel() {
    Node labelNode = nodeFactory.createLabelNode("a", root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);
    when(compiler.addToDebugLog(anyString())).thenReturn(null);

    root.addChild(labelNode);
    renameLabels.process(nodeFactory.createNodeList(), root);
    assertEquals("label renamed: a => a", compiler.getDebugLog());
    assertEquals("a", labelNode.getString());
  }

  @Test
  public void testProcess_MultipleLabelsWithSameName() {
    String name = "labelName";
    Node labelNode1 = nodeFactory.createLabelNode(name, root);
    Node labelNode2 = nodeFactory.createLabelNode(name, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);
    when(compiler.addToDebugLog(anyString())).thenReturn(null);

    root.addChild(labelNode1);
    root.addChild(labelNode2);
    renameLabels.process(nodeFactory.createNodeList(), root);
    assertEquals("label renamed: labelName => labelName0", compiler.getDebugLog());
    assertEquals("labelName0", labelNode1.getString());
    assertEquals("labelName0", labelNode2.getString());
  }

  @Test
  public void testProcess_LabelWithReferencedName() {
    String name = "referencedLabel";
    Node labelNode = nodeFactory.createLabelNode(name, root);
    Node breakNode = nodeFactory.createBreakStatementNode(name, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);
    when(compiler.addToDebugLog(anyString())).thenReturn(null);

    root.addChild(labelNode);
    root.addChild(breakNode);
    renameLabels.process(nodeFactory.createNodeList(), root);
    assertEquals("label renamed: referencedLabel => referencedLabel0", compiler.getDebugLog());
    assertEquals("referencedLabel0", labelNode.getString());
    assertEquals("referencedLabel0", breakNode.getString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullCompiler() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, root, scope);
    new RenameLabels(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullRoot() {
    new RenameLabels(compiler);
    new RenameLabels(compiler, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullScope() {
    new RenameLabels(compiler, null, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_UnreferencedLabel_WithoutRemoveUnused() {
    String name = "unreferencedLabel";
    Node labelNode = nodeFactory.createLabelNode(name, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);

    root.addChild(labelNode);
    renameLabels.process(nodeFactory.createNodeList(), root);
  }

  @Test
  public void testProcess_UnreferencedLabel_WithRemoveUnused() {
    String name = "unreferencedLabel";
    Node labelNode = nodeFactory.createLabelNode(name, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, scope);

    root.addChild(labelNode);
    renameLabels newRenameLabels = new RenameLabels(compiler, null, false);
    newRenameLabels.process(nodeFactory.createNodeList(), root);
    assertEquals(0, root.getChildCount());
  }
}