package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    String name = "myFunction";
    FunctionType functionType = builder.withName(name).build();
    assertEquals(name, functionType.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    Node sourceNode = new Node();
    FunctionType functionType = builder.withSourceNode(sourceNode).build();
    assertNotNull(functionType.getSource());
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node paramsNode = paramBuilder.withName("param1").withType(registry.getBooleanType()).build();

    FunctionType functionType = builder.withParams(paramBuilder).withReturnType(registry.getBooleanType()).build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    Node paramsNode = new Node();

    FunctionType functionType = builder.withParamsNode(paramsNode).withReturnType(registry.getBooleanType()).build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    JSType returnType = registry.getBooleanType();
    FunctionType functionType = builder.withReturnType(returnType).build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    JSType returnType = registry.getBooleanType();
    FunctionType functionType = builder.withReturnType(returnType).withInferredReturnType(returnType).build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    ObjectType typeOfThis = new ObjectType(registry, "Object");
    FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    String templateTypeName = "MyTemplate";
    FunctionType functionType = builder.withTemplateName(templateTypeName).build();
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder.forConstructor().build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder.forNativeType().build();
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    String name = "myFunction";
    Node sourceNode = new Node();
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    Node paramsNode = paramBuilder.withName("param1").withType(registry.getBooleanType()).build();

    FunctionType otherType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, paramsNode, registry.getBooleanType(false), false), null, null, false, false);

    FunctionType functionType = builder.copyFromOtherFunction(otherType).build();

    assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }
}