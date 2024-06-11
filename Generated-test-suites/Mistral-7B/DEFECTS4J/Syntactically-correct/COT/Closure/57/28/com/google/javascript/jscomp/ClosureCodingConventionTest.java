package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

import org.junit.Test;

/**
 * Test class for ClosureCodingConvention.
 */
public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor", ImmutableList.of());
    FunctionType childCtor = createMockFunctionType("ChildCtor", ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;

    new ClosureCodingConvention().applySubclassRelationship(parentCtor, childCtor, type);

    assertTrue(childCtor.hasProperty("superClass_"));
    assertSame(parentCtor, childCtor.getProperty("superClass_"));
    assertTrue(childCtor.getPrototype().hasProperty("constructor"));
    assertSame(childCtor, childCtor.getPrototype().getProperty("constructor"));
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentPrototype = createMockFunctionType("ParentPrototype", ImmutableList.of());
    FunctionType childPrototype = createMockFunctionType("ChildPrototype", ImmutableList.of());

    new ClosureCodingConvention().applySubclassRelationship(null, parentPrototype, SubclassType.MIXIN);

    assertTrue(childPrototype.hasProperty("constructor"));
    assertSame(null, childPrototype.getProperty("constructor"));
    assertFalse(childPrototype.hasProperty("superClass_"));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createMockCallNode("goog.inherits", true, "Child", "Parent");
    SubclassType expectedType = SubclassType.INHERITS;
    SubclassRelationship actualResult = new ClosureCodingConvention().getclasssDefinedByCall(callNode);

    assertSame(expectedType, actualResult.getType());
    assertSame("Child", actualResult.getSubclass());
    assertSame("Parent", actualResult.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_Deprecated() {
    Node callNode = createMockCallNode("Child.inherits", false, null, "Parent");
    SubclassType expectedType = SubclassType.INHERITS;
    SubclassRelationship actualResult = new ClosureCodingConvention().getclasssDefinedByCall(callNode);

    assertSame(expectedType, actualResult.getType());
    assertSame("Child", actualResult.getSubclass());
    assertSame("Parent", actualResult.getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall_Mixin() {
    Node callNode = createMockCallNode("Child.prototype.mixin", false, "ParentPrototype", null);
    SubclassType expectedType = SubclassType.MIXIN;
    SubclassRelationship actualResult = new ClosureCodingConvention().getclasssDefinedByCall(callNode);

    assertSame(expectedType, actualResult.getType());
    assertSame("Child.prototype", actualResult.getSubclass());
    assertSame("ParentPrototype", actualResult.getSuperclass());
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(new ClosureCodingConvention().isSuperClassReference("superClass_"));
    assertFalse(new ClosureCodingConvention().isSuperClassReference("anotherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = createMockNode("a.b.c.prototype");
    assertTrue(new ClosureCodingConvention().endsWithPrototype(qualifiedName));

    qualifiedName = createMockNode("a.b.c");
    assertFalse(new ClosureCodingConvention().endsWithPrototype(qualifiedName));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createMockProvideNode("goog.provide('X')");
    String expectedClassName = "X";
    String actualResult = new ClosureCodingConvention().extractClassNameIfProvide(node, null);

    assertEquals(expectedClassName, actualResult);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createMockRequireNode("goog.require('X')");
    String expectedClassName = "X";
    String actualResult = new ClosureCodingConvention().extractClassNameIfRequire(node, null);

    assertEquals(expectedClassName, actualResult);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String expectedFunctionName = "goog.exportProperty";
    String actualResult = new ClosureCodingConvention().getExportPropertyFunction();

    assertEquals(expectedFunctionName, actualResult);
  }

  @Test
  public void testGetExportSymbolFunction() {
    String expectedFunctionName = "goog.exportSymbol";
    String actualResult = new ClosureCodingConvention().getExportSymbolFunction();

    assertEquals(expectedFunctionName, actualResult);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node callNode = createMockCallNode("goog.addDependency('X')");
    List<String> expectedTypeNames = ImmutableList.of("X");
    List<String> actualResult = new ClosureCodingConvention().identifyTypeDeclarationCall(callNode);

    assertEquals(expectedTypeNames, actualResult);
  }

  @Test
  public void testGetAbstractMethodName() {
    String expectedAbstractMethodName = "goog.abstractMethod";
    String actualResult = new ClosureCodingConvention().getAbstractMethodName();

    assertEquals(expectedAbstractMethodName, actualResult);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = createMockCallNode("goog.addSingletonGetter(getSingletonMethod, 'SingletonName')");
    String expectedClassName = "getSingletonMethod";
    String actualResult = new ClosureCodingConvention().getSingletonGetterClassName(callNode);

    assertEquals(expectedClassName, actualResult);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createMockFunctionType("SingletonMethod", ImmutableList.of());
    FunctionType getterType = createMockFunctionType("Getter", ImmutableList.of());
    ObjectType objectType = createMockObjectType("SingletonObject");

    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertTrue(functionType.hasProperty("getInstance"));
    assertTrue(functionType.hasProperty("instance_"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = createMockCallNode("goog.isDef", null, "val");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(callNode));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = createMockCallNode("goog.reflect.object.cast", null, null, "X");
    ObjectLiteralCast expectedResult = new ObjectLiteralCast("X", null);
    ObjectLiteralCast actualResult = new ClosureCodingConvention().getObjectLiteralCast(null, callNode);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = createMockNode("?param");
    assertFalse(new ClosureCodingConvention().isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = createMockNode("param...");
    assertFalse(new ClosureCodingConvention().isVarArgsParameter(parameter));
  }

  @Test
  public void testIsPrivate() {
    String name = "privateName";
    assertFalse(new ClosureCodingConvention().isPrivate(name));
  }

  @Test(expected = NullPointerException.class)
  public void testGetAssertionFunctions_NullNode() {
    new ClosureCodingConvention().getAssertionFunctions(null);
  }

  @Test
  public void testDescribeFunctionBind() {
    Node callNode = createMockCallNode("goog.bind(fn, thisValue, arg1, arg2)");
    Bind expectedResult = new Bind("fn", "thisValue", ImmutableList.of("arg1", "arg2"));
    Bind actualResult = new ClosureCodingConvention().describeFunctionBind(callNode);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testDescribeFunctionBind_Partial() {
    Node callNode = createMockCallNode("goog.partial(fn, arg1, arg2)");
    Bind expectedResult = new Bind("fn", null, ImmutableList.of("arg1", "arg2"));
    Bind actualResult = new ClosureCodingConvention().describeFunctionBind(callNode);

    assertEquals(expectedResult, actualResult);
  }

  private Node createMockNode(String nodeType) {
    return createMockNode(nodeType, null, null);
  }

  private Node createMockNode(String nodeType, Node parent) {
    return createMockNode(nodeType, parent, null);
  }

  private Node createMockNode(String nodeType, Node parent, Node next) {
    Node node = createMockNode(nodeType, null);
    node.setParent(parent);
    node.setNext(next);
    return node;
  }

  private FunctionType createMockFunctionType(String name, List<String> parameterTypes) {
    FunctionType functionType = new FunctionType(name, ImmutableList.of(), null);
    functionType.setSourceName("MockSourceName");
    functionType.setSourcePosition(new int[] {0, 0});
    return functionType;
  }

  private ObjectType createMockObjectType(String name) {
    ObjectType objectType = new ObjectType(name, null);
    objectType.setSourceName("MockSourceName");
    objectType.setSourcePosition(new int[] {0, 0});
    return objectType;
  }

  private Node createMockCallNode(String functionName, boolean useGoogPrefix, String... arguments) {
    Node callNode = new Node_Call(Token.CALL, null);
    Node callName = new Node_GetProp(Token.GETPROP, createMockNode(functionName, null));
    callNode.setFirstChild(callName);
    if (useGoogPrefix) {
      callName.setQualifiedName("goog." + functionName);
    }
    for (String argument : arguments) {
      Node argumentNode = new Node_String(Token.STRING, argument);
      callNode.addChild(argumentNode);
    }
    return callNode;
  }
}