package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

    @Test
    public void testFunctionBuilder() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.build();
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithName() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        String name = "myFunction";
        FunctionBuilder functionBuilderWithName = functionBuilder.withName(name);
        FunctionType functionType = functionBuilderWithName.build();

        assertEquals(name, functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithSourceNode() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        Node sourceNode = mock(Node.class);
        FunctionBuilder functionBuilderWithSourceNode = functionBuilder.withSourceNode(sourceNode);
        FunctionType functionType = functionBuilderWithSourceNode.build();

        assertEquals(sourceNode, functionType.getSource());
        assertNull(functionType.getName());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithParams() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
        Node parametersNode = mock(Node.class);
        when(paramBuilder.build()).thenReturn(parametersNode);

        FunctionBuilder functionBuilderWithParams = functionBuilder.withParams(paramBuilder);
        FunctionType functionType = functionBuilderWithParams.build();

        assertEquals(parametersNode, functionType.getParametersNode());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithParamsNode() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        Node parametersNode = mock(Node.class);
        FunctionType functionType = functionBuilder.build();

        FunctionBuilder functionBuilderWithParamsNode = functionBuilder.withParamsNode(parametersNode);
        FunctionType functionTypeWithParamsNode = functionBuilderWithParamsNode.build();

        assertEquals(parametersNode, functionTypeWithParamsNode.getParametersNode());
        assertEquals(functionType, functionTypeWithParamsNode);
    }

    @Test
    public void testFunctionBuilderWithReturnType() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        JSType returnType = mock(JSType.class);
        FunctionBuilder functionBuilderWithReturnType = functionBuilder.withReturnType(returnType);
        FunctionType functionType = functionBuilderWithReturnType.build();

        assertEquals(returnType, functionType.getReturnType());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        JSType returnType = mock(JSType.class);
        FunctionBuilder functionBuilderWithInferredReturnType = functionBuilder.withReturnType(returnType).withInferredReturnType(returnType);
        FunctionType functionType = functionBuilderWithInferredReturnType.build();

        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithTypeOfThis() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        ObjectType typeOfThis = mock(ObjectType.class);
        FunctionBuilder functionBuilderWithTypeOfThis = functionBuilder.withTypeOfThis(typeOfThis);
        FunctionType functionType = functionBuilderWithTypeOfThis.build();

        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithTemplateName() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        String templateTypeName = "TemplateTypeName";
        FunctionBuilder functionBuilderWithTemplateName = functionBuilder.withTemplateName(templateTypeName);
        FunctionType functionType = functionBuilderWithTemplateName.build();

        assertEquals(templateTypeName, functionType.getTemplateTypeName());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
    }

    @Test
    public void testFunctionBuilderForConstructor() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionBuilder functionBuilderForConstructor = functionBuilder.forConstructor();
        FunctionType functionType = functionBuilderForConstructor.build();

        assertTrue(functionType.isConstructor());
        assertFalse(functionType.isNativeType());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
    }

    @Test
    public void testFunctionBuilderForNativeType() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionBuilder functionBuilderForNativeType = functionBuilder.forNativeType();
        FunctionType functionType = functionBuilderForNativeType.build();

        assertFalse(functionType.isConstructor());
        assertTrue(functionType.isNativeType());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
    }

    @Test
    public void testFunctionBuilderCopyFromOtherFunction() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        FunctionType otherType = mock(FunctionType.class);

        String name = "otherName";
        Node sourceNode = mock(Node.class);
        Node parametersNode = mock(Node.class);
        JSType returnType = mock(JSType.class);
        ObjectType typeOfThis = mock(ObjectType.class);
        String templateTypeName = "templateTypeName";
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

        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionBuilder functionBuilderCopy = functionBuilder.copyFromOtherFunction(otherType);

        assertEquals(name, functionBuilderCopy.getName());
        assertEquals(sourceNode, functionBuilderCopy.getSourceNode());
        assertEquals(parametersNode, functionBuilderCopy.getParametersNode());
        assertEquals(returnType, functionBuilderCopy.getReturnType());
        assertEquals(typeOfThis, functionBuilderCopy.getTypeOfThis());
        assertEquals(templateTypeName, functionBuilderCopy.getTemplateTypeName());
        assertEquals(isConstructor, functionBuilderCopy.isConstructor());
        assertEquals(isNativeType, functionBuilderCopy.isNativeType());
    }
}