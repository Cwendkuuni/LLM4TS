import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ProcessLabels;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    // Arrange
    Compiler compiler = mock(Compiler.class);
    Node root = createTestNode();
    ProcessLabels processLabels = new ProcessLabels();

    when(compiler.addToDebugLog(anyString())).thenReturn(null);
    when(compiler.reportCodeChange()).thenReturn(null);

    // Act
    CompilerPass renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    // Assert
    Node expected = createExpectedNode();
    Node actual = NodeUtil.deepCopy(root);
    NodeUtil.tryMergeBlocks(actual);
    assertEquals(expected.toString(), actual.toString());
  }

  @org.junit.Test
  public void testRenameLabelsWithUnusedLabel() {
    // Arrange
    Compiler compiler = mock(Compiler.class);
    Node root = createTestNodeWithUnusedLabel();
    ProcessLabels processLabels = new ProcessLabels();

    when(compiler.addToDebugLog(anyString())).thenReturn(null);
    when(compiler.reportCodeChange()).thenReturn(null);

    // Act
    CompilerPass renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(null, root);

    // Assert
    Node expected = createExpectedNodeWithUnusedLabel();
    Node actual = NodeUtil.deepCopy(root);
    NodeUtil.tryMergeBlocks(actual);
    assertEquals(expected.toString(), actual.toString());
  }

  @org.junit.Test
  public void testRenameLabelsWithDuplicateLabel() {
    // Arrange
    Compiler compiler = mock(Compiler.class);
    Node root = createTestNodeWithDuplicateLabel();
    ProcessLabels processLabels = new ProcessLabels();

    when(compiler.addToDebugLog(anyString())).thenReturn(null);
    when(compiler.reportCodeChange()).thenReturn(null);

    // Act
    CompilerPass renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(null, root);

    // Assert
    Node expected = createExpectedNodeWithDuplicateLabel();
    Node actual = NodeUtil.deepCopy(root);
    NodeUtil.tryMergeBlocks(actual);
    assertEquals(expected.toString(), actual.toString());
  }

  // Helpers
  private Node createTestNode() {
    Node root = new Node(Token.PROGRAM);
    Node func = new Node(Token.FUNCTION, new Node(Token.ID, "bar"), new ArrayList<>());
    root.setFirstChild(func);

    Node label1 = new Node(Token.LABEL, "a");
    Node label2 = new Node(Token.LABEL, "b");
    Node label3 = new Node(Token.LABEL, "a");
    Node block1 = new Node(Token.BLOCK, new ArrayList<>());
    Node break1 = new Node(Token.BREAK, new Node(Token.ID, "a"));
    Node block2 = new Node(Token.BLOCK, new ArrayList<>());
    Node break2 = new Node(Token.BREAK, new Node(Token.ID, "b"));
    Node continue1 = new Node(Token.CONTINUE, null);

    label1.setNext(label2);
    func.addChild(label1);
    func.addChild(block1);
    block1.addChild(break1);
    func.addChild(label3);
    func.addChild(block2);
    block2.addChild(continue1);
    block2.addChild(break2);

    return root;
  }

  private Node createTestNodeWithUnusedLabel() {
    Node root = new Node(Token.PROGRAM);
    Node func = new Node(Token.FUNCTION, new Node(Token.ID, "bar"), new ArrayList<>());
    root.setFirstChild(func);

    Node label1 = new Node(Token.LABEL, "a");
    Node label2 = new Node(Token.LABEL, "b");
    Node block1 = new Node(Token.BLOCK, new ArrayList<>());
    Node break1 = new Node(Token.BREAK, new Node(Token.ID, "a"));
    Node block2 = new Node(Token.BLOCK, new ArrayList<>());

    label1.setNext(label2);
    func.addChild(label1);
    func.addChild(block1);
    block1.addChild(break1);
    func.addChild(block2);

    return root;
  }

  private Node createTestNodeWithDuplicateLabel() {
    Node root = new Node(Token.PROGRAM);
    Node func = new Node(Token.FUNCTION, new Node(Token.ID, "bar"), new ArrayList<>());
    root.setFirstChild(func);

    Node label1 = new Node(Token.LABEL, "a");
    Node label2 = new Node(Token.LABEL, "a");
    Node block1 = new Node(Token.BLOCK, new ArrayList<>());
    Node break1 = new Node(Token.BREAK, new Node(Token.ID, "a"));
    Node block2 = new Node(Token.BLOCK, new ArrayList<>());
    Node break2 = new Node(Token.BREAK, new Node(Token.ID, "a"));

    label1.setNext(label2);
    func.addChild(label1);
    func.addChild(block1);
    block1.addChild(break1);
    func.addChild(block2);
    block2.addChild(break2);

    return root;
  }

  private Node createExpectedNode() {
    Node root = new Node(Token.PROGRAM);
    Node func = new Node(Token.FUNCTION, new Node(Token.ID, "bar"), new ArrayList<>());
    root.setFirstChild(func);

    Node label1 = new Node(Token.LABEL, "b");
    Node label2 = new Node(Token.LABEL, "c");
    Node block1 = new Node(Token.BLOCK, new ArrayList<>());
    Node break1 = new Node(Token.BREAK, new Node(Token.ID, "b"));
    Node block2 = new Node(Token.BLOCK, new ArrayList<>());
    Node continue1 = new Node(Token.CONTINUE, null);

    label1.setNext(label2);
    func.addChild(label1);
    func.addChild(block1);
    block1.addChild(break1);
    func.addChild(block2);
    block2.addChild(continue1);

    return root;
  }

  private Node createExpectedNodeWithUnusedLabel() {
    Node root = new Node(Token.PROGRAM);
    Node func = new Node(Token.FUNCTION, new Node(Token.ID, "bar"), new ArrayList<>());
    root.setFirstChild(func);

    Node label1 = new Node(Token.LABEL, "b");
    Node block1 = new Node(Token.BLOCK, new ArrayList<>());
    Node break1 = new Node(Token.BREAK, new Node(Token.ID, "b"));

    label1.setNext(null);
    func.addChild(label1);
    func.addChild(block1);
    block1.addChild(break1);

    return root;
  }
}