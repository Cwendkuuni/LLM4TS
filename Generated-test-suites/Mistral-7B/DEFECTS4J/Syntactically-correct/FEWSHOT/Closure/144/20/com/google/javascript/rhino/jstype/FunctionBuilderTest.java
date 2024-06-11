package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {
    private JSTypeRegistry registry;
    private FunctionBuilder builder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new FunctionBuilder(registry);
    }

    @Test
    public void testFunctionBuilderWithName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName("testFunction");
        FunctionType functionType = functionBuilder.build();
        assertEquals("testFunction", functionType.getReferenceName());
    }

    @Test
    public void testFunctionBuilderWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).withSourceNode(sourceNode);
        FunctionType functionType = functionBuilder.build();
        assertNotNull(functionType.getSource());
        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testFunctionBuilderWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry).withName("param1").withType(registry.getBooleanType());
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withParams(paramBuilder)
                .withReturnType(registry.getBooleanType());
        FunctionType functionType = functionBuilder.build();
        assertNotNull(functionType.getParametersNode());
        assertEquals(1, functionType.getParameterCount());
        assertEquals("param1", functionType.getParameter(0).getName());
        assertEquals(registry.getBooleanType(), functionType.getParameter(0).getType());
    }

    @Test
    public void testFunctionBuilderWithParamsNode() {
        Node parametersNode = new Node();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withParamsNode(parametersNode)
                .withReturnType(registry.getBooleanType());
        FunctionType functionType = functionBuilder.build();
        assertNotNull(functionType.getParametersNode());
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testFunctionBuilderWithReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withReturnType(registry.getBooleanType());
        FunctionType functionType = functionBuilder.build();
        assertEquals(registry.getBooleanType(), functionType.getReturnType());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withReturnType(registry.getAnyType())
                .withInferredReturnType(registry.getBooleanType());
        FunctionType functionType = functionBuilder.build();
        assertEquals(registry.getBooleanType(), functionType.getReturnType());
        assertEquals(true, functionType.isInferredReturnType());
    }

    @Test
    public void testFunctionBuilderWithTypeOfThis() {
        ObjectType typeOfThis = registry.getObjectType("Object");
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withTypeOfThis(typeOfThis);
        FunctionType functionType = functionBuilder.build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testFunctionBuilderWithTemplateName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry)
                .withTemplateName("TemplateFunction");
        FunctionType functionType = functionBuilder.build();
        assertEquals("TemplateFunction", functionType.getTemplateTypeName());
    }

    @Test
    public void testFunctionBuilderForConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).forConstructor();
        FunctionType functionType = functionBuilder.build();
        assertEquals(true, functionType.isConstructor());
    }

    @Test
    public void testFunctionBuilderCopyFromOtherFunction() {
        String name = "testFunction";
        Node sourceNode = new Node();
        Node parametersNode = new Node();
        JSType returnType = registry.getBooleanType();
        ObjectType typeOfThis = registry.getObjectType("Object");
        String templateTypeName = "TemplateFunction";
        boolean isConstructor = true;
        FunctionType otherType = new FunctionType(registry, name, sourceNode,
                new ArrowType(registry, parametersNode, returnType, false), typeOfThis,
                templateTypeName, isConstructor, false);

        FunctionBuilder functionBuilder = new FunctionBuilder(registry).copyFromOtherFunction(otherType);
        FunctionType functionType = functionBuilder.build();

        assertEquals(name, functionType.getReferenceName());
        assertEquals(sourceNode, functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
        assertEquals(isConstructor, functionType.isConstructor());
        assertEquals(false, functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderForNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry).forNativeType();
        FunctionType functionType = functionBuilder.build();
        assertEquals(true, functionType.isNativeObjectType());
        assertNull(functionType.getName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
    }
}