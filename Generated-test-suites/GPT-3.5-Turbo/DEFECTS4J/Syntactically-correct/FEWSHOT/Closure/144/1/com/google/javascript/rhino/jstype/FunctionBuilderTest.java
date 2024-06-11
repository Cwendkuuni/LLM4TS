
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

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        FunctionBuilder newBuilder = builder.withName("test");
        assertEquals("test", newBuilder.build().getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node();
        FunctionBuilder newBuilder = builder.withSourceNode(sourceNode);
        assertEquals(sourceNode, newBuilder.build().getSource());
    }

    @Test
    public void testWithParams() {
        FunctionParamBuilder params = new FunctionParamBuilder(registry);
        Node parametersNode = params.build();
        FunctionBuilder newBuilder = builder.withParams(params);
        assertEquals(parametersNode, newBuilder.build().getParametersNode());
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = new Node();
        FunctionBuilder newBuilder = builder.withParamsNode(parametersNode);
        assertEquals(parametersNode, newBuilder.build().getParametersNode());
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.VOID_TYPE);
        FunctionBuilder newBuilder = builder.withReturnType(returnType);
        assertEquals(returnType, newBuilder.build().getReturnType());
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = registry.getNativeType(JSTypeNative.VOID_TYPE);
        FunctionBuilder newBuilder = builder.withInferredReturnType(returnType);
        assertEquals(returnType, newBuilder.build().getReturnType());
        assertTrue(newBuilder.build().isReturnTypeInferred());
    }

    @Test
    public void testWithTypeOfThis() {
        ObjectType typeOfThis = new ObjectType(registry);
        FunctionBuilder newBuilder = builder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, newBuilder.build().getTypeOfThis());
    }

    @Test
    public void testWithTemplateName() {
        String templateTypeName = "TemplateType";
        FunctionBuilder newBuilder = builder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, newBuilder.build().getTemplateTypeName());
    }

    @Test
    public void testForConstructor() {
        FunctionBuilder newBuilder = builder.forConstructor();
        assertTrue(newBuilder.build().isConstructor());
    }

    @Test
    public void testForNativeType() {
        FunctionBuilder newBuilder = builder.forNativeType();
        assertTrue(newBuilder.build().isNativeObjectType());
    }

    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = new FunctionType(registry, "other", new Node(),
                new ArrowType(registry, new Node(), registry.getNativeType(JSTypeNative.VOID_TYPE), false),
                new ObjectType(registry), "otherTemplate", true, false);

        FunctionBuilder newBuilder = builder.copyFromOtherFunction(otherType);
        FunctionType resultType = newBuilder.build();

        assertEquals("other", resultType.getReferenceName());
        assertEquals(otherType.getSource(), resultType.getSource());
        assertEquals(otherType.getParametersNode(), resultType.getParametersNode());
        assertEquals(otherType.getReturnType(), resultType.getReturnType());
        assertEquals(otherType.getTypeOfThis(), resultType.getTypeOfThis());
        assertEquals(otherType.getTemplateTypeName(), resultType.getTemplateTypeName());
        assertTrue(resultType.isConstructor());
        assertFalse(resultType.isNativeObjectType());
    }

    @Test
    public void testBuild() {
        FunctionType type = builder.build();
        assertNull(type.getReferenceName());
        assertNull(type.getSource());
        assertNull(type.getParametersNode());
        assertNull(type.getReturnType());
        assertNull(type.getTypeOfThis());
        assertNull(type.getTemplateTypeName());
        assertFalse(type.isConstructor());
        assertFalse(type.isNativeObjectType());
    }
}
