package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newCallNode(Token.CALL, "goog.inherits", null,
        Node.newNameNode("SubClass"), Node.newNameNode("SuperClass"));
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);
    assertThat(relationship.getSubclass()).isEqualTo("SubClass");
    assertThat(relationship.getSuperclass()).isEqualTo("SuperClass");

    node = Node.newCallNode(Token.CALL, "goog.mixin", null,
        Node.newNameNode("SubClass.prototype"),
        Node.newNameNode("SuperClass.prototype"));
    relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.MIXIN);
    assertThat(relationship.getSubclass()).isEqualTo("SubClass");
    assertThat(relationship.getSuperclass()).isEqualTo("SuperClass");
  }

  @Test
  public void testGetclasssDefinedByCall_Null() {
    Node node = null;
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @Test
  public void testGetclasssDefinedByCall_WrongSyntax() {
    Node node = Node.newCallNode(Token.CALL, "goog.mixin", null,
        Node.newNameNode("SubClass"), Node.newNameNode("SuperClass"));
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @Test
  public void testIsSuperClassReference() {
    assertThat(new ClosureCodingConvention().isSuperClassReference("superClass_"))
        .isTrue();
    assertThat(new ClosureCodingConvention().isSuperClassReference("other"))
        .isFalse();
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newCallNode(Token.CALL, "goog.provide", null,
        Node.newNameNode("X"));
    String className = new ClosureCodingConvention()
        .extractClassNameIfProvide(node, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfProvide_Null() {
    Node node = null;
    String className = new ClosureCodingConvention()
        .extractClassNameIfProvide(node, null);
    assertNull(className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newCallNode(Token.CALL, "goog.require", null,
        Node.newNameNode("X"));
    String className = new ClosureCodingConvention()
        .extractClassNameIfRequire(node, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire_Null() {
    Node node = null;
    String className = new ClosureCodingConvention()
        .extractClassNameIfRequire(node, null);
    assertNull(className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String functionName = new ClosureCodingConvention()
        .getExportPropertyFunction();
    assertThat(functionName).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    String functionName = new ClosureCodingConvention()
        .getExportSymbolFunction();
    assertThat(functionName).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newCallNode(Token.CALL, "goog.addDependency", null,
        Node.newNameNode("MyClass"),
        Node.newArrayLiteralNode(
            Node.newStringLiteralNode("MyDependency1"),
            Node.newStringLiteralNode("MyDependency2")));
    List<String> typeNames =
        new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertThat(typeNames).containsExactly("MyDependency1", "MyDependency2");
  }

  @Test
  public void testIdentifyTypeDeclarationCall_Null() {
    Node node = null;
    List<String> typeNames = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(node);
    assertNull(typeNames);
  }

  @Test
  public void testIdentifyTypeDeclarationCall_NotArray() {
    Node node = Node.newCallNode(Token.CALL, "goog.addDependency", null,
        Node.newNameNode("MyClass"), Node.newNameNode("MyDependency"));
    List<String> typeNames = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(node);
    assertNull(typeNames);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newCallNode(Token.CALL, "goog.addSingletonGetter", null,
        Node.newNameNode("MySingletonGetter"), Node.newNameNode("MyClass"));
    String className = new ClosureCodingConvention()
        .getSingletonGetterClassName(node);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testGetSingletonGetterClassName_Null() {
    Node node = null;
    String className = new ClosureCodingConvention()
        .getSingletonGetterClassName(node);
    assertNull(className);
  }

  @Test
  public void testGetSingletonGetterClassName_WrongSyntax() {
    Node node = Node.newCallNode(Token.CALL, "goog.addSingletonGetter", null,
        Node.newNameNode("MySingletonGetter"), Node.newNameNode("MyClass"),
        Node.newNameNode("foo"));
    String className = new ClosureCodingConvention()
        .getSingletonGetterClassName(node);
    assertNull(className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType(Node.newNameNode("MyClass"),
        ImmutableList.of(), ObjectType.VOID, null);
    ObjectType getterType = new ObjectType(Node.newNameNode("MyClass"),
        ImmutableList.of(
            Node.newNameNode("getInstance"),
            Node.newNameNode("instance_")));
    new ClosureCodingConvention()
        .applySingletonGetter(functionType, getterType, null);
    FunctionType expectedFunctionType = new FunctionType(Node.newNameNode("MyClass"),
        ImmutableList.of(), ObjectType.VOID, null);
    expectedFunctionType.defineDeclaredProperty("getInstance", getterType,
        null);
    expectedFunctionType.defineDeclaredProperty("instance_", null, null);
    assertThat(functionType).isEqualTo(expectedFunctionType);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newCallNode(Token.CALL, "goog.isString", null,
        Node.newStringLiteralNode("foo"));
    boolean isPropertyTestFunction = new ClosureCodingConvention()
        .isPropertyTestFunction(node);
    assertThat(isPropertyTestFunction).isTrue();
  }

  @Test
  public void testIsPropertyTestFunction_WrongSyntax() {
    Node node = Node.newCallNode(Token.CALL, "goog.isString", null,
        Node.newNameNode("foo"), Node.newNameNode("bar"));
    boolean isPropertyTestFunction = new ClosureCodingConvention()
        .isPropertyTestFunction(node);
    assertThat(isPropertyTestFunction).isFalse();
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newCallNode(Token.CALL, "goog.reflect.object.cast", null,
        Node.newNameNode("goog.global"),
        Node.newQualifiedNameNode("MyObjectType"));
    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention()
        .getObjectLiteralCast(NodeUtil.createNodeTraversal(null), node);
    assertThat(objectLiteralCast).isNotNull();
    assertThat(objectLiteralCast.getTypeName()).isEqualTo("MyObjectType");
    assertThat(objectLiteralCast.getObjectNode()).isNotNull();
  }

  @Test
  public void testGetObjectLiteralCast_Null() {
    Node node = null;
    ObjectLiteralCast objectLiteralCast =
        new ClosureCodingConvention().getObjectLiteralCast(null, node);
    assertNull(objectLiteralCast);
  }

  @Test
  public void testGetObjectLiteralCast_WrongSyntax() {
    Node node = Node.newCallNode(Token.CALL, "goog.reflect.object.cast", null,
        Node.newNameNode("goog.global"), Node.newNameNode("Object"));
    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention()
        .getObjectLiteralCast(NodeUtil.createNodeTraversal(null), node);
    assertNull(objectLiteralCast);
  }
}

class SubclassRelationship {
  final SubclassType type;
  final String subclass;
  final String superclass;

  SubclassRelationship(SubclassType type, String subclass, String superclass) {
    this.type = type;
    this.subclass = subclass;
    this.superclass = superclass;
  }
}