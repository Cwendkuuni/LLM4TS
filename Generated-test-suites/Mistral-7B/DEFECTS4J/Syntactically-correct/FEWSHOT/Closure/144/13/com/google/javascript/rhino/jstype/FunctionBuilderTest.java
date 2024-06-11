package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "myFunction";
    FunctionBuilder functionBuilderWithName = functionBuilder.withName(name);

    FunctionType functionType = functionBuilderWithName.build();
    assertEquals(name, functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node sourceNode = new Object();
    FunctionBuilder functionBuilderWithSourceNode = functionBuilder.withSourceNode(sourceNode);

    FunctionType functionType = functionBuilderWithSourceNode.build();
    assertEquals(sourceNode, functionType.getSource());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder("paramName", JSType.STRING);
    Node parametersNode = functionParamBuilder.build();

    FunctionBuilder functionBuilderWithParams = functionBuilder.withParams(functionParamBuilder);

    FunctionType functionType = functionBuilderWithParams.build();
    assertEquals(parametersNode, functionType.getParametersNode());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node parametersNode = new Object();

    FunctionBuilder functionBuilderWithParamsNode = functionBuilder.withParamsNode(parametersNode);

    FunctionType functionType = functionBuilderWithParamsNode.build();
    assertEquals(parametersNode, functionType.getParametersNode());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    JSType returnType = JSType.STRING;
    FunctionBuilder functionBuilderWithReturnType = functionBuilder.withReturnType(returnType);

    FunctionType functionType = functionBuilderWithReturnType.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    JSType returnType = JSType.STRING;
    FunctionBuilder functionBuilderWithInferredReturnType = functionBuilder.withReturnType(returnType).withInferredReturnType(returnType);

    FunctionType functionType = functionBuilderWithInferredReturnType.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isInferredReturnType());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    ObjectType typeOfThis = new ObjectType("ObjectType");
    FunctionBuilder functionBuilderWithTypeOfThis = functionBuilder.withTypeOfThis(typeOfThis);

    FunctionType functionType = functionBuilderWithTypeOfThis.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String templateTypeName = "TemplateType";
    FunctionBuilder functionBuilderWithTemplateName = functionBuilder.withTemplateName(templateTypeName);

    FunctionType functionType = functionBuilderWithTemplateName.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilderForConstructor = functionBuilder.forConstructor();

    FunctionType functionType = functionBuilderForConstructor.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionBuilder functionBuilderForNativeType = functionBuilder.forNativeType();

    FunctionType functionType = functionBuilderForNativeType.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "myFunction";
    JSType returnType = JSType.STRING;
    Node parametersNode = new Object();
    ObjectType typeOfThis = new ObjectType("ObjectType");
    String templateTypeName = "TemplateType";

    FunctionType otherType = new FunctionType(registry, name, null, null, null, templateTypeName, true, true);

    FunctionBuilder functionBuilderCopy = functionBuilder.copyFromOtherFunction(otherType);

    FunctionType functionType = functionBuilderCopy.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }
}