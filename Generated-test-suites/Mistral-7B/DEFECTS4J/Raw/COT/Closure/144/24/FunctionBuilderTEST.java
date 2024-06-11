package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

/**
 * Test class for FunctionBuilder.
 */
public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  private FunctionType functionType;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilder() {
    FunctionType expectedFunctionType = new FunctionType(registry, "functionName", null, null, null, null, false, false);
    functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNotNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
    assertNotNull(expectedFunctionType.getReferenceName());
    assertNull(expectedFunctionType.getSource());
    assertNull(expectedFunctionType.getParametersNode());
    assertNull(expectedFunctionType.getReturnType());
    assertNull(expectedFunctionType.getTypeOfThis());
    assertNull(expectedFunctionType.getTemplateTypeName());
    assertFalse(expectedFunctionType.isConstructor());
    assertFalse(expectedFunctionType.isNativeType());
    assertEquals(expectedFunctionType.getReferenceName(), functionType.getReferenceName());
  }

  @Test
  public void testSetName() {
    functionBuilder.withName("functionName");
    functionType = functionBuilder.build();
    assertEquals("functionName", functionType.getReferenceName());
  }

  @Test
  public void testSetSourceNode() {
    Node sourceNode = new Node();
    functionBuilder.withSourceNode(sourceNode);
    functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testSetParametersNode() {
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    Node parametersNode = functionParamBuilder.build();
    functionBuilder.withParamsNode(parametersNode);
    functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testSetReturnType() {
    FunctionType returnTypeFunctionType = new FunctionType(registry, "returnTypeFunction", null, null, null, null, false, false);
    functionBuilder.withReturnType(returnTypeFunctionType);
    functionType = functionBuilder.build();
    assertEquals(returnTypeFunctionType, functionType.getReturnType());
  }

  @Test
  public void testSetInferredReturnType() {
    FunctionType returnTypeFunctionType = new FunctionType(registry, "returnTypeFunction", null, null, null, null, false, false);
    functionBuilder.withInferredReturnType(returnTypeFunctionType);
    functionType = functionBuilder.build();
    assertTrue(functionType.isInferredReturnType());
    assertEquals(returnTypeFunctionType, functionType.getReturnType());
  }

  @Test
  public void testSetTypeOfThis() {
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    functionBuilder.withTypeOfThis(objectType);
    functionType = functionBuilder.build();
    assertEquals(objectType, functionType.getTypeOfThis());
  }

  @Test
  public void testSetTemplateName() {
    functionBuilder.withTemplateName("templateName");
    functionType = functionBuilder.build();
    assertEquals("templateName", functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    functionBuilder.forConstructor();
    functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test(expected = AssertionError.class)
  public void testForConstructorTwice() {
    functionBuilder.forConstructor();
    functionBuilder.forConstructor();
  }

  @Test
  public void testForNativeType() {
    functionBuilder.forNativeType();
    functionType = functionBuilder.build();
    assertTrue(functionType.isNativeType());
  }

  @Test(expected = AssertionError.class)
  public void testForNativeTypeTwice() {
    functionBuilder.forNativeType();
    functionBuilder.forNativeType();
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry, "otherFunctionType", null, null, null, null, false, false);
    functionBuilder.copyFromOtherFunction(otherType);
    functionType = functionBuilder.build();
    assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertEquals(otherType.isNativeType(), functionType.isNativeType());
  }
}