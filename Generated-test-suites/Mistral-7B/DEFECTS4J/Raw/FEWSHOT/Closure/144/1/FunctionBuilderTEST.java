package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

/**
 * Test cases for FunctionBuilder class
 */
public class FunctionBuilderTest {

    @Test
    public void testFunctionBuilderWithBasicParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("param1", registry);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("param2", registry);
        Node parametersNode = new Node_Sequence();
        parametersNode.addChild(paramBuilder1.build());
        parametersNode.addChild(paramBuilder2.build());

        functionBuilder.withName("testFunction")
                .withSourceNode(new Node_Identifier("testFunction"))
                .withParams(paramBuilder1, paramBuilder2)
                .withParamsNode(parametersNode)
                .withReturnType(registry.getStringType())
                .build();

        FunctionType functionType = functionBuilder.build();
        assertEquals("testFunction", functionType.getReferenceName());
        assertEquals(new Node_Identifier("testFunction"), functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(registry.getStringType(), functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("param1", registry);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("param2", registry);
        Node parametersNode = new Node_Sequence();
        parametersNode.addChild(paramBuilder1.build());
        parametersNode.addChild(paramBuilder2.build());

        functionBuilder.withName("testFunction")
                .withSourceNode(new Node_Identifier("testFunction"))
                .withParams(paramBuilder1, paramBuilder2)
                .withParamsNode(parametersNode)
                .withReturnType(null)
                .withInferredReturnType(registry.getNumberType())
                .build();

        FunctionType functionType = functionBuilder.build();
        assertEquals("testFunction", functionType.getReferenceName());
        assertEquals(new Node_Identifier("testFunction"), functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(registry.getNumberType(), functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithObjectTypeReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("param1", registry);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("param2", registry);
        Node parametersNode = new Node_Sequence();
        parametersNode.addChild(paramBuilder1.build());
        parametersNode.addChild(paramBuilder2.build());

        functionBuilder.withName("testFunction")
                .withSourceNode(new Node_Identifier("testFunction"))
                .withParams(paramBuilder1, paramBuilder2)
                .withParamsNode(parametersNode)
                .withReturnType(new ObjectType("TestObject"))
                .build();

        FunctionType functionType = functionBuilder.build();
        assertEquals("testFunction", functionType.getReferenceName());
        assertEquals(new Node_Identifier("testFunction"), functionType.getSource());
        assertEquals(parametersNode, functionType.getParametersNode());
        assertEquals(new ObjectType("TestObject"), functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        functionBuilder.forConstructor();

        FunctionType functionType = functionBuilder.build();
        assertEquals(true, functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }

    @Test
    public void testFunctionBuilderWithNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        functionBuilder.forNativeType();

        FunctionType functionType = functionBuilder.build();
        assertEquals(true, functionType.isNativeObjectType());
        assertFalse(functionType.isConstructor());
    }

    @Test
    public void testFunctionBuilderCopy() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("param1", registry);
        FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("param2", registry);
        Node parametersNode = new Node_Sequence();
        parametersNode.addChild(paramBuilder1.build());
        parametersNode.addChild(paramBuilder2.build());

        functionBuilder.withName("testFunction")
                .withSourceNode(new Node_Identifier("testFunction"))
                .withParams(paramBuilder1, paramBuilder2)
                .withParamsNode(parametersNode)
                .withReturnType(registry.getStringType())
                .build();

        FunctionType functionType = functionBuilder.build();
        FunctionBuilder functionBuilderCopy = new FunctionBuilder(registry);
        FunctionBuilder copiedFunctionBuilder = functionBuilderCopy.copyFromOtherFunction(functionType);

        FunctionType copiedFunctionType = copiedFunctionBuilder.build();
        assertEquals(functionType.getReferenceName(), copiedFunctionType.getReferenceName());
        assertEquals(functionType.getSource(), copiedFunctionType.getSource());
        assertEquals(functionType.getParametersNode(), copiedFunctionType.getParametersNode());
        assertEquals(functionType.getReturnType(), copiedFunctionType.getReturnType());
        assertEquals(functionType.getTypeOfThis(), copiedFunctionType.getTypeOfThis());
        assertEquals(functionType.getTemplateTypeName(), copiedFunctionType.getTemplateTypeName());
        assertEquals(functionType.isConstructor(), copiedFunctionType.isConstructor());
        assertEquals(functionType.isNativeObjectType(), copiedFunctionType.isNativeObjectType());
    }
}