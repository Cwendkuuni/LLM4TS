package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.CodingConvention.AssertionFunctionSpec;
import com.google.javascript.jscomp.CodingConvention.SubclassRelationship;
import com.google.javascript.jscomp.CodingConvention.SubclassType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Test;

public class ClosureCodingConventionTest {
  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(), null);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(), null);

    CodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertThat(childCtor.getProperties()).containsExact("superClass_", parentCtor);
    assertThat(parentCtor.getPrototype().getProperties()).containsExact("constructor", childCtor);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.newCall("goog.inherits", Node.newQualifiedName("SubClass"), Node.newQualifiedName("SuperClass"));

    CodingConvention codingConvention = new ClosureCodingConvention();
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newCall("goog.provide", Node.newString("MyClass"));

    CodingConvention codingConvention = new ClosureCodingConvention();
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newCall("goog.require", Node.newString("MyClass"));

    CodingConvention codingConvention = new ClosureCodingConvention();
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newQualifiedName("a.b.c.prototype");

    CodingConvention codingConvention = new ClosureCodingConvention();
    boolean result = codingConvention.endsWithPrototype(qualifiedName);
    assertThat(result).isTrue();
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node callNode = Node.newCall("goog.addDependency", Node.newArrayLiteral(Node.newString("MyType1"), Node.newString("MyType2")));

    CodingConvention codingConvention = new ClosureCodingConvention();
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(callNode);
    assertThat(typeNames).containsExact("MyType1");
    assertThat(typeNames).containsExact("MyType2");
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.newCall(Node.newQualifiedName("goog.isDef"), Node.newQualifiedName("obj"));

    CodingConvention codingConvention = new ClosureCodingConvention();
    boolean result = codingConvention.isPropertyTestFunction(callNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = Node.newCall(Node.newQualifiedName("goog.reflect.object", "is"), Node.newQualifiedName("obj"), Node.newString("MyType"));

    CodingConvention codingConvention = new ClosureCodingConvention();
    ObjectLiteralCast objectLiteralCast = codingConvention.getObjectLiteralCast(new NodeTraversal(), callNode);
    assertThat(objectLiteralCast).isNotNull();
  }

  @Test
  public void testAssertionFunctions() {
    CodingConvention codingConvention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> assertionFunctions = codingConvention.getAssertionFunctions();

    assertThat(assertionFunctions).hasSize(8);
    for (AssertionFunctionSpec assertionFunctionSpec : assertionFunctions) {
      assertThat(assertionFunctionSpec.functionName).isNotNull();
      assertThat(assertionFunctionSpec.functionType).isNotNull();
    }
  }

  @Test
  public void testDescribeFunctionBind() {
    Node callNode = Node.newCall(Node.newQualifiedName("goog.bind"), Node.newQualifiedName("fn"), Node.newNull(), Node.newArrayLiteral());

    CodingConvention codingConvention = new ClosureCodingConvention();
    Bind bind = codingConvention.describeFunctionBind(callNode);
    assertThat(bind).isNotNull();
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = Node.newName("p");

    CodingConvention codingConvention = new ClosureCodingConvention();
    boolean result = codingConvention.isOptionalParameter(parameter);
    assertNull(result);
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = Node.newName("p");

    CodingConvention codingConvention = new ClosureCodingConvention();
    boolean result = codingConvention.isVarArgsParameter(parameter);
    assertNull(result);
  }

  @Test
  public void testIsPrivate() {
    String name = "privateVar";

    CodingConvention codingConvention = new ClosureCodingConvention();
    boolean result = codingConvention.isPrivate(name);
    assertNull(result);
  }
}