package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

/**
 * Test cases for ClosureCodingConvention.
 */
public class ClosureCodingConventionTest {

  private static final ClosureCodingConvention CONVENTION = new ClosureCodingConvention();

  @org.junit.Test
  public void testSubclassRelationship() {
    FunctionType parentCtor = createFunctionType("Parent", ImmutableList.of());
    FunctionType childCtor = createFunctionType("Child", ImmutableList.of());
    SubclassType relationshipType = SubclassType.INHERITS;
    CONVENTION.applySubclassRelationship(parentCtor, childCtor, relationshipType);
    FunctionType childProto = childCtor.getPrototype();
    assertEquals("Parent", childProto.getName());
    assertEquals(parentCtor, childProto.getConstructor());

    relationshipType = SubclassType.MIXIN;
    CONVENTION.applySubclassRelationship(parentCtor, childCtor, relationshipType);
    assertEquals("Parent", childProto.getName());
    assertEquals(parentCtor, childProto.getPrototype().getConstructor());
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('X')");
    assertEquals("X", CONVENTION.extractClassNameIfProvide(node, null));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('X')");
    assertEquals("X", CONVENTION.extractClassNameIfRequire(node, null));
  }

  @org.junit.Test
  public void testSubclassRelationshipWithPrototype() {
    FunctionType parentCtor = createFunctionType("Parent", ImmutableList.of());
    FunctionType childCtor = createFunctionType("Child", ImmutableList.of());
    Node parentProto = createObjectType("ParentPrototype", null);
    parentCtor.setPrototype(parentProto);
    Node childProto = createObjectType("ChildPrototype", null);
    childCtor.setPrototype(childProto);
    SubclassRelationship relationship = CONVENTION.getclasssDefinedByCall(
        createCallNode("goog.inherits", parentCtor, childCtor));
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("Parent", relationship.getSubclass());
    assertEquals("ParentPrototype", relationship.getSuperclass());
  }

  @org.junit.Test
  public void testSubclassRelationshipWithMixin() {
    FunctionType parentCtor = createFunctionType("Parent", ImmutableList.of());
    FunctionType childCtor = createFunctionType("Child", ImmutableList.of());
    Node parentProto = createObjectType("ParentPrototype", null);
    parentCtor.setPrototype(parentProto);
    Node childProto = createObjectType("ChildPrototype", null);
    childCtor.setPrototype(childProto);
    SubclassRelationship relationship = CONVENTION.getclasssDefinedByCall(
        createCallNode("goog.mixin", parentProto, childProto));
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildPrototype", relationship.getSubclass());
    assertEquals("ParentPrototype", relationship.getSuperclass());
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    assertEquals("superClass_", CONVENTION.isSuperClassReference("superClass_"));
    assertNull(CONVENTION.isSuperClassReference("someOtherProperty"));
  }

  @org.junit.Test
  public void testEndsWithPrototype() {
    Node qualifiedName = createNode("a.b.c");
    assertFalse(CONVENTION.endsWithPrototype(qualifiedName));

    qualifiedName = createNode("a.b.c.prototype");
    assertTrue(CONVENTION.endsWithPrototype(qualifiedName));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node callNode = createCallNode("goog.isDef");
    assertTrue(CONVENTION.isPropertyTestFunction(callNode));
  }

  @org.junit.Test
  public void testIsObjectType() {
    ObjectType objectType = createObjectType("MyObjectType", null);
    assertTrue(CONVENTION.isObjectType(objectType));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node callNode = createCallNode("goog.reflect.object.getPrototypeOf",
        createObjectType("MyObjectType", null), null);
    ObjectLiteralCast expected = new ObjectLiteralCast(
        "goog.reflect.object.ObjectType",
        createObjectLiteral(ImmutableList.of("__proto__"), null));
    ObjectLiteralCast actual = CONVENTION.getObjectLiteralCast(
        new NodeTraversal(), callNode);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", CONVENTION.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node callNode = createCallNode("goog.addSingletonGetter",
        createNode("MySingletonGetter"), null);
    assertEquals("MySingletonGetter", CONVENTION.getSingletonGetterClassName(callNode));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("MyFunction",
        ImmutableList.of("value"));
    FunctionType getterType = createFunctionType("MyGetter", ImmutableList.of());
    ObjectType objectType = createObjectType("MyObject", null);

    CONVENTION.applySingletonGetter(functionType, getterType, objectType);
    FunctionType prototype = functionType.getPrototype();
    assertEquals("MyObject", prototype.getName());
    assertEquals(getterType, prototype.getConstructor());
  }

  @org.junit.Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", CONVENTION.getGlobalObject());
  }

  @org.junit.Test
  public void testIsPropertyTestFunctionWithPrototype() {
    Node callNode = createCallNode("goog.isObject", createNode("x"));
    assertTrue(CONVENTION.isPropertyTestFunction(callNode));

    Node prototype = createObjectType("Prototype", null);
    Node prototypeCallNode = createCallNode("goog.isObject", prototype);
    assertTrue(CONVENTION.isPropertyTestFunction(prototypeCallNode));
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    FunctionType fnType = createFunctionType("MyFunction",
        ImmutableList.of("x"));
    Node bindNode = createCallNode("goog.bind", fnType, null, null);
    assertEquals("MyFunction", CONVENTION.describeFunctionBind(bindNode)
        .getFunction().getName());
    assertEquals(null, CONVENTION.describeFunctionBind(bindNode).getThis());
    assertEquals(ImmutableList.of(), CONVENTION.describeFunctionBind(bindNode)
        .getArguments());

    FunctionType fnTypeWithThis = createFunctionType("MyFunction",
        ImmutableList.of("thisArg", "x"));
    Node bindNodeWithThis = createCallNode("goog.bind", fnTypeWithThis,
        createNode("thisArg"), null);
    assertEquals("MyFunction", CONVENTION.describeFunctionBind(bindNodeWithThis)
        .getFunction().getName());
    assertEquals(createNode("thisArg"), CONVENTION.describeFunctionBind(bindNodeWithThis)
        .getThis());
    assertEquals(ImmutableList.of("x"), CONVENTION.describeFunctionBind(bindNodeWithThis)
        .getArguments());
  }

  private FunctionType createFunctionType(String name, List<String> arguments) {
    ObjectType objectType = createObjectType(name, null);
    return new FunctionType(name, ImmutableList.copyOf(arguments), objectType);
  }

  private ObjectType createObjectType(String name, Node prototype) {
    return new ObjectType(name, prototype);
  }

  private Node createObjectType(String name, Node prototype) {
    return new Node(Token.OBJECT);
  }

  private Node createNode(String string) {
    return new Node(Token.STRING, string);
  }

  private Node createCallNode(String functionName, Node... arguments) {
    Node callNode = new Node(Token.CALL);
    Node callTarget = new Node(Token.GETPROP, null, new Node(Token.NAME, functionName));
    callNode.addChild(callTarget);
    for (Node argument : arguments) {
      callNode.addChild(argument);
    }
    return callNode;
  }

  private Node createObjectLiteral(List<Node> properties, Node prototype) {
    Node objectNode = new Node(Token.OBJECT);
    for (Node property : properties) {
      objectNode.addChild(property);
    }
    if (prototype != null) {
      objectNode.setPrototype(prototype);
    }
    return objectNode;
  }
}