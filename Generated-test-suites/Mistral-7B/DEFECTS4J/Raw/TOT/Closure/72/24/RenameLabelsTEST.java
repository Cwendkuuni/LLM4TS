import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class RenameLabelsTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    options = mock(CompilerOptions.class);
    root = Node.objectLiteral();
  }

  @Test
  public void testSimpleLabelRenaming() {
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(options, root);

    Node label = Node.label("a");
    Node breakStatement = Node.breakStatement(label);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    NodeTraversal.traverse(traversal, root, new RenameLabels.ProcessLabels());

    assertEquals("a", label.getFirstChild().getString());
    assertEquals("a", breakStatement.getFirstChild().getString());
  }

  @Test
  public void testLabelRenamingWithNestedLabels() {
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(options, root);

    Node label1 = Node.label("a");
    Node label2 = Node.label("b");
    Node breakStatement1 = Node.breakStatement(label1);
    Node breakStatement2 = Node.breakStatement(label2);

    Node block = Node.block(Lists.newArrayList(label1, label2, breakStatement1, breakStatement2));
    root.addChild(block);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    NodeTraversal.traverse(traversal, root, new RenameLabels.ProcessLabels());

    assertEquals("a", label1.getFirstChild().getString());
    assertEquals("b", label2.getFirstChild().getString());
    assertEquals("a", breakStatement1.getFirstChild().getString());
    assertEquals("b", breakStatement2.getFirstChild().getString());
  }

  @Test
  public void testLabelRenamingWithUnusedLabels() {
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(options, root);

    Node label1 = Node.label("a");
    Node label2 = Node.label("a");
    Node breakStatement1 = Node.breakStatement(label1);
    Node breakStatement2 = Node.breakStatement(label2);

    Node block = Node.block(Lists.newArrayList(label1, label2, breakStatement1, breakStatement2));
    root.addChild(block);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    NodeTraversal.traverse(traversal, root, new RenameLabels.ProcessLabels());

    assertEquals("a", label1.getFirstChild().getString());
    assertEquals("b", label2.getFirstChild().getString());
    assertEquals("a", breakStatement1.getFirstChild().getString());
    assertEquals("b", breakStatement2.getFirstChild().getString());

    // Unused label should be removed
    assertEquals(1, root.getChildCount());
  }

  @Test
  public void testLabelRenamingWithBreakAndContinue() {
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(options, root);

    Node label1 = Node.label("a");
    Node breakStatement = Node.breakStatement(label1);
    Node continueStatement = Node.continueStatement(label1);

    Node block = Node.block(Lists.newArrayList(label1, breakStatement, continueStatement));
    root.addChild(block);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    NodeTraversal.traverse(traversal, root, new RenameLabels.ProcessLabels());

    assertEquals("a", label1.getFirstChild().getString());
    assertEquals("a", breakStatement.getFirstChild().getString());
    assertEquals("a", continueStatement.getFirstChild().getString());
  }

  @Test
  public void testLabelRenamingWithKeyword() {
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(options, root);

    Node label = Node.label("break");
    Node breakStatement = Node.breakStatement(label);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    NodeTraversal.traverse(traversal, root, new RenameLabels.ProcessLabels());

    assertEquals("break_0", label.getFirstChild().getString());
    assertEquals("break_0", breakStatement.getFirstChild().getString());
  }
}