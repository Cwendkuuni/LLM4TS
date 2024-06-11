package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", new ObjectType("ParentClass"));
    FunctionType childCtor = new FunctionType("ChildCtor", new ObjectType("ChildClass"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    // Check that properties have been added to the child class
    FunctionType childPrototype = childCtor.getPrototype();
    assertThat(childPrototype.hasProperty("constructor")).isTrue();
    assertThat(childPrototype.hasProperty("superClass_")).isTrue();
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newQualifiedName("goog.inherits")),
        Node.newNode(Token.NAME, "MySubClass"),
        Node.newNode(Token.GETPROP, Node.newQualifiedName("SuperClass"))
    );

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getParentName()).isEqualTo("SuperClass");
    assertThat(relationship.getChildName()).isEqualTo("MySubClass");
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newQualifiedName("goog.mixin")),
        Node.newNode(Token.GETPROP, Node.newQualifiedName("SubClassPrototype")),
        Node.newNode(Token.GETPROP, Node.newQualifiedName("SuperClassPrototype"))
    );

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getParentName()).isEqualTo("SuperClassPrototype");
    assertThat(relationship.getChildName()).isEqualTo("SubClassPrototype");
  }

  @Test
  public void testGetclasssDefinedByCall_null() {
    Node node = Node.newNode(Token.NAME, "null");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newQualifiedName("goog.provide")),
        Node.newNode(Token.STRING, "MyClass")
    );

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String extractedClassName = convention.extractClassNameIfProvide(node, null);
    assertThat(extractedClassName).isEqualTo("MyClass");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newQualifiedName("goog.require")),
        Node.newNode(Token.STRING, "MyClass")
    );

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String extractedClassName = convention.extractClassNameIfRequire(node, null);
    assertThat(extractedClassName).isEqualTo("MyClass");
  }

  @Test
  public void testExtractClassNameIfGoog_null() {
    Node node = Node.newNode(Token.NAME, "null");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String extractedClassName = convention.extractClassNameIfGoog(node, null, "goog.provide");
    assertNull(extractedClassName);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    boolean result = convention.isSuperClassReference("superClass_");
    assertThat(result).isTrue();
  }

  @Test
  public void testIsSuperClassReference_invalid() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    boolean result = convention.isSuperClassReference("invalidProperty");
    assertThat(result).isFalse();
  }

  @Test
  public void testExtractPropertyTestFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Set<String> propertyTestFunctions = convention.getPropertyTestFunctions();
    assertThat(propertyTestFunctions.size()).isEqualTo(8);
    for (String functionName : propertyTestFunctions) {
      Node call = Node.newNode(Token.CALL, Node.newNode(Token.GETPROP, Node.newQualifiedName(functionName)));
      boolean result = convention.isPropertyTestFunction(call);
      assertThat(result).isTrue();
    }
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newQualifiedName("goog.addDependency")),
        Node.newNode(Token.NAME, "MyClass"),
        Node.newNode(Token.ARRAYLIT,
          Node.newArrayElement(Node.newNode(Token.STRING, "MyType1")),
          Node.newArrayElement(Node.newNode(Token.STRING, "MyType2"))
        ));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);
    assertThat(typeNames).containsExactly("MyType1", "MyType2");
  }

  @Test
  public void testIdentifyTypeDeclarationCall_invalid() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newQualifiedName("goog.addDependency")),
        Node.newNode(Token.NAME, "MyClass"),
        Node.newNode(Token.ARRAYLIT, Node.newArrayElement(Node.newNode(Token.STRING, "MyType")))
    );

    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);
    assertThat(typeNames).isEmpty();
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyFunction", new ObjectType("MyFunctionClass"));
    ObjectType objectType = new ObjectType("MyObjectClass");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, functionType, objectType);

    // Check that the properties have been added to the function type
    assertThat(functionType.hasProperty("getInstance")).isTrue();
    assertThat(functionType.hasProperty("instance_")).isTrue();
  }
}