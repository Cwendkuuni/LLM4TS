package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.createCallNode(
        "goog.inherits",
        Node.createQualifiedNameNode("MySubClass"),
        Node.createQualifiedNameNode("SuperClass"));

    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);

    node = Node.createCallNode(
        "goog.mixin",
        Node.createQualifiedNameNode("MySubClass"),
        Node.createQualifiedNameNode("SuperClass.prototype"));

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.MIXIN);

    node = Node.createCallNode(
        "MySubClass.inherits",
        Node.createQualifiedNameNode("SuperClass"));

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertNull(relationship);

    node = Node.createCallNode(
        "goog.inherits",
        Node.createQualifiedNameNode("MySubClass"),
        null);

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertNull(relationship);

    node = Node.createCallNode(
        "goog.mixin",
        Node.createQualifiedNameNode("MySubClass.prototype"),
        Node.createQualifiedNameNode("SuperClass.prototype"));

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.createCallNode(
        "goog.provide",
        Node.createQualifiedNameNode("MyClass"));

    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.createCallNode(
        "goog.require",
        Node.createQualifiedNameNode("MyClass"));

    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    boolean result = convention.isSuperClassReference("superClass_");
    assertThat(result).isTrue();

    result = convention.isSuperClassReference("someOtherProperty");
    assertThat(result).isFalse();
  }

  @Test
  public void testEndsWithPrototype() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createQualifiedNameNode("a.b.c");
    boolean result = convention.endsWithPrototype(node);
    assertThat(result).isFalse();

    node = Node.createQualifiedNameNode("a.b.c.prototype");
    result = convention.endsWithPrototype(node);
    assertThat(result).isTrue();
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType();
    parentCtor.setName("SuperClass");
    FunctionType childCtor = new FunctionType();

    new ClosureCodingConvention().applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    ObjectType prototype = parentCtor.getPrototype();
    FunctionType constructor = childCtor.getPrototype();

    assertThat(prototype.hasProperty("constructor")).isTrue();
    assertThat(constructor.hasProperty("superClass_")).isTrue();

    constructor.getPrototype().setName("MySubclass");
    new ClosureCodingConvention().applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    // Check that the constructor property is not overwritten when reapplying.
    assertThat(constructor.getPrototype().getName()).isEqualTo("MySubclass");
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = Node.createCallNode(
        "goog.isDef",
        Node.createQualifiedNameNode("someValue"));

    boolean result = new ClosureCodingConvention().isPropertyTestFunction(call);
    assertThat(result).isTrue();
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.createCallNode(
        "goog.reflect.object",
        Node.createQualifiedNameNode("MyObject"),
        Node.createObjectLiteralNode(
            "myProperty",
            "someValue"));

    ObjectLiteralCast result = new ClosureCodingConvention().getObjectLiteralCast(
        NodeTraversal.EMPTY, node);

    assertThat(result).isNotNull();
    assertThat(result.getTypeName()).isEqualTo("MyObject");
    assertThat(result.getObjectLiteral()).isEqualTo(
        Node.createObjectLiteralNode(
            "myProperty",
            "someValue"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.createCallNode(
        "goog.addDependency",
        Node.createQualifiedNameNode("MyClass"),
        Node.createArrayLiteralNode(
            Node.createStringNode("MyType1"),
            Node.createStringNode("MyType2")));

    List<String> types = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertThat(types).containsExactly("MyType1", "MyType2");
  }

  @Test
  public void testGetExportPropertyFunction() {
    String result = new ClosureCodingConvention().getExportPropertyFunction();
    assertThat(result).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    String result = new ClosureCodingConvention().getExportSymbolFunction();
    assertThat(result).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = new FunctionType();
    getterType.setName("MyGetter");

    FunctionType functionType = new FunctionType();
    functionType.setName("MyClass");

    ObjectType objectType = new ObjectType();
    objectType.setName("MyClass");

    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertThat(functionType.hasProperty("getInstance")).isTrue();
    assertThat(functionType.hasProperty("instance_")).isTrue();
  }

  @Test
  public void testGetGlobalObject() {
    String result = new ClosureCodingConvention().getGlobalObject();
    assertThat(result).isEqualTo("goog.global");
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = Node.createQualifiedNameNode("MyOptionalParameter");

    boolean result = new ClosureCodingConvention().isOptionalParameter(parameter);
    assertFalse(result);
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = Node.createQualifiedNameNode("MyVarArgsParameter");

    boolean result = new ClosureCodingConvention().isVarArgsParameter(parameter);
    assertFalse(result);
  }

  @Test
  public void testIsPrivate() {
    String name = "privateProperty";

    boolean result = new ClosureCodingConvention().isPrivate(name);
    assertFalse(result);
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Collection<AssertionFunctionSpec> functions = convention.getAssertionFunctions();
    List<AssertionFunctionSpec> list = Lists.newArrayList(functions);

    int size = list.size();
    assertThat(size).isEqualTo(8);

    for (AssertionFunctionSpec function : list) {
      if (function.getName().equals("goog.asserts.assertFunction")) {
        JSTypeNative expectedType = JSTypeNative.FUNCTION_INSTANCE_TYPE;
        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertArray")) {
        JSTypeNative expectedType = JSTypeNative.ARRAY_TYPE;
        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertInstanceof")) {
        JSTypeNative expectedType = JSTypeNative.OBJECT_TYPE;
        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertNumber")) {
        JSTypeNative expectedType = JSTypeNative.NUMBER_TYPE;
        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertObject")) {
        JSTypeNative expectedType = JSTypeNative.OBJECT_TYPE;
        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertString")) {
        JSTypeNative expectedType = JSTypeNative.STRING_TYPE;
        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assert")) {
        FunctionType expectedType = new FunctionType();
        expectedType.setName("goog.asserts.assert");
        expectedType.setReturnType(JSTypeNative.VOID_TYPE);
        expectedType.setParameters(ImmutableList.of(
            new ParameterType("value", JSTypeNative.ANY_TYPE),
            new ParameterType("message", JSTypeNative.STRING_TYPE)));

        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertBoolean")) {
        FunctionType expectedType = new FunctionType();
        expectedType.setName("goog.asserts.assertBoolean");
        expectedType.setReturnType(JSTypeNative.VOID_TYPE);
        expectedType.setParameters(ImmutableList.of(
            new ParameterType("value", JSTypeNative.BOOLEAN_TYPE),
            new ParameterType("message", JSTypeNative.STRING_TYPE)));

        assertThat(function.getType()).isEqualTo(expectedType);
      }

      if (function.getName().equals("goog.asserts.assertString")) {
        FunctionType expectedType = new FunctionType();
        expectedType.setName("goog.asserts.assertString");
        expectedType.setReturnType(JSTypeNative.VOID_TYPE);
        expectedType.setParameters(ImmutableList.of(
            new ParameterType("value", JSTypeNative.STRING_TYPE),
            new ParameterType("message", JSTypeNative.STRING_TYPE)));

        assertThat(function.getType()).isEqualTo(expectedType);
      }
    }
  }
}