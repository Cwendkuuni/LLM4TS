package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Test
 public void testFunctionBuilder() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testWithName() {
 functionBuilder.withName("testFunction");
 assertEquals("testFunction", functionBuilder.build().getReferenceName());
 }

 @Test
 public void testWithSourceNode() {
 Node sourceNode = new Node_(); // assuming Node_ is a valid Node class constructor
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getSource());
 }

 @Test
 public void testWithParamsNode() {
 Node parametersNode = new Node_();
 functionBuilder.withParamsNode(parametersNode);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getParametersNode());
 }

 @Test
 public void testWithParamsBuilder() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("paramName").withType(new IntType(registry));
 Node parametersNode = paramBuilder.build();
 functionBuilder.withParams(paramBuilder);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getParametersNode());
 }

 @Test
 public void testWithReturnType() {
 JSType returnType = new IntType(registry);
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getReturnType());
 }

 @Test
 public void testWithInferredReturnType() {
 JSType returnType = new IntType(registry);
 functionBuilder.withInferredReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "TestClass");
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals("TestClass", functionType.getTypeOfThis().getName());
 }

 @Test
 public void testWithTemplateName() {
 functionBuilder.withTemplateName("TemplateName");
 FunctionType functionType = functionBuilder.build();
 assertEquals("TemplateName", functionType.getTemplateTypeName());
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
 FunctionType otherType = new FunctionType(registry, "otherFunction", null, null, null, null, true, false);
 functionBuilder.copyFromOtherFunction(otherType);
 FunctionType functionType = functionBuilder.build();
 assertEquals("otherFunction", functionType.getReferenceName());
 }
}