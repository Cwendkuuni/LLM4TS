package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.SubclassRelationship;
import com.google.javascript.rhino.jstype.SubclassType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

/**
 * Tests for the ClosureCodingConvention class.
 */
public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    codingConvention = new ClosureCodingConvention();
    traversal = mock(NodeTraversal.class);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = mockFunctionType("ParentCtor");
    FunctionType childCtor = mockFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(SubclassType.INHERITS, type);
    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentPrototype = mockFunctionType("ParentPrototype");
    FunctionType childPrototype = mockFunctionType("ChildPrototype");
    SubclassType type = SubclassType.MIXIN;

    codingConvention.applySubclassRelationship(null, null, type);

    assertNull(parentPrototype);
    assertNull(childPrototype);
    assertEquals(SubclassType.MIXIN, type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySubclassRelationship_IllegalArguments() {
    FunctionType parentCtor = mockFunctionType("ParentCtor");
    FunctionType childCtor = mockFunctionType("ChildCtor");
    SubclassType type = null;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createMockCallNode("goog.inherits", "ChildClass",
        "ParentClass");

    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass());
    assertEquals("ParentClass", relationship.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_DeprecatedSyntax() {
    Node callNode = createMockCallNode("ChildClass.inherits", "ParentClass");

    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass());
    assertEquals("ParentClass", relationship.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_Mixin() {
    Node callNode = createMockCallNode("ChildPrototype.mixin", "ParentPrototype");

    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertNull(relationship.getSubclass());
    assertEquals("ParentPrototype", relationship.getSuperclass());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createMockNode("goog.provide('ChildClass')");

    String className = codingConvention.extractClassNameIfProvide(node, null);

    assertEquals("ChildClass", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createMockNode("goog.require('ChildClass')");

    String className = codingConvention.extractClassNameIfRequire(node, null);

    assertEquals("ChildClass", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = codingConvention.getExportPropertyFunction();

    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = createMockCallNode("goog.addSingletonGetter",
        "getPropertyFunctionName");

    String className = codingConvention.getSingletonGetterClassName(callNode);

    assertEquals("getPropertyFunctionName", className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = mockFunctionType("ChildClass");
    FunctionType getterType = mockFunctionType("GetterFunction");
    ObjectType objectType = mockObjectType("ChildClass");

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(functionType, functionType.getPrototype().get("getInstance"));
    assertEquals(objectType, functionType.getPrototype().get("instance_"));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
    assertFalse(codingConvention.isSuperClassReference("anotherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = createMockNode("a.b.c.prototype");

    assertTrue(codingConvention.endsWithPrototype(qualifiedName));
  }

  @Test
  public void testEndsWithPrototype_False() {
    Node qualifiedName = createMockNode("a.b.c");

    assertFalse(codingConvention.endsWithPrototype(qualifiedName));
  }

  private FunctionType mockFunctionType(String name) {
    FunctionType functionType = mock(FunctionType.class);
    when(functionType.getName()).thenReturn(name);
    return functionType;
  }

  private ObjectType mockObjectType(String name) {
    ObjectType objectType = mock(ObjectType.class);
    when(objectType.getName()).thenReturn(name);
    return objectType;
  }

  private Node createMockCallNode(String functionName, String... args) {
    Node callNode = mock(Node.class);
    Node callName = createMockNode(functionName);
    Node[] arguments = new Node[args.length];

    for (int i = 0; i < args.length; i++) {
      arguments[i] = createMockNode(args[i]);
    }

    when(callName.getQualifiedName()).thenReturn(functionName);
    when(callName.getNext()).thenReturn(Node.createArrayLiteral(arguments));

    when(callNode.getFirstChild()).thenReturn(callName);

    return callNode;
  }

  private Node createMockNode(String stringValue) {
    Node node = mock(Node.class);
    when(node.getString()).thenReturn(stringValue);
    return node;
  }

  
  // Add any additional test methods here.
  
}