package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private ObjectType objectType;

  @Test
  public void testFunctionBuilder() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    assertNull(functionBuilder.getName());
    assertNull(functionBuilder.getSourceNode());
    assertNull(functionBuilder.getParametersNode());
    assertNull(functionBuilder.getReturnType());
    assertNull(functionBuilder.getTypeOfThis());
    assertNull(functionBuilder.getTemplateTypeName());
    assertFalse(functionBuilder.isInferredReturnType());
    assertFalse(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());

    objectType = new ObjectType(registry, "Object");
    functionBuilder = new FunctionBuilder(registry).withTypeOfThis(objectType);
    assertEquals("Object", functionBuilder.getTypeOfThis().getName());
  }

  @Test
  public void testWithName() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withName("testFunction");
    assertEquals("testFunction", functionBuilder.getName());
  }

  @Test
  public void testWithSourceNode() {
    registry = new JSTypeRegistry();
    Node sourceNode = new Object();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withSourceNode(sourceNode);
    assertEquals(sourceNode, functionBuilder.getSourceNode());
  }

  @Test
  public void testWithParams() {
    functionParamBuilder = new FunctionParamBuilder();
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withParams(functionParamBuilder.build());
    assertNotNull(functionBuilder.getParametersNode());
  }

  @Test
  public void testWithParamsNode() {
    registry = new JSTypeRegistry();
    Node parametersNode = new Object();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withParamsNode(parametersNode);
    assertEquals(parametersNode, functionBuilder.getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    registry = new JSTypeRegistry();
    JSType returnType = new IntType(registry);
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withReturnType(returnType);
    assertEquals(returnType, functionBuilder.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    registry = new JSTypeRegistry();
    JSType returnType = new IntType(registry);
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withInferredReturnType(returnType);
    assertEquals(returnType, functionBuilder.getReturnType());
    assertTrue(functionBuilder.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    registry = new JSTypeRegistry();
    ObjectType typeOfThis = new ObjectType(registry, "Object");
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withTemplateName("TestTemplate");
    assertEquals("TestTemplate", functionBuilder.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.forConstructor();
    assertTrue(functionBuilder.isConstructor());
  }

  @Test
  public void testForNativeType() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.forNativeType();
    assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    registry = new JSTypeRegistry();
    String name = "testFunction";
    Node sourceNode = new Object();
    Node parametersNode = new Object();
    JSType returnType = new IntType(registry);
    ObjectType typeOfThis = new ObjectType(registry, "Object");
    String templateTypeName = "TestTemplate";
    boolean isConstructor = true;
    boolean isNativeType = false;
    FunctionType otherType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeType);
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals(name, functionBuilder.getName());
    assertEquals(sourceNode, functionBuilder.getSourceNode());
    assertEquals(parametersNode, functionBuilder.getParametersNode());
    assertEquals(returnType, functionBuilder.getReturnType());
    assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
    assertEquals(templateTypeName, functionBuilder.getTemplateTypeName());
    assertTrue(functionBuilder.isConstructor());
    assertFalse(functionBuilder.isNativeType());
  }

  @Test
  public void testBuild() {
    registry = new JSTypeRegistry();
    String name = "testFunction";
    Node sourceNode = new Object();
    Node parametersNode = new Object();
    JSType returnType = new IntType(registry);
    ObjectType typeOfThis = new ObjectType(registry, "Object");
    String templateTypeName = "TestTemplate";
    boolean isConstructor = true;
    boolean isNativeType = false;
    FunctionType expectedType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeType);
    functionBuilder = new FunctionBuilder(registry);
    functionBuilder = functionBuilder.withName(name)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder.build())
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .forConstructor();
    FunctionType actualType = functionBuilder.build();
    assertEquals(expectedType, actualType);
  }
}