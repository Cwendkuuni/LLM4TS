package com.google.javascript.jscomp.test.unit.ClosureCodingConvention;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.ClassLikeType;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionInstanceType;
import com.google.javascript.rhino.jstype.ObjectInstanceType;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention convention;

  @org.junit.Before
  public void setUp() {
    convention = new ClosureCodingConvention();
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newTextNode("goog.provide('Foo')");
    Node parent = Node.newTextNode("");
    assertEquals("Foo", convention.extractClassNameIfProvide(node, parent));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newTextNode("goog.require('Foo')");
    Node parent = Node.newTextNode("");
    assertEquals("Foo", convention.extractClassNameIfRequire(node, parent));
  }

  @Test
  public void testExtractClassNameIfGoogNotProvideOrRequire() {
    Node node = Node.newTextNode("goog.something('Foo')");
    Node parent = Node.newTextNode("");
    assertNull(convention.extractClassNameIfGoog(node, parent, "goog.something"));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        convention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        convention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newTextNode("goog.addDependency('Foo', ['Bar'])");
    List<String> result = convention.identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("Bar"), result);
  }

  @Test
  public void testIdentifyTypeDeclarationCallNoArgs() {
    Node node = Node.newTextNode("goog.addDependency('Foo')");
    List<String> result = convention.identifyTypeDeclarationCall(node);
    assertNull(result);
  }

  @Test
  public void testIdentifyTypeDeclarationCallWrongCall() {
    Node node = Node.newTextNode("goog.addDependency('Foo', 42)");
    List<String> result = convention.identifyTypeDeclarationCall(node);
    assertNull(result);
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        convention.getAbstractMethodName());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newTextNode("goog.addSingletonGetter(Foo, 'bar')");
    assertEquals("Foo.bar",
        convention.getSingletonGetterClassName(node));
  }

  @Test
  public void testGetSingletonGetterClassNameWrongCall() {
    Node node = Node.newTextNode("goog.addSingletonGetter(Foo, 42)");
    assertNull(convention.getSingletonGetterClassName(node));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("Foo",
        ImmutableList.<FunctionType>of(), new ObjectType("Object"),
        new ObjectInstanceType("FooInstance"));
    ObjectType getterType = new ObjectType("GetterFunction");
    ObjectType objectType = new ObjectType("Foo");
    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(1, functionType.getDeclaredProperties().size());
    FunctionInstanceType instance = functionType.getDeclaredProperties().get(0);
    assertEquals("getInstance", instance.getName().getValue());
    assertEquals(getterType, instance.getType());

    assertEquals(1, functionType.getDeclaredProperties().size());
    instance = functionType.getDeclaredProperties().get(0);
    assertEquals("instance_", instance.getName().getValue());
    assertEquals(objectType, instance.getType());
  }

  @Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", convention.getGlobalObject());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newTextNode("goog.isDef(foo)");
    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testIsPropertyTestFunctionWrongCall() {
    Node node = Node.newTextNode("goog.isDefAndNotNull(foo)");
    assertFalse(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newTextNode("goog.reflect.object.cast(foo, 'Bar')");
    FunctionType type = new FunctionType("ObjectCaster",
        ImmutableList.<FunctionType>of(), new ObjectType("Object"),
        new ObjectType("Object"));
    ObjectType objectType = new ObjectType("Bar");
    ObjectLiteralCast expected = new ObjectLiteralCast("Bar", node.getLastChild());
    ObjectLiteralCast result = convention.getObjectLiteralCast(node, null);
    assertEquals(expected, result);
  }

  @Test
  public void testGetObjectLiteralCastNoCast() {
    Node node = Node.newTextNode("goog.reflect.object.get(foo, 'bar')");
    assertNull(convention.getObjectLiteralCast(node, null));
  }

  @Test
  public void testGetObjectLiteralCastWrongCall() {
    Node node = Node.newTextNode("goog.reflect.object.get(foo, 42)");
    assertNull(convention.getObjectLiteralCast(node, null));
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = Node.newTextNode("function Foo(a) {}");
    FunctionType functionType = (FunctionType) convention.getFunctionType(node);
    assertFalse(convention.isOptionalParameter(functionType.getParameters().get(0)));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = Node.newTextNode("function Foo(a, ...b) {}");
    FunctionType functionType = (FunctionType) convention.getFunctionType(node);
    assertFalse(convention.isVarArgsParameter(functionType.getParameters().get(1)));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(convention.isPrivate("foo"));
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("Parent",
        ImmutableList.<FunctionType>of(), new ObjectType("Object"),
        new ObjectInstanceType("ParentInstance"));
    FunctionType childCtor = new FunctionType("Child",
        ImmutableList.<FunctionType>of(parentCtor.getType()),
        new ObjectType("Object"), new ObjectInstanceType("ChildInstance"));
    SubclassType type = SubclassType.INHERITS;
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(2, childCtor.getDeclaredProperties().size());
    FunctionInstanceType superClass = childCtor.getDeclaredProperties().get(0);
    assertEquals("superClass_", superClass.getName().getValue());
    assertEquals(parentCtor.getType(), superClass.getType());

    FunctionInstanceType constructor = childCtor.getDeclaredProperties().get(1);
    assertEquals("constructor", constructor.getName().getValue());
    assertEquals(childCtor, constructor.getType());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newTextNode("goog.inherits(Child, Parent)");
    SubclassRelationship result = convention.getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, result.getType());
    assertEquals("Child", result.getClassName1());
    assertEquals("Parent", result.getClassName2());
  }

  @Test
  public void testGetclasssDefinedByCallNoCall() {
    Node node = Node.newTextNode("Child");
    assertNull(convention.getclasssDefinedByCall(node));
  }

  @Test
  public void testGetclasssDefinedByCallWrongCall() {
    Node node = Node.newTextNode("goog.inherits(Child, ['Parent'])");
    assertNull(convention.getclasssDefinedByCall(node));
  }

  @Test
  public void testGetclasssDefinedByCallMixin() {
    Node node = Node.newTextNode("goog.mixin(Child, Parent.prototype)");
    SubclassType type = SubclassType.MIXIN;
    SubclassRelationship result = convention.getclasssDefinedByCall(node);
    assertEquals(type, result.getType());
    assertEquals("Child", result.getClassName1());
    assertEquals("Parent", result.getClassName2());
  }

  @Test
  public void testGetclasssDefinedByCallMixinWrongCall() {
    Node node = Node.newTextNode("goog.mixin(Child, ['Parent.prototype'])");
    assertNull(convention.getclasssDefinedByCall(node));
  }

  @Test
  public void testGetclasssDefinedByCallMixinPrototype() {
    Node node = Node.newTextNode("goog.mixin(Child.prototype, Parent.prototype)");
    SubclassType type = SubclassType.MIXIN;
    SubclassRelationship result = convention.getclasssDefinedByCall(node);
    assertEquals(type, result.getType());
    assertEquals("Child", result.getClassName1());
    assertEquals("Parent", result.getClassName2());
  }

  @Test
  public void testGetclasssDefinedByCallMixinPrototypeWrongCall() {
    Node node = Node.newTextNode("goog.mixin(Child.prototype, ['Parent.prototype'])");
    assertNull(convention.getclasssDefinedByCall(node));
  }
}