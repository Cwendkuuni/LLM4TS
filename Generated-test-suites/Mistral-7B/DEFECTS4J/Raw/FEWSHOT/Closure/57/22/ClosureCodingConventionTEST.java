package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
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
    FunctionType parentCtor = createFunctionType("ParentClass");
    FunctionType childCtor = createFunctionType("ChildClass");
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    FunctionType prototype = parentCtor.getPrototype();
    ObjectType constructor = childCtor.getPrototype();

    assertThat(prototype.getDeclaredProperties().keySet()).contains("constructor");
    assertThat(constructor.getDeclaredProperties().keySet())
        .contains("constructor", "superClass_");
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = createCallNode("goog.inherits(ChildClass, ParentClass)");
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);
  }

  @Test
  public void testGetclasssDefinedByCallWithMixin() {
    Node node = createCallNode("goog.mixin(ChildClass.prototype, ParentClass.prototype)");
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.MIXIN);
  }

  @Test
  public void testGetclasssDefinedByCallWithInvalidSyntax() {
    Node node = createCallNode("goog.mixin(ChildClass, ParentClass.prototype)");
    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertNull(relationship);
  }

  private FunctionType createFunctionType(String name) {
    ObjectType objectType = new ObjectType(name);
    FunctionType functionType = new FunctionType(name, ImmutableList.of(), objectType);
    return functionType;
  }

  private Node createCallNode(String callName) {
    Node callNode = Node.newCall(null, "goog", callName);
    return callNode;
  }
}