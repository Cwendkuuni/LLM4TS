package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private ReferenceCollectingCallback callback;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    when(passManager.getCompiler()).thenReturn(compiler);

    callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
  }

  @Test
  public void testInlineObjectWithSingleProperty() {
    rootNode = NodeFactory.createRootNode(
        "var foo = { bar: 42 };", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    Node inlineNode = traverser.getScope().getVar("foo");
    assertEquals(7, inlineNode.getNumChildren());

    for (int i = 1; i < inlineNode.getNumChildren(); i += 2) {
      Node child = inlineNode.getChildAtIndex(i);
      assertTrue(child.isAssign());
      assertEquals("JSCompiler_object_inline_bar_" + i / 2, child.getFirstChild().getName().getString());
      assertEquals(42, NodeUtil.getNumberValue(child.getLastChild()));
    }

    Node bar1 = inlineNode.getChildAtIndex(5);
    Node bar2 = inlineNode.getChildAtIndex(6);
    assertTrue(bar1.isGetProp());
    assertTrue(bar2.isGetProp());
    assertEquals("bar", bar1.getLastChild().getString());
    assertEquals("bar", bar2.getLastChild().getString());

    // Check that no side-effects were introduced
    assertTrue(NodeUtil.areEqual(rootNode, NodeUtil.deepCopy(traverser.getRootNode())));
  }

  @Test
  public void testInlineObjectWithMultipleProperties() {
    rootNode = NodeFactory.createRootNode(
        "var foo = { a: 1, b: 2 };", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    Node inlineNode = traverser.getScope().getVar("foo");
    assertEquals(13, inlineNode.getNumChildren());

    for (int i = 1; i < inlineNode.getNumChildren(); i += 4) {
      Node child = inlineNode.getChildAtIndex(i);
      assertTrue(child.isAssign());
      assertEquals("JSCompiler_object_inline_a_" + (i - 1) / 4, child.getFirstChild().getName().getString());
      assertEquals(1, NodeUtil.getNumberValue(child.getLastChild()));
    }

    for (int i = 3; i < inlineNode.getNumChildren(); i += 4) {
      Node child = inlineNode.getChildAtIndex(i);
      assertTrue(child.isAssign());
      assertEquals("JSCompiler_object_inline_b_" + (i - 1) / 4, child.getFirstChild().getName().getString());
      assertEquals(2, NodeUtil.getNumberValue(child.getLastChild()));
    }

    for (int i = 0; i < 2; i++) {
      Node a = inlineNode.getChildAtIndex(i + 1);
      Node b = inlineNode.getChildAtIndex(i + 3);

      assertTrue(a.isGetProp());
      assertTrue(a.getLastChild().isName());
      assertEquals("a", a.getLastChild().getName().getString());

      assertTrue(b.isGetProp());
      assertTrue(b.getLastChild().isName());
      assertEquals("b", b.getLastChild().getName().getString());
    }

    // Check that no side-effects were introduced
    assertTrue(NodeUtil.areEqual(rootNode, NodeUtil.deepCopy(traverser.getRootNode())));
  }

  @Test
  public void testInlineObjectWithSelfReferentialAssignment() {
    rootNode = NodeFactory.createRootNode(
        "var foo = { a: { b: foo } };", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    // Check that the object literal was not inlined
    Node inlineNode = traverser.getScope().getVar("foo");
    assertNotNull(inlineNode);
    assertEquals(1, inlineNode.getNumChildren());
    assertTrue(inlineNode.isObjectLit());
  }

  @Test
  public void testInlineObjectWithExportedVariable() {
    rootNode = NodeFactory.createRootNode(
        "var bar = { foo: 'exported' };\n" +
        "export var baz = bar.foo;", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    Node inlineNode = traverser.getScope().getVar("bar");
    assertEquals(3, inlineNode.getNumChildren());

    Node foo = inlineNode.getChildAtIndex(1);
    assertEquals("JSCompiler_object_inline_foo_0", foo.getName().getString());
    assertEquals("foo", foo.getLastChild().getString());

    Node baz = traverser.getScope().getVar("baz");
    assertEquals("exported", NodeUtil.getStringValue(baz));

    // Check that no side-effects were introduced
    assertTrue(NodeUtil.areEqual(rootNode, NodeUtil.deepCopy(traverser.getRootNode())));
  }

  @Test
  public void testInlineObjectWithExternVariable() {
    rootNode = NodeFactory.createRootNode(
        "var bar = { foo: MyModule.baz };", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    Node inlineNode = traverser.getScope().getVar("bar");
    assertNotNull(inlineNode);
    assertEquals(1, inlineNode.getNumChildren());
    assertTrue(inlineNode.isObjectLit());

    // TODO(user): Implement a more proper test for externs
  }

  @Test
  public void testInlineObjectWithUndefinedProperty() {
    rootNode = NodeFactory.createRootNode(
        "var foo = { bar: undefined };", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    Node inlineNode = traverser.getScope().getVar("foo");
    assertEquals(3, inlineNode.getNumChildren());

    Node bar = inlineNode.getChildAtIndex(1);
    assertNull(bar.getFirstChild());
    assertNull(bar.getLastChild());

    // Check that no side-effects were introduced
    assertTrue(NodeUtil.areEqual(rootNode, NodeUtil.deepCopy(traverser.getRootNode())));
  }

  @Test
  public void testInlineObjectWithNestedObject() {
    rootNode = NodeFactory.createRootNode(
        "var foo = { a: { b: 1 } };", null);

    NodeTraversal traverser = new NodeTraversal(rootNode, passManager);
    callback.process(null, rootNode);

    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).addPass(captor.capture());
    assertEquals("InlineObjectLiterals", captor.getValue().getClass().getSimpleName());

    Node inlineNode = traverser.getScope().getVar("foo");
    assertEquals(7, inlineNode.getNumChildren());

    Node a = inlineNode.getChildAtIndex(1);
    assertTrue(a.isObjectLit());
    assertEquals("JSCompiler_object_inline_a_0", a.getName().getString());

    Node b = a.getChildAtIndex(1);
    assertEquals(1, b.getNumChildren());
    assertEquals("JSCompiler_object_inline_b_1", b.getName().getString());
    assertEquals(1, NodeUtil.getNumberValue(b.getLastChild()));

    // Check that no side-effects were introduced
    assertTrue(NodeUtil.areEqual(rootNode, NodeUtil.deepCopy(traverser.getRootNode())));
  }
}