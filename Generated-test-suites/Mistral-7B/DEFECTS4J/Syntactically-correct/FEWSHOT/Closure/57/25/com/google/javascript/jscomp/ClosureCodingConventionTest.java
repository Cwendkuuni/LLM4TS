package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {
  private static final Diagnostics DIAGNOSTICS = new Diagnostics();
  private static final Compiler COMPILER = mock(Compiler.class);
  private static final NodeTraversal TRAVERSAL = new NodeTraversal(COMPILER, DIAGNOSTICS);

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("ParentCtor", childCtor.getPrototype().getTypeName());
    assertEquals("constructor", childCtor.getName());
    assertEquals("ParentCtor", childCtor.getPrototype().getDeclaredProperties().get("constructor").getTypeName());
  }

  @Test
  public void testApplySubclassRelationshipWithMixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype");
    FunctionType childPrototype = createFunctionType("ChildPrototype");
    FunctionType childCtor = createFunctionType("ChildCtor");
    SubclassType type = SubclassType.MIXIN;

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(null, childCtor, type);

    assertEquals("ParentPrototype", childPrototype.getTypeName());
    assertEquals("constructor", childPrototype.getName());
    assertEquals("ChildPrototype", childCtor.getPrototype().getTypeName());
    assertEquals("constructor", childCtor.getName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('X')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('X')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfGoog() {
    Node node = createNode("goog.foo()");
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.foo");
    assertNull(className);

    node = createNode("goog.exportSymbol('X', 'x')");
    className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.exportSymbol");
    assertEquals("X", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
    assertFalse(codingConvention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = createNode("a.b.prototype");
    assertTrue(new ClosureCodingConvention().endsWithPrototype(node));

    node = createNode("a.b");
    assertFalse(new ClosureCodingConvention().endsWithPrototype(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = createNode("goog.reflect.object.cast(X)");
    ObjectLiteralCast expected = new ObjectLiteralCast("X.TYPE", node.getNext());
    ObjectLiteralCast actual = new ClosureCodingConvention().getObjectLiteralCast(TRAVERSAL, node);
    assertSame(expected, actual);
  }

  // Add more test cases as needed for the other methods in ClosureCodingConvention.

  private FunctionType createFunctionType(String name) {
    FunctionType functionType = mock(FunctionType.class);
    when(functionType.getName()).thenReturn(name);
    return functionType;
  }

  private Node createNode(String nodeString) {
    return Node.newNode(Token.NEWLINE, null, null, null, null, null, null, nodeString);
  }
}