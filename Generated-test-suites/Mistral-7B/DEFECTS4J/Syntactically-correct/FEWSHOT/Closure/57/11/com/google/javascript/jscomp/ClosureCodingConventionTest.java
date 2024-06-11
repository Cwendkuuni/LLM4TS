package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.ErrorType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor");
    FunctionType childCtor = new FunctionType("ChildCtor");
    SubclassType relationship = ClosureCodingConvention.SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, relationship);
    Node parentPrototype = parentCtor.getPrototype();
    Node childPrototype = childCtor.getPrototype();
    assertNotNull(childPrototype.getProperty("constructor"));
    assertNotNull(childPrototype.getProperty("superClass_"));
    assertEquals(parentPrototype, childPrototype.getProperty("superClass_"));
    assertEquals(childPrototype, parentPrototype.getChild(0).getProperty("prototype"));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newInstance("goog.inherits(MySubClass, SuperClass)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertEquals(ClosureCodingConvention.SubclassType.INHERITS, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclass().getName());
    assertEquals("SuperClass", relationship.getSuperclass().getName());
  }

  @Test
  public void testGetclasssDefinedByCallWithMixin() {
    Node node = Node.newInstance("goog.mixin(MySubClass.prototype, SuperClass.prototype)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertEquals(ClosureCodingConvention.SubclassType.MIXIN, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclass().getName());
    assertEquals("SuperClass", relationship.getSuperclass().getName());
  }

  @Test
  public void testGetclasssDefinedByCallInvalid() {
    Node node = Node.newInstance("goog.inherit(MySubClass, SuperClass)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertNull(relationship);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newInstance("goog.provide('MyClass')");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, null);
    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newInstance("goog.require('MyClass')");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);
    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfGoogInvalid() {
    Node node = Node.newInstance("goog.foo()");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfGoog(node, null, "goog.foo");
    assertNull(className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = Node.newInstance("a.b.c.prototype");
    assertTrue(ClosureCodingConvention.endsWithPrototype(node));
  }

  @Test
  public void testEndsWithPrototypeInvalid() {
    Node node = Node.newInstance("a.b.c");
    assertFalse(ClosureCodingConvention.endsWithPrototype(node));
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
    Node node = Node.newInstance("goog.addDependency(['MyType']);");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);
    assertEquals(1, typeNames.size());
    assertEquals("MyType", typeNames.get(0));
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("goog.abstractMethod", convention.getAbstractMethodName());
  }

  @Test
  public void testSingletonGetterClassName() {
    Node node = Node.newInstance("goog.addSingletonGetter(MyClass, 'getName')");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.getSingletonGetterClassName(node);
    assertEquals("MyClass", className);
  }

  @Test
  public void testSingletonGetterClassNameInvalid() {
    Node node = Node.newInstance("goog.addSingletonGetter(MyClass, 'getFoo')");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.getSingletonGetterClassName(node);
    assertNull(className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyClass");
    FunctionType getterType = new FunctionType("getName", JSTypeNative.STRING_TYPE);
    ObjectType objectType = new ObjectType("MyClass");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);
    assertEquals("getName", functionType.getProperties().get("getInstance").getName());
    assertEquals("getName", functionType.getProperties().get("instance_").getName());
  }

  @Test
  public void testGetGlobalObject() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("goog.global", convention.getGlobalObject());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newInstance("goog.isString('foo')");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testIsPropertyTestFunctionInvalid() {
    Node node = Node.newInstance("goog.isDef('foo')");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPropertyTestFunction(node));
  }

  @Test
  public void testObjectLiteralCast() {
    Node node = Node.newInstance("goog.reflect.object.getPrototypeOf({foo: 'bar'})");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(NodeTraversal.emptyTraversal(), node);
    assertEquals("Object", objectLiteralCast.getTypeName());
    assertEquals("{foo: 'bar'}", objectLiteralCast.getObjectLiteral().toString());
  }

  @Test
  public void testObjectLiteralCastInvalid() {
    Node node = Node.newInstance("goog.reflect.object.getPrototypeOf(42)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(NodeTraversal.emptyTraversal(), node);
    assertNull(objectLiteralCast);
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = Node.newInstance("function foo(bar?) { }");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isOptionalParameter(Node.newInstance("bar")));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = Node.newInstance("function foo(args) { }");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isVarArgsParameter(Node.newInstance("args")));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("foo"));
  }

  @Test
  public void testAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> functions = convention.getAssertionFunctions();
    assertEquals(8, functions.size());
    for (AssertionFunctionSpec function : functions) {
      assertEquals(1, function.getParameterTypes().size());
      assertEquals(function.getName(), function.getCallName());
    }
  }

  @Test
  public void testBind() {
    Node node = Node.newInstance("goog.bind(function(a, b) { }, thisValue, args)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind bind = convention.describeFunctionBind(node);
    assertEquals("function(a, b)", bind.getFunction().toString());
    assertEquals("thisValue", bind.getThisValue().toString());
    assertEquals("args", bind.getArguments().toString());
  }

  @Test
  public void testBindWithPartial() {
    Node node = Node.newInstance("goog.partial(function(a, b) { }, arg1)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind bind = convention.describeFunctionBind(node);
    assertEquals("function(a, b)", bind.getFunction().toString());
    assertEquals(null, bind.getThisValue());
    assertEquals("arg1", bind.getArguments().get(0).toString());
  }

  @Test
  public void testBindInvalid() {
    Node node = Node.newInstance("goog.bind(function(a) { }, thisValue, arg1, arg2)");
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Bind bind = convention.describeFunctionBind(node);
    assertNull(bind);
  }
}