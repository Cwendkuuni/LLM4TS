package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testSubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    SubclassType relationshipType = ClosureCodingConvention.SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, relationshipType);

    assertHasProperty(childCtor, "superClass_", parentCtor);
    assertHasProperty(childCtor, "constructor", childCtor);
  }

  @Test
  public void testSubclassRelationship_mixin() {
    FunctionType parentPrototype = createMockFunctionType("ParentPrototype");
    FunctionType childPrototype = createMockFunctionType("ChildPrototype");
    SubclassType relationshipType = ClosureCodingConvention.SubclassType.MIXIN;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(null, parentPrototype, relationshipType);
    convention.applySubclassRelationship(childPrototype, null, relationshipType);

    assertHasProperty(childPrototype, "constructor", parentPrototype);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node inheritsCall = createMockCallNode("SubClass.inherits(SuperClass)");
    Node mixinCall = createMockCallNode("SubClass.mixin(SuperClass.prototype)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(inheritsCall);
    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());

    relationship = convention.getclasssDefinedByCall(mixinCall);
    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("SubClass.prototype", relationship.getSubclassName());
    assertEquals("SuperClass.prototype", relationship.getSuperclassName());
  }

  @Test
  public void testGetclasssDefinedByCall_invalid() {
    Node callNode = createMockCallNode("SubClass.inherits(SuperClass, AnotherClass)");
    Node callNode2 = createMockCallNode("SuperClass.inherits(SubClass)");
    Node callNode3 = createMockCallNode("SubClass.mixin(SuperClass).prototype");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertNull(convention.getclasssDefinedByCall(callNode));
    assertNull(convention.getclasssDefinedByCall(callNode2));
    assertNull(convention.getclasssDefinedByCall(callNode3));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node provideCall = createMockCallNode("goog.provide('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(provideCall, null);
    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node requireCall = createMockCallNode("goog.require('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(requireCall, null);
    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfGoog_invalid() {
    Node callNode = createMockCallNode("goog.foo('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfGoog(callNode, null, "goog.foo");
    assertNull(className);
  }

  private Node createMockCallNode(String nodeString) {
    Node node = Node.newNode(Token.CALL);
    Node callName = Node.newNode(Token.GETPROPNAME, "CallingClass", "methodName");
    Node args = Node.newNode(Token.LIST);
    Node classNameNode = Node.newNode(Token.STRING, "ClassName");
    args.addChild(callName);
    args.addChild(classNameNode);
    node.addChild(args);
    return Node.newNode(Token.GETPROP, "goog", nodeString).addChild(node);
  }

  private void assertHasProperty(FunctionType type, String propertyName,
      FunctionType expectedType) {
    Node property = type.getPrototype().getDeclaredProperty(propertyName);
    assertEquals(expectedType, property.getType());
  }

  private void assertNotNull(SubclassRelationship relationship) {
    assertNotNull(relationship.getType());
    assertNotNull(relationship.getSubclassName());
    assertNotNull(relationship.getSuperclassName());
  }

  private void assertNull(SubclassRelationship relationship) {
    assertNull(relationship.getType());
    assertNull(relationship.getSubclassName());
    assertNull(relationship.getSuperclassName());
  }
}