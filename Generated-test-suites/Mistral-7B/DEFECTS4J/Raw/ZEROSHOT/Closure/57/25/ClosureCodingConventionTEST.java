package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention;
  private NodeTraversal nodeTraversal;

  @Before
  public void setup() {
    codingConvention = new ClosureCodingConvention();
    nodeTraversal = mock(NodeTraversal.class);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = createNode("goog.inherits(MySubClass, SuperClass)");
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node, null);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclass().getString());
    assertEquals("SuperClass", relationship.getSuperclass().getString());

    node = createNode("goog.mixin(MySubClass.prototype, SuperClass.prototype)");
    relationship = codingConvention.getclasssDefinedByCall(node, null);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("MySubClass.prototype", relationship.getSubclass().getString());
    assertEquals("SuperClass.prototype", relationship.getSuperclass().getString());

    node = createNode("MySubClass.inherits(SuperClass)");
    assertNull(codingConvention.getclasssDefinedByCall(node, nodeTraversal));

    node = createNode("MySubClass.mixin(SuperClass.prototype)");
    assertNull(codingConvention.getclasssDefinedByCall(node, nodeTraversal));

    node = createNode("MySubClass.inherits(cond ? SuperClass1 : SuperClass2)");
    assertNull(codingConvention.getclasssDefinedByCall(node, nodeTraversal));

    node = createNode("MySubClass.mixin(SuperClass1.prototype)");
    assertNull(codingConvention.getclasssDefinedByCall(node, nodeTraversal));
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("SuperClass", ImmutableList.of());
    FunctionType childCtor = createFunctionType("MySubClass", ImmutableList.of());
    SubclassRelationship relationship = new SubclassRelationship(SubclassType.INHERITS, null, null);

    codingConvention.applySubclassRelationship(parentCtor, childCtor, relationship);

    assertEquals("superClass_", childCtor.getProperties().get("superClass_").getName());
    assertEquals("constructor", childCtor.getPrototype().getProperties().get("constructor").getName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('MyClass')");
    assertEquals("MyClass", codingConvention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('MyClass')");
    assertEquals("MyClass", codingConvention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", codingConvention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", codingConvention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createNode("goog.addDependency('MyClass', ['Dependency1', 'Dependency2'])");
    List<String> dependencies = codingConvention.identifyTypeDeclarationCall(node);
    assertEquals(2, dependencies.size());
    assertEquals("Dependency1", dependencies.get(0));
    assertEquals("Dependency2", dependencies.get(1));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = createNode("a.b.c.prototype");
    assertTrue(codingConvention.endsWithPrototype(node));

    node = createNode("a.b.c");
    assertFalse(codingConvention.endsWithPrototype(node));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = createNode("goog.isDef(x)");
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = createNode("goog.reflect.object(foo)");
    ObjectLiteralCast objectLiteralCast = codingConvention.getObjectLiteralCast(nodeTraversal, node);
    assertEquals("foo", objectLiteralCast.getObjectExpression().getString());
    assertEquals("goog.reflect.object", objectLiteralCast.getTypeName().getString());
  }

  @Test
  public void testIsOptionalParameter() {
    assertFalse(codingConvention.isOptionalParameter(createNode("function foo(x)")));
  }

  @Test
  public void testIsVarArgsParameter() {
    assertFalse(codingConvention.isVarArgsParameter(createNode("function foo(x, ...args)")));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(codingConvention.isPrivate("foo"));
  }

  @Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> assertionFunctions = codingConvention.getAssertionFunctions();
    assertEquals(8, assertionFunctions.size());
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = createNode("goog.bind(foo, {self: this})");
    Bind bind = codingConvention.describeFunctionBind(node);
    assertEquals("foo", bind.getFunction().getString());
    assertEquals("this", bind.getThisValue().getString());
    assertEquals(ImmutableList.of(), bind.getArguments());

    node = createNode("goog.bind(foo, this, 'arg1')");
    bind = codingConversion.describeFunctionBind(node);
    assertEquals("foo", bind.getFunction().getString());
    assertEquals("this", bind.getThisValue().getString());
    assertEquals(ImmutableList.of("arg1"), bind.getArguments());
  }

  private FunctionType createFunctionType(String name, List<String> parameters) {
    ObjectType prototype = new ObjectType(name + "_prototype", ImmutableList.of());
    FunctionType functionType = new FunctionType(name, ImmutableList.of(), prototype, null);
    when(nodeTraversal.getParent(functionType)).thenReturn(prototype);
    return functionType;
  }

  private Node createNode(String nodeString) {
    Node node = Node.createTree(nodeString);
    when(nodeTraversal.getSourceName()).thenAnswer((InvocationOnMock invocation) -> "test");
    return node;
  }

  private static class SubclassRelationship {
    private SubclassType type;
    private Node subclass;
    private Node superclass;

    public SubclassRelationship(SubclassType type, Node subclass, Node superclass) {
      this.type = type;
      this.subclass = subclass;
      this.superclass = superclass;
    }

    public SubclassType getType() {
      return type;
    }

    public Node getSubclass() {
      return subclass;
    }

    public Node getSuperclass() {
      return superclass;
    }
  }

  private static class AssertionFunctionSpec {
    private String functionName;
    private ObjectType functionType;

    public AssertionFunctionSpec(String functionName) {
      this(functionName, JSTypeNative.OBJECT_TYPE);
    }

    public AssertionFunctionSpec(String functionName, ObjectType functionType) {
      this.functionName = functionName;
      this.functionType = functionType;
    }

    public String getFunctionName() {
      return functionName;
    }

    public ObjectType getFunctionType() {
      return functionType;
    }
  }

  private static class Bind {
    private Node function;
    private Node thisValue;
    private List<Node> arguments;

    public Bind(Node function, Node thisValue, List<Node> arguments) {
      this.function = function;
      this.thisValue = thisValue;
      this.arguments = arguments;
    }

    public Node getFunction() {
      return function;
    }

    public Node getThisValue() {
      return thisValue;
    }

    public List<Node> getArguments() {
      return arguments;
    }
  }

  private static boolean isDeprecatedCall(Node callNode) {
    return callNode.getChildCount() == 2 &&
        callNode.getChild(0).getType() == Token.GETPROP;
  }

  private static Node safeNext(Node n) {
    if (n != null) {
      return n.getNext();
    }
    return null;
  }

  private static boolean endsWithPrototype(Node qualifiedName) {
    return qualifiedName.getType() == Token.GETPROP &&
        qualifiedName.getLastChild().getString().equals("prototype");
  }

  private static boolean typeofClassDefiningName(Node callName, String expected) {
    String methodName = callName.getLastChild().getString();
    return methodName.equals(expected);
  }

  private static SubclassType typeofClassDefiningName(Node callName) {
    return typeofClassDefiningName(callName, null) != null ? SubclassType.INHERITS : SubclassType.MIXIN;
  }

  private static Node createNode(String nodeString) {
    return Node.createTree(nodeString);
  }

  private static Node createNode(String nodeString, String name) {
    Node node = Node.createTree(nodeString);
    node.setQualifiedName(name);
    return node;
  }

  private static Node createNode(String nodeString, String name, List<Node> children) {
    Node node = Node.createTree(nodeString);
    node.setQualifiedName(name);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private static Node createNode(String nodeString, String name, List<Node> children, ObjectType objectType) {
    FunctionType functionType = new FunctionType(name, ImmutableList.of(), objectType, null);
    Node node = Node.createTree(nodeString);
    node.setQualifiedName(name);
    node.setType(functionType);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private static Node createNode(String nodeString, String name, List<Node> children, ObjectType objectType, Node parent) {
    FunctionType functionType = new FunctionType(name, ImmutableList.of(), objectType, parent);
    Node node = Node.createTree(nodeString);
    node.setQualifiedName(name);
    node.setType(functionType);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private static void whenNodeIsCall(Node node, String callName, List<Node> args) {
    when(nodeTraversal.getFirstChild(node)).thenReturn(createNode("goog." + callName, null));
    when(nodeTraversal.getNext(node)).thenReturn(args.get(0));
    for (int i = 1; i < args.size(); i++) {
      when(nodeTraversal.getNext(node)).thenReturn(args.get(i));
    }
  }

  private static Answer<Node> returnNullAnswer = new Answer<Node>() {
    @Override
    public Node answer(InvocationOnMock invocation) throws Throwable {
      return null;
    }
  };

  private static void whenNodeIsNull(Node node) {
    when(nodeTraversal.getSourceName()).thenAnswer(returnNullAnswer);
    when(nodeTraversal.getFirstChild(node)).thenAnswer(returnNullAnswer);
  }

  private static void whenNodeIsFunctionType(Node node, FunctionType functionType) {
    when(nodeTraversal.getSourceName()).thenReturn("test");
    when(nodeTraversal.getFirstChild(node)).thenReturn(createNode("function " + functionType.getName() + "()"));
  }

  private static void whenNodeIsObjectType(Node node, ObjectType objectType) {
    when(nodeTraversal.getSourceName()).thenReturn("test");
    when(nodeTraversal.getFirstChild(node)).thenReturn(createNode("ObjectType " + objectType.getName(), null));
  }

  private static void whenNodeIsObjectLiteral(Node node, ObjectType objectType) {
    when(nodeTraversal.getSourceName()).thenReturn("test");
    when(nodeTraversal.getFirstChild(node)).thenReturn(createNode("{ }", objectType.getName()));
  }

  private static void whenNodeIsArrayLiteral(Node node, ObjectType arrayType) {
    when(nodeTraversal.getSourceName()).thenReturn("test");
    when(nodeTraversal.getFirstChild(node)).thenReturn(createNode("[ ]", null));
    when(nodeTraversal.getNext(node)).thenReturn(createNode("null", null));
  }

  private static void whenNodeIsCallWithArguments(Node node, Node callNode, List<Node> arguments) {
    whenNodeIsCall(node, "CALL", ImmutableList.of(callNode, createArrayLiteral(arguments)));
  }

  private static void whenNodeIsCallWithArgumentsAndThisValue(Node node, Node callNode, Node thisValue, List<Node> arguments) {
    whenNodeIsCall(node, "CALL", ImmutableList.of(callNode, thisValue, createArrayLiteral(arguments)));
  }

  private static void whenNodeIsPropertyAccess(Node node, String propertyName, Node object) {
    whenNodeIsCall(node, "GETPROP", ImmutableList.of(createNode(propertyName), object));
  }

  private static Node createArrayLiteral(List<Node> nodes) {
    Node arrayLiteral = Node.createTree("[ ]");
    for (Node node : nodes) {
      arrayLiteral.addChild(node);
    }
    return arrayLiteral;
  }
}