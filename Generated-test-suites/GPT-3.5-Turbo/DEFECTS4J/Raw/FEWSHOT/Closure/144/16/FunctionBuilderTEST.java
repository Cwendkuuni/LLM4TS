package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ArrowType;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
    private FunctionBuilder functionBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        String name = "testName";
        FunctionBuilder result = functionBuilder.withName(name);
        assertEquals(name, result.getName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = mock(Node.class);
        FunctionBuilder result = functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, result.getSourceNode());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = mock(FunctionParamBuilder.class);
        Node parametersNode = mock(Node.class);
        when(params.build()).thenReturn(parametersNode);

        FunctionBuilder result = functionBuilder.withParams(params);
        assertEquals(parametersNode, result.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = mock(Node.class);
        FunctionBuilder result = functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, result.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = mock(JSType.class);
        FunctionBuilder result = functionBuilder.withReturnType(returnType);
        assertEquals(returnType, result.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = mock(JSType.class);
        FunctionBuilder result = functionBuilder.withInferredReturnType(returnType);
        assertEquals(returnType, result.getReturnType());
        assertEquals(true, result.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = mock(ObjectType.class);
        FunctionBuilder result = functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, result.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "testTemplateName";
        FunctionBuilder result = functionBuilder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, result.getTemplateName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder result = functionBuilder.forConstructor();
        assertEquals(true, result.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder result = functionBuilder.forNativeType();
        assertEquals(true, result.isNativeType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = mock(FunctionType.class);
        String name = "testName";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        JSType returnType = mock(JSType.class);
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "testTemplateName";
        boolean isConstructor = true;
        boolean isNativeType = true;

        when(otherType.getReferenceName()).thenReturn(name);
        when(otherType.getSource()).thenReturn(sourceNode);
        when(otherType.getParametersNode()).thenReturn(parametersNode);
        when(otherType.getReturnType()).thenReturn(returnType);
        when(otherType.getTypeOfThis()).thenReturn(typeOfThis);
        when(otherType.getTemplateTypeName()).thenReturn(templateTypeName);
        when(otherType.isConstructor()).thenReturn(isConstructor);
        when(otherType.isNativeObjectType()).thenReturn(isNativeType);

        FunctionBuilder result = functionBuilder.copyFromOtherFunction(otherType);
        assertEquals(name, result.getName());
        assertEquals(sourceNode, result.getSourceNode());
        assertEquals(parametersNode, result.getParametersNode());
        assertEquals(returnType, result.getReturnType());
        assertEquals(typeOfThis, result.getTypeOfThis());
        assertEquals(templateTypeName, result.getTemplateName());
        assertEquals(isConstructor, result.isConstructor());
        assertEquals(isNativeType, result.isNativeType());
    }

    @Test
    public void testBuild() {
        String name = "testName";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        JSType returnType = mock(JSType.class);
        boolean inferredReturnType = true;
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "testTemplateName";
        boolean isConstructor = true;
        boolean isNativeType = true;

        functionBuilder.withName(name);
        functionBuilder.withSourceNode(sourceNode);
        functionBuilder.withParamsNode(parametersNode);
        functionBuilder.withReturnType(returnType);
        functionBuilder.withTypeOfThis(typeOfThis);
        functionBuilder.withTemplateName(templateTypeName);
        functionBuilder.forConstructor();
        functionBuilder.forNativeType();

        ArrowType expectedArrowType = new ArrowType(registry, parametersNode, returnType, inferredReturnType);

        FunctionType result = functionBuilder.build();
        assertEquals(name, result.getName());
        assertEquals(sourceNode, result.getSourceNode());
        assertEquals(expectedArrowType, result.getArrowType());
        assertEquals(typeOfThis, result.getTypeOfThis());
        assertEquals(templateTypeName, result.getTemplateTypeName());
        assertEquals(isConstructor, result.isConstructor());
        assertEquals(isNativeType, result.isNativeObjectType());
    }
}