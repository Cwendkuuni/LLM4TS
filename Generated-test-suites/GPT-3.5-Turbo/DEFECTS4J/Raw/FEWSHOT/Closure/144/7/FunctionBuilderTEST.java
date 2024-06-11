package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {
    private JSTypeRegistry registry;
    private FunctionBuilder functionBuilder;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        FunctionBuilder result = functionBuilder.withName("test");
        assertEquals("test", result.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode1 = new Node();
        Node sourceNode2 = new Node();
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode1);
        assertEquals(sourceNode1, result.build().getSource());
        result.withSourceNode(sourceNode2);
        assertEquals(sourceNode2, result.build().getSource());
    }

    @Test
    public void testWithParams() {
        Node parametersNode1 = new Node();
        Node parametersNode2 = new Node();
        FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
        FunctionBuilder result = functionBuilder.withParams(functionParamBuilder);
        assertEquals(functionParamBuilder.build(), result.build().getParametersNode());
        result.withParamsNode(parametersNode1);
        assertEquals(parametersNode1, result.build().getParametersNode());
        result.withParamsNode(parametersNode2);
        assertEquals(parametersNode2, result.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType1 = new JSType();
        JSType returnType2 = new JSType();
        FunctionBuilder result = functionBuilder.withReturnType(returnType1);
        assertEquals(returnType1, result.build().getReturnType());
        result.withReturnType(returnType2);
        assertEquals(returnType2, result.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType1 = new JSType();
        JSType returnType2 = new JSType();
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType1);
        assertEquals(returnType1, result.build().getReturnType());
        assertTrue(result.build().isInferredReturnType());
        result.withInferredReturnType(returnType2);
        assertEquals(returnType2, result.build().getReturnType());
        assertTrue(result.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis1 = new ObjectType();
        ObjectType typeOfThis2 = new ObjectType();
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis1);
        assertEquals(typeOfThis1, result.build().getTypeOfThis());
        result.withTypeOfThis(typeOfThis2);
        assertEquals(typeOfThis2, result.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName1 = "template1";
        String templateTypeName2 = "template2";
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName1);
        assertEquals(templateTypeName1, result.build().getTemplateTypeName());
        result.withTemplateName(templateTypeName2);
        assertEquals(templateTypeName2, result.build().getTemplateTypeName());
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
        FunctionType functionType = new FunctionType(registry);
        functionType.setReferenceName("test");
        functionType.setSource(new Node());
        functionType.setParametersNode(new Node());
        functionType.setReturnType(new JSType());
        functionType.setTypeOfThis(new ObjectType());
        functionType.setTemplateTypeName("template");
        functionType.setIsConstructor(true);
        functionType.setIsNativeObjectType(true);

        FunctionBuilder result = functionBuilder.copyFromOtherFunction(functionType);
        assertEquals(functionType.getReferenceName(), result.build().getReferenceName());
        assertEquals(functionType.getSource(), result.build().getSource());
        assertEquals(functionType.getParametersNode(), result.build().getParametersNode());
        assertEquals(functionType.getReturnType(), result.build().getReturnType());
        assertEquals(functionType.getTypeOfThis(), result.build().getTypeOfThis());
        assertEquals(functionType.getTemplateTypeName(), result.build().getTemplateTypeName());
        assertEquals(functionType.isConstructor(), result.build().isConstructor());
        assertEquals(functionType.isNativeObjectType(), result.build().isNativeObjectType());
    }
}