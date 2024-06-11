package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
    private JSTypeRegistry registry;
    private FunctionBuilder functionBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        FunctionBuilder result = functionBuilder.withName("testName");
        assertEquals("testName", result.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, result.build().getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
        paramBuilder.addOptionalParam("param1", registry.getNativeType(JSTypeNative.STRING_TYPE));
        paramBuilder.addRequiredParam("param2", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        FunctionBuilder result = functionBuilder.withParams(paramBuilder);
        assertEquals(paramBuilder.build(), result.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node(Token.NAME);
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, result.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        assertEquals(returnType, result.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.NULL_TYPE);
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        assertEquals(returnType, result.build().getReturnType());
        assertTrue(result.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = registry.getObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, result.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder result = functionBuilder.withTemplateName("templateName");
        assertEquals("templateName", result.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder result = functionBuilder.forConstructor();
        assertTrue(result.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder result = functionBuilder.forNativeType();
        assertTrue(result.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry);
        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        FunctionType builtFunction = result.build();
        assertEquals(otherType.getReferenceName(), builtFunction.getReferenceName());
        assertEquals(otherType.getSource(), builtFunction.getSource());
        assertEquals(otherType.getParametersNode(), builtFunction.getParametersNode());
        assertEquals(otherType.getReturnType(), builtFunction.getReturnType());
        assertEquals(otherType.getTypeOfThis(), builtFunction.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builtFunction.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builtFunction.isConstructor());
        assertEquals(otherType.isNativeObjectType(), builtFunction.isNativeObjectType());
    }
}