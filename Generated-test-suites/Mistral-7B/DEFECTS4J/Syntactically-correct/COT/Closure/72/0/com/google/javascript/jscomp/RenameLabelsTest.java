import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * RenameLabelsTest class to test the functionality of RenameLabels class.
 */
public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private static AbstractCompiler getCompilerMock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeUtil nodeUtil = mock(NodeUtil.class);
    when(compiler.getSourceFile()).thenReturn(nodeUtil);
    return compiler;
  }

  /**
   * Test method for the constructor RenameLabels(AbstractCompiler).
   */
  @org.junit.Test
  public void testRenameLabelsConstructor() {
    AbstractCompiler compiler = getCompilerMock();
    RenameLabels renameLabels = new RenameLabels(compiler);
  }

  /**
   * Test method for the constructor RenameLabels(AbstractCompiler, Supplier,
   * boolean).
   */
  @org.junit.Test
  public void testRenameLabelsConstructorWithSupplier() {
    AbstractCompiler compiler = getCompilerMock();
    Supplier<String> nameSupplier = () -> "testName";
    RenameLabels renameLabels = new RenameLabels(compiler, nameSupplier, true);
  }

  /**
   * Test method for the process method.
   */
  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = getCompilerMock();
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node node = mock(Node.class);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getRoot()).thenReturn(node);
    when(nodeTraversal.enterScope(null)).thenReturn(true);
    when(nodeTraversal.exitScope(null)).thenReturn(true);

    renameLabels.process(null, node);
  }

  /**
   * Test method for the visitLabel method.
   */
  @org.junit.Test
  public void testVisitLabel() {
    AbstractCompiler compiler = getCompilerMock();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Node nameNode = mock(Node.class);

    when(nameNode.getString()).thenReturn("testName");
    when(node.getFirstChild()).thenReturn(nameNode);
    when(node.getLastChild()).thenReturn(mock(Node.class));
    when(parent.replaceChild(node, node.getLastChild())).thenReturn(true);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getRoot()).thenReturn(node);
    when(nodeTraversal.enterScope(null)).thenReturn(true);
    when(nodeTraversal.exitScope(null)).thenReturn(true);
    when(nodeTraversal.visit(node, parent)).thenCallRealMethod();

    renameLabels.process(null, node);
  }

  /**
   * Test method for the visitBreakOrContinue method.
   */
  @org.junit.Test
  public void testVisitBreakOrContinue() {
    AbstractCompiler compiler = getCompilerMock();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node node = mock(Node.class);

    Node nameNode = mock(Node.class);
    when(nameNode.getString()).thenReturn("testName");
    when(node.getFirstChild()).thenReturn(nameNode);

    NodeUtil nodeUtil = mock(NodeUtil.class);
    when(nodeUtil.tryMergeBlock(node)).thenReturn(true);

    when(node.getType()).thenReturn(Node.TOKEN_BREAK);

    renameLabels.process(null, node);
  }

  /**
   * Test method for the shouldTraverse method with a label node.
   */
  @org.junit.Test
  public void testShouldTraverseLabel() {
    AbstractCompiler compiler = getCompilerMock();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node node = mock(Node.class);

    Node nameNode = mock(Node.class);
    when(nameNode.getString()).thenReturn("testName");
    when(node.getFirstChild()).thenReturn(nameNode);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getRoot()).thenReturn(node);
    when(nodeTraversal.enterScope(null)).thenReturn(true);
    when(nodeTraversal.exitScope(null)).thenReturn(true);
    when(nodeTraversal.shouldTraverse(node, null, null)).thenCallRealMethod();

    renameLabels.process(null, node);
  }

  /**
   * Test method for the shouldTraverse method with a non-label node.
   */
  @org.junit.Test
  public void testShouldTraverseNonLabel() {
    AbstractCompiler compiler = getCompilerMock();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node node = mock(Node.class);

    when(node.getType()).thenReturn(Node.TOKEN_VAR);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getRoot()).thenReturn(node);
    when(nodeTraversal.shouldTraverse(node, null, null)).thenReturn(true);

    renameLabels.process(null, node);
  }
}