package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

/**
 * Tests for {@link ClosureCodingConvention} class.
 */
public class ClosureCodingConventionTest {

  private static final CodingConvention convention = new ClosureCodingConvention();

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType prototype = mockFunctionType("Prototype");
    ObjectType constructor = mockObjectType("Constructor");
    when(parentCtor.getPrototype()).thenReturn(prototype);
    when(prototype.defineDeclaredProperty(anyString(), anyObject(),
        anyObject())).thenReturn(constructor);
    when(prototype.getSource()).thenReturn("ParentCtor prototype");

    when(childCtor.defineDeclaredProperty("superClass_", anyObject(),
        anyObject())).thenReturn(prototype);
    when(childCtor.getPrototype()).thenReturn(constructor);
    when(childCtor.getSource()).thenReturn("ChildCtor prototype");

    assertThat(childCtor.getPrototype().getProperties().get("constructor"))
        .isEqualTo(childCtor);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = mockNode("goog.inherits(MySubClass, MySuperClass)");
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode);
    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);
    assertThat(relationship.getSubclass()).isEqualTo("MySubClass");
    assertThat(relationship.getSuperclass()).isEqualTo("MySuperClass");
  }

  @Test
  public void testExtractClassNameIfProvideAndRequire() {
    Node callNodeProvide = mockNode("goog.provide('MyClass')");
    String classNameProvide = convention.extractClassNameIfProvide(callNodeProvide, null);
    assertThat(classNameProvide).isEqualTo("MyClass");

    Node callNodeRequire = mockNode("goog.require('MyClass')");
    String classNameRequire = convention.extractClassNameIfRequire(callNodeRequire, null);
    assertThat(classNameRequire).isEqualTo("MyClass");
  }

  @Test
  public void testIsSuperClassReference() {
    String propertyName = "superClass_";
    assertThat(convention.isSuperClassReference(propertyName)).isTrue();
  }

  @Test
  public void testExtractClassNameIfGoog() {
    Node callNode = mockNode("goog.addSingletonGetter(MyClass.prototype, 'myProperty')");
    String className = convention.extractClassNameIfSingletonGetter(callNode);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testGetExportPropertyFunction() {
    String functionName = convention.getExportPropertyFunction();
    assertThat(functionName).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    String functionName = convention.getExportSymbolFunction();
    assertThat(functionName).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node callNode = mockNode("goog.addDependency('MyType')");
    List<String> typeNames = convention.identifyTypeDeclarationCall(callNode);
    assertThat(typeNames).containsExactly("MyType");
  }

  @Test
  public void testGetAbstractMethodName() {
    String functionName = convention.getAbstractMethodName();
    assertThat(functionName).isEqualTo("goog.abstractMethod");
  }

  @Test
  public void testSingletonGetterClassName() {
    Node callNode = mockNode("goog.addSingletonGetter(MyClass.prototype, 'myProperty')");
    String className = convention.getSingletonGetterClassName(callNode);
    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = mockFunctionType("MyClass");
    FunctionType getterType = mockFunctionType("MyClass.prototype.getMyProperty");
    ObjectType objectType = mockObjectType("MyClass.prototype");
    convention.applySingletonGetter(functionType, getterType, objectType);

    FunctionType instance = mockFunctionType("MyClass.instance_");
    when(instance.getPrototype()).thenReturn(objectType);
    when(instance.defineDeclaredProperty("getInstance", getterType,
        anyObject())).thenReturn(functionType);
    when(instance.defineDeclaredProperty("instance_", objectType,
        anyObject())).thenReturn(objectType);

    FunctionType prototype = mockFunctionType("MyClass.prototype");
    when(prototype.getProperties().get("constructor")).thenReturn(functionType);
    when(prototype.getProperties().get("instance_")).thenReturn(instance);

    assertThat(functionType.getPrototype()).isEqualTo(prototype);
  }

  @Test
  public void testGetGlobalObject() {
    String globalObject = convention.getGlobalObject();
    assertThat(globalObject).isEqualTo("goog.global");
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = mockNode("goog.isDef(myVar)");
    boolean isPropertyTestFunction = convention.isPropertyTestFunction(callNode);
    assertThat(isPropertyTestFunction).isTrue();
  }

  @Test
  public void testPropertyTestFunctions() {
    Set<String> propertyTestFunctions = convention.getPropertyTestFunctions();
    assertThat(propertyTestFunctions).containsExactly("goog.isDef",
        "goog.isNull", "goog.isDefAndNotNull", "goog.isString",
        "goog.isNumber", "goog.isBoolean", "goog.isFunction",
        "goog.isArray", "goog.isObject");
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = mockNode("goog.reflect.object.cast(myObj, MyType)");
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(
        new NodeTraversal(callNode), callNode);

    assertThat(objectLiteralCast).isNotNull();
    assertThat(objectLiteralCast.getType()).isEqualTo("MyType");
    assertThat(objectLiteralCast.getValue()).isEqualTo("myObj");
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = mockNode("function MyFunction(optParam = 1)");
    boolean isOptionalParameter = convention.isOptionalParameter(parameter);
    assertThat(isOptionalParameter).isFalse();
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = mockNode("function MyFunction(args...)");
    boolean isVarArgsParameter = convention.isVarArgsParameter(parameter);
    assertThat(isVarArgsParameter).isFalse();
  }

  @Test
  public void testIsPrivate() {
    String name = "myPrivateVariable";
    boolean isPrivate = convention.isPrivate(name);
    assertThat(isPrivate).isFalse();
  }

  @Test
  public void testGetAssertionFunctions() {
    Collection<AssertionFunctionSpec> assertionFunctions = convention.getAssertionFunctions();
    List<String> assertionFunctionNames = ImmutableList.copyOf(assertionFunctions);

    assertThat(assertionFunctionNames).containsExactly(
        "goog.asserts.assert", "goog.asserts.assertNumber",
        "goog.asserts.assertString", "goog.asserts.assertFunction",
        "goog.asserts.assertObject", "goog.asserts.assertArray",
        "goog.asserts.assertInstanceof"
    );
  }

  @Test
  public void testBindFunction() {
    Node callNode = mockNode("goog.bind(MyFunction, thisValue, arg1, arg2)");
    Bind bind = convention.describeFunctionBind(callNode);

    assertThat(bind).isNotNull();
    assertThat(bind.getFunction()).isEqualTo("MyFunction");
    assertThat(bind.getThisValue()).isNotNull();
    assertThat(bind.getArguments()).containsExactly("arg1", "arg2");
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

  private Node mockNode(String nodeString) {
    Node node = Node.newNode(Token.DUMMY);
    node.setSourcePosition(1, 1, "TestFile.js");
    node.setSourceName("TestFile.js");
    node.setString(nodeString);
    return node;
  }
}