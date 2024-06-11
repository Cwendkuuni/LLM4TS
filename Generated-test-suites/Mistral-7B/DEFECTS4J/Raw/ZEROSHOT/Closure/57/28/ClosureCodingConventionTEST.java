package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collections;
import java.util.List;

/**
 * Tests for the ClosureCodingConvention class.
 */
public class ClosureCodingConventionTest {

  private static final SubclassType INHERITS = SubclassType.INHERITS;
  private static final SubclassType MIXIN = SubclassType.MIXIN;

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor");
    FunctionType childCtor = createFunctionType("ChildCtor");
    ClosureCodingConvention convention = new ClosureCodingConvention();

    convention.applySubclassRelationship(parentCtor, childCtor, INHERITS);
    assertNotNull(childCtor.getPrototype().getDeclaredProperty("constructor"));
    assertEquals(parentCtor, childCtor.getPrototype().getDeclaredProperty("constructor"));
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_inherit() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newName("goog", "inherits")),
        Node.newName("SubClass"), Node.newName("SuperClass"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals(MIXIN, convention.getclasssDefinedByCall(node.getFirstChild(), node));
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_mixin() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newName("goog", "mixin"),
        Node.newName("SubClass.prototype"),
        Node.newName("SuperClass.prototype"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals(MIXIN, convention.getclasssDefinedByCall(node.getFirstChild(), node));
  }

  @org.junit.Test
  public void testGetclasssDefinedByCall_none() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("SomeFunction"), null);
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertNull(convention.getclasssDefinedByCall(node.getFirstChild(), node));
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("goog", "provide"),
        Node.newName("X"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("X", convention.extractClassNameIfProvide(node, null));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("goog", "require"),
        Node.newName("X"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("X", convention.extractClassNameIfRequire(node, null));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("goog", "addSingletonGetter"),
        Node.newName("getX"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("getX", convention.getSingletonGetterClassName(node, null));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = createFunctionType("X");
    FunctionType getterType = createFunctionType("getX");
    ObjectType objectType = createObjectType("X");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);
    assertNotNull(functionType.getDeclaredProperty("getInstance"));
    assertNotNull(functionType.getDeclaredProperty("instance_"));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("goog", "isDef"),
        Node.newName("x"));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("goog.reflect.object", "cast"),
        Node.newName("goog.Object"), Node.newObjectLiteral());
    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast result = convention.getObjectLiteralCast(null, node);
    assertNotNull(result);
    assertEquals("goog.Object", result.getTypeName());
    assertEquals(Node.newObjectLiteral(), result.getCastedNode());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isOptionalParameter(Node.newName("x")));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isVarArgsParameter(Node.newName("args")));
  }

  @org.junit.Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("x"));
  }

  @org.junit.Test
  public void testAssertFunctionSpecs() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> assertions = convention.getAssertionFunctions();
    assertEquals(8, assertions.size());
    for (AssertionFunctionSpec assertion : assertions) {
      assertNotNull(assertion);
    }
  }

  @org.junit.Test
  public void testBind() {
    Node node = Node.newNode(Token.CALL, null, Node.newName("goog.bind"),
        Node.newName("fn"), Node.newName("self"), Node.newArrayLiteral());
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind result = convention.describeFunctionBind(node);
    assertNotNull(result);
    assertEquals(Node.newName("fn"), result.getFunction());
    assertEquals(Node.newName("self"), result.getThisValue());
    assertEquals(Collections.emptyList(), result.getParameters());
  }

  private FunctionType createFunctionType(String name) {
    FunctionType type = new FunctionType(Node.newName(name), null, null);
    type.setSourceName("Test");
    return type;
  }

  private ObjectType createObjectType(String name) {
    ObjectType type = new ObjectType(Node.newName(name), null, null);
    type.setSourceName("Test");
    return type;
  }

  private Node newArrayLiteral() {
    return Node.newArrayLiteral();
  }

  private Node newObjectLiteral() {
    return Node.newObjectLiteral();
  }

  private static enum SubclassType {
    INHERITS,
    MIXIN
  }

  private static class AssertionFunctionSpec {
    private final String name;
    private final JSTypeNative type;

    AssertionFunctionSpec(String name, JSTypeNative type) {
      this.name = name;
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public JSTypeNative getType() {
      return type;
    }
  }
}