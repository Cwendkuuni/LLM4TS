package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Set;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ObjectType.OBJECT);
    FunctionType childCtor = new FunctionType("ChildCtor", ObjectType.OBJECT);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newBinaryOp("goog.inherits",
        Node.newName("ChildClass"),
        Node.newName("ParentClass"),
        null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclassName());
    assertEquals("ParentClass", relationship.getSuperclassName());
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node node = Node.newCall("goog.mixin",
        Node.newName("ChildClassPrototype"),
        Node.newName("ParentClassPrototype"),
        null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("ChildClassPrototype", relationship.getSubclassName());
    assertEquals("ParentClassPrototype", relationship.getSuperclassName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newCall("goog.provide", Node.newString("X"), null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, null);

    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newCall("goog.require", Node.newString("X"), null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);

    assertEquals("X", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("notSuperClass"));
  }

  @Test
  public void testEndsWithPrototype() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.endsWithPrototype(Node.newName("a.b")));
    assertTrue(convention.endsWithPrototype(Node.newCall(
        "goog.getObjectByName", "a.b.prototype", null, null)));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = new FunctionType("GetterFunction",
        ObjectType.OBJECT, ObjectType.OBJECT);
    ObjectType objectType = new ObjectType("ObjectType");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(
        new FunctionType("FunctionType", ObjectType.OBJECT),
        getterType, objectType);

    assertEquals(getterType, FunctionType.getByName("FunctionType").getPrototype().getMethod("getInstance"));
    assertEquals(objectType, FunctionType.getByName("FunctionType").getPrototype().getField("instance_"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newCall(
        Node.newName("goog.isString"),
        Node.newString("foo"), null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newCall(
        Node.newName("goog.reflect.object"),
        Node.newName("ObjectType"),
        Node.newObjectLiteral(
            Node.newProperty(
                "propertyName", Node.newString("value"))), null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast cast = convention.getObjectLiteralCast(null, node);

    assertEquals("ObjectType", cast.getTypeName());
    assertEquals(Node.newProperty(
        "propertyName", Node.newString("value")), cast.getCastedNode());
  }

  @Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isOptionalParameter(Node.newName("paramName")));
  }

  @Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isVarArgsParameter(Node.newName("paramName")));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertFalse(convention.isPrivate("name"));
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    List<AssertionFunctionSpec> functions = convention.getAssertionFunctions();

    assertEquals(8, functions.size());
    for (AssertionFunctionSpec function : functions) {
      assertEquals(JSTypeNative.OBJECT_TYPE, function.getType());
    }
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newCall("goog.bind",
        Node.newName("fn"),
        Node.newThis(),
        null, null);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind bind = convention.describeFunctionBind(node);

    assertEquals("fn", bind.getFunctionName());
    assertEquals(Node.newThis(), bind.getThisValue());
    assertNull(bind.getParameters());
  }
}