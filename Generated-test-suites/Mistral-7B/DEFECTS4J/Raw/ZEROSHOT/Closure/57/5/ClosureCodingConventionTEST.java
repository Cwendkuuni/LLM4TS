package com.example.junit;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention = new ClosureCodingConvention();

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType();
    FunctionType childCtor = createMockFunctionType();
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType expectedParentPrototype = new FunctionType(
        "anonymous", JSTypeNative.OBJECT_TYPE, ImmutableList.of());
    FunctionType expectedChildPrototype = new FunctionType(
        "anonymous", JSTypeNative.OBJECT_TYPE, ImmutableList.of());
    FunctionType expectedChild = new FunctionType(
        "anonymous", JSTypeNative.FUNCTION_INSTANCE_TYPE,
        ImmutableList.of(JSTypeNative.OBJECT_TYPE, JSTypeNative.OBJECT_TYPE));

    assertEquals(expectedParentPrototype, parentCtor.getPrototype());
    assertEquals(expectedChildPrototype, childCtor.getPrototype());
    assertEquals(expectedChild, childCtor);

    ObjectType expectedChildPrototypeObject = new ObjectType(
        "anonymous");
    expectedChildPrototypeObject.defineDeclaredProperty("constructor",
        childCtor, parentCtor.getSource());

    assertEquals(expectedChildPrototypeObject, childCtor.getPrototype());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createMockNode("goog.inherits(MySubClass, SuperClass)");
    SubclassType expectedType = SubclassType.INHERITS;
    Node subclass = createMockNode("MySubClass");
    Node superclass = createMockNode("SuperClass");

    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertEquals(expectedType, relationship.getType());
    assertEquals(subclass, relationship.getSubclass());
    assertEquals(superclass, relationship.getSuperclass());
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = createMockNode("goog.provide('MyClass')");
    String expectedClassName = "MyClass";

    String extractedClassName = codingConvention.extractClassNameIfProvide(node, null);

    assertEquals(expectedClassName, extractedClassName);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = createMockNode("goog.require('MyClass')");
    String expectedClassName = "MyClass";

    String extractedClassName = codingConvention.extractClassNameIfRequire(node, null);

    assertEquals(expectedClassName, extractedClassName);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    String superClassReference = "superClass_";

    assertTrue(codingConvention.isSuperClassReference(superClassReference));
  }

  @org.junit.Test
  public void testEndsWithPrototype() {
    Node qualifiedName = createMockNode("a.b.c.prototype");

    assertTrue(codingConvention.endsWithPrototype(qualifiedName));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    String expectedFunctionName = "goog.exportProperty";

    String actualFunctionName = codingConvention.getExportPropertyFunction();

    assertEquals(expectedFunctionName, actualFunctionName);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node callNode = createMockNode("goog.addSingletonGetter(MyClass, getInstance)");
    String expectedSingletonGetterClassName = "getInstance";

    String actualSingletonGetterClassName = codingConvention.getSingletonGetterClassName(callNode);

    assertEquals(expectedSingletonGetterClassName, actualSingletonGetterClassName);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createMockFunctionType();
    FunctionType getterType = createMockFunctionType();
    ObjectType objectType = new ObjectType("anonymous");

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    FunctionType expectedInstance = new FunctionType(
        "getInstance", JSTypeNative.FUNCTION_INSTANCE_TYPE,
        ImmutableList.of());
    FunctionType expectedInstanceObject = new FunctionType(
        "getInstance", JSTypeNative.FUNCTION_INSTANCE_TYPE,
        ImmutableList.of());
    FunctionType expectedPrototype = new FunctionType(
        "anonymous", JSTypeNative.OBJECT_TYPE, ImmutableList.of());
    ObjectType expectedObject = new ObjectType("anonymous");

    expectedObject.defineDeclaredProperty("getInstance", expectedInstance,
        functionType.getSource());
    expectedObject.defineDeclaredProperty("instance_", expectedInstanceObject,
        functionType.getSource());

    assertEquals(expectedPrototype, functionType.getPrototype());
    assertEquals(expectedObject, functionType);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    String functionName = "goog.isDef";
    Node callNode = createMockNode("goog.isDef(someValue)");

    assertTrue(codingConvention.isPropertyTestFunction(callNode));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node callNode = createMockNode("goog.reflect.object(someObject)");
    String expectedTypeName = "Object";
    Node expectedObjectLiteral = createMockNode("{a: 1, b: 2}");

    ObjectLiteralCast expectedObjectLiteralCast = codingConvention.getObjectLiteralCast(
        null, callNode);

    assertEquals(expectedTypeName, expectedObjectLiteralCast.getTypeName());
    assertEquals(expectedObjectLiteral, expectedObjectLiteralCast.getNode());
  }

  private FunctionType createMockFunctionType() {
    return new FunctionType("anonymous", JSTypeNative.FUNCTION_INSTANCE_TYPE,
        ImmutableList.of());
  }

  private Node createMockNode(String nodeString) {
    Token token = new Token(Token.KEYWORD, nodeString, null, null);
    Node node = new Node(token);
    return node;
  }

  enum SubclassType {
    INHERITS,
    MIXIN
  }

  static class SubclassRelationship {
    final SubclassType type;
    final Node subclass;
    final Node superclass;

    SubclassRelationship(SubclassType type, Node subclass, Node superclass) {
      this.type = type;
      this.subclass = subclass;
      this.superclass = superclass;
    }
  }

  static class AssertionFunctionSpec {
    final String name;
    final JSType type;

    AssertionFunctionSpec(String name, JSType type) {
      this.name = name;
      this.type = type;
    }
  }

  static class Bind {
    final Node function;
    final Node thisValue;
    final Node parameters;

    Bind(Node function, Node thisValue, Node parameters) {
      this.function = function;
      this.thisValue = thisValue;
      this.parameters = parameters;
    }
  }
}