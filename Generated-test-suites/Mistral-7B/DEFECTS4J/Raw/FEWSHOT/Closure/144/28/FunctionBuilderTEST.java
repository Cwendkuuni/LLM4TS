package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

    private JSTypeRegistry registry;
    private NodeFactory nodeFactory;
    private FunctionParamBuilder paramBuilder;

    @Before
    public void setup() {
        registry = mock(JSTypeRegistry.class);
        nodeFactory = mock(NodeFactory.class);
        paramBuilder = new FunctionParamBuilder(registry, nodeFactory);
    }

    @Test
    public void testFunctionBuilderWithNameAndReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        JSType returnType = mock(JSType.class);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withReturnType(returnType)
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testFunctionBuilderWithParamsAndReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);
        JSType returnType = mock(JSType.class);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);
        JSType returnType = mock(JSType.class);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParamsNode(parametersNode)
                .withInferredReturnType(returnType)
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testFunctionBuilderWithTypeOfThis() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);
        JSType returnType = mock(JSType.class);
        ObjectType typeOfThis = mock(ObjectType.class);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .withTypeOfThis(typeOfThis)
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testFunctionBuilderWithTemplateName() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);
        JSType returnType = mock(JSType.class);
        String templateTypeName = "MyTemplate";

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .withTemplateName(templateTypeName)
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testFunctionBuilderWithParams() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry, nodeFactory);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry, nodeFactory);
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);

        when(paramBuilder1.build()).thenReturn(nodeFactory.createObjectLiteral());
        when(paramBuilder2.build()).thenReturn(nodeFactory.createObjectLiteral());

        when(nodeFactory.createCallExpression(paramBuilder1, paramBuilder2, null)).thenReturn(parametersNode);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParams(paramBuilder1, paramBuilder2)
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testFunctionBuilderWithNulls() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.build();
        assertNull(functionType);

        functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withName(null);
        functionType = functionBuilder.build();
        assertNull(functionType);

        functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withReturnType(null);
        functionType = functionBuilder.build();
        assertNull(functionType);

        functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withTypeOfThis(null);
        functionType = functionBuilder.build();
        assertNull(functionType);

        functionBuilder = new FunctionBuilder(registry);
        functionBuilder.withTemplateName(null);
        functionType = functionBuilder.build();
        assertNull(functionType);
    }

    @Test
    public void testFunctionBuilderWithIsConstructor() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);
        JSType returnType = mock(JSType.class);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .forConstructor()
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testFunctionBuilderWithIsNativeType() {
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        String name = "myFunction";
        Node parametersNode = nodeFactory.createCallExpression(null, null, null);
        JSType returnType = mock(JSType.class);

        FunctionType functionType = functionBuilder
                .withName(name)
                .withParamsNode(parametersNode)
                .withReturnType(returnType)
                .forNativeType()
                .build();

        assertNotNull(functionType);
        assertEquals(name, functionType.getReferenceName());
        assertEquals(returnType, functionType.getReturnType());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertTrue(functionType.isNativeType());
    }
}