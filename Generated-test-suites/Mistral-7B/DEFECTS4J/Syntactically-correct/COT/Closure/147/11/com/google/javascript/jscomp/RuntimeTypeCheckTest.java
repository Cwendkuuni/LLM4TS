package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.NodeTraversal.AbstractCallback;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node root;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setup() {
    compiler = mock(CompilerPass.class);
    root = mock(Node.class);
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_NoFunctions() {
    runtimeTypeCheck.process(null, null);
    // No assertions here as there are no functions to test
  }

  @Test
  public void testAddMarkers_FunctionWithoutImplementedInterfaces() {
    FunctionType functionType = mock(FunctionType.class);
    when(functionType.getAllImplementedInterfaces()).thenReturn(
        ImmutableList.of());

    Node functionNode = mock(Node.class);
    when(functionNode.getJSType()).thenReturn(functionType);

    ArgumentCaptor<Node> markerCaptor = ArgumentCaptor.forClass(Node.class);
    runtimeTypeCheck.new AddMarkers(compiler).visitFunction(
        mock(NodeTraversal.class), functionNode, null);

    Node markerNode = markerCaptor.getValue();

    assertTrue(markerNode.getType() == Token.GETPROP);
    assertTrue(markerNode.getFirstChild().getType() == Token.IDENT);
    assertTrue(markerNode.getFirstChild().getStringValue().equals(
        "instance_of__" + functionType.getName()));
  }

  @Test
  public void testAddMarkers_FunctionWithImplementedInterfaces() {
    FunctionType functionType = mock(FunctionType.class);
    ObjectType interfaceType1 = mock(ObjectType.class);
    ObjectType interfaceType2 = mock(ObjectType.class);
    when(interfaceType1.getReferenceName()).thenReturn("I1");
    when(interfaceType2.getReferenceName()).thenReturn("I2");
    when(functionType.getAllImplementedInterfaces())
        .thenReturn(ImmutableList.of(interfaceType1, interfaceType2));

    Node functionNode = mock(Node.class);
    when(functionNode.getJSType()).thenReturn(functionType);

    ArgumentCaptor<Node> markerCaptor = ArgumentCaptor.forClass(Node.class);
    runtimeTypeCheck.new AddMarkers(compiler).visitFunction(
        mock(NodeTraversal.class), functionNode, null);

    Node markerNode1 = markerCaptor.getValue();
    Node markerNode2 = markerCaptor.getValue();

    assertTrue(markerNode1.getType() == Token.GETPROP);
    assertTrue(markerNode1.getFirstChild().getType() == Token.IDENT);
    assertTrue(markerNode1.getFirstChild().getStringValue().equals(
        "instance_of__" + functionType.getName()));

    assertTrue(markerNode2.getType() == Token.GETPROP);
    assertTrue(markerNode2.getFirstChild().getType() == Token.IDENT);
    assertTrue(markerNode2.getFirstChild().getStringValue().equals(
        "implements__I1"));

    Node interfaceMarkerNode1 = markerNode1.getNext();
    assertTrue(interfaceMarkerNode1.getType() == Token.GETPROP);
    assertTrue(interfaceMarkerNode1.getFirstChild().getType() == Token.IDENT);
    assertTrue(interfaceMarkerNode1.getFirstChild().getStringValue().equals(
        "implements__I2"));
  }

  @Test
  public void testAddMarkers_ClassWithImplementedInterfaces() {
    ObjectType objectType = mock(ObjectType.class);
    ObjectType interfaceType1 = mock(ObjectType.class);
    ObjectType interfaceType2 = mock(ObjectType.class);
    when(interfaceType1.getReferenceName()).thenReturn("I1");
    when(interfaceType2.getReferenceName()).thenReturn("I2");
    when(objectType.getAllImplementedInterfaces())
        .thenReturn(ImmutableList.of(interfaceType1, interfaceType2));

    Node classNode = mock(Node.class);
    when(classNode.getJSType()).thenReturn(objectType);

    ArgumentCaptor<Node> markerCaptor = ArgumentCaptor.forClass(Node.class);
    runtimeTypeCheck.new AddMarkers(compiler).visitFunction(
        mock(NodeTraversal.class), classNode, null);

    Node markerNode1 = markerCaptor.getValue();
    Node markerNode2 = markerCaptor.getValue();

    assertTrue(markerNode1.getType() == Token.GETPROP);
    assertTrue(markerNode1.getFirstChild().getType() == Token.IDENT);
    assertTrue(markerNode1.getFirstChild().getStringValue().equals(
        "instance_of__" + objectType.getName()));

    assertTrue(markerNode2.getType() == Token.GETPROP);
    assertTrue(markerNode2.getFirstChild().getType() == Token.IDENT);
    assertTrue(markerNode2.getFirstChild().getStringValue().equals(
        "implements__I1"));

    Node interfaceMarkerNode1 = markerNode1.getNext();
    assertTrue(interfaceMarkerNode1.getType() == Token.GETPROP);
    assertTrue(interfaceMarkerNode1.getFirstChild().getType() == Token.IDENT);
    assertTrue(interfaceMarkerNode1.getFirstChild().getStringValue().equals(
        "implements__I2"));
  }

  @Test
  public void testAddChecks_FunctionWithPrimitiveReturnType() {
    FunctionType functionType = mock(FunctionType.class);
    functionType.getReturnType().setType(JSType.BOOLEAN_VALUE_TYPE);

    Node functionNode = mock(Node.class);
    when(functionNode.getJSType()).thenReturn(functionType);

    runtimeTypeCheck.new AddChecks(compiler).visitFunction(
        mock(NodeTraversal.class), functionNode, null);

    Node checkNode = functionNode.getLastChild();
    assertTrue(checkNode.getType() == Token.CALL);
    assertTrue(checkNode.getFirstChild().toString().equals(
        "checkType(expr, [valueChecker('boolean')])"));
  }

  @Test
  public void testAddChecks_FunctionWithUnionReturnType() {
    FunctionType functionType = mock(FunctionType.class);
    UnionType unionType = mock(UnionType.class);
    when(unionType.getAlternates())
        .thenReturn(ImmutableList.of(JSType.STRING_VALUE_TYPE, JSType.NUMBER_VALUE_TYPE));
    when(functionType.getReturnType()).thenReturn(unionType);

    Node functionNode = mock(Node.class);
    when(functionNode.getJSType()).thenReturn(functionType);

    runtimeTypeCheck.new AddChecks(compiler).visitFunction(
        mock(NodeTraversal.class), functionNode, null);

    Node checkNode = functionNode.getLastChild();
    assertTrue(checkNode.getType() == Token.CALL);
    assertTrue(checkNode.getFirstChild().toString().equals(
        "checkType(expr, [" +
            "[valueChecker('string')], [" +
            "classChecker('" + functionType.getName() + "')]]))"));
  }

  @Test
  public void testAddChecks_FunctionWithClassReturnType() {
    FunctionType functionType = mock(FunctionType.class);
    ObjectType returnType = mock(ObjectType.class);
    when(returnType.getReferenceName()).thenReturn("Foo");
    when(functionType.getReturnType()).thenReturn(returnType);

    Node functionNode = mock(Node.class);
    when(functionNode.getJSType()).thenReturn(functionType);

    runtimeTypeCheck.new AddChecks(compiler).visitFunction(
        mock(NodeTraversal.class), functionNode, null);

    Node checkNode = functionNode.getLastChild();
    assertTrue(checkNode.getType() == Token.CALL);
    assertTrue(checkNode.getFirstChild().toString().equals(
        "checkType(expr, [classChecker('Foo')])"));
  }
}