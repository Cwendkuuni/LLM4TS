package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    private boolean inferredReturnType;
    private boolean isConstructor;
    private boolean isNativeType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        sourceNode = new Node(Node.TOKEN_PLACEHOLDER);
        parametersNode = new Node(Node.TOKEN_PLACEHOLDER);
        returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        typeOfThis = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        templateTypeName = "TemplateType";
        inferredReturnType = true;
        isConstructor = true;
        isNativeType = true;
    }

    @Test
    public void testWithAllParameters() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withName("testFunction")
                .withSourceNode(sourceNode)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .withInferredReturnType(inferredReturnType)
                .withTypeOfThis(typeOfThis)
                .withTemplateName(templateTypeName)
                .forConstructor()
                .forNativeType();

        FunctionType functionType = functionBuilder.build();

        assertNotNull(functionType);
        assertEquals("testFunction", functionType.getReferenceName());
        assertEquals(sourceNode, functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
        assertTrue(functionType.isConstructor());
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testWithRequiredParameters() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withName("testFunction")
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .withTypeOfThis(typeOfThis);

        FunctionType functionType = functionBuilder.build();

        assertNotNull(functionType);
        assertEquals("testFunction", functionType.getReferenceName());
        assertNull(functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testWithDefaultParameters() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withParamsNode(parametersNode)
                .withReturnType(returnType);

        FunctionType functionType = functionBuilder.build();

        assertNotNull(functionType);
        assertNull(functionType.getReferenceName());
        assertNull(functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(returnType, functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }
}