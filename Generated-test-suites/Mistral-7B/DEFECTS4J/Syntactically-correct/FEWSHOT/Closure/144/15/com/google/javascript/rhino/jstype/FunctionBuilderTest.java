package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private FunctionBuilder builder;
  private JSTypeRegistry registry;
  private FunctionParamBuilder paramBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    paramBuilder = mock(FunctionParamBuilder.class);
    builder = new FunctionBuilder(registry);
  }

  @Test
  public void testBuild_WithNameAndReturnType() {
    String name = "testFunction";
    JSType returnType = mock(JSType.class);
    FunctionType functionType = builder.withName(name).withReturnType(returnType).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
  }

  @Test
  public void testBuild_WithNameSourceNodeAndParams() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    FunctionType functionType = builder.withName(name).withSourceNode(sourceNode).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getSource());
  }

  @Test
  public void testBuild_WithNameSourceNodeParamsReturnType() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    FunctionType functionType = builder.withName(name).withSourceNode(sourceNode)
        .withParamsNode(mock(Node.class)).withReturnType(returnType).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getSource());
    assertNotNull(functionType.getReturnType());
  }

  @Test
  public void testBuild_WithNameSourceNodeParamsReturnTypeInferredReturnType() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    FunctionType functionType = builder.withName(name).withSourceNode(sourceNode)
        .withParamsNode(mock(Node.class)).withReturnType(returnType)
        .withInferredReturnType(true).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getSource());
    assertNotNull(functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testBuild_WithNameParamsReturnType() {
    String name = "testFunction";
    FunctionParamBuilder param1 = mock(FunctionParamBuilder.class);
    FunctionParamBuilder param2 = mock(FunctionParamBuilder.class);
    JSType returnType = mock(JSType.class);
    when(paramBuilder.build()).thenReturn(new Node[] { mock(Node.class), mock(Node.class) });
    when(paramBuilder.getParamNames()).thenReturn(Arrays.asList("param1", "param2").toArray());
    Node paramsNode = new Node[] { param1.build(), param2.build() };
    FunctionType functionType = builder.withName(name).withParams(paramBuilder)
        .withReturnType(returnType).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
    assertEquals(2, functionType.getParametersCount());
    assertEquals("param1", functionType.getParameterName(0));
    assertEquals("param2", functionType.getParameterName(1));
  }

  @Test
  public void testBuild_WithNameParamsReturnTypeTypeOfThis() {
    String name = "testFunction";
    FunctionParamBuilder param1 = mock(FunctionParamBuilder.class);
    FunctionParamBuilder param2 = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    when(paramBuilder.build()).thenReturn(new Node[] { mock(Node.class), mock(Node.class) });
    when(paramBuilder.getParamNames()).thenReturn(Arrays.asList("param1", "param2").toArray());
    Node paramsNode = new Node[] { param1.build(), param2.build() };
    FunctionType functionType = builder.withName(name).withParams(paramBuilder)
        .withReturnType(mock(JSType.class)).withTypeOfThis(typeOfThis).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
  }

  @Test
  public void testBuild_WithNameParamsReturnTypeTypeOfThisTemplateTypeName() {
    String name = "testFunction";
    String templateTypeName = "TemplateTypeName";
    FunctionParamBuilder param1 = mock(FunctionParamBuilder.class);
    FunctionParamBuilder param2 = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    when(paramBuilder.build()).thenReturn(new Node[] { mock(Node.class), mock(Node.class) });
    when(paramBuilder.getParamNames()).thenReturn(Arrays.asList("param1", "param2").toArray());
    Node paramsNode = new Node[] { param1.build(), param2.build() };
    FunctionType functionType = builder.withName(name).withParams(paramBuilder)
        .withReturnType(mock(JSType.class)).withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testBuild_WithNameParamsReturnTypeInferredReturnTypeTypeOfThis() {
    String name = "testFunction";
    FunctionParamBuilder param1 = mock(FunctionParamBuilder.class);
    FunctionParamBuilder param2 = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    when(paramBuilder.build()).thenReturn(new Node[] { mock(Node.class), mock(Node.class) });
    when(paramBuilder.getParamNames()).thenReturn(Arrays.asList("param1", "param2").toArray());
    Node paramsNode = new Node[] { param1.build(), param2.build() };
    FunctionType functionType = builder.withName(name).withParams(paramBuilder)
        .withReturnType(mock(JSType.class)).withTypeOfThis(typeOfThis)
        .withInferredReturnType(true).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testBuild_WithNameParamsReturnTypeInferredReturnTypeTypeOfThisTemplateTypeName() {
    String name = "testFunction";
    String templateTypeName = "TemplateTypeName";
    FunctionParamBuilder param1 = mock(FunctionParamBuilder.class);
    FunctionParamBuilder param2 = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    when(paramBuilder.build()).thenReturn(new Node[] { mock(Node.class), mock(Node.class) });
    when(paramBuilder.getParamNames()).thenReturn(Arrays.asList("param1", "param2").toArray());
    Node paramsNode = new Node[] { param1.build(), param2.build() };
    FunctionType functionType = builder.withName(name).withParams(paramBuilder)
        .withReturnType(mock(JSType.class)).withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName).withInferredReturnType(true).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertTrue(functionType.isInferredReturnType());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testCopyFromOtherFunction() {
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    Node paramsNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "TemplateTypeName";
    boolean isConstructor = true;
    boolean isNativeType = true;
    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, paramsNode, returnType, true), typeOfThis, templateTypeName, isConstructor, isNativeType);
    FunctionType functionType = builder.copyFromOtherFunction(otherFunctionType).build();
    assertEquals(name, functionType.getReferenceName());
    assertNotNull(functionType.getSource());
    assertNotNull(functionType.getReturnType());
    assertEquals(2, functionType.getParametersCount());
    assertEquals("param1", functionType.getParameterName(0));
    assertEquals("param2", functionType.getParameterName(1));
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forConstructor();
    assertTrue(builder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.forNativeType();
    assertTrue(builder.isNativeType());
  }
}