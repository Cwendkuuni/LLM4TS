package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private CompilerPassManager passManager;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setup() {
    compiler = mock(Compiler.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getInput("test.js")).thenReturn(new CompilerInput("test.js", ""));
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(passManager.addPass(any(CompilerPass.class))).thenReturn(passManager);
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess() {
    Node root = Node.newObjectLiteral();
    Node externs = Node.newObjectLiteral();
    runtimeTypeCheck.process(externs, root);
    assertNotNull(runtimeTypeCheck);
  }

  @Test
  public void testAddMarkers() {
    Node root = Node.newObjectLiteral();
    FunctionType functionType = new FunctionType(
        NodeUtil.newQualifiedNameNode("String", -1, -1),
        ImmutableList.of(),
        Node.newObjectType(),
        null);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    ArgumentCaptor<NodeTraversal> captor = ArgumentCaptor.forClass(NodeTraversal.class);
    window.setImmediate(mock(Function.class));
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(root);

    runtimeTypeCheck.process(externs, root);

    NodeTraversal traversal = captor.getValue();
    Node functionNode = NodeUtil.newFunctionNode(
        NodeUtil.newQualifiedNameNode("String", -1, -1),
        Node.newObjectType(),
        Node.newObjectType(),
        null);
    Node constructorNode = functionNode.getLastChild();
    Node markerNode = null;
    Node next = constructorNode.getNext();

    traversal.visit(NodeUtil.newQualifiedNameNode("String"), next);
    markerNode = next;

    traversal.visit(functionNode);
    assertNotNull(markerNode);
  }

  @Test
  public void testAddChecks() {
    Node root = Node.newObjectLiteral();
    FunctionType functionType = new FunctionType(
        NodeUtil.newQualifiedNameNode("String", -1, -1),
        ImmutableList.of(),
        Node.newObjectType(),
        null);
    Node functionNode = NodeUtil.newFunctionNode(
        NodeUtil.newQualifiedNameNode("String", -1, -1),
        Node.newObjectType(),
        Node.newObjectType(),
        null);
    Node block = functionNode.getLastChild();

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    ArgumentCaptor<NodeTraversal> captor = ArgumentCaptor.forClass(NodeTraversal.class);
    window.setImmediate(mock(Function.class));
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(root);

    runtimeTypeCheck.process(externs, root);

    NodeTraversal traversal = captor.getValue();
    Node paramNode = NodeUtil.getFnParameters(functionNode).getFirstChild();
    Node checkNode = null;

    traversal.visit(functionNode);

    checkNode = block.getFirstChild();
    assertNotNull(checkNode);
  }

  @Test(expected = AssertionError.class)
  public void testAddChecksWithUnknownType() {
    Node root = Node.newObjectLiteral();
    FunctionType functionType = new FunctionType(
        NodeUtil.newQualifiedNameNode("String", -1, -1),
        ImmutableList.of(),
        new UnionType(ImmutableList.of(
            new ObjectType(NodeUtil.newQualifiedNameNode("String", -1, -1)),
            new ObjectType(NodeUtil.newQualifiedNameNode("Number", -1, -1))),
        null),
        null);
    Node functionNode = NodeUtil.newFunctionNode(
        NodeUtil.newQualifiedNameNode("String", -1, -1),
        Node.newObjectType(),
        Node.newObjectType(),
        null);
    Node block = functionNode.getLastChild();

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    ArgumentCaptor<NodeTraversal> captor = ArgumentCaptor.forClass(NodeTraversal.class);
    window.setImmediate(mock(Function.class));
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(root);

    runtimeTypeCheck.process(externs, root);

    NodeTraversal traversal = captor.getValue();
    Node paramNode = NodeUtil.getFnParameters(functionNode).getFirstChild();
    Node checkNode = null;

    traversal.visit(functionNode);

    checkNode = block.getFirstChild();
    assertNotNull(checkNode);

    // This check should fail because we don't know how to check the union type
    traversal.visit(paramNode.getNext());
  }
}