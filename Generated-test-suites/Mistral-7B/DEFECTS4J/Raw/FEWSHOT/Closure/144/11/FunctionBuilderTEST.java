package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.FunctionParamBuilder.FunctionParamType;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  private FunctionParamType paramType;
  private Node paramsNode;
  private ObjectType returnType;
  private ObjectType typeOfThis;
  private String templateTypeName;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    paramType = mock(FunctionParamType.class);
    returnType = mock(ObjectType.class);
    typeOfThis = mock(ObjectType.class);
    paramsNode = mock(Node.class);
    when(paramType.build()).thenReturn(paramsNode);
    when(registry.createObjectType(Type.OBJECT)).thenReturn(typeOfThis);
  }

  @Test
  public void testWithName() {
    String name = "myFunction";
    functionBuilder.withName(name);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReferenceName());
    assertEquals(name, functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = mock(Node.class);
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getSource());
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    functionBuilder.withParams(new FunctionParamBuilder().withName("param1").build());
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    functionBuilder.withParamsNode(paramsNode);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(paramsNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType inferredReturnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(inferredReturnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
    assertEquals(inferredReturnType, functionType.getReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    functionBuilder.withTemplateName("TemplateTypeName");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTemplateTypeName());
    assertEquals("TemplateTypeName", functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = mock(FunctionType.class);
    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReferenceName());
    assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
    assertNotNull(functionType.getSource());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertNotNull(functionType.getParametersNode());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertNotNull(functionType.getTemplateTypeName());
    assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertEquals(otherType.isConstructor(), functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
    assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
  }
}