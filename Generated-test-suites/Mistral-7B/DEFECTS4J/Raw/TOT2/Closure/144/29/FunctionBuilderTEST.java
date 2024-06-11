package com.google.javascript.rhino.jstype.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderNullRegistry() {
    try {
      new FunctionBuilder(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // Test for other edge cases and exceptions can be added here

  @Test
  public void testFunctionBuilderSetName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.withName("");
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.getName().isEmpty());
  }

  @Test
  public void testFunctionBuilderSetSourceNodeNull() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.withSourceNode(null);
    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getSource());
  }

  @Test
  public void testFunctionBuilderSetParamsEmptyFunctionParamBuilder() {
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.withParams(functionParamBuilder);
    FunctionType functionType = functionBuilder.build();
    assertEquals(0, functionType.getParameterCount());
  }

  @Test
  public void testFunctionBuilderSetParamsNodeNull() {
    Node node = null;
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.withParamsNode(node);
    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderSetReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType invalidType = new JSType() {
      // Add invalid implementation here
    };
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withReturnType(invalidType);
    try {
      FunctionType functionType = functionBuilder.build();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFunctionBuilderSetInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType invalidType = new JSType() {
      // Add invalid implementation here
    };
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.withInferredReturnType(invalidType);
    try {
      FunctionType functionType = functionBuilder.build();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFunctionBuilderSetTypeOfThisNull() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    functionBuilder.withTypeOfThis(null);
    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getTypeOfThis());
  }

  // Test for other edge cases and exceptions can be added here

  @Test
  public void testFunctionBuilderBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String name = "myFunction";
    Node sourceNode = new JSFunctionDeclaration(); // Add valid implementation here
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parametersNode = functionParamBuilder.build();
    JSType returnType = new ObjectType("String");
    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder)
        .withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(name, functionType.getName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(null, functionType.getTypeOfThis());
    assertEquals("", functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  // Test for other methods and edge cases can be added here

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String name1 = "myFunction1";
    String name2 = "myFunction2";
    Node sourceNode = new JSFunctionDeclaration(); // Add valid implementation here
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parametersNode1 = functionParamBuilder.build();
    JSType returnType1 = new ObjectType("String");
    FunctionType functionType1 = new FunctionType(registry, name1, sourceNode, new ArrowType(registry, parametersNode1, returnType1, false), null, "", false, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry)
        .copyFromOtherFunction(functionType1);
    FunctionType functionType2 = functionBuilder.build();

    assertEquals(name1, functionType1.getName());
    assertEquals(name1, functionType2.getName());
    assertEquals(sourceNode, functionType1.getSource());
    assertEquals(sourceNode, functionType2.getSource());
    assertEquals(parametersNode1, functionType1.getParametersNode());
    assertEquals(parametersNode1, functionType2.getParametersNode());
    assertEquals(returnType1, functionType1.getReturnType());
    assertEquals(returnType1, functionType2.getReturnType());
    assertEquals(null, functionType1.getTypeOfThis());
    assertEquals(null, functionType2.getTypeOfThis());
    assertEquals("", functionType1.getTemplateTypeName());
    assertEquals("", functionType2.getTemplateTypeName());
    assertTrue(functionType1.isConstructor());
    assertTrue(functionType2.isConstructor());
    assertFalse(functionType1.isNativeType());
    assertFalse(functionType2.isNativeType());
  }
}