package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(), new ObjectType("Object"));
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(), new ObjectType("Object"));
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("superClass_", childCtor.getDeclaredProperties().get("superClass_").getName());
    assertEquals("constructor", childCtor.getPrototype().getDeclaredProperties().get("constructor").getName());
  }

  @Test
  public void testGetclasssDefinedByCall_Inherits() {
    Node node = Node.newNode("goog.inherits(ChildClass, ParentClass)");
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());

    Node childClass = relationship.getChildClassName();
    Node parentClass = relationship.getParentClassName();
    assertEquals("ChildClass", childClass.getString());
    assertEquals("ParentClass", parentClass.getString());
  }

  @Test
  public void testGetclasssDefinedByCall_Mixin() {
    Node node = Node.newNode("goog.mixin(ChildClass.prototype, ParentClass.prototype)");
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertNotNull(relationship);
    assertEquals(SubclassType.MIXIN, relationship.getType());

    Node childClass = relationship.getChildClassName();
    Node parentClass = relationship.getParentClassName();
    assertEquals("ChildClass", childClass.getString());
    assertEquals("ParentClass", parentClass.getString());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode("goog.provide('MyClass')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("MyClass", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode("goog.require('MyClass')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("MyClass", className);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode("goog.addDependency('MyType', ['Dependency1', 'Dependency2'])");
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("Dependency1", typeNames.get(0));
    assertEquals("Dependency2", typeNames.get(1));
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newNode("goog.bind(fn, thisValue, arg1, arg2)");
    Bind bind = new ClosureCodingConvention().describeFunctionBind(node);
    assertNotNull(bind);
    assertEquals("fn", bind.getFunctionName());
    assertEquals("thisValue", bind.getThisValue());
    List<Node> arguments = bind.getArguments();
    assertEquals(2, arguments.size());
    assertEquals("arg1", arguments.get(0).getString());
    assertEquals("arg2", arguments.get(1).getString());
  }
}