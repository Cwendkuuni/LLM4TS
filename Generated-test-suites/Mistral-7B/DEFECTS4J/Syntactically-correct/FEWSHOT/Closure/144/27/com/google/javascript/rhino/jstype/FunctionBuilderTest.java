package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.FunctionBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FunctionBuilderTest {

    private FunctionBuilder functionBuilder;
    private FunctionParamBuilder functionParamBuilder;
    private ObjectType objectType;
    private JSType registry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registry = mock(JSTypeRegistry.class);
        functionBuilder = new FunctionBuilder(registry);
        objectType = mock(ObjectType.class);
        functionParamBuilder = mock(FunctionParamBuilder.class);
    }

    @Test
    public void testFunctionBuilderWithName() {
        String name = "myFunction";
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name);
        assertNotNull(functionBuilder.getName());
        assertEquals(name, functionBuilder.getName());
    }

    @Test
    public void testFunctionBuilderWithSourceNode() {
        Node sourceNode = mock(Node.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);
        assertNotNull(functionBuilder.getSourceNode());
        assertEquals(sourceNode, functionBuilder.getSourceNode());
    }

    @Test
    public void testFunctionBuilderWithParams() {
        FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
        Node parametersNode = mock(Node.class);
        when(functionParamBuilder.build()).thenReturn(parametersNode);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withParams(functionParamBuilder);
        assertNotNull(functionBuilder.getParametersNode());
        assertEquals(parametersNode, functionBuilder.getParametersNode());
    }

    @Test
    public void testFunctionBuilderWithParamsNode() {
        Node parametersNode = mock(Node.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withParamsNode(parametersNode);
        assertNotNull(functionBuilder.getParametersNode());
        assertEquals(parametersNode, functionBuilder.getParametersNode());
    }

    @Test
    public void testFunctionBuilderWithReturnType() {
        JSType returnType = mock(JSType.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withReturnType(returnType);
        assertNotNull(functionBuilder.getReturnType());
        assertEquals(returnType, functionBuilder.getReturnType());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
        JSType returnType = mock(JSType.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withInferredReturnType(returnType);
        assertNotNull(functionBuilder.getReturnType());
        assertEquals(returnType, functionBuilder.getReturnType());
        assertTrue(functionBuilder.isInferredReturnType());
    }

    @Test
    public void testFunctionBuilderWithTypeOfThis() {
        ObjectType typeOfThis = mock(ObjectType.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withTypeOfThis(typeOfThis);
        assertNotNull(functionBuilder.getTypeOfThis());
        assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
    }

    @Test
    public void testFunctionBuilderWithTemplateName() {
        String templateTypeName = "templateName";
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withTemplateName(templateTypeName);
        assertNotNull(functionBuilder.getTemplateTypeName());
        assertEquals(templateTypeName, functionBuilder.getTemplateTypeName());
    }

    @Test
    public void testFunctionBuilderForConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .forConstructor();
        assertTrue(functionBuilder.isConstructor());
    }

    @Test
    public void testFunctionBuilderForNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .forNativeType();
        assertTrue(functionBuilder.isNativeType());
    }

    @Test
    public void testFunctionBuilderCopyFromOtherFunction() {
        String name = "myFunction";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        JSType returnType = mock(JSType.class);
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "templateName";
        boolean isConstructor = true;
        FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode,
            new ArrowType(registry, parametersNode, returnType, false), typeOfThis,
            templateTypeName, isConstructor, false);

        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .copyFromOtherFunction(otherFunctionType);

        assertNotNull(functionBuilder.getName());
        assertEquals(name, functionBuilder.getName());
        assertNotNull(functionBuilder.getSourceNode());
        assertEquals(sourceNode, functionBuilder.getSourceNode());
        assertNotNull(functionBuilder.getParametersNode());
        assertEquals(parametersNode, functionBuilder.getParametersNode());
        assertNotNull(functionBuilder.getReturnType());
        assertEquals(returnType, functionBuilder.getReturnType());
        assertNotNull(functionBuilder.getTypeOfThis());
        assertEquals(typeOfThis, functionBuilder.getTypeOfThis());
        assertNotNull(functionBuilder.getTemplateTypeName());
        assertEquals(templateTypeName, functionBuilder.getTemplateTypeName());
        assertTrue(functionBuilder.isConstructor());
        assertEquals(isConstructor, functionBuilder.isConstructor());
        assertTrue(functionBuilder.isNativeType());
        assertEquals(otherFunctionType.isNativeObjectType(), functionBuilder.isNativeType());
    }

    @Test
    public void testFunctionBuilderBuild() {
        String name = "myFunction";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        JSType returnType = mock(JSType.class);
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "templateName";
        boolean isConstructor = true;

        FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode,
            new ArrowType(registry, parametersNode, returnType, false), typeOfThis,
            templateTypeName, isConstructor, false);

        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
            .withName(name)
            .withSourceNode(sourceNode)
            .withParams(functionParamBuilder)
            .withReturnType(returnType)
            .withTypeOfThis(typeOfThis)
            .withTemplateName(templateTypeName)
            .forConstructor();

        FunctionType actualFunctionType = functionBuilder.build();
        assertNotNull(actualFunctionType);
        assertEquals(expectedFunctionType, actualFunctionType);
    }
}