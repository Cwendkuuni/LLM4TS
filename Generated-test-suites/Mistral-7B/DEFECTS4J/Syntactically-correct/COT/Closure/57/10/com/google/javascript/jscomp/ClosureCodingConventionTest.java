package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.util.List;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentClass", new ObjectType("Object"),
        ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildClass", new ObjectType("Object"),
        ImmutableList.of());

    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    FunctionType childPrototype = childCtor.getPrototype();
    ObjectType constructor = childPrototype.getDeclaredProperties().get("constructor");
    FunctionType superClass = childPrototype.getDeclaredProperties().get("superClass_");

    assertEquals(parentCtor, superClass);
    assertEquals(childPrototype, constructor.getValueType());
  }

  @Test(expected = AssertionError.class)
  public void testApplySubclassRelationshipWithInvalidArguments() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentClass", new ObjectType("Object"),
        ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildClass", new ObjectType("Object"),
        ImmutableList.of());

    convention.applySubclassRelationship(parentCtor, childCtor, null);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.newCall("goog.inherits", Node.newName("SubClass"),
        Node.newName("SuperClass"));

    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCallWithMixin() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.newCall("goog.mixin", Node.newName("SubClass"),
        Node.newName("SuperClass.prototype"));

    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("SubClass", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.newCall("goog.provide", Node.newString("MyClass"));
    assertEquals("MyClass", convention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.newCall("goog.require", Node.newString("MyClass"));
    assertEquals("MyClass", convention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.exportSymbol", convention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.newCall(Node.newName("goog.addDependency"),
        Node.newArrayLiteral(Node.newString("MyClass"), Node.newNull()));

    List<String> typeNames = convention.identifyTypeDeclarationCall(node);
    assertEquals(1, typeNames.size());
    assertEquals("MyClass", typeNames.get(0));
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.abstractMethod", convention.getAbstractMethodName());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.newCall("goog.addSingletonGetter",
        Node.newName("MyClass.prototype", "myProperty"));

    String className = convention.getSingletonGetterClassName(callNode);
    assertEquals("MyClass", className);
  }

  @Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    FunctionType functionType = new FunctionType("MyClass", new ObjectType("Object"),
        ImmutableList.of());
    ObjectType getterType = new ObjectType("GetterFunction", new FunctionType("getValue",
        new ObjectType("Object"), ImmutableList.of()));
    ObjectType objectType = new ObjectType("MyValueObject", ImmutableList.of());

    convention.applySingletonGetter(functionType, getterType, objectType);

    FunctionType instance = functionType.getPrototype();
    ObjectType instancePrototype = instance.getPrototype();
    FunctionType getter = instancePrototype.getDeclaredProperties().get("getInstance");
    ObjectType getterInstance = getter.getValueType();

    assertEquals(getterType, getterInstance);
    assertEquals(functionType, getter.getReturnType());
    assertEquals(objectType, instancePrototype.getDeclaredProperties().get("instance_"));
  }

  @Test
  public void testGetGlobalObject() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.global", convention.getGlobalObject());
  }

  @Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.newCall(Node.newName("goog.isDef"), Node.newNull());

    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.newCall("goog.reflect.object", Node.newString("MyObject"),
        Node.newObjectLiteral(ImmutableList.of(
            Node.newName("name", Node.newString("John")),
            Node.newName("age", Node.newNumber(30)))));

    ObjectLiteralCast cast = convention.getObjectLiteralCast(NodeTraversal.EMPTY, callNode);

    assertEquals("MyObject", cast.getType());
    assertEquals(Node.newObjectLiteral(ImmutableList.of(
        Node.newName("name", Node.newString("John")),
        Node.newName("age", Node.newNumber(30)))), cast.getValue());
  }

  @Test(expected = AssertionError.class)
  public void testGetObjectLiteralCastWithInvalidCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.newCall("goog.reflect.object", Node.newNull(),
        Node.newObjectLiteral(ImmutableList.of()));

    ObjectLiteralCast cast = convention.getObjectLiteralCast(NodeTraversal.EMPTY, callNode);
  }

  @Test
  public void testPropertyTestFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.newCall(Node.newName("goog.isDef"), Node.newNull());

    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testDescribeFunctionBind() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node callNode = Node.newCall("goog.bind", Node.newName("MyFunction"),
        Node.newNull(), Node.newArrayLiteral(Node.newNull(), Node.newBoolean(true)));

    Bind bind = convention.describeFunctionBind(callNode);

    assertEquals(Node.newName("MyFunction"), bind.getFunction());
    assertEquals(Node.newNull(), bind.getThisValue());
    assertEquals(Node.newArrayLiteral(Node.newNull(), Node.newBoolean(true)), bind.getParameters());
  }
}