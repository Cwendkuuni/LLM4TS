package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DefaultCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.test.util.NodeUtil;

import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  public void testApplySubclassRelationship() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    FunctionType parentCtor = new FunctionType("ParentCtor", ObjectType.ANY,
        ImmutableList.of(), ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ObjectType.ANY,
        ImmutableList.of(), ImmutableList.of());

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor,
        SubclassType.INHERITS);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());

    FunctionType parentCtor2 = new FunctionType("ParentCtor2", ObjectType.ANY,
        ImmutableList.of(), ImmutableList.of());
    FunctionType childCtor2 = new FunctionType("ChildCtor2", ObjectType.ANY,
        ImmutableList.of(), ImmutableList.of());

    codingConvention.applySubclassRelationship(parentCtor2, childCtor2,
        SubclassType.MIXIN);

    assertEquals(parentCtor2.getPrototype(), childCtor2.getPrototype());
    assertNull(childCtor2.getPrototype().getConstructor());
  }

  public void testGetclasssDefinedByCall() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    String parentClassName = "Parent";
    String childClassName = "Child";

    Node parentNode = Node.newQualifiedName(Node.newName(parentClassName));
    Node superclassNode = Node.newName("superClass_");
    Node parentCallNode = Node.newCall(parentNode, superclassNode);

    Node childNode = Node.newQualifiedName(Node.newName(childClassName));
    Node subclassNode = Node.newName("Child");
    Node childCallNode = Node.newCall(parentNode, childNode);

    Node mixinParentNode = Node.newQualifiedName(Node.newName("Parent"));
    Node mixinSuperclassNode = Node.newName("prototype");
    Node mixinParentCallNode = Node.newCall(Node.newName("goog.mixin"),
        mixinParentNode, mixinSuperclassNode);

    Node mixinChildNode = Node.newQualifiedName(Node.newName(childClassName)
        + ".prototype");
    Node mixinChildCallNode = Node.newCall(Node.newName("goog.mixin"),
        mixinChildNode, mixinSuperclassNode);

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();

    SubclassRelationship relationship = codingConvention
        .getclasssDefinedByCall(parentCallNode);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals(childNode, relationship.getSubclass());
    assertEquals(parentNode, relationship.getSuperclass());

    relationship = codingConvention.getclasssDefinedByCall(childCallNode);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals(parentNode, relationship.getSubclass());
    assertEquals(parentNode, relationship.getSuperclass());

    relationship = codingConvention.getclasssDefinedByCall(mixinParentCallNode);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals(mixinChildNode, relationship.getSubclass());
    assertEquals(mixinParentNode, relationship.getSuperclass());

    relationship = codingConvention.getclasssDefinedByCall(mixinChildCallNode);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals(mixinParentNode, relationship.getSubclass());
    assertEquals(mixinSuperclassNode, relationship.getSuperclass());
  }

  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("superClass_", codingConvention.isSuperClassReference("superClass_"));
    assertFalse(codingConvention.isSuperClassReference("someProperty"));
  }

  public void testExtractClassNameIfProvide() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    Node node = Node.newCall(
        Node.newQualifiedName("goog.provide"),
        Node.newStringLiteral("MyClass"));

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals("MyClass", className);
  }

  public void testExtractClassNameIfRequire() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    Node node = Node.newCall(
        Node.newQualifiedName("goog.require"),
        Node.newStringLiteral("MyClass"));

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals("MyClass", className);
  }

  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", codingConvention.getExportPropertyFunction());
  }

  public void testGetSingletonGetterClassName() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    Node node = Node.newCall(
        Node.newQualifiedName("goog.addSingletonGetter"),
        Node.newQualifiedName("MyClass", "myProperty"),
        Node.newStringLiteral("getMyProperty"));

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    String className = codingConvention.getSingletonGetterClassName(node);
    assertEquals("MyClass", className);
  }

  public void testApplySingletonGetter() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    FunctionType functionType = new FunctionType("MyClass", ObjectType.ANY,
        ImmutableList.of(), ImmutableList.of());
    FunctionType getterType = new FunctionType("getMyProperty", ObjectType.ANY,
        ImmutableList.of(), ImmutableList.of());
    ObjectType objectType = new ObjectType("MyClass", ImmutableList.of());

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(getterType, functionType.getPrototype().getMethods()
        .get(0));
    assertEquals(objectType, functionType.getPrototype().getInstance());
  }

  public void testIsPropertyTestFunction() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    Node node = Node.newCall(Node.newQualifiedName("goog.isDef"),
        Node.newQualifiedName("MyClass", "myProperty"));

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  public void testGetObjectLiteralCast() {
    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new ClosureCodingConvention());

    Node node = Node.newCall(Node.newQualifiedName("goog.reflect.object",
        "cast"), Node.newKeywordTypeName(FunctionType.ANY_TYPE),
        Node.newObjectLiteral(ImmutableList.of()));

    ClosureCodingConvention codingConvention = (ClosureCodingConvention) compiler
        .getCodingConvention();
    ObjectLiteralCast objectLiteralCast = codingConvention
        .getObjectLiteralCast(null, node);
    assertEquals(FunctionType.ANY_TYPE, objectLiteralCast.getType());
    assertEquals(Node.newObjectLiteral(ImmutableList.of()),
        objectLiteralCast.getCasts()[0]);
  }
}