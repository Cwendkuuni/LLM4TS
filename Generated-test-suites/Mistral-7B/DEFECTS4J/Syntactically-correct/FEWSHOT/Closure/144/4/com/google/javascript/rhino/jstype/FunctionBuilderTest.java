package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.ArrowType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getName());
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
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName("testFunction");
    FunctionType functionType = functionBuilder.build();
    assertEquals("testFunction", functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node sourceNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
    assertNull(functionType.getName());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithParametersNode() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    Node parametersNode = mock(Node.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParamsNode(parametersNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    String templateTypeName = "TemplateTypeName";
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTemplateName(templateTypeName);
    FunctionType functionType = functionBuilder.build();
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertFalse(functionType.isConstructor());
    assertTrue(functionType.isNativeType());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType returnType = mock(JSType.class);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(returnType);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    String name = "testFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "TemplateTypeName";
    boolean isConstructor = true;
    boolean isNativeType = false;

    FunctionType otherType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeType);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.copyFromOtherFunction(otherType);

    FunctionType functionType = functionBuilder.build();
    assertEquals(name, functionType.getName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertEquals(isConstructor, functionType.isConstructor());
    assertEquals(isNativeType, functionType.isNativeType());
  }
}