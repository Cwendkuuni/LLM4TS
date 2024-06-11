package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.CodingConvention.SubclassRelationship;
import com.google.javascript.jscomp.CodingConvention.SubclassType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    CodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    FunctionType childCtor = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    SubclassType type = SubclassType.INHERITS;

    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("superClass_", childCtor.getPrototype().getDeclaredFieldNames().get(0).getName());
    assertEquals("constructor", childCtor.getPrototype().getDeclaredFields().get(0).getName());
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    CodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    FunctionType childCtor = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    SubclassType type = SubclassType.MIXIN;

    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("constructor", childCtor.getPrototype().getDeclaredFields().get(0).getName());
  }

  @Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_InvalidCall() {
    CodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    FunctionType childCtor = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    SubclassType type = SubclassType.INHERITS;

    Node parentNode = Node.createCall(Type.VOID,
        Node.createName("goog.inherits"),
        Node.createName("Parent"),
        Node.createName("Child"));

    convention.applySubclassRelationship(parentCtor, childCtor, type, new NodeTraversal(parentNode));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.VOID,
        Node.createName("goog.inherits"),
        Node.createName("Child"),
        Node.createName("Parent"));

    SubclassRelationship result = convention.getclasssDefinedByCall(node);

    assertNull(result);

    node = Node.createCall(Type.VOID,
        Node.createName("goog.inherits"),
        Node.createName("Child"),
        Node.createObjectLiteral(ImmutableList.of(
            Node.createProperty(Node.createName("Parent"), Node.createName("Parent")))));

    result = convention.getclasssDefinedByCall(node);

    assertEquals(SubclassType.INHERITS, result.getType());
    assertEquals("Child", result.getSubclass().getName());
    assertEquals("Parent", result.getSuperclass().getName());

    node = Node.createCall(Type.VOID,
        Node.createName("goog.mixin"),
        Node.createName("Child"),
        Node.createObjectLiteral(ImmutableList.of(
            Node.createProperty(Node.createName("Parent"), Node.createName("Parent.prototype")))));

    result = convention.getclasssDefinedByCall(node);

    assertEquals(SubclassType.MIXIN, result.getType());
    assertEquals("Child", result.getSubclass().getName());
    assertEquals("Parent", result.getSuperclass().getName());
  }

  @Test
  public void testIsSuperClassReference() {
    CodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createName("a.b.c");

    assertFalse(convention.endsWithPrototype(node));

    node = Node.createName("a.b.c.prototype");

    assertTrue(convention.endsWithPrototype(node));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.VOID,
        Node.createName("goog.provide"),
        Node.createArrayLiteral(Node.createName("X")));

    assertEquals("X", convention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.VOID,
        Node.createName("goog.require"),
        Node.createArrayLiteral(Node.createName("X")));

    assertEquals("X", convention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    CodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.VOID,
        Node.createName("goog.addSingletonGetter"),
        Node.createName("X"),
        Node.createName("getX"));

    assertEquals("getX", convention.getSingletonGetterClassName(node));
  }

  @Test
  public void testApplySingletonGetter() {
    CodingConvention convention = new ClosureCodingConvention();

    FunctionType fnType = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    FunctionType getterType = new FunctionType(Type.VOID, ImmutableList.of(), Type.VOID);
    ObjectType objectType = new ObjectType(ImmutableList.of());

    convention.applySingletonGetter(fnType, getterType, objectType);

    assertEquals("getInstance", fnType.getDeclaredFieldNames().get(0).getName());
    assertEquals("instance_", fnType.getDeclaredFields().get(0).getName());
  }

  @Test
  public void testIsPropertyTestFunction() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.BOOLEAN,
        Node.createName("goog.isDef"),
        Node.createName("x"));

    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    CodingConvention convention = new ClosureCodingConvention();

    NodeTraversal traversal = new NodeTraversal(Node.createCall(Type.VOID,
        Node.createName("goog.reflect.object"),
        Node.createName("X"),
        Node.createName("Y")));

    ObjectLiteralCast result = convention.getObjectLiteralCast(traversal, traversal.getCurrentNode());

    assertNull(result);

    traversal.setCurrent(traversal.getCurrent().getPrevious());

    result = convention.getObjectLiteralCast(traversal, traversal.getCurrentNode());

    assertNotNull(result);
    assertEquals("X", result.getClassName());
    assertEquals("Y", result.getObject());
  }

  @Test
  public void testIsOptionalParameter() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.INTEGER,
        Node.createName("function"),
        Node.createArrayLiteral(
            Node.createName("a"),
            Node.createName("b"),
            Node.createOptionalParam(Node.createName("c"))));

    assertFalse(convention.isOptionalParameter(node.getChild(2)));
  }

  @Test
  public void testIsVarArgsParameter() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.INTEGER,
        Node.createName("function"),
        Node.createArrayLiteral(
            Node.createName("a"),
            Node.createName("b"),
            Node.createVarArgsParam(Node.createName("args"))));

    assertFalse(convention.isVarArgsParameter(node.getChild(2)));
  }

  @Test
  public void testIsPrivate() {
    CodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isPrivate("publicProperty"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.VOID,
        Node.createName("goog.addDependency"),
        Node.createArrayLiteral(
            Node.createName("X"),
            Node.createArrayLiteral(
                Node.createName("Y"),
                Node.createName("Z"))));

    assertEquals(ImmutableList.of("Y", "Z"), convention.identifyTypeDeclarationCall(node));
  }

  @Test
  public void testDescribeFunctionBind() {
    CodingConvention convention = new ClosureCodingConvention();

    Node node = Node.createCall(Type.VOID,
        Node.createName("goog.bind"),
        Node.createName("fn"),
        Node.createName("self"),
        Node.createArrayLiteral(
            Node.createName("arg1"),
            Node.createName("arg2")));

    Bind result = convention.describeFunctionBind(node);

    assertEquals("fn", result.getFunction().getName());
    assertEquals("self", result.getThis());
    assertEquals(ImmutableList.of("arg1", "arg2"), result.getArguments());
  }
}