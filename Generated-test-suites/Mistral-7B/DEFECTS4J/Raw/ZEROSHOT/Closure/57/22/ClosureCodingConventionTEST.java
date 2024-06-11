package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  private static final String CLASS_NAME = "MyClass";
  private static final String SUPER_CLASS_NAME = "SuperClass";

  @org.junit.Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = createFunctionType(CLASS_NAME + "_parent");
    FunctionType childCtor = createFunctionType(CLASS_NAME);
    SubclassType type = SubclassType.INHERITS;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().constructor);
  }

  @org.junit.Test
  public void testApplySubclassRelationship_MIXIN() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = createFunctionType(SUPER_CLASS_NAME + "_prototype");
    FunctionType childCtor = createFunctionType(CLASS_NAME);
    SubclassType type = SubclassType.MIXIN;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(parentCtor.getPrototype(), childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().constructor);
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_INHERITS() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode(
        "goog.inherits",
        createNode(CLASS_NAME),
        createNode(SUPER_CLASS_NAME)
    );
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.type);
    assertEquals(CLASS_NAME, relationship.subclass);
    assertEquals(SUPER_CLASS_NAME, relationship.superclass);
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_MIXIN() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode(
        "goog.mixin",
        createNode(CLASS_NAME + "_prototype"),
        createNode(SUPER_CLASS_NAME + "_prototype")
    );
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_MIXIN_SubClass() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode(
        "goog.mixin",
        createNode(CLASS_NAME),
        createNode(SUPER_CLASS_NAME + "_prototype")
    );
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("superClass_", codingConvention.isSuperClassReference("superClass_"));
    assertNull(codingConvention.isSuperClassReference("anotherProperty"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode("goog.provide", createNode(CLASS_NAME));
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals(CLASS_NAME, className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode("goog.require", createNode(CLASS_NAME));
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals(CLASS_NAME, className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", codingConvention.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode("goog.addSingletonGetter",
        createNode(CLASS_NAME + "_singletonProperty"));
    String className = codingConvention.getSingletonGetterClassName(node);
    assertEquals(CLASS_NAME, className);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType functionType = createFunctionType(CLASS_NAME);
    FunctionType getterType = createFunctionType(CLASS_NAME + "_getter");
    ObjectType objectType = createObjectType(CLASS_NAME);
    codingConvention.applySingletonGetter(functionType, getterType, objectType);
    FunctionType prototype = functionType.getPrototype();
    assertEquals(getterType, prototype.getDeclaredProperties().get("getInstance"));
    assertEquals(objectType, prototype.getDeclaredProperties().get("instance_"));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode("goog.isDef", createNode(true));
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    NodeTraversal traversal = new NodeTraversal();
    Node node = createNode(
        "goog.reflect.object",
        createNode("cast"),
        createNode("{foo: 'bar'}"),
        createNode("MyType")
    );
    ObjectLiteralCast cast = codingConvention.getObjectLiteralCast(traversal, node);
    assertEquals("MyType", cast.typeName);
    assertEquals("{foo: 'bar'}", cast.objectLiteral.toString());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode("function(optionalParam) {}");
    assertFalse(codingConvention.isOptionalParameter(node.getFirstChild()));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode("function(varArgs) {var args = arguments;}");
    assertFalse(codingConvention.isVarArgsParameter(node.getFirstChild()));
  }

  @org.junit.Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertFalse(codingConvention.isPrivate("publicProperty"));
  }

  @org.junit.Test
  public void testAssertionFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> assertionFunctions = codingConvention.getAssertionFunctions();
    assertEquals(8, assertionFunctions.size());
  }

  @org.junit.Test
  public void testBind() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode(
        "goog.bind",
        createNode("function(a, b) {return a + b;}"),
        createNode(createNode("thisValue")),
        createNode(createNode("args"))
    );
    Bind bind = codingConvention.describeFunctionBind(node);
    FunctionType fn = bind.functionType;
    Object func = fn.getPrototype().call(bind.thisValue, bind.argsToNodes.get(0));
    assertEquals(createNode("thisValue"), func.call(createNode("a"), createNode("b")));
  }

  @org.junit.Test
  public void testPartial() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createNode(
        "goog.partial",
        createNode("function(a, b, c) {return a + b + c;}"),
        createNode(createNode("a"), createNode("b"))
    );
    Bind bind = codingConvention.describeFunctionBind(node);
    FunctionType fn = bind.functionType;
    Object func = fn.getPrototype().call(null, bind.argsToNodes.get(0), bind.argsToNodes.get(1));
    assertEquals(createNode("a"), func.call(createNode("x"), createNode("y")));
  }

  private FunctionType createFunctionType(String name) {
    return new FunctionType(name, ImmutableList.<FunctionType>of(), ObjectType.OBJECT, ImmutableSet.<String>of());
  }

  private ObjectType createObjectType(String name) {
    return new ObjectType(name, ImmutableSet.<String>of());
  }

  private Node createNode(String name, Node... childNodes) {
    Object value = name == null ? null : createQualifiedName(name);
    return new Node(name != null ? Token.NAME : Token.NULL, value, childNodes);
  }

  private Node createQualifiedName(String name) {
    String[] parts = name.split("\\.");
    Node result = parts[0].isEmpty() ? Token.NULL : new Node(Token.NAME, parts[0]);
    for (int i = 1; i < parts.length; ++i) {
      result = new Node(Token.GETPROP, result, new Node(Token.NAME, parts[i]));
    }
    return result;
  }
}