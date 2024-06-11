package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType(null, ImmutableList.of(),
        new ObjectType(null, "parent"));
    FunctionType childCtor = new FunctionType(null, ImmutableList.of(),
        new ObjectType(null, "child"));
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType childPrototype = childCtor.getPrototype();
    ObjectType constructor = childPrototype.getPrototype();

    assertEquals("child", constructor.getName());
    assertEquals("function () {}\nconstructor: child",
        constructor.getSourceDescription());
    assertEquals("child", childPrototype.getName());
    assertEquals("function () {}\nconstructor: child\nsuperClass_: parent",
        childPrototype.getSourceDescription());
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentCtor = new FunctionType(null, ImmutableList.of(),
        new ObjectType(null, "parent"));
    FunctionType childCtor = new FunctionType(null, ImmutableList.of(),
        new ObjectType(null, "child"));
    SubclassType type = SubclassType.MIXIN;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType childPrototype = childCtor.getPrototype();
    ObjectType constructor = childPrototype.getPrototype();

    assertEquals("child", constructor.getName());
    assertEquals("function () {}\nconstructor: child",
        constructor.getSourceDescription());

    FunctionType parentPrototype = parentCtor.getPrototype();
    ObjectType childPrototype1 = childPrototype;

    for (int i = 0; i < 10; i++) {
      FunctionType newChildPrototype = new FunctionType(null,
          Collections.emptyList(), new ObjectType(null, "child_" + i));
      constructor.defineDeclaredProperty("prototype_" + i, newChildPrototype,
          constructor.getSource());
      childPrototype = newChildPrototype;
    }

    assertEquals(11, constructor.getDeclaredPropertyNames().size());
    assertEquals("prototype_0", constructor.getDeclaredPropertyNames().get(0));
    assertEquals("prototype_1", constructor.getDeclaredPropertyNames().get(1));
    // ...
    assertEquals("prototype_10", constructor.getDeclaredPropertyNames().get(10));

    assertEquals("child", childPrototype.getName());
    assertEquals("function () {}\nconstructor: child\nsuperClass_: parent",
        childPrototype.getSourceDescription());

    ObjectType childPrototypeSuperClass = childPrototype.get("superClass_");
    assertEquals("parent", childPrototypeSuperClass.getName());

    for (int i = 0; i < 11; i++) {
      ObjectType property = childPrototype.getDeclaredProperty("prototype_" + i);
      assertEquals("child_" + i, property.getName());
      assertEquals("function () {}\nsuperClass_: parent",
          property.getSourceDescription());
    }
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, null, "goog.provide",
        ImmutableList.of(Node.string("X")));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, null);

    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, null, "goog.require",
        ImmutableList.of(Node.string("X")));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);

    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfGoog_notProvideOrRequire() {
    Node node = Node.newNode(Token.CALL, null, "goog.foo", ImmutableList.of());

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfGoog(node, null, "goog.foo");

    assertNull(className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("goog.exportSymbol", convention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(
        Token.CALL, null, "goog.addDependency",
        ImmutableList.of(Node.string("X"), Node.newArrayLiteral(Node.string("Y"))));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> types = convention.identifyTypeDeclarationCall(node);

    assertNotNull(types);
    assertEquals(1, types.size());
    assertEquals("Y", types.get(0));
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newNode(Token.GETPROP, null, "a",
        Node.newNode(Token.GETPROP, null, "b", Node.newNode(Token.STRING,
            "prototype")));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.endsWithPrototype(qualifiedName));

    qualifiedName = Node.newNode(Token.GETPROP, null, "a", Node.newNode(
        Token.STRING, "b"));

    assertFalse(convention.endsWithPrototype(qualifiedName));
  }

  @Test(expected = AssertionError.class)
  public void testGetObjectLiteralCast_missingObjectLiteral() {
    NodeTraversal traversal = NodeTraversal.create();
    Node callNode = Node.newNode(Token.CALL, null, "goog.reflect.object",
        ImmutableList.of(Node.newNode(Token.OBJECT, null), Node.newNode(
            Token.NULL, null)));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast cast = convention.getObjectLiteralCast(traversal, callNode);

    assertNull(cast);
  }

  @Test
  public void testGetObjectLiteralCast_valid() {
    NodeTraversal traversal = NodeTraversal.create();
    Node callNode = Node.newNode(Token.CALL, null, "goog.reflect.object",
        ImmutableList.of(Node.newNode(Token.OBJECT, null), Node.newNode(
            Token.OBJECTLIT, null, Arrays.asList(
                Node.newObjectProperty(Node.newName("a"), Node.newString("A")),
                Node.newObjectProperty(Node.newName("b"), Node.newString("B"))))));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast cast = convention.getObjectLiteralCast(traversal, callNode);

    assertNotNull(cast);
    assertEquals("goog.reflect.object", cast.getTypeName());
    assertEquals(Arrays.asList(Node.newName("a"), Node.newName("b")),
        cast.getProperties());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.newNode(Token.CALL, null,
        Node.newName("goog.isString"), ImmutableList.of(Node.newString("")));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isPropertyTestFunction(callNode));
  }

  @Test
  public void testIsPropertyTestFunction_invalid() {
    Node callNode = Node.newNode(Token.CALL, null, "goog.foo",
        ImmutableList.of(Node.newString("")));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isPropertyTestFunction(callNode));
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newNode(Token.CALL, null, "goog.addSingletonGetter",
        ImmutableList.of(Node.newName("SingletonGetter")));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertEquals("SingletonGetter", convention.getSingletonGetterClassName(callNode));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType singletonGetterType = new FunctionType(null,
        ImmutableList.of(), new ObjectType(null, "SingletonGetter"));
    FunctionType getterType = new FunctionType(null, ImmutableList.of(),
        new ObjectType(null, "Getter"));
    ObjectType objectType = new ObjectType(null, "Object");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(singletonGetterType, getterType, objectType);

    assertEquals("Getter instance_", getterType.getDeclaredPropertyNames().get(0));
    assertEquals("Object instance_", getterType.getDeclaredPropertyNames().get(1));
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameterNode = Node.newNode(Token.VAR, null, Node.newName("p"),
        Node.newNull());

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isOptionalParameter(parameterNode));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameterNode = Node.newNode(Token.VAR, null, Node.newName("args"),
        Node.newArrayLiteral(Node.newNull()));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isVarArgsParameter(parameterNode));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isPrivate("public"));
    assertFalse(convention.isPrivate("protected"));
    assertFalse(convention.isPrivate(""));
    assertFalse(convention.isPrivate("private"));
    assertFalse(convention.isPrivate("private static"));
    assertFalse(convention.isPrivate("private final"));

    assertFalse(convention.isPrivate("_private"));
    assertFalse(convention.isPrivate("__private"));
    assertFalse(convention.isPrivate("_private_"));
    assertFalse(convention.isPrivate("__private_"));

    assertFalse(convention.isPrivate("this._private"));
    assertFalse(convention.isPrivate("this.__private"));

    assertTrue(convention.isPrivate("_ClosureCodingConvention__private"));
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    List<AssertionFunctionSpec> functions = convention.getAssertionFunctions();

    assertEquals(13, functions.size());
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newNode(Token.CALL, null, "goog.bind",
        ImmutableList.of(Node.newName("fn"), Node.newName("self"),
            Node.newArrayLiteral(Node.newName("args"))));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind bind = convention.describeFunctionBind(node);

    assertNotNull(bind);
    assertEquals("fn", bind.getFunction());
    assertEquals("self", bind.getThisValue());
    assertEquals(Arrays.asList("args"), bind.getParameters());
  }

  @Test
  public void testDescribeFunctionBind_invalid() {
    Node node = Node.newNode(Token.CALL, null, "goog.bind",
        ImmutableList.of(Node.newName("fn"), Node.newName("self")));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertNull(convention.describeFunctionBind(node));
  }
}