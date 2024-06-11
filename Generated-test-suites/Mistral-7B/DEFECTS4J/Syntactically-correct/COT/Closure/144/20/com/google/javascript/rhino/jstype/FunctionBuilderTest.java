package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private FunctionBuilder functionBuilder;
  private FunctionType functionType;
  private JSTypeRegistry registry;
  private FunctionParamBuilder functionParamBuilder;
  private ObjectType objectType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionType = new FunctionType(registry, "FunctionType", null, null, null, null, false, false);
    functionParamBuilder = new FunctionParamBuilder(registry);
    objectType = new ObjectType(registry, "ObjectType", null);
  }

  @Test
  public void testFunctionBuilderWithName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName("testFunction");
    assertNotNull(functionBuilder.getName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(new Node("testSource"));
    assertNotNull(functionBuilder.getSourceNode());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder paramBuilder =
        functionParamBuilder.withName("param1").withType(new IntType(registry));
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withParams(functionParamBuilder)
        .withReturnType(new StringType(registry));
    Node paramsNode = functionBuilder.build().getParametersNode();
    assertNotNull(paramsNode);
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    Node paramsNode = new Node("testParamsNode");
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParamsNode(paramsNode)
        .withReturnType(new StringType(registry));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withReturnType(new IntType(registry));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withReturnType(new VoidType(registry))
        .withInferredReturnType(new IntType(registry));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withTypeOfThis(objectType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withTemplateName("testTemplateName");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test(expected = RuntimeException.class)
  public void testFunctionBuilderForNativeTypeWithoutSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).forNativeType();
  }

  @Test
  public void testFunctionBuilderForNativeTypeWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).forNativeType();
    functionBuilder.withSourceNode(new Node("testSourceNode"));
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType originalFunctionType = new FunctionType(registry, "testFunction", null, null, null, null, false, false);
    FunctionType copyFunctionType = functionBuilder.copyFromOtherFunction(originalFunctionType).build();
    assertEquals(originalFunctionType, copyFunctionType);
  }

  @Test
  public void testFunctionBuilderBuild() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withName("testFunction")
        .withSourceNode(new Node("testSource"))
        .withParams(functionParamBuilder)
        .withReturnType(new IntType(registry))
        .withTypeOfThis(objectType)
        .withTemplateName("testTemplateName")
        .forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getSource());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertNotNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
  }
}