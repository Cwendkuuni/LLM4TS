package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention;

  @Before
  public void setUp() {
    codingConvention = new ClosureCodingConvention();
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = mock(FunctionType.class);
    FunctionType childCtor = mock(FunctionType.class);
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType childPrototype = childCtor.getPrototype();
    ObjectType constructor = childPrototype.getDeclaredProperty("constructor");

    assertEquals(childCtor, constructor.getValueType());
    assertEquals(parentCtor, childPrototype.getDeclaredProperty("superClass_")
        .getValueType());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.newNode(Token.CALL, null, ImmutableList.of(), null);
    SubclassType type = SubclassType.INHERITS;

    String subclassName = "MySubClass";
    Node subclassNode = Node.newNode(Token.NAME, null, ImmutableList.of(
        Node.newNode(Token.IDENT, null, null, subclassName)), null);

    String superclassName = "SuperClass";
    Node superclassNode = Node.newNode(Token.NAME, null, ImmutableList.of(
        Node.newNode(Token.IDENT, null, null, superclassName)), null);

    when(typeofClassDefiningName(callNode.getFirstChild()))
        .thenReturn(type);
    when(callNode.getLastChild()).thenReturn(superclassNode);

    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(callNode);

    assertEquals(type, relationship.getType());
    assertEquals(subclassName, relationship.getSubclassName());
    assertEquals(superclassName, relationship.getSuperclassName());
  }

  @Test
  public void testExtractClassNameIfProvideAndRequire() {
    Node node = Node.newNode(Token.CALL, null, ImmutableList.of(), null);
    String className = "MyClass";

    when(extractClassNameIfGoog(node, null, "goog.provide"))
        .thenReturn(className);

    assertEquals(className, codingConvention.extractClassNameIfProvide(node, null));

    node.setFirstChild(Node.newNode(Token.GETPROP, null, null, "require"));
    assertEquals(className, codingConvention.extractClassNameIfRequire(node, null));
  }

  @Test(expected = AssertionError.class)
  public void testExtractClassNameIfGoogWithInvalidCall() {
    Node node = Node.newNode(Token.CALL, null, ImmutableList.of(), null);

    codingConvention.extractClassNameIfGoog(node, null, "goog.invalidCall");
  }

  @Test
  public void testIsSuperClassReference() {
    assertEquals(true, codingConvention.isSuperClassReference("superClass_"));
    assertEquals(false, codingConvention.isSuperClassReference("myProperty"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.newNode(Token.CALL, null, ImmutableList.of(), null);
    Node callArg = Node.newNode(Token.NAME, null, ImmutableList.of(
        Node.newNode(Token.IDENT, null, null, "goog.isDef")), null);

    assertEquals(true, codingConvention.isPropertyTestFunction(callNode));
    assertEquals(false, codingConvention.isPropertyTestFunction(callArg));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = Node.newNode(Token.CALL, null, ImmutableList.of(), null);
    Node callName = Node.newNode(Token.GETPROP, null, null, "goog.reflect.object");
    Node typeNode = Node.newNode(Token.NAME, null, ImmutableList.of(
        Node.newNode(Token.IDENT, null, null, "goog.Type.Object")), null);
    Node objectNode = Node.newNode(Token.OBJECTLIT, null, ImmutableList.of(), null);

    Node objectLiteralCast = codingConvention.getObjectLiteralCast(null, callNode);

    assertEquals(typeNode.getQualifiedName(), objectLiteralCast.getTypeName());
    assertEquals(objectNode, objectLiteralCast.getObject());
  }

  // Add more test cases for other methods in ClosureCodingConvention if necessary
}