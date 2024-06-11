package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionBuilderTest {
    @Test
    public void testWithName() {
        String name = "function";
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withName(name);
        FunctionType functionType = builder.build();
        assertEquals(name, functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withSourceNode(sourceNode);
        FunctionType functionType = builder.build();
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.withName("param1").withType(new JSType());
        Node parametersNode = paramBuilder.build();
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withParamsNode(parametersNode);
        FunctionType functionType = builder.build();
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType();
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateName = "Template";
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.withTemplateName(templateName);
        FunctionType functionType = builder.build();
        assertEquals(templateName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forConstructor();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.forNativeType();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(new JSTypeRegistry());
        otherType.setReferenceName("otherFunction");
        otherType.setSource(new Node());
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
        paramBuilder.withName("param1").withType(new JSType());
        otherType.setParametersNode(paramBuilder.build());
        otherType.setReturnType(new JSType());
        otherType.setTypeOfThis(new ObjectType());
        otherType.setTemplateTypeName("OtherTemplate");
        otherType.setConstructor(true);
        otherType.setNativeObjectType(true);
        
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        builder.copyFromOtherFunction(otherType);
        FunctionType functionType = builder.build();
        assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherType.getSource(), functionType.getSource());
        assertEquals(otherType.getParametersNode(), functionType.getParametersNode());
        assertEquals(otherType.getReturnType(), functionType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), functionType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), functionType.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), functionType.isConstructor());
        assertEquals(otherType.isNativeObjectType(), functionType.isNativeObjectType());
    }
}