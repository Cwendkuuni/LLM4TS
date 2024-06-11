package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder builder;
  private Node sourceNode;
  private FunctionParamBuilder paramsBuilder;
  private ObjectType typeOfThis;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    sourceNode = mock(Node.class);
    paramsBuilder = mock(FunctionParamBuilder.class);
    typeOfThis = mock(ObjectType.class);
    builder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    String name = "myFunction";
    builder.withName(name);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getName());
    assertEquals(name, functionType.getName());
  }

  @Test
  public void testWithSourceNode() {
    builder.withSourceNode(sourceNode);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getSource());
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    when(paramsBuilder.build()).thenReturn(mock(Node.class));
    builder.withParams(paramsBuilder);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = mock(JSType.class);
    builder.withReturnType(returnType);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    builder.withInferredReturnType(returnType);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    builder.withTypeOfThis(typeOfThis);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testForConstructor() {
    builder.forConstructor();
    FunctionType functionType = builder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    builder.forNativeType();
    FunctionType functionType = builder.build();
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    String name = "myFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "templateTypeName";
    boolean isConstructor = true;
    FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, true), typeOfThis, templateTypeName, isConstructor, false);

    builder.copyFromOtherFunction(otherFunctionType);
    FunctionType functionType = builder.build();

    assertNotNull(functionType.getName());
    assertEquals(name, functionType.getName());
    assertNotNull(functionType.getSource());
    assertEquals(sourceNode, functionType.getSource());
    assertNotNull(functionType.getParametersNode());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }
}