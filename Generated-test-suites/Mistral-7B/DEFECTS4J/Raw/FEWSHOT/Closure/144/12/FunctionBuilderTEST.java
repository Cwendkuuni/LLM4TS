package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private JSTypeRegistry registry;
  private ObjectType objectType;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);
  }

  @Test
  public void testWithName() {
    functionBuilder.withName("testFunction");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getName());
    assertEquals("testFunction", functionType.getName());
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
    when(functionParamBuilder.build()).thenReturn(new Node(1));
    functionBuilder.withParams(functionParamBuilder);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(new Node(1), functionType.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = mock(Node.class);
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    functionBuilder.withTypeOfThis(objectType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(objectType, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    functionBuilder.withTemplateName("testTemplate");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getTemplateTypeName());
    assertEquals("testTemplate", functionType.getTemplateTypeName());
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
}