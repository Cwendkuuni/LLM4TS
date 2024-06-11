package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;

import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * Tests for ClosureCodingConvention class.
 */
public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        new ObjectType("ParentClass"), null);
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        new ObjectType("ChildClass"), null);
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType expectedParentCtor = new FunctionType("ParentCtor",
        ImmutableList.of(), new ObjectType("ParentClass"), null);
    FunctionType expectedChildCtor = new FunctionType("ChildCtor",
        ImmutableList.of(), new ObjectType("ChildClass"), null);

    // ParentCtor properties on ChildCtor
    FunctionProperty superClassProp = expectedChildCtor.getDeclaredProperty(
        "superClass_");
    assertSame(expectedParentCtor, superClassProp.getType());
    FunctionProperty constructorProp = expectedChildCtor.getDeclaredProperty(
        "constructor");
    assertSame(expectedChildCtor, constructorProp.getType());

    // ParentClass properties on ChildClass
    FunctionProperty superClassPropOnChildClass = expectedChildClass.get(
        "superClass_");
    assertSame(expectedParentClass, superClassPropOnChildClass.getType());
    FunctionProperty constructorPropOnChildClass = expectedChildClass.get(
        "constructor");
    assertSame(expectedChildCtor, constructorPropOnChildClass.getType());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = Node.createCall("goog.inherits", new Node[]{"ChildClass",
          "SuperClass"});
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);

    assertNull(relationship);

    callNode = Node.createCall("ChildClass.inherits", new Node[]{"SuperClass"});
    relationship = convention.getclasssDefinedByCall(callNode);
    assertNotNull(relationship);
    SubclassType expectedType = SubclassType.INHERITS;
    assertSame(expectedType, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testExtractClassNameIfProvideAndRequire() {
    Node provideCall = Node.createCall("goog.provide", new Node[]{
          Node.createString("X")});
    String extractedClassName = new ClosureCodingConvention()
        .extractClassNameIfProvide(provideCall, null);
    assertEquals("X", extractedClassName);

    Node requireCall = Node.createCall("goog.require", new Node[]{
          Node.createString("X")});
    extractedClassName = new ClosureCodingConvention()
        .extractClassNameIfRequire(requireCall, null);
    assertEquals("X", extractedClassName);
  }

  @Test
  public void testGetExportPropertyFunctionAndSymbolFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String exportPropertyFunction = convention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", exportPropertyFunction);

    String exportSymbolFunction = convention.getExportSymbolFunction();
    assertEquals("goog.exportSymbol", exportSymbolFunction);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node callNode = Node.createCall("goog.addDependency", new Node[]{
          Node.createArrayLiteral(new Node[]{
              Node.createString("X"),
              Node.createString("Y"),
              Node.createString("Z")})});
    List<String> expected = ImmutableList.of("X", "Y", "Z");
    List<String> identified = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(callNode);
    assertEquals(expected, identified);

    callNode = Node.createCall("goog.addDependency", new Node[]{
          Node.createCall("goog.require", new Node[]{
              Node.createString("X")})});
    identified = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(callNode);
    assertNull(identified);
  }

  @Test
  public void testPropertyTestFunctions() {
    Node callNode = Node.createCall("goog.isDef", Node.createIdent("x"));
    assertTrue(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));

    callNode = Node.createCall("goog.isString", Node.createString("x"));
    assertTrue(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));

    callNode = Node.createCall("goog.isFunction", Node.createFunctionExpr(
            Node.createIdent("x"), new Node[0], null));
    assertTrue(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));

    callNode = Node.createCall("goog.isArray", Node.createArrayLiteral(
            new Node[]{Node.createNumber(1)}));
    assertTrue(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));

    callNode = Node.createCall("goog.isObject", Node.createNull());
    assertFalse(new ClosureCodingConvention()
        .isPropertyTestFunction(callNode));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = Node.createCall("goog.reflect.object", Node.createIdent(
            "x"), Node.createObjectType("{a: Number, b: String}"));
    ObjectLiteralCast expected = new ObjectLiteralCast(
        "goog.reflect.object", Node.createObjectType(
            "{a: Number, b: String}"));
    ObjectLiteralCast actual = new ClosureCodingConvention()
        .getObjectLiteralCast(NodeTraversal.NULL, callNode);
    assertEquals(expected, actual);

    assertNull(new ClosureCodingConvention()
        .getObjectLiteralCast(NodeTraversal.NULL, Node.createCall(
            "goog.reflect.object", Node.createIdent("x"))));
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameterNode = Node.createIdent("x");
    assertFalse(new ClosureCodingConvention()
        .isOptionalParameter(parameterNode));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameterNode = Node.createIdent("x");
    Node varArgsCall = Node.createCall("goog.function",
        new Node[]{Node.createArrayLiteral(new Node[]{"x"}),
                  Node.createArrayLiteral(new Node[]{Node.createIdent("args")})});
    FunctionType functionType = new FunctionType(
        "googFunction", ImmutableList.of(), new ObjectType("Object"), null);
    ObjectType objectType = new ObjectType("Object");
    new ClosureCodingConvention()
        .applySingletonGetter(functionType, null, objectType);
    Node functionExpression = Node.createFunctionExpr(
        Node.createIdent("googFunction"), new Node[0], null);
    Node functionCall = Node.createCall(functionExpression,
        new Node[]{parameterNode, Node.createArrayLiteral(new Node[0])});
    assertFalse(new ClosureCodingConvention()
        .isVarArgsParameter(parameterNode));
    assertTrue(new ClosureCodingConvention()
        .isVarArgsParameter(functionCall));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(new ClosureCodingConvention()
        .isPrivate("x"));
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> assertionFunctions = convention
        .getAssertionFunctions();
    for (AssertionFunctionSpec functionSpec : assertionFunctions) {
      FunctionType functionType = functionSpec.getType();
      String functionName = functionSpec.getName();
      Object literalCast = functionType.getPrototype();
      assertEquals(FunctionType.class, literalCast.getClass());
      ObjectType objectType = literalCast.getType();
      ObjectType expectedObjectType = new ObjectType(functionName + "Type");
      assertEquals(expectedObjectType, objectType);
    }
  }

  @Test
  public void testDescribeFunctionBind() {
    Node functionCall = Node.createCall("goog.bind", new Node[]{
          Node.createFunctionExpr(Node.createIdent("fn"), new Node[0], null),
          Node.createNull(), Node.createArrayLiteral(new Node[0])});
    Bind expected = new Bind(Node.createFunctionExpr(Node.createIdent("fn"),
        new Node[0], null), Node.createNull(), Node.createArrayLiteral(new Node[0]));
    Bind actual = new ClosureCodingConvention()
        .describeFunctionBind(functionCall);
    assertEquals(expected, actual);

    Node partialCall = Node.createCall("goog.partial", new Node[]{
          Node.createFunctionExpr(Node.createIdent("fn"), new Node[0], null),
          Node.createArrayLiteral(new Node[]{Node.createString("arg1"),
                                            Node.createNumber(1)})});
    expected = new Bind(Node.createFunctionExpr(Node.createIdent("fn"),
        new Node[]{"arg1", Node.createNumber(1)}, null), null,
        Node.createArrayLiteral(new Node[]{Node.createIdent("arg1"),
                                          Node.createNumber(1)}));
    actual = new ClosureCodingConvention()
        .describeFunctionBind(partialCall);
    assertEquals(expected, actual);
  }
}