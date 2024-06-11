package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
    private JSTypeRegistry registry;
    private FunctionBuilder builder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        String name = "functionName";
        builder.withName(name);
        assertEquals(name, builder.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node(Token.NEW);
        builder.withSourceNode(sourceNode);
        assertEquals(sourceNode, builder.build().getSource());
    }

    @Test
    public void testWithParams() {
        Node parametersNode = new Node(Token.PARAM_LIST);
        builder.withParamsNode(parametersNode);
        assertEquals(parametersNode, builder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = new JSType();
        builder.withReturnType(returnType);
        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = new JSType();
        builder.withInferredReturnType(returnType);
        assertTrue(builder.build().isInferredReturnType());
        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType();
        builder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, builder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateName = "template";
        builder.withTemplateName(templateName);
        assertEquals(templateName, builder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        builder.forConstructor();
        assertTrue(builder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        builder.forNativeType();
        assertTrue(builder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry);
        otherType.setReferenceName("otherName");
        otherType.setSource(new Node(Token.VAR, new Node(Token.NAME, new Node(Token.STRING))));
        otherType.setParametersNode(new Node(Token.ASSIGN, new Node(Token.NAME, new Node(Token.THIS))));
        otherType.setReturnType(new JSType());
        otherType.setTypeOfThis(new ObjectType());
        otherType.setTemplateTypeName("otherTemplate");
        otherType.setIsConstructor(true);
        otherType.setIsNativeObjectType(true);

        builder.copyFromOtherFunction(otherType);
        FunctionType builtType = builder.build();

        assertEquals(otherType.getReferenceName(), builtType.getReferenceName());
        assertEquals(otherType.getSource(), builtType.getSource());
        assertEquals(otherType.getParametersNode(), builtType.getParametersNode());
        assertEquals(otherType.getReturnType(), builtType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), builtType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builtType.getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builtType.isConstructor());
        assertEquals(otherType.isNativeObjectType(), builtType.isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType builtType = builder.build();
        assertNull(builtType.getReferenceName());
        assertNull(builtType.getSource());
        assertNull(builtType.getParametersNode());
        assertNull(builtType.getReturnType());
        assertNull(builtType.getTypeOfThis());
        assertNull(builtType.getTemplateTypeName());
        assertFalse(builtType.isConstructor());
        assertFalse(builtType.isNativeObjectType());
    }
}