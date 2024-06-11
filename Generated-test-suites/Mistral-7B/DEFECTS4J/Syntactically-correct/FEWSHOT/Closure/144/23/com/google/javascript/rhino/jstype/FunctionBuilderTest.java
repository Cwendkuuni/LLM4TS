package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Test withName
    FunctionBuilder nameFunctionBuilder = functionBuilder.withName("myFunction");
    assertNotNull(nameFunctionBuilder.getName());
    assertEquals("myFunction", nameFunctionBuilder.getName());

    // Test withSourceNode
    Node sourceNode = new Object();
    FunctionBuilder sourceFunctionBuilder = nameFunctionBuilder.withSourceNode(sourceNode);
    assertNotNull(sourceFunctionBuilder.getSourceNode());
    assertEquals(sourceNode, sourceFunctionBuilder.getSourceNode());

    // Test withParams
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(new StringType(registry));
    Node paramsNode = paramBuilder.build();
    FunctionBuilder paramsFunctionBuilder = sourceFunctionBuilder.withParams(paramBuilder);
    assertNotNull(paramsFunctionBuilder.getParametersNode());
    assertEquals(paramsNode, paramsFunctionBuilder.getParametersNode());

    // Test withReturnType
    JSType returnType = new IntType(registry);
    FunctionBuilder returnTypeFunctionBuilder = paramsFunctionBuilder.withReturnType(returnType);
    assertNotNull(returnTypeFunctionBuilder.getReturnType());
    assertEquals(returnType, returnTypeFunctionBuilder.getReturnType());

    // Test withInferredReturnType
    FunctionBuilder inferredReturnTypeFunctionBuilder = returnTypeFunctionBuilder.withInferredReturnType(new VoidType(registry));
    assertTrue(inferredReturnTypeFunctionBuilder.isInferredReturnType());

    // Test withTypeOfThis
    ObjectType typeOfThis = new ObjectType(registry, "MyObject");
    FunctionBuilder typeOfThisFunctionBuilder = inferredReturnTypeFunctionBuilder.withTypeOfThis(typeOfThis);
    assertNotNull(typeOfThisFunctionBuilder.getTypeOfThis());
    assertEquals(typeOfThis, typeOfThisFunctionBuilder.getTypeOfThis());

    // Test build
    FunctionType functionType = typeOfThisFunctionBuilder.build();
    assertNotNull(functionType);
  }

  @Test
  public void testFunctionBuilderCopy() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "myFunction";
    Node sourceNode = new Object();
    Node parametersNode = new Object();
    JSType returnType = new IntType(registry);
    ObjectType typeOfThis = new ObjectType(registry, "MyObject");
    String templateTypeName = "MyTemplateType";
    boolean isConstructor = true;
    boolean isNativeType = false;

    FunctionBuilder functionBuilderToCopy = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("param1").withType(new StringType(registry)).build())
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .forConstructor();

    FunctionBuilder copiedFunctionBuilder = functionBuilderToCopy.copyFromOtherFunction(functionBuilderToCopy.build());

    assertEquals(name, copiedFunctionBuilder.getName());
    assertEquals(sourceNode, copiedFunctionBuilder.getSourceNode());
    assertEquals(parametersNode, copiedFunctionBuilder.getParametersNode());
    assertEquals(returnType, copiedFunctionBuilder.getReturnType());
    assertEquals(typeOfThis, copiedFunctionBuilder.getTypeOfThis());
    assertEquals(templateTypeName, copiedFunctionBuilder.getTemplateTypeName());
    assertTrue(copiedFunctionBuilder.isConstructor());
    assertFalse(copiedFunctionBuilder.isNativeType());
  }
}