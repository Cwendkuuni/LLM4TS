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
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    functionBuilder.withName("testFunction");
    FunctionType functionType = functionBuilder.build();
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testWithSourceNode() {
    Node node = mock(Node.class);
    functionBuilder.withSourceNode(node);
    FunctionType functionType = functionBuilder.build();
    assertEquals(node, functionType.getSource());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder params = mock(FunctionParamBuilder.class);
    when(params.build()).thenReturn(mock(Node.class));
    functionBuilder.withParams(params);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = mock(Node.class);
    functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    Type returnType = mock(Type.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    Type returnType = mock(Type.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = mock(ObjectType.class);
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    functionBuilder.withTemplateName("testTemplate");
    FunctionType functionType = functionBuilder.build();
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
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = mock(FunctionType.class);
    when(otherType.getReferenceName()).thenReturn("testFunction");
    when(otherType.getSource()).thenReturn(mock(Node.class));
    when(otherType.getParametersNode()).thenReturn(mock(Node.class));
    when(otherType.getReturnType()).thenReturn(mock(Type.class));
    when(otherType.getTypeOfThis()).thenReturn(mock(ObjectType.class));
    when(otherType.getTemplateTypeName()).thenReturn("testTemplate");
    when(otherType.isConstructor()).thenReturn(true);
    when(otherType.isNativeType()).thenReturn(true);

    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName());
    assertEquals(otherType.getSource(), functionType.getSource());
    assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
    assertEquals(otherType.getReturnType(), functionType.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals("testTemplate", functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testBuild() {
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testBuildWithNullParametersNode() {
    functionBuilder.withParamsNode(null);
    try {
      functionBuilder.build();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}