package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FunctionBuilderTest {

 @Mock
 private JSTypeRegistry registry;

 @InjectMocks
 private FunctionBuilder functionBuilder;

 @Test
 public void testConstructor() {
     FunctionBuilder builder = new FunctionBuilder(mock(JSTypeRegistry.class));
     assertNull(builder.getName());
     assertNull(builder.getSourceNode());
     assertNull(builder.getParametersNode());
     assertNull(builder.getReturnType());
     assertNull(builder.getTypeOfThis());
     assertNull(builder.getTemplateTypeName());
     assertFalse(builder.isConstructor());
     assertFalse(builder.isNativeType());
     assertNull(builder.build());
 }

 @Test
 public void testWithName() {
     String name = "testFunction";
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withName(name);
     FunctionType functionType = builder.build();
     assertEquals(name, functionType.getReferenceName());
 }

 @Test
 public void testWithSourceNode() {
     Node sourceNode = mock(Node.class);
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withSourceNode(sourceNode);
     FunctionType functionType = builder.build();
     assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testWithParams() {
     FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
     Node parametersNode = paramBuilder.build();
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withParams(paramBuilder);
     FunctionType functionType = builder.build();
     assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testWithParamsNode() {
     Node parametersNode = mock(Node.class);
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withParamsNode(parametersNode);
     FunctionType functionType = builder.build();
     assertEquals(parametersNode, functionType.getParametersNode());
 }

 @Test
 public void testWithReturnType() {
     JSType returnType = mock(JSType.class);
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withReturnType(returnType);
     FunctionType functionType = builder.build();
     assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testWithInferredReturnType() {
     JSType returnType = mock(JSType.class);
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withReturnType(returnType);
     builder.withInferredReturnType(returnType);
     FunctionType functionType = builder.build();
     assertTrue(functionType.isInferredReturnType());
     assertEquals(returnType, functionType.getReturnType());
 }

 @Test
 public void testWithTypeOfThis() {
     ObjectType typeOfThis = mock(ObjectType.class);
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withTypeOfThis(typeOfThis);
     FunctionType functionType = builder.build();
     assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testWithTemplateName() {
     String templateTypeName = "testTemplate";
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.withTemplateName(templateTypeName);
     FunctionType functionType = builder.build();
     assertEquals(templateTypeName, functionType.getTemplateTypeName());
 }

 @Test
 public void testForConstructor() {
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.forConstructor();
     FunctionType functionType = builder.build();
     assertTrue(functionType.isConstructor());
 }

 @Test
 public void testForNativeType() {
     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.forNativeType();
     FunctionType functionType = builder.build();
     assertTrue(functionType.isNativeType());
 }

 @Test
 public void testCopyFromOtherFunction() {
     String name = "testFunction";
     Node sourceNode = mock(Node.class);
     Node parametersNode = mock(Node.class);
     JSType returnType = mock(JSType.class);
     ObjectType typeOfThis = mock(ObjectType.class);
     String templateTypeName = "testTemplate";
     boolean isConstructor = true;
     FunctionType otherType = new FunctionType(registry, name, sourceNode,
         new ArrowType(registry, parametersNode, returnType, false),
         typeOfThis, templateTypeName, isConstructor, false);

     FunctionBuilder builder = new FunctionBuilder(registry);
     builder.copyFromOtherFunction(otherType);
     FunctionType functionType = builder.build();

     assertEquals(name, functionType.getReferenceName());
     assertEquals(sourceNode, functionType.getSource());
     assertEquals(parametersNode, functionType.getParametersNode());
     assertEquals(returnType, functionType.getReturnType());
     assertEquals(typeOfThis, functionType.getTypeOfThis());
     assertEquals(templateTypeName, functionType.getTemplateTypeName());
     assertTrue(functionType.isConstructor());
     assertFalse(functionType.isNativeType());
 }
}