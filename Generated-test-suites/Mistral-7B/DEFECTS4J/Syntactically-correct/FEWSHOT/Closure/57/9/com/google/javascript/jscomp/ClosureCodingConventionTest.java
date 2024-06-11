package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.DefaultCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectLiteralCast;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  private static final ClosureCodingConvention CC = new ClosureCodingConvention();

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("parent");
    FunctionType childCtor = createFunctionType("child");

    CC.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertThat(childCtor.hasProperty("superClass_")).isTrue();
    ObjectType superClass = (ObjectType) childCtor.getProperty("superClass_");
    assertThat(superClass.getPrototype()).isSameInstance(parentCtor.getPrototype());
    assertThat(childCtor.hasProperty("constructor")).isTrue();
    assertThat(childCtor.getProperty("constructor").getType()).isSameInstance(childCtor);
  }

  @Test
  public void testApplySubclassRelationship_mixin() {
    FunctionType parentProto = createFunctionType("parentProto");
    FunctionType childProto = createFunctionType("childProto");

    CC.applySubclassRelationship(parentProto, childProto, SubclassType.MIXIN);

    assertThat(childProto.hasProperty("constructor")).isTrue();
    FunctionType constructor = (FunctionType) childProto.getProperty("constructor");
    assertThat(constructor.getPrototype()).isSameInstance(parentProto);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.newCallNode("goog.inherits",
        Node.newQualifiedNameNode("SubClass"),
        Node.newQualifiedNameNode("SuperClass"));

    SubclassRelationship relationship = CC.getclasssDefinedByCall(callNode);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);
    assertThat(relationship.getSubclass()).isSameInstance(
        Node.newQualifiedNameNode("SubClass"));
    assertThat(relationship.getSuperclass()).isSameInstance(
        Node.newQualifiedNameNode("SuperClass"));
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node callNode = Node.newCallNode("goog.mixin",
        Node.newQualifiedNameNode("SubClass"),
        Node.newQualifiedNameNode("SuperClass.prototype"));

    SubclassRelationship relationship = CC.getclasssDefinedByCall(callNode);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.MIXIN);
    assertThat(relationship.getSubclass()).isSameInstance(
        Node.newQualifiedNameNode("SubClass"));
    assertThat(relationship.getSuperclass()).isSameInstance(
        Node.newQualifiedNameNode("SuperClass.prototype"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node callNode = Node.newCallNode("goog.provide",
        Node.newStringLiteral("X"));

    String className = CC.extractClassNameIfProvide(callNode, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node callNode = Node.newCallNode("goog.require",
        Node.newStringLiteral("X"));

    String className = CC.extractClassNameIfRequire(callNode, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testIsSuperClassReference() {
    assertThat(CC.isSuperClassReference("superClass_")).isTrue();
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newQualifiedNameNode("a.b.c");
    assertThat(CC.endsWithPrototype(qualifiedName)).isFalse();

    qualifiedName = Node.newQualifiedNameNode("a.b.c.prototype");
    assertThat(CC.endsWithPrototype(qualifiedName)).isTrue();
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = createFunctionType("getter");
    ObjectType objectType = new ObjectType("ObjectType");

    CC.applySingletonGetter(createFunctionType("functionType"), getterType, objectType);

    assertThat(functionType.hasProperty("getInstance")).isTrue();
    FunctionType getter = (FunctionType) functionType.getProperty("getInstance");
    assertThat(getter.hasProperty("instance_")).isTrue();
    ObjectType instance = (ObjectType) getter.getProperty("instance_");
    assertThat(instance.getTypeName()).isEqualTo("ObjectType");
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newCallNode("goog.addDependency",
        Node.newArrayLiteral(Node.newStringLiteral("X")));

    List<String> typeNames = CC.identifyTypeDeclarationCall(node);
    assertThat(typeNames).hasSize(1);
    assertThat(typeNames.get(0)).isEqualTo("X");
  }

  @Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = CC.getAbstractMethodName();
    assertThat(abstractMethodName).isEqualTo("goog.abstractMethod");
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newCallNode("goog.addSingletonGetter",
        Node.newQualifiedNameNode("getter"),
        Node.newQualifiedNameNode("Getters"));

    String className = CC.getSingletonGetterClassName(callNode);
    assertThat(className).isEqualTo("Getters");
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = Node.newCallNode("goog.reflect.object",
        Node.newStringLiteral("typeof"),
        Node.newObjectLiteral(Node.newProperty("foo", Node.newNumberLiteral(42))));

    ObjectLiteralCast cast = CC.getObjectLiteralCast(null, callNode);
    assertThat(cast).isNotNull();
    assertThat(cast.getTypeName()).isEqualTo("Object");
    assertThat(cast.getObject()).isSameInstance(callNode.getLastChild());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.newCallNode("goog.isDef", Node.newNull(), null);

    boolean isPropertyTestFunction = CC.isPropertyTestFunction(callNode);
    assertThat(isPropertyTestFunction).isTrue();
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = Node.newIdentifier("optParam");

    boolean isOptionalParameter = CC.isOptionalParameter(parameter);
    assertFalse(isOptionalParameter);
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = Node.newIdentifier("varArgsParam");

    boolean isVarArgsParameter = CC.isVarArgsParameter(parameter);
    assertFalse(isVarArgsParameter);
  }

  @Test
  public void testIsPrivate() {
    String name = "privateVar";

    boolean isPrivate = CC.isPrivate(name);
    assertFalse(isPrivate);
  }

  @Test
  public void testFindAssertionFunctions() {
    List<AssertionFunctionSpec> assertionFunctions = CC.getAssertionFunctions();

    assertThat(assertionFunctions).hasSize(8);
    for (AssertionFunctionSpec functionSpec : assertionFunctions) {
      assertThat(functionSpec.getName()).isNotNull();
      assertThat(functionSpec.getType()).isNotNull();
    }
  }

  @Test
  public void testDescribeFunctionBind() {
    Node callNode = Node.newCallNode("goog.bind",
        Node.newCallNode("fn", Node.newNull()),
        Node.newThisValueNode(), Node.newArrayLiteral());

    Bind bind = CC.describeFunctionBind(callNode);
    assertThat(bind).isNotNull();
    assertThat(bind.getFunction()).isSameInstance(callNode.getFirstChild());
    assertThat(bind.getThisValue()).isSameInstance(callNode.getNext());
    assertThat(bind.getArguments()).isEmpty();
  }

  private FunctionType createFunctionType(String name) {
    FunctionType functionType = new FunctionType(name, ImmutableList.of(),
        JSTypeNative.OBJECT_TYPE);
    return functionType;
  }
}