package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link ClosureCodingConvention} class.
 */
public class ClosureCodingConventionTest {

  private static final String CLASS_NAME = "MyClass";
  private static final String SUPER_CLASS_NAME = "MySuperClass";

  private ClosureCodingConvention codingConvention;

  @Before
  public void setUp() {
    codingConvention = mock(ClosureCodingConvention.class);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = createNode("goog.inherits(MyClass, MySuperClass)");
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals(CLASS_NAME, relationship.getSubclassName());
    assertEquals(SUPER_CLASS_NAME, relationship.getSuperclassName());
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node node = createNode("MyClass.mixin(MySuperClass.prototype)");
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals(CLASS_NAME, relationship.getSubclassName());
    assertEquals(SUPER_CLASS_NAME, relationship.getSuperclassName());
  }

  @Test
  public void testGetclasssDefinedByCall_invalid() {
    Node node = createNode("goog.inherits(MyClass, MySuperClass, MyThirdClass)");
    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);
    assertEquals(null, relationship);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('MyClass')");
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals(CLASS_NAME, className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('MyClass')");
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals(CLASS_NAME, className);
  }

  @Test
  public void testExtractClassNameIfGoog_invalidCall() {
    Node node = createNode("goog.otherFunction()");
    String className = codingConvention.extractClassNameIfGoog(node, null, "goog.require");
    assertEquals(null, className);
  }

  @Test
  public void testExtractClassNameIfGoog_invalidFunctionName() {
    Node node = createNode("goog.require('MyClass')");
    String className = codingConvention.extractClassNameIfGoog(node, null, "goog.otherFunction");
    assertEquals(null, className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String functionName = codingConvention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", functionName);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = createNode("goog.addSingletonGetter(MyClass, 'myProperty')");
    String getterClassName = codingConvention.getSingletonGetterClassName(node);
    assertEquals("MyClass.myProperty", getterClassName);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = mock(FunctionType.class);
    FunctionType getterType = mock(FunctionType.class);
    ObjectType objectType = mock(ObjectType.class);

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals("getInstance", functionType.getName());
    assertEquals("instance_", functionType.getProperty("instance_"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = createNode("goog.isDef(myVariable)");
    boolean isPropertyTestFunction = codingConvention.isPropertyTestFunction(node);
    assertEquals(true, isPropertyTestFunction);
  }

  @Test
  public void testIsPropertyTestFunction_invalid() {
    Node node = createNode("myVariable = 5");
    boolean isPropertyTestFunction = codingConvention.isPropertyTestFunction(node);
    assertEquals(false, isPropertyTestFunction);
  }

  @Test
  public void testObjectLiteralCast() {
    Node node = createNode("goog.reflect.object(myObject)");
    ObjectLiteralCast objectLiteralCast =
        codingConvention.getObjectLiteralCast(null, node);
    assertEquals("goog.reflect.object", objectLiteralCast.getType());
    assertEquals("myObject", objectLiteralCast.getValue());
  }

  @Test
  public void testObjectLiteralCast_invalid() {
    Node node = createNode("goog.reflect.object('myString')");
    ObjectLiteralCast objectLiteralCast =
        codingConvention.getObjectLiteralCast(null, node);
    assertEquals(null, objectLiteralCast);
  }

  private Node createNode(String jsCode) {
    Node node = Node.newNode(Token.SCOPENAME, CLASS_NAME);
    Node traverser = NodeTraverser.create(codingConvention, null);
    traverser.parseAndTraverse(jsCode, null, null);
    return traverser.getLastParsedNode();
  }

}