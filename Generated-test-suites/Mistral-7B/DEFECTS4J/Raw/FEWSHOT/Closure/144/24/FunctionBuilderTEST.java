package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit. Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.FunctionParamBuilder;

public class FunctionBuilderTest {

 JSTypeRegistry registry = new JSTypeRegistry();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);

 @Test
 public void testFunctionBuilder() {
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 }

 @Test
 public void testFunctionBuilderWithName() {
 FunctionType functionType = functionBuilder.withName("testFunction")
 .build();
 assertEquals("testFunction", functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 FunctionType functionType = functionBuilder.withSourceNode(sourceNode)
 .build();
 assertNotNull(functionType);
 assertNotNull(functionType.getSource());
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 FunctionType functionType = functionBuilder.withParams(paramBuilder)
 .build();
 assertNotNull(functionType);
 assertNotNull(functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node parametersNode = new Node();
 FunctionType functionType = functionBuilder.withParamsNode(parametersNode)
 .build();
 assertNotNull(functionType);
 assertNotNull(functionType.getParametersNode());
 assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = registry.createStringType();
 FunctionType functionType = functionBuilder.withReturnType(returnType)
 .build();
 assertNotNull(functionType);
 assertNotNull(functionType.getReturnType());
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = registry.createStringType();
 FunctionType functionType = functionBuilder.withReturnType(returnType)
 .withInferredReturnType(returnType)
 .build();
 assertNotNull(functionType);
 assertNotNull(functionType.getReturnType());
 assertEquals(returnType, functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = registry.createObjectType("Object");
 FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis)
 .build();
 assertNotNull(functionType);
 assertNotNull(functionType.getTypeOfThis());
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 String templateTypeName = "TemplateType";
 FunctionType functionType = functionBuilder.withTemplateName(templateTypeName)
 .build();
 assertNotNull(functionType);
 assertEquals(templateTypeName, functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 FunctionType functionType = functionBuilder.forConstructor().build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 FunctionType functionType = functionBuilder.forNativeType().build();
 assertTrue(functionType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "otherType", null,
 new ArrowType(registry, null, registry.createStringType(), false), null, null,
 false, false);
 FunctionType functionType = functionBuilder.copyFromOtherFunction(otherType)
 .build();
 assertEquals("otherType", functionType.getReferenceName());
 assertNotNull(functionType.getSource());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 }
}