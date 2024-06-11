package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {

  private static final CodingConvention codingConvention = new ClosureCodingConvention();

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    Node prototype = childCtor.getPrototype();
    FunctionType constructor = prototype.getDeclaredProperty("constructor");
    assertSame(childCtor, constructor);
    Node superClass_ = prototype.getDeclaredProperty("superClass_");
    assertSame(parentCtor, superClass_);
  }

  @org.junit.Test
  public void testApplySubclassRelationshipWithMixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype");
    FunctionType childPrototype = createFunctionType("ChildPrototype");
    FunctionType childCtor = createFunctionType("ChildCtor");
    SubclassType type = SubclassType.MIXIN;
    codingConvention.applySubclassRelationship(parentPrototype, childPrototype, type);

    FunctionType childCtorWithPrototype = createFunctionType("ChildCtorWithPrototype");
    codingConvention.applySubclassRelationship(childPrototype, childCtorWithPrototype, type);

    Node prototype = childCtorWithPrototype.getPrototype();
    FunctionType constructor = prototype.getDeclaredProperty("constructor");
    assertSame(childCtorWithPrototype, constructor);
    Node superClass_ = prototype.getDeclaredProperty("superClass_");
    assertSame(childPrototype, superClass_);
    Node parentPrototype_ = childPrototype.getDeclaredProperty("prototype");
    assertSame(parentPrototype, parentClass());
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('MyClass')");
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('MyClass')");
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    String functionName = codingConvention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", functionName);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = createNode("goog.addSingletonGetter(MyClass.myProperty, MyClass.prototype.getMyProperty)");
    String className = codingConvention.getSingletonGetterClassName(node);
    assertEquals("MyClass.prototype.getMyProperty", className);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createNode("goog.addDependency('MyType')");
    List<String> types = codingConvention.identifyTypeDeclarationCall(node);
    assertEquals(1, types.size());
    assertEquals("MyType", types.get(0));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = createNode("goog.isDef(x)");
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    FunctionType type = createFunctionType("MyType");
    Node node = createNode("goog.reflect.object.cast(x, MyType)");
    ObjectLiteralCast cast = codingConvention.getObjectLiteralCast(null, node);
    assertEquals("MyType", cast.getType());
    assertEquals(node.getNext(), cast.getValue());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    Node parameter = createNode("x?");
    assertFalse(codingConvention.isOptionalParameter(parameter));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    Node parameter = createNode("args...");
    assertTrue(codingConvention.isVarArgsParameter(parameter));
  }

  @org.junit.Test
  public void testIsPrivate() {
    String name = "privateVariable";
    assertFalse(codingConvention.isPrivate(name));
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog() {
    Node node = createNode("goog.require('MyClass')");
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog2() {
    Node node = createNode("goog.provide('MyClass')");
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    Node node = createNode("goog.bind(myFunction, thisObject, arg1, arg2)");
    Bind bind = codingConvention.describeFunctionBind(node);
    assertEquals(myFunction, bind.getFunction());
    assertEquals(thisObject, bind.getThisValue());
    assertEquals(Lists.newArrayList(arg1, arg2), bind.getArguments());
  }

  private FunctionType createFunctionType(String name) {
    ObjectType objectType = new ObjectType();
    objectType.setName(name);
    FunctionType functionType = new FunctionType(null, null, new ObjectType[0],
        null, null, objectType, null, null, null, null, null, null, null,
        null, null, null, null, null, null);
    return functionType;
  }

  private Node createNode(String nodeString) {
    return Node.parseTree(nodeString).getFirstChild();
  }

  private FunctionType parentClass() {
    return createFunctionType("ParentClass");
  }
}