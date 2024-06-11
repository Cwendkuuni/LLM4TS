package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.FunctionBuilder;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithBasicConfiguration() {
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
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "myFunction";
    FunctionType functionType = functionBuilder.withName(name).build();

    assertEquals(name, functionType.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node sourceNode = new Node();
    FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();

    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(new IntType(registry));
    Node paramsNode = paramBuilder.build();

    FunctionType functionType = functionBuilder.withParams(paramBuilder).build();

    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    Node paramsNode = new Node();
    FunctionType functionType = functionBuilder.withParamsNode(paramsNode).build();

    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    JSType returnType = new IntType(registry);
    FunctionType functionType = functionBuilder.withReturnType(returnType).build();

    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    JSType returnType = new IntType(registry);
    FunctionType functionType = functionBuilder.withReturnType(returnType).withInferredReturnType(returnType).build();

    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    ObjectType typeOfThis = new ObjectType(registry, "MyType");
    FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();

    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String templateTypeName = "MyTemplate";
    FunctionType functionType = functionBuilder.withTemplateName(templateTypeName).build();

    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.forConstructor().build();

    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.forNativeType().build();

    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionType otherType = new FunctionType(registry, null, null, null, null, null, true, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);    FunctionType functionType = functionBuilder.copyFromOtherFunction(otherType).build();

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