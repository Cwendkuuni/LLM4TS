
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
    private FunctionBuilder builder;
    private FunctionParamBuilder paramBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new FunctionBuilder(registry);
        paramBuilder = new FunctionParamBuilder(registry);
    }

    @Test
    public void testWithName() {
        FunctionType functionType = builder.withName("foo").build();
        assertEquals("foo", functionType.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node node = new Node();
        FunctionType functionType = builder.withSourceNode(node).build();
        assertEquals(node, functionType.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionType functionType = builder.withParams(paramBuilder.build()).build();
        assertEquals(paramBuilder.build(), functionType.getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node node = new Node();
        FunctionType functionType = builder.withParamsNode(node).build();
        assertEquals(node, functionType.getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        FunctionType functionType = builder.withReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        FunctionType functionType = builder.withInferredReturnType(returnType).build();
        assertEquals(returnType, functionType.getReturnType());
        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        FunctionType functionType = builder.withTypeOfThis(typeOfThis).build();
        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        FunctionType functionType = builder.withTemplateName("T").build();
        assertEquals("T", functionType.getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionType functionType = builder.forConstructor().build();
        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionType functionType = builder.forNativeType().build();
        assertTrue(functionType.isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry);
        otherType.setReferenceName("bar");
        otherType.setSource(new Node());
        otherType.setReturnType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        
        FunctionType functionType = builder.copyFromOtherFunction(otherType).build();
        assertEquals(otherType.getReferenceName(), functionType.getReferenceName());
        assertEquals(otherType.getSource(), functionType.getSource());
        assertEquals(otherType.getReturnType(), functionType.getReturnType());
    }
}
