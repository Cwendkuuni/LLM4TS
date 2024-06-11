package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

    @Test
    public void testFunctionBuilder() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.build();
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName("testFunction");

        FunctionType functionType = functionBuilder.build();
        assertEquals("testFunction", functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node sourceNode = new Node();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);

        FunctionType functionType = functionBuilder.build();
        assertEquals(sourceNode, functionType.getSource());
        assertNull(functionType.getName());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithParams() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry).withName("param1").withType(Integer.TYPE);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry).withName("param2").withType(String.TYPE);

        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withParams(new FunctionParamBuilder(registry).withParameters(paramBuilder1, paramBuilder2));

        Node parametersNode = functionBuilder.build().getParametersNode();
        Node param1 = parametersNode.getFirstChild();
        Node param2 = parametersNode.getChildAt(1);

        assertEquals(paramBuilder1.getName(), param1.getFirstChild().getFirstChild().getString());
        assertEquals(Integer.TYPE, param1.getChildAt(1).getFirstChild().getJSType());

        assertEquals(paramBuilder2.getName(), param2.getFirstChild().getFirstChild().getString());
        assertEquals(String.TYPE, param2.getChildAt(1).getFirstChild().getJSType());
    }

    @Test
    public void testFunctionBuilderWithParamsNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parametersNode = new Node();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withParamsNode(parametersNode);

        FunctionType functionType = functionBuilder.build();
        assertEquals(parametersNode, functionType.getParametersNode());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(Integer.TYPE);

        FunctionType functionType = functionBuilder.build();
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertEquals(Integer.TYPE, functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withReturnType(null).withInferredReturnType(Boolean.TYPE);

        FunctionType functionType = functionBuilder.build();
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertEquals(Boolean.TYPE, functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testFunctionBuilderWithTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ObjectType objectType = new ObjectType(registry);
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTypeOfThis(objectType);

        FunctionType functionType = functionBuilder.build();
        assertEquals(objectType, functionType.getTypeOfThis());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithTemplateName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withTemplateName("TemplateFunction");

        FunctionType functionType = functionBuilder.build();
        assertEquals("TemplateFunction", functionType.getTemplateTypeName());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderForConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).forConstructor();

        FunctionType functionType = functionBuilder.build();
        assertTrue(functionType.isConstructor());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderForNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).forNativeType();

        FunctionType functionType = functionBuilder.build();
        assertFalse(functionType.isConstructor());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String name = "testFunction";
        Node sourceNode = new Node();
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry).withName("param1").withType(Integer.TYPE);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry).withName("param2").withType(String.TYPE);
        Node parametersNode = new Node();
        parametersNode.setFirstChild(paramBuilder1.build().build());
        parametersNode.setChildAt(1, paramBuilder2.build().build());
        JSType returnType = Integer.TYPE;
        ObjectType typeOfThis = new ObjectType(registry);
        String templateTypeName = "TemplateFunction";
        boolean isConstructor = true;
        boolean isNativeObjectType = false;

        FunctionType otherType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeObjectType);

        FunctionBuilder functionBuilder = new FunctionBuilder(registry).copyFromOtherFunction(otherType);

        FunctionType functionType = functionBuilder.build();
        assertEquals(name, functionType.getName());
        assertEquals(sourceNode, functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
        assertEquals(isConstructor, functionType.isConstructor());
        assertEquals(isNativeObjectType, functionType.isNativeObjectType());
    }
}