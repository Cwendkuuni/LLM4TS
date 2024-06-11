package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class FunctionBuilderTest {
    
    private JSTypeRegistry registry;
    private Node sourceNode;
    private Node parametersNode;
    private JSType returnType;
    private ObjectType typeOfThis;
    private String templateTypeName;
    private FunctionBuilder functionBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        sourceNode = new Node();
        parametersNode = new Node();
        returnType = registry.getUnknownType();
        typeOfThis = registry.getUnknownType().toObjectType();
        templateTypeName = "TemplateType";
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        FunctionBuilder result = functionBuilder.withName("FunctionName");
        assertEquals("FunctionName", result.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, result.build().getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
        FunctionBuilder result = functionBuilder.withParams(paramBuilder);
        assertEquals(paramBuilder.build(), result.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, result.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        assertEquals(returnType, result.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        assertEquals(returnType, result.build().getReturnType());
        assertTrue(result.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, result.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, result.build().getTemplateTypeName());
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
        FunctionType otherType = new FunctionType(registry, "other", sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, true, true);
        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        assertEquals("other", result.build().getReferenceName());
        assertEquals(sourceNode, result.build().getSource());
        assertEquals(parametersNode, result.build().getParametersNode());
        assertEquals(returnType, result.build().getReturnType());
        assertEquals(typeOfThis, result.build().getTypeOfThis());
        assertEquals(templateTypeName, result.build().getTemplateTypeName());
        assertTrue(result.build().isConstructor());
        assertTrue(result.build().isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType functionType = functionBuilder.withName("FunctionName")
                .withSourceNode(sourceNode)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .withTypeOfThis(typeOfThis)
                .withTemplateName(templateTypeName)
                .forConstructor()
                .forNativeType()
                .build();
        
        assertEquals("FunctionName", functionType.getReferenceName());
        assertEquals(sourceNode, functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
        assertTrue(functionType.isConstructor());
        assertTrue(functionType.isNativeObjectType());
    }

}