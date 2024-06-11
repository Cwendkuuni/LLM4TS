package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    FunctionBuilder result = functionBuilder.withName("test");
    assertEquals("test", result.build().getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, result.build().getSource());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    Node parametersNode = paramBuilder.build();
    FunctionBuilder result = functionBuilder.withParams(paramBuilder);
    assertEquals(parametersNode, result.build().getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, result.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = new JSType(registry);
    FunctionBuilder result = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, result.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = new JSType(registry);
    FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
    assertTrue(result.build().isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType(registry);
    FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, result.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder result = functionBuilder.withTemplateName("template");
    assertEquals("template", result.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder result = functionBuilder.forConstructor();
    assertTrue(result.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder result = functionBuilder.forNativeType();
    assertTrue(result.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, null, null, null, null, null, false, false);
    FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
    FunctionType builtType = result.build();

    assertEquals(otherType.getReferenceName(), builtType.getReferenceName());
    assertEquals(otherType.getSource(), builtType.getSource());
    assertEquals(otherType.getParametersNode(), builtType.getParametersNode());
    assertEquals(otherType.getReturnType(), builtType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), builtType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), builtType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), builtType.isConstructor());
    assertEquals(otherType.isNativeObjectType(), builtType.isNativeObjectType());
  }

  @Test
  public void testBuild() {
    FunctionType builtType = functionBuilder.build();
    assertNull(builtType.getReferenceName());
    assertNull(builtType.getSource());
    assertNull(builtType.getParametersNode());
    assertNull(builtType.getReturnType());
    assertNull(builtType.getTypeOfThis());
    assertNull(builtType.getTemplateTypeName());
    assertFalse(builtType.isConstructor());
    assertFalse(builtType.isNativeObjectType());
  }
}