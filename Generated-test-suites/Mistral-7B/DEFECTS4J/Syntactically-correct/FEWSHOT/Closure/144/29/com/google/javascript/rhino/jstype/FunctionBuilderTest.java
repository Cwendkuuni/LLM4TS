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

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private Node node;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    node = mock(Node.class);
  }

  @Test
  public void testWithName() {
    functionBuilder.withName("name");
    assertNotNull(functionBuilder.getName());
  }

  @Test
  public void testWithSourceNode() {
    functionBuilder.withSourceNode(node);
    assertNotNull(functionBuilder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    when(functionParamBuilder.build()).thenReturn(new Object[] { node, node });
    functionBuilder.withParams(functionParamBuilder);
    assertNotNull(functionBuilder.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    functionBuilder.withParamsNode(node);
    assertNotNull(functionBuilder.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    assertNotNull(functionBuilder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    assertTrue(functionBuilder.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = mock(ObjectType.class);
    functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    functionBuilder.withTemplateName("templateTypeName");
    assertEquals("templateTypeName", functionBuilder.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    functionBuilder.forConstructor();
    assertTrue(functionBuilder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    functionBuilder.forNativeType();
    assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = mock(FunctionType.class);
    when(otherType.getReferenceName()).thenReturn("otherName");
    when(otherType.getSource()).thenReturn(node);
    when(otherType.getParametersNode()).thenReturn(node);
    when(otherType.getReturnType()).thenReturn(mock(JSType.class));
    when(otherType.getTypeOfThis()).thenReturn(mock(ObjectType.class));
    when(otherType.getTemplateTypeName()).thenReturn("templateTypeName");
    when(otherType.isConstructor()).thenReturn(true);
    when(otherType.isNativeObjectType()).thenReturn(false);

    functionBuilder.copyFromOtherFunction(otherType);

    assertEquals("otherName", functionBuilder.getName());
    assertNotNull(functionBuilder.getSourceNode());
    assertNotNull(functionBuilder.getParametersNode());
    assertNotNull(functionBuilder.getReturnType());
    assertNotNull(functionBuilder.getTypeOfThis());
    assertEquals("templateTypeName", functionBuilder.getTemplateTypeName());
    assertTrue(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionType expectedFunctionType = mock(FunctionType.class);
    when(expectedFunctionType.getReferenceName()).thenReturn("name");
    when(expectedFunctionType.getSource()).thenReturn(node);
    when(expectedFunctionType.getParametersNode()).thenReturn(node);
    when(expectedFunctionType.getReturnType()).thenReturn(mock(JSType.class));
    when(expectedFunctionType.getTypeOfThis()).thenReturn(mock(ObjectType.class));
    when(expectedFunctionType.getTemplateTypeName()).thenReturn("templateTypeName");
    when(expectedFunctionType.isConstructor()).thenReturn(false);
    when(expectedFunctionType.isNativeObjectType()).thenReturn(false);

    FunctionType actualFunctionType = functionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }
}