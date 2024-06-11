package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.CodingConvention.SubclassType;
import com.google.javascript.jscomp.CodingConvention.SubclassRelationship;

public class ClosureCodingConventionTest {

  private static CodingConvention codingConvention = new ClosureCodingConvention();

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node("goog.provide('MyClass')");
    Node parent = new Node("(function() { })");
    assertEquals("MyClass", codingConvention.extractClassNameIfProvide(node, parent));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = new Node("goog.require('MyClass')");
    Node parent = new Node("(function() { })");
    assertEquals("MyClass", codingConvention.extractClassNameIfRequire(node, parent));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        codingConvention.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = new Node("goog.addSingletonGetter(MyClass, 'getProperty')");
    assertEquals("getProperty", codingConvention.getSingletonGetterClassName(node));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyClass", ImmutableList.<FunctionType>of());
    ObjectType objectType = new ObjectType("MyClass");
    codingConvention.applySingletonGetter(functionType, null, objectType);
    FunctionType getterType = functionType.getDeclaredProperty("getInstance");
    FunctionType instanceType = getterType.getReturnType();
    ObjectType instanceObjectType = instanceType.getPrototype();
    assertTrue(instanceObjectType.hasProperty("instance_"));
  }

  @org.junit.Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", codingConvention.getGlobalObject());
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = new Node("goog.isString(value)");
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node node = new Node("goog.reflect.object(value)");
    ObjectLiteralCast result = codingConvention.getObjectLiteralCast(
        new NodeTraversal(), node);
    assertNotNull(result);
    assertEquals("value", result.getType());
    assertEquals(node.getNext(), result.getValue());
  }

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentClass",
        ImmutableList.<FunctionType>of());
    FunctionType childCtor = new FunctionType("ChildClass",
        ImmutableList.<FunctionType>of());
    SubclassType type = codingConvention.getclasssDefinedByCall(
        new Node("goog.inherits(ChildClass, ParentClass)"));
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);
    ObjectType childPrototype = childCtor.getPrototype();
    ObjectType parentPrototype = parentCtor.getPrototype();
    assertTrue(childPrototype.hasProperty("superClass_"));
    FunctionType superClass = (FunctionType) childPrototype.getProperty("superClass_");
    assertEquals(parentPrototype, superClass.getPrototype());
    assertEquals(childPrototype, superClass.getDeclaredProperties().get("constructor"));
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = new Node("goog.addDependency('MyModule', ['Type1', 'Type2'])");
    List<String> types = codingConvention.identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("Type1", "Type2"), types);
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    Node node = new Node("goog.bind(function() {}, this, 'arg1')");
    Bind result = codingConvention.describeFunctionBind(node);
    assertNotNull(result);
    assertEquals("function() {}", result.getFunction());
    assertEquals("this", result.getThis());
    assertEquals("arg1", result.getArguments());
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @org.junit.Test
  public void testEndsWithPrototype() {
    Node node = new Node("a.b.c.prototype");
    assertTrue(codingConvention.endsWithPrototype(node));
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        codingConvention.getAbstractMethodName());
  }
}