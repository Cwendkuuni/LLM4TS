package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RuntimeTypeCheckTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private NodeFactory nodeFactory;

  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_SimpleFunction_NoTypeChecks() {
    Node root = nodeFactory.createNodeFromString("function test() {}");
    runtimeTypeCheck.process(null, root);

    verify(compiler, times(0)).reportCodeChange();
  }

  @Test
  public void testProcess_FunctionWithTypes_AddsTypeChecks() {
    Node root = nodeFactory.createNodeFromString("function test(x: number, y: string) {}");

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getNodeForCodeInsertion(any())).thenReturn(nodeTraversal);

    runtimeTypeCheck.process(null, root);

    verify(nodeTraversal, times(2)).addChildAfter(any(), any());
  }

  @Test
  public void testProcess_NullFunction_NoTypeChecks() {
    Node root = null;
    runtimeTypeCheck.process(null, root);

    verify(compiler, times(0)).reportCodeChange();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullCompiler_ThrowsException() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node root = nodeFactory.createNodeFromString("function test() {}");
    runtimeTypeCheck.process(null, root);
  }

  @Test
  public void testProcess_FunctionWithUnionTypes_AddsMultipleCheckerNodes() {
    String functionSource =
        "function test(x: number|boolean) {}";

    Node root = nodeFactory.createNodeFromString(functionSource);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getNodeForCodeInsertion(any())).thenReturn(nodeTraversal);

    runtimeTypeCheck.process(null, root);

    verify(nodeTraversal, times(2)).addChildAfter(any(), any());
  }

  @Test
  public void testProcess_ClassWithInterfaceImplements_AddsInterfaceMarkers() {
    String className = "MyClass";
    String interfaceName = "MyInterface";

    Node root = nodeFactory.createNodeFromString(
        "class " + className + " implements " + interfaceName + " {}");

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getNodeForCodeInsertion(any())).thenReturn(nodeTraversal);

    runtimeTypeCheck.process(null, root);

    Node classNode = nodeFactory.createQualifiedNameNode(className, -1, -1);
    Node interfaceMarker = nodeFactory.newString("implements__" + interfaceName);

    verify(nodeTraversal, times(1))
        .addChildAfter(eq(classNode), eq(interfaceMarker));
  }
}