package com.google.javascript.jscomp.test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  private static Node createCallNode(String name, Node... args) {
    Node callNode = new Node_call(Token.CALL, null, null);
    Node callName = new Node_property(Token.GETPROP, null, null);
    callName.setQualifiedName(name);
    callNode.setFirstChild(callName);
    for (Node arg : args) {
      callNode.addChild(arg);
    }
    return callNode;
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);
    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testApplySubclassRelationshipWithMixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype");
    FunctionType childPrototype = createFunctionType("ChildPrototype");
    FunctionType childCtor = createFunctionType("ChildCtor");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(null, childCtor, SubclassType.MIXIN);
    Node parent = createCallNode("ParentPrototype");
    Node child = createCallNode("ChildPrototype");
    childCtor.setPrototype(parent);
    childCtor.getPrototype().setConstructor(childCtor);
    Node childCall = createCallNode("ChildCtor");
    Node parentCall = createCallNode("ChildPrototype");
    Node traversal = new NodeTraversal(parentCall);
    Bind bind = convention.describeFunctionBind(traversal.getCurrentNode());
    assertEquals(childCtor, bind.getFunction());
    assertEquals(parent, bind.getThisValue());
    assertEquals(newArrayList(), bind.getParameters());
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createCallNode("goog.provide", "X");
    assertEquals("X", convention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createCallNode("goog.require", "X");
    assertEquals("X", convention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", convention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createCallNode("goog.addDependency", null,
        createArrayLiteral(createStringLiteral("X")));
    assertEquals(newArrayList("X"), convention.identifyTypeDeclarationCall(node));
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", convention.getAbstractMethodName());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = createCallNode("goog.addSingletonGetter", "X");
    assertEquals("X", convention.getSingletonGetterClassName(node));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("SingletonGetter");
    ObjectType objectType = createObjectType("SingletonType");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, null, objectType);
    assertEquals(functionType, functionType.getPrototype().getConstructor());
    assertEquals(functionType.getPrototype(), functionType.getPrototype().getPrototype());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = createCallNode("goog.isDef");
    assertTrue(convention.isPropertyTestFunction(call));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = createCallNode("goog.reflect.object",
        createStringLiteral("X"));
    ObjectLiteralCast expected = new ObjectLiteralCast("X",
        createObjectLiteral());
    ObjectLiteralCast actual = convention.getObjectLiteralCast(null, callNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = createCallNode("goog.someFunction", null);
    assertFalse(convention.isOptionalParameter(node));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = createCallNode("goog.someFunction", null,
        createArrayLiteral(createStringLiteral("arg1"),
            createStringLiteral("arg2")));
    assertFalse(convention.isVarArgsParameter(node));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("foo"));
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> functions = convention.getAssertionFunctions();
    assertEquals(8, functions.size());
  }

  @Test
  public void testDescribeFunctionBind() {
    FunctionType fn = createFunctionType("Fn");
    Node callNode = createCallNode("goog.bind", fn, null);
    Bind expected = new Bind(fn, null, null);
    Bind actual = convention.describeFunctionBind(callNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testDescribeFunctionBindWithThisValue() {
    FunctionType fn = createFunctionType("Fn");
    Node self = createCallNode("SelfType");
    Node callNode = createCallNode("goog.bind", fn, self);
    Bind expected = new Bind(fn, self, null);
    Bind actual = convention.describeFunctionBind(callNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testDescribeFunctionBindWithArguments() {
    FunctionType fn = createFunctionType("Fn");
    Node arg1 = createCallNode("Arg1Type");
    Node arg2 = createCallNode("Arg2Type");
    Node callNode = createCallNode("goog.bind", fn, arg1, arg2);
    Bind expected = new Bind(fn, null, newArrayList(arg1, arg2));
    Bind actual = convention.describeFunctionBind(callNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testDescribeFunctionBindWithPartial() {
    FunctionType fn = createFunctionType("Fn");
    Node arg1 = createCallNode("Arg1Type");
    Node callNode = createCallNode("goog.partial", fn, arg1);
    Bind expected = new Bind(fn, null, newArrayList(arg1));
    Bind actual = convention.describeFunctionBind(callNode);
    assertEquals(expected, actual);
  }

  private FunctionType createFunctionType(String name) {
    FunctionType functionType = new FunctionType(name, new ObjectType("Object"));
    functionType.setParameters(new ObjectType[0]);
    functionType.setSourceName("SourceName");
    return functionType;
  }

  private ObjectType createObjectType(String name) {
    ObjectType objectType = new ObjectType(name);
    objectType.setSourceName("SourceName");
    return objectType;
  }

  private Node createCallNode(String name, Node... args) {
    return createCallNode(null, name, args);
  }

  private Node createCallNode(Node parent, String name, Node... args) {
    Node callNode = new Node_call(Token.CALL, parent, null);
    Node callName = new Node_property(Token.GETPROP, null, null);
    callName.setQualifiedName(name);
    callNode.setFirstChild(callName);
    for (Node arg : args) {
      callNode.addChild(arg);
    }
    return callNode;
  }

  private Node createArrayLiteral(Node... elements) {
    Node arrayLiteral = new Node_arrayliteral(Token.ARRAYLIT, null, null);
    for (Node element : elements) {
      arrayLiteral.addChild(element);
    }
    return arrayLiteral;
  }

  private Node createStringLiteral(String value) {
    return new Node_string(Token.STRING, null, value);
  }
}