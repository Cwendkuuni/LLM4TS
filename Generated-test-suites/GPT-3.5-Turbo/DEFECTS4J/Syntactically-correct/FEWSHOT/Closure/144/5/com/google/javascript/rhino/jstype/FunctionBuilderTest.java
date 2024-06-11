package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionBuilderTest {
    private JSTypeRegistry registry = new JSTypeRegistry();
    
    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionBuilder result = builder.withName("testName");
        
        assertEquals(result, builder);
        assertEquals("testName", builder.build().getReferenceName());
    }
    
    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node sourceNode = new Node();
        FunctionBuilder result = builder.withSourceNode(sourceNode);
        
        assertEquals(result, builder);
        assertEquals(sourceNode, builder.build().getSource());
    }
    
    @Test
    public void testWithParams() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionParamBuilder params = new FunctionParamBuilder(registry);
        Node parametersNode = params.build();
        FunctionBuilder result = builder.withParams(params);
        
        assertEquals(result, builder);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }
    
    @Test
    public void testWithParamsNode() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        Node parametersNode = new Node();
        FunctionBuilder result = builder.withParamsNode(parametersNode);
        
        assertEquals(result, builder);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }
    
    @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        FunctionBuilder result = builder.withReturnType(returnType);
        
        assertEquals(result, builder);
        assertEquals(returnType, builder.build().getReturnType());
    }
    
    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        JSType returnType = new JSType();
        FunctionBuilder result = builder.withInferredReturnType(returnType);
        
        assertEquals(result, builder);
        assertEquals(returnType, builder.build().getReturnType());
        assertEquals(true, builder.build().isInferredReturnType());
    }
    
    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder result = builder.withTypeOfThis(typeOfThis);
        
        assertEquals(result, builder);
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
    }
    
    @Test
    public void testWithTemplateName() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionBuilder result = builder.withTemplateName("templateName");
        
        assertEquals(result, builder);
        assertEquals("templateName", builder.build().getTemplateTypeName());
    }
    
    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionBuilder result = builder.forConstructor();
        
        assertEquals(result, builder);
        assertEquals(true, builder.build().isConstructor());
    }
    
    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionBuilder result = builder.forNativeType();
        
        assertEquals(result, builder);
        assertEquals(true, builder.build().isNativeObjectType());
    }
    
    @Test
    public void testCopyFromOtherFunction() {
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType otherType = new FunctionType(registry);
        
        FunctionBuilder result = builder.copyFromOtherFunction(otherType);
        
        assertEquals(result, builder);
        assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
        assertEquals(otherType.getSource(), builder.build().getSource());
        assertEquals(otherType.getParametersNode(), builder.build().getParametersNode());
        assertEquals(otherType.getReturnType(), builder.build().getReturnType());
        assertEquals(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builder.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
    }
}