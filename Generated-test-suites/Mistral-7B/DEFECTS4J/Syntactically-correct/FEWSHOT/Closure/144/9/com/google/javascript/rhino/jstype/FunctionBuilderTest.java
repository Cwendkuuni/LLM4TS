package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.types.ArrowType;
import com.google.javascript.rhino.types.FunctionParamBuilder;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
    private FunctionBuilder functionBuilder;
    private JSTypeRegistry registry;
    private NodeFactory nodeFactory;
    private FunctionParamBuilder functionParamBuilder;

    @Before
    public void setup() {
        registry = mock(JSTypeRegistry.class);
        nodeFactory = mock(NodeFactory.class);
        functionParamBuilder = mock(FunctionParamBuilder.class);
        when(registry.createFunctionType(anyString(), any(Node.class), any(ArrowType.class), any(ObjectType.class), anyString(), anyBoolean(), anyBoolean())).thenReturn(mock(FunctionType.class));
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        String name = "testFunction";
        FunctionBuilder builder = functionBuilder.withName(name);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getName());
        assertEquals(name, functionType.getName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = mock(Node.class);
        FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getSource());
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
        Node parametersNode = mock(Node.class);
        when(paramBuilder.build()).thenReturn(parametersNode);
        FunctionBuilder builder = functionBuilder.withParams(paramBuilder);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getParametersNode());
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = mock(Node.class);
        FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getParametersNode());
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = mock(JSType.class);
        FunctionBuilder builder = functionBuilder.withReturnType(returnType);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getReturnType());
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = mock(JSType.class);
        FunctionBuilder builder = functionBuilder.withReturnType(returnType).withInferredReturnType(returnType);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getReturnType());
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = mock(ObjectType.class);
        FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getTypeOfThis());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "templateName";
        FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
        FunctionType functionType = builder.build();
        assertNotNull(functionType.getTemplateTypeName());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = functionBuilder.forConstructor();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = functionBuilder.forNativeType();
        FunctionType functionType = builder.build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = mock(FunctionType.class);
        String name = "testFunction";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        ArrowType arrowType = mock(ArrowType.class);
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "templateName";
        boolean isConstructor = true;
        boolean isNativeObjectType = true;

        when(otherType.getReferenceName()).thenReturn(name);
        when(otherType.getSource()).thenReturn(sourceNode);
        when(otherType.getParametersNode()).thenReturn(parametersNode);
        when(otherType.getReturnType()).thenReturn(arrowType);
        when(otherType.getTypeOfThis()).thenReturn(typeOfThis);
        when(otherType.getTemplateTypeName()).thenReturn(templateTypeName);
        when(otherType.isConstructor()).thenReturn(isConstructor);
        when(otherType.isNativeObjectType()).thenReturn(isNativeObjectType);

        FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);

        FunctionType functionType = builder.build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getName());
        assertEquals(sourceNode, functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(arrowType, functionType.getReturnType());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
        assertTrue(functionType.isConstructor());
        assertTrue(functionType.isNativeObjectType());
    }
}