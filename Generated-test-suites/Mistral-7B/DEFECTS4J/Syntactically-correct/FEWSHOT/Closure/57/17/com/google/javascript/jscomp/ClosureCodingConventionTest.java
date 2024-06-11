package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Tests for {@link ClosureCodingConvention}.
 */
public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertHasProperty(childCtor.getPrototype(), "constructor", childCtor);
    assertHasProperty(childCtor, "superClass_", parentCtor);
  }

  @Test
  public void testGetclasssDefinedByCall_inherits() {
    Node node = createCallNode("goog.inherits", "Parent", "Child");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertThat(className).isEqualTo("Child");
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node node = createCallNode("goog.mixin", "Parent.prototype", "Child.prototype");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertThat(className).isEqualTo("Child");
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testExtractClassNameIfProvide_googProvide() {
    Node node = createCallNode("goog.provide", "X");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire_googRequire() {
    Node node = createCallNode("goog.require", "X");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfGoog_googProvide() {
    Node node = createCallNode("goog.provide", "goog.X");
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.provide");
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfGoog_googRequire() {
    Node node = createCallNode("goog.require", "goog.X");
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.require");
    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = new ClosureCodingConvention().getExportPropertyFunction();
    assertThat(exportPropertyFunction).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    String exportSymbolFunction = new ClosureCodingConvention().getExportSymbolFunction();
    assertThat(exportSymbolFunction).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createCallNode("goog.addDependency", null, "X", "Y");
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertThat(typeNames).containsExactly("X");
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("X");
    FunctionType getterType = createFunctionType("X.getInstance");
    ObjectType objectType = createObjectType("X.instance_");

    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertHasProperty(functionType, "getInstance", getterType);
    assertHasProperty(functionType, "instance_", objectType);
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = createCallNode("goog.reflect.object", "new Object()", "ObjectType");
    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention().getObjectLiteralCast(new NodeTraversal(null), callNode);
    assertThat(objectLiteralCast.getTypeName()).isEqualTo("ObjectType");
    assertThat(objectLiteralCast.getObjectNode()).isNotNull();
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = createCallNode("goog.isNumber", null);
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(callNode));
  }

  @Test
  public void testIsOptionalParameter() {
    FunctionType functionType = createFunctionType("X");
    Node parameter = createNode(Token.VAR);
    Node node = createCallNode(functionType, null, parameter);

    assertFalse(new ClosureCodingConvention().isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    FunctionType functionType = createFunctionType("X");
    Node parameter = createNode(Token.VAR);
    Node varArgsParameter = createNode(Token.VAR, "args");
    Node callNode = createCallNode(functionType, null, parameter, varArgsParameter);

    assertFalse(new ClosureCodingConvention().isVarArgsParameter(parameter));
    assertTrue(new ClosureCodingConvention().isVarArgsParameter(varArgsParameter));
  }

  @Test
  public void testIsPrivate() {
    String name = "privateVariable";
    assertFalse(new ClosureCodingConvention().isPrivate(name));
  }

  @Test
  public void testGetAssertionFunctions() {
    Collection<AssertionFunctionSpec> assertionFunctions = new ClosureCodingConvention().getAssertionFunctions();
    Set<String> expectedFunctionNames = Sets.newHashSet("goog.asserts.assert",
        "goog.asserts.assertNumber", "goog.asserts.assertString",
        "goog.asserts.assertFunction", "goog.asserts.assertObject",
        "goog.asserts.assertArray", "goog.asserts.assertInstanceof");

    assertEquals(expectedFunctionNames, assertionFunctions.stream().map(AssertionFunctionSpec::getName).collect(ImmutableSet.toImmutableSet()));
  }

  @Test
  public void testBind() {
    Node fn = createNode(Token.FUNCTION, "fn");
    Node self = createNode(Token.NAME, "self");
    Node args = createNode(Token.ARRAYLIT);

    Bind bind = new ClosureCodingConvention().describeFunctionBind(createCallNode(Token.CALL, fn, self, args));

    assertThat(bind.getFunction()).isSameInstance(fn);
    assertThat(bind.getThisValue()).isSameInstance(self);
    List<Node> parameters = bind.getParameters();
    assertThat(parameters).hasSize(args.getChildCount());

    for (int i = 0; i < args.getChildCount(); i++) {
      Node arg = args.getChild(i);
      Node bindArg = parameters.get(i);
      assertEquals(arg, bindArg);
    }
  }

  private FunctionType createFunctionType(String name) {
    ObjectType prototype = new ObjectType(name + "_prototype");
    return new FunctionType(name, ImmutableList.of(), JSTypeNative.VOID_TYPE, prototype);
  }

  private ObjectType createObjectType(String name) {
    return new ObjectType(name);
  }

  private Node createCallNode(Token type, Node... nodes) {
    Node callNode = new Node(type, null);
    for (Node node : nodes) {
      callNode.appendChild(node);
    }
    return callNode;
  }

  private Node createNode(int type, String name) {
    Node node = new Node(type, name);
    node.setSourcePosition(0, 0);
    return node;
  }

  private void assertHasProperty(Node node, String propertyName, Node expected) {
    Node property = node.getDeclaredProperty(propertyName);
    Node expectedNode = (expected == null) ? null : expected;
    assertEquals(expectedNode, property);
  }

  private void assertEquals(Node expected, Node actual) {
    assertEquals(expected.toString(), actual.toString());
  }

  private enum SubclassType {
    INHERITS,
    MIXIN
  }

  private static class AssertionFunctionSpec {
    private final String name;
    private final JSType type;

    AssertionFunctionSpec(String name) {
      this(name, null);
    }

    AssertionFunctionSpec(String name, JSType type) {
      this.name = name;
      this.type = type;
    }

    String getName() {
      return name;
    }
  }
}