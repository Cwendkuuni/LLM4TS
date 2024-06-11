package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilder() {
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType);
 }

 @Test
 public void testFunctionBuilderWithName() {
 functionBuilder.withName("myFunction");
 FunctionType functionType = functionBuilder.build();
 assertEquals("myFunction", functionType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Object();
 functionBuilder.withSourceNode(sourceNode);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
 paramBuilder.withName("param1").withType(Integer.TYPE);
 Node paramsNode = paramBuilder.build();
 functionBuilder.withParams(paramBuilder);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithParamsNode() {
 Node paramsNode = new Object();
 functionBuilder.withParamsNode(paramsNode);
 FunctionType functionType = functionBuilder.build();
 assertNotNull(functionType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = Integer.TYPE;
 functionBuilder.withReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = Integer.TYPE;
 functionBuilder.withInferredReturnType(returnType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(returnType, functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "MyType");
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType functionType = functionBuilder.build();
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 String templateTypeName = "MyTemplate";
 functionBuilder.withTemplateName(templateTypeName);
 FunctionType functionType = functionBuilder.build();
 assertEquals(templateTypeName, functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 functionBuilder.forConstructor();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 functionBuilder.forNativeType();
 FunctionType functionType = functionBuilder.build();
 assertTrue(functionType.isNativeType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = registry.createFunctionType("otherName", null, null, null, null, null, false, false);
 functionBuilder.copyFromOtherFunction(otherType);
 FunctionType functionType = functionBuilder.build();
 assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
 assertEquals(otherType.getSource(), functionType.getSource());
 assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
 assertEquals(otherType.getReturnType(), functionType.getReturnType());
 assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
 assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
 assertTrue(otherType.isConstructor(), functionType.isConstructor());
 assertTrue(otherType.isNativeType(), functionType.isNativeType());
 }
}