package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        // Initialize the registry
        registry = new JSTypeRegistry();
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
        Node parametersNode = paramBuilder.withParam("param1")
                                          .withOptionalParam("param2")
                                          .withVariableParam("param3")
                                          .build();

        FunctionBuilder builder = new FunctionBuilder(registry)
                                        .withParams(paramBuilder)
                                        .withParamsNode(parametersNode);

        assertEquals(parametersNode, builder.build().getParametersNode());
        assertTrue(builder.build().getSignature().contains("param1"));
        assertTrue(builder.build().getSignature().contains("param2"));
        assertTrue(builder.build().getSignature().contains("param3"));
    }

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .withName("testFunction");

        assertEquals("testFunction", builder.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node(Token.SCRIPT);
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .withSourceNode(sourceNode);
        
        assertEquals(sourceNode, builder.build().getSource());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .withReturnType(returnType);

        assertEquals(returnType, builder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .withReturnType(returnType)
                                    .withInferredReturnType(returnType);

        assertEquals(returnType, builder.build().getReturnType());
        assertTrue(builder.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .withTypeOfThis(objectType);

        assertEquals(objectType, builder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateName = "T";
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .withTemplateName(templateName);

        assertEquals(templateName, builder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .forConstructor();

        assertTrue(builder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .forNativeType();

        assertTrue(builder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionBuilder(registry)
                                    .withName("otherFunction")
                                    .withReturnType(registry.getNativeType(JSTypeNative.VOID_TYPE))
                                    .build();

        FunctionBuilder builder = new FunctionBuilder(registry)
                                    .copyFromOtherFunction(otherType);

        assertEquals(otherType.getReferenceName(), builder.build().getReferenceName());
        assertEquals(otherType.getReturnType(), builder.build().getReturnType());
    }

    @Test
    public void testBuild() {
        FunctionBuilder builder = new FunctionBuilder(registry);

        FunctionType functionType = builder.build();

        assertNull(functionType.getReferenceName());
        assertNull(functionType.getSource());
        assertNull(functionType.getParametersNode());
        assertNull(functionType.getReturnType());
        assertNull(functionType.getTypeOfThis());
        assertNull(functionType.getTemplateTypeName());
        assertFalse(functionType.isConstructor());
        assertFalse(functionType.isNativeObjectType());
    }
}