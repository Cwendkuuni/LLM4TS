package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("Parent", new ObjectType("Parent"));
    FunctionType childCtor = createFunctionType("Child", new ObjectType("Child"));

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals("Parent", childCtor.getPrototype().getClassName());
    assertEquals("constructor", childCtor.getPrototype().getDeclaredFieldNames().get(0));
    assertEquals("superClass_", childCtor.getPrototype().getDeclaredFieldNames().get(1));

    FunctionType superClassPrototype = parentCtor.getPrototype();
    assertEquals("Parent", superClassPrototype.getClassName());
    assertNull(superClassPrototype.getDeclaredFields());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createCallNode("Parent.inherits(Child)");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("Parent", relationship.getSuperclass().getName());
    assertEquals("Child", relationship.getSubclass().getName());
  }

  @Test
  public void testGetclasssDefinedByCallDeprecated() {
    Node callNode = createCallNode("Parent.inherits(Child)");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("Parent", relationship.getSuperclass().getName());
    assertEquals("Child", relationship.getSubclass().getName());

    callNode = createCallNode("Parent.inherits(Child)"); // deprecated syntax

    codingConvention = new ClosureCodingConvention();
    relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("Parent", relationship.getSuperclass().getName());
    assertEquals("Child", relationship.getSubclass().getName());
  }

  @Test
  public void testGetclasssDefinedByCallMixin() {
    Node callNode = createCallNode("Child.prototype.mixin(Parent.prototype)");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertNull(relationship);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = createFunctionType("Getter", new ObjectType("Getter"));
    FunctionType functionType = createFunctionType("Function", new ObjectType("Function"));

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySingletonGetter(functionType, getterType, new ObjectType("Function"));

    assertEquals("getInstance", functionType.getDeclaredMethods().get(0).getName());
    assertEquals("instance_", functionType.getDeclaredFields().get(0).getName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createCallNode("goog.provide('Foo')");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String className = codingConvention.extractClassNameIfProvide(node, null);

    assertEquals("Foo", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createCallNode("goog.require('Foo')");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String className = codingConvention.extractClassNameIfRequire(node, null);

    assertEquals("Foo", className);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createCallNode("goog.addDependency('Foo', ['Bar', 'Baz'])");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(node);

    assertEquals(ImmutableList.of("Bar", "Baz"), typeNames);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = createCallNode("goog.isString(x)");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertTrue(codingConvention.isPropertyTestFunction(callNode));
  }

  @Test
  public void testIsPropertyTestFunctionWithInvalidCall() {
    Node callNode = createCallNode("goog.isString");

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertFalse(codingConvention.isPropertyTestFunction(callNode));
  }

  private Node createCallNode(String qualifiedName) {
    Node callName = createNode("call", new ObjectType("Call"), null);
    Node callTarget = createNode("getprop", new ObjectType("Getprop"), null);
    callTarget.setQualifiedName(qualifiedName);
    callName.setFirstChild(callTarget);

    return callName;
  }

  private Node createNode(String type, ObjectType objectType, Node next) {
    Node node = new Node();
    node.setType(Token.getToken(type));
    node.setParent(next);
    node.setClassName(objectType.getName());
    return node;
  }

  private FunctionType createFunctionType(String name, ObjectType objectType) {
    FunctionType functionType = new FunctionType(name, objectType, null);
    return functionType;
  }

  private static SubclassType typeofClassDefiningName(Node callName) {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    return codingConvention.typeofClassDefiningName(callName);
  }

  @Test
  public void testTypeofClassDefiningNameInherits() {
    Node callName = createNode("name", new ObjectType("Name"), null);
    callName.setString("inherits");

    assertEquals(SubclassType.INHERITS, typeofClassDefiningName(callName));
  }

  @Test
  public void testTypeofClassDefiningNameMixin() {
    Node callName = createNode("name", new ObjectType("Name"), null);
    callName.setString("mixin");

    assertEquals(SubclassType.MIXIN, typeofClassDefiningName(callName));
  }

  @Test
  public void testTypeofClassDefiningNameInvalid() {
    Node callName = createNode("name", new ObjectType("Name"), null);
    callName.setString("invalid");

    assertNull(typeofClassDefiningName(callName));
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testIsSuperClassReferenceInvalid() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertFalse(codingConvention.isSuperClassReference("SuperClass"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = createNode("getprop", new ObjectType("Getprop"), null);
    qualifiedName.setQualifiedName("a.b.c.prototype");

    assertTrue(new ClosureCodingConvention().endsWithPrototype(qualifiedName));
  }

  @Test
  public void testEndsWithPrototypeInvalid() {
    Node qualifiedName = createNode("getprop", new ObjectType("Getprop"), null);
    qualifiedName.setQualifiedName("a.b.c");

    assertFalse(new ClosureCodingConvention().endsWithPrototype(qualifiedName));
  }
}