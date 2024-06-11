package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RuntimeTypeCheckTest {

  private RuntimeTypeCheck runtimeTypeCheck;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    this.nodeFactory = new NodeFactory();
    this.runtimeTypeCheck = new RuntimeTypeCheck(null, null);
  }

  @Test
  public void testAddMarkers() {
    String className = "TestClass";
    String interfaceName = "TestInterface";

    Node functionType = NodeFactory.createFunctionType(
        NodeFactory.createName(className), ImmutableList.of(), null);

    Node interfaceType = NodeFactory.createObjectType(
        NodeFactory.createName(interfaceName));

    Node constructorNode = NodeFactory.createFunction(
        NodeFactory.createName(className), ImmutableList.of(), null, null);

    Node classNode = NodeFactory.createNew(constructorNode);

    Node markerNode = runtimeTypeCheck.new AddMarkers(null).addMarker(
        functionType, classNode, interfaceType);

    assertTrue(markerNode != null);
  }

  @Test
  public void testAddChecks() {
    String className = "TestClass";
    String interfaceName = "TestInterface";

    Node functionType = NodeFactory.createFunctionType(
        NodeFactory.createName(className), ImmutableList.of(), null);

    ObjectType interfaceType = NodeFactory.createObjectType(
        NodeFactory.createName(interfaceName));

    Node constructorNode = NodeFactory.createFunction(
        NodeFactory.createName(className), ImmutableList.of(), null, null);

    Node paramNode = NodeFactory.createName("param");
    Node paramType = NodeFactory.createInstanceType(
        NodeFactory.createName(className));

    Node checkNode = runtimeTypeCheck.new AddChecks(null).createCheckTypeCallNode(
        paramType, paramNode);

    assertTrue(checkNode != null);
  }

  @Test
  public void testAddBoilerplateCode() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node boilerplateCode = RuntimeTypeCheck.getBoilerplateCode(
        null, null);

    assertTrue(boilerplateCode != null);
  }

  @Test(expected = NullPointerException.class)
  public void testAddChecksWithNullType() {
    String className = "TestClass";

    Node functionType = NodeFactory.createFunctionType(null, ImmutableList.of(), null);

    Node paramNode = NodeFactory.createName("param");
    Node checkNode = runtimeTypeCheck.new AddChecks(null).createCheckTypeCallNode(
        functionType, paramNode);

    assertFalse(checkNode != null);
  }

  @Test(expected = NullPointerException.class)
  public void testAddChecksWithNullExpression() {
    String className = "TestClass";

    Node functionType = NodeFactory.createFunctionType(
        NodeFactory.createName(className), ImmutableList.of(), null);

    Node checkNode = runtimeTypeCheck.new AddChecks(null)
        .createCheckTypeCallNode(functionType, null);

    assertFalse(checkNode != null);
  }

  @Test
  public void testCreateCheckerNodeForNullType() {
    Node checkNode = runtimeTypeCheck.new AddChecks(null).createCheckerNode(null);

    assertFalse(checkNode != null);
  }

  @Test
  public void testCreateCheckerNodeForValueType() {
    Node checkNode = runtimeTypeCheck.new AddChecks(null)
        .createCheckerNode(NodeFactory.createNumberValueType());

    assertTrue(checkNode != null);
  }

  @Test
  public void testCreateCheckerNodeForClassType() {
    String className = "TestClass";

    Node constructorNode = NodeFactory.createFunction(
        NodeFactory.createName(className), ImmutableList.of(), null, null);

    ObjectType objectType = NodeFactory.createObjectType(
        NodeFactory.createName(className));

    Node checkNode = runtimeTypeCheck.new AddChecks(null)
        .createCheckerNode(objectType);

    assertTrue(checkNode != null);
  }

  @Test
  public void testCreateCheckerNodeForInterfaceType() {
    String className = "TestClass";
    String interfaceName = "TestInterface";

    Node constructorNode = NodeFactory.createFunction(
        NodeFactory.createName(className), ImmutableList.of(), null, null);

    ObjectType objectType = NodeFactory.createObjectType(
        NodeFactory.createName(className));
    ObjectType interfaceType = NodeFactory.createObjectType(
        NodeFactory.createName(interfaceName));

    interfaceType.addInterfaceOf(objectType);

    Node checkNode = runtimeTypeCheck.new AddChecks(null)
        .createCheckerNode(interfaceType);

    assertTrue(checkNode != null);
  }

  @Test
  public void testCreateCheckerNodeForUnionType() {
    Node stringType = NodeFactory.createStringValueType();
    Node numberType = NodeFactory.createNumberValueType();

    UnionType unionType = NodeFactory.createUnionType(Arrays.asList(
        stringType, numberType));

    Node checkNode = runtimeTypeCheck.new AddChecks(null)
        .createCheckerNode(unionType);

    assertTrue(checkNode != null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerNodeForUnknownType() {
    Node checkNode = runtimeTypeCheck.new AddChecks(null)
        .createCheckerNode(NodeFactory.createUnknownType());

    assertFalse(checkNode != null);
  }
}