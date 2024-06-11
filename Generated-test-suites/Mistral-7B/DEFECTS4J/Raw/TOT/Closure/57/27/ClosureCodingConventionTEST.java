package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = newFunctionType("ParentCtor");
    FunctionType childCtor = newFunctionType("ChildCtor");
    SubclassType relationshipType = SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, relationshipType);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentPrototype = newFunctionType("ParentPrototype");
    FunctionType childPrototype = newFunctionType("ChildPrototype");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentPrototype, childPrototype,
        SubclassType.MIXIN);

    assertNull(childPrototype.getPrototype());
    assertEquals(childPrototype, parentPrototype.getPrototype().getPrototype());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createCallNode("goog.inherits", "ChildClass",
        "ParentClass");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclassName());
    assertEquals("ParentClass", relationship.getSuperclassName());
  }

  @Test
  public void testGetclasssDefinedByCall_MIXIN() {
    Node callNode = createCallNode("goog.mixin", "ChildPrototype",
        "ParentPrototype");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildPrototype", relationship.getSubclassName());
    assertEquals("ParentPrototype", relationship.getSuperclassName());
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("anotherProperty"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("MyClass", convention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('MyClass')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("MyClass", convention.extractClassNameIfRequire(node, null));
  }

  private Node createCallNode(String functionName, String... args) {
    Node callNode = createCallNode(functionName, null, args);
    return callNode;
  }

  private Node createCallNode(String functionName, Node target, String... args) {
    Node callName = createNode(functionName);
    Node callNode = new Node(Token.CALL, callName, argsLength(args));

    if (target != null) {
      callNode.setParent(target);
    }

    return callNode;
  }

  private Node createNode(String nodeName) {
    Node node = new Node(Token.NAME, nodeName);
    node.setSource(new SourceFile("TestSource.js"));
    node.setStartPosition(1, 1);
    node.setEndPosition(1, nodeName.length() + 1);
    return node;
  }

  private int argsLength(String[] args) {
    return args.length;
  }
}