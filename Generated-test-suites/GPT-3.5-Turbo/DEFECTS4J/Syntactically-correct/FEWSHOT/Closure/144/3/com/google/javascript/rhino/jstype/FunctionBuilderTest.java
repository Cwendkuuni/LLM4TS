package com.google.javascript.rhino.jstype;

import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithParams() {
        // Create a sample FunctionParamBuilder
        FunctionParamBuilder params = new FunctionParamBuilder()
                .withParam("param1", new JSType())
                .withParam("param2", new JSType())
                .withParam("param3", new JSType());

        // Create a FunctionBuilder and set params using the FunctionParamBuilder
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.withParams(params);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the parametersNode in the builder is set correctly
        assertEquals(params.build(), builder.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        // Create a sample parameters node
        Node parametersNode = new Node();

        // Create a FunctionBuilder and set the parametersNode
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.withParamsNode(parametersNode);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the parametersNode in the builder is set correctly
        assertSame(parametersNode, builder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        // Create a sample return type
        JSType returnType = new JSType();

        // Create a FunctionBuilder and set the return type
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.withReturnType(returnType);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the return type in the builder is set correctly
        assertSame(returnType, builder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        // Create a sample inferred return type
        JSType returnType = new JSType();

        // Create a FunctionBuilder and set the inferred return type
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.withInferredReturnType(returnType);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the inferred return type in the builder is set correctly
        assertSame(returnType, builder.build().getReturnType());
        assertTrue(builder.build().isInferredReturnType());
    }

    @Test
    public void testWithTypeOfThis() {
        // Create a sample type of this
        ObjectType typeOfThis = new ObjectType();

        // Create a FunctionBuilder and set the type of this
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.withTypeOfThis(typeOfThis);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the type of this in the builder is set correctly
        assertSame(typeOfThis, builder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        // Create a sample template name
        String templateName = "Template";

        // Create a FunctionBuilder and set the template name
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.withTemplateName(templateName);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the template name in the builder is set correctly
        assertEquals(templateName, builder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        // Create a FunctionBuilder and set it as a constructor
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.forConstructor();

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the isConstructor flag in the builder is set correctly
        assertTrue(builder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        // Create a FunctionBuilder and set it as a native type
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.forNativeType();

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that the isNativeType flag in the builder is set correctly
        assertTrue(builder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        // Create a sample function type
        FunctionType otherType = new FunctionType(new JSTypeRegistry());

        // Create a FunctionBuilder and copy from the sample function type
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionBuilder result = builder.copyFromOtherFunction(otherType);

        // Assert that the returned builder is the same as the original builder
        assertSame(builder, result);

        // Assert that all properties in the builder are set correctly
        assertSame(otherType.getReferenceName(), builder.build().getReferenceName());
        assertSame(otherType.getSource(), builder.build().getSource());
        assertSame(otherType.getParametersNode(), builder.build().getParametersNode());
        assertSame(otherType.getReturnType(), builder.build().getReturnType());
        assertSame(otherType.getTypeOfThis(), builder.build().getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), builder.build().getTemplateTypeName());
        assertEquals(otherType.isConstructor(), builder.build().isConstructor());
        assertEquals(otherType.isNativeObjectType(), builder.build().isNativeObjectType());
    }

    @Test
    public void testBuild() {
        // Create a sample JSTypeRegistry
        JSTypeRegistry registry = new JSTypeRegistry();

        // Create a FunctionBuilder and build the function type
        FunctionBuilder builder = new FunctionBuilder(registry);
        FunctionType functionType = builder.build();

        // Assert that the built function type is not null
        assertNotNull(functionType);

        // Assert that the JSTypeRegistry in the built function type is the same as the original registry
        assertSame(registry, functionType.getRegistry());
    }
}