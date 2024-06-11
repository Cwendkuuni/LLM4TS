package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testSubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass");
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("ParentClass", childCtor.getPrototype().getClassName());
    assertEquals("ParentClass", childCtor.getPrototype().getSuperclass());
    assertEquals("ParentClass", childCtor.getSuperclass().getClassName());
    assertEquals("ChildClass", childCtor.getClassName());

    // Test with mixin relationship
    type = SubclassType.MIXIN;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("ParentClass", childCtor.getPrototype().getClassName());
    assertEquals("ParentClass", childCtor.getPrototype().getSuperclass());
    assertEquals("ChildClass", childCtor.getClassName());
    assertNull(childCtor.getSuperclass());
  }

  @Test
  public void testExtractClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.provide('MyClass')");
    assertEquals("MyClass", codingConvention.extractClassNameIfProvide(node, null));

    node = createNode("goog.require('MyClass')");
    assertEquals("MyClass", codingConvention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", codingConvention.getExportPropertyFunction());
  }

  @Test
  public void testSingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createNode("goog.addSingletonGetter(MySingleton, 'myGetter')");
    assertEquals("MySingleton", codingConvention.getSingletonGetterClassName(callNode));
  }

  @Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType functionType = createFunctionType("MySingleton");
    FunctionType getterType = createFunctionType("MySingleton.myGetter");
    ObjectType objectType = new ObjectType("MySingleton");

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(getterType, functionType.getPrototype().getDeclaredProperty("getInstance"));
    assertEquals(objectType, functionType.getPrototype().getDeclaredProperty("instance_"));
  }

  @Test
  public void testPropertyTestFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node call = createNode("goog.isString('abc')");
    assertTrue(codingConvention.isPropertyTestFunction(call));

    Node anotherCall = createNode("goog.isNumber(123)");
    assertTrue(codingConvention.isPropertyTestFunction(anotherCall));
  }

  @Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.reflect.object.cast(MyObject, MyType)");
    ObjectLiteralCast expected = new ObjectLiteralCast("MyType", node.getLastChild());
    ObjectLiteralCast result = codingConvention.getObjectLiteralCast(null, node);
    assertEquals(expected, result);
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertFalse(codingConvention.isPrivate("name"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = createNode("goog.addDependency(['MyClass1', 'MyClass2'])");
    List<String> expected = ImmutableList.of("MyClass1", "MyClass2");
    List<String> result = codingConvention.identifyTypeDeclarationCall(node);
    assertEquals(expected, result);
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.abstractMethod", codingConvention.getAbstractMethodName());
  }

  @Test
  public void testSubclassRelationshipWithPrototype() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass.prototype");
    SubclassType type = SubclassType.MIXIN;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("ParentClass.prototype", childCtor.getPrototype().getClassName());
    assertEquals("ParentClass", childCtor.getPrototype().getSuperclass().getClassName());
    assertEquals("ChildClass.prototype", childCtor.getClassName());
    assertNull(childCtor.getSuperclass());
  }

  @Test
  public void testSubclassRelationshipWithPrototypeAndSuperclass() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass.prototype");
    FunctionType superclassCtor = createFunctionType("SuperClass");
    SubclassType type = SubclassType.MIXIN;

    codingConvention.applySubclassRelationship(parentCtor, superclassCtor, SubclassType.INHERITS);
    codingConvention.applySubclassRelationship(superclassCtor, childCtor, type);

    assertEquals("ParentClass", childCtor.getPrototype().getClassName());
    assertEquals("ParentClass", childCtor.getPrototype().getSuperclass().getClassName());
    assertEquals("SuperClass", superclassCtor.getPrototype().getSuperclass().getClassName());
    assertEquals("ChildClass.prototype", childCtor.getClassName());
    assertNull(childCtor.getSuperclass());
  }

  @Test
  public void testBindFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createNode("goog.bind(myFunction, { foo: 'bar' }, 1)");
    Bind expected = new Bind("myFunction", createNode("{ foo: 'bar' }"),
        createNode(Integer.toString(1)));
    Bind result = codingConvention.describeFunctionBind(callNode);
    assertEquals(expected, result);
  }

  @Test
  public void testPartialFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node callNode = createNode("goog.partial(myFunction, 1, 2)");
    Bind expected = new Bind("myFunction", null,
        createNode(Integer.toString(1)), createNode(Integer.toString(2)));
    Bind result = codingConvention.describeFunctionBind(callNode);
    assertEquals(expected, result);
  }

  private FunctionType createFunctionType(String name) {
    Node node = createNode("function " + name + "() {}");
    return new FunctionType(node.getQualifiedName(), new ObjectType(name));
  }

  private Node createNode(String nodeString) {
    return NodeUtil.createNodeFromString(nodeString, null);
  }

  private void assertTrue(boolean condition) {
    assertEquals(true, condition);
  }

  private void assertFalse(boolean condition) {
    assertEquals(false, condition);
  }
}