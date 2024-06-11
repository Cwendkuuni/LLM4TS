package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

public class ClosureCodingConventionTest extends CompilationTestBase {

  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("Parent");
    FunctionType childCtor = new FunctionType("Child");
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    ObjectType prototype = parentCtor.getPrototype();
    FunctionType constructor = childCtor.getPrototype();

    assertEquals(parentCtor, constructor.getPrototype());
    assertEquals(childCtor, prototype.getDeclaredProperties().get("constructor"));
  }

  public void testGetclasssDefinedByCall() {
    String source =
        "goog.inherits(MySubClass, SuperClass)";

    Node ast = parse(source);
    Compiler compiler = new Compiler();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    SubclassRelationship relationship = convention.getclasssDefinedByCall(ast);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("MySubClass", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  public void testExtractClassNameIfProvide() {
    String source = "goog.provide('MyClass')";

    Node ast = parse(source);
    Compiler compiler = new Compiler();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.extractClassNameIfProvide(ast, null);

    assertEquals("MyClass", className);
  }

  public void testExtractClassNameIfRequire() {
    String source = "goog.require('MyClass')";

    Node ast = parse(source);
    Compiler compiler = new Compiler();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.extractClassNameIfRequire(ast, null);

    assertEquals("MyClass", className);
  }

  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String functionName = convention.getExportPropertyFunction();

    assertEquals("goog.exportProperty", functionName);
  }

  public void testGetSingletonGetterClassName() {
    String source = "goog.addSingletonGetter(MySingletonGetter, MySingletonClass)";

    Node ast = parse(source);
    Compiler compiler = new Compiler();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    String className = convention.getSingletonGetterClassName(ast);

    assertEquals("MySingletonClass", className);
  }

  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyClass");
    FunctionType getterType = new FunctionType("getMyInstance");
    ObjectType objectType = new ObjectType("MyInstance");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);

    FunctionType instance_ = functionType.getDeclaredProperties().get("instance_");
    assertEquals(objectType, instance_);
    FunctionType getter = functionType.getDeclaredProperties().get("getInstance");
    assertEquals(getterType, getter);
  }

  public void testIsPropertyTestFunction() {
    String source = "goog.isDef(someValue)";

    Node ast = parse(source);
    Compiler compiler = new Compiler();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node call = ast.getFirstChild().getNext();
    assertTrue(convention.isPropertyTestFunction(call));
  }

  public void testGetObjectLiteralCast() {
    String source = "goog.reflect.object.cast(someObject, MyType)";

    Node ast = parse(source);
    Compiler compiler = new Compiler();
    ClosureCodingConvention convention = new ClosureCodingConvention();

    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(
        new NodeTraversal(ast), ast);

    assertNotNull(objectLiteralCast);
    assertEquals("MyType", objectLiteralCast.getTypeName());
    assertEquals("someObject", objectLiteralCast.getValueExpression());
  }
}