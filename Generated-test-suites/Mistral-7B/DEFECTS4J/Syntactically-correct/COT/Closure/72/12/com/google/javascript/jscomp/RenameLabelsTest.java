import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ProcessLabels;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for the RenameLabels class.
 */
public class RenameLabelsTest {

  /**
   * Test for the RenameLabels constructor.
   */
  @org.junit.Test
  public void testRenameLabelsConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameLabels renameLabels = new RenameLabels(compiler);
    assertEquals(compiler, renameLabels.compiler);
  }

  /**
   * Test for the process method of the RenameLabels class.
   */
  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { b: break a; } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { c: break c; } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with unused labels.
   */
  @org.junit.Test
  public void testProcessWithRemoveUnused() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    Node root = createNode("function foo() { a: { b: break a; } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with nested labels.
   */
  @org.junit.Test
  public void testProcessWithNestedLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { b: { c: break c; } } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { d: { c: break d; } } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with label clashes.
   */
  @org.junit.Test
  public void testProcessWithLabelClashes() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { a: break a; } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { b: break b; } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with label references.
   */
  @org.junit.Test
  public void testProcessWithLabelReferences() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { b: break a; goToA: break a; } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { c: break c; goToA: break c; } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with invalid label names.
   */
  @org.junit.Test
  public void testProcessWithInvalidLabelNames() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { 1: break a; } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { a: break a; } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with label names longer
   * than 1 character.
   */
  @org.junit.Test
  public void testProcessWithLongLabelNames() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { z: break z; } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { y: break y; } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  /**
   * Test for the process method of the RenameLabels class with nested labels and
   * label references.
   */
  @org.junit.Test
  public void testProcessWithNestedLabelsAndLabelReferences() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerContext context = mock(CompilerContext.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    when(compiler.getContext()).thenReturn(context);
    when(compiler.getFactory()).thenReturn(nodeFactory);

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = createNode("function foo() { a: { b: { c: break c; d: break a; } } }");

    renameLabels.process(null, root);

    Node expected = createNode("function foo() { a: { e: { f: break f; d: break e; } } }");
    assertEquals(expected.toSource(), root.toSource());
  }

  private Node createNode(String nodeSource) {
    NodeFactory nodeFactory = mock(NodeFactory.class);
    Node root = NodeUtil.parseTree(nodeSource, null, null, null, nodeFactory);
    return root;
  }
}