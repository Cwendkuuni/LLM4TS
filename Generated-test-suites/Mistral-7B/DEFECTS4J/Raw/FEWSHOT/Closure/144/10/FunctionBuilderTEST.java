package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;

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
        assertFalse(functionType.isNativeType());

        FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
                .withName("testFunction")
                .withSourceNode(new Object())
                .withParams(new FunctionParamBuilder().withName("param1").build())
                .withParams(new FunctionParamBuilder().withName("param2").build())
                .withReturnType(IntegerType.INSTANCE)
                .withTypeOfThis(ObjectType.OBJECT)
                .build();

        FunctionType functionType1 = functionBuilder1.build();
        assertEquals("testFunction", functionType1.getName());
        assertNotNull(functionType1.getSource());
        assertNotNull(functionType1.getParametersNode());
        assertEquals(IntegerType.INSTANCE, functionType1.getReturnType());
        assertEquals(ObjectType.OBJECT, functionType1.getTypeOfThis());
        assertNull(functionType1.getTemplateTypeName());
        assertFalse(functionType1.isConstructor());
        assertFalse(functionType1.isNativeType());
    }

    @Test
    public void testFunctionBuilderWithInferredReturnType() {
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
        assertFalse(functionType.isNativeType());

        FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
                .withName("testFunction")
                .withSourceNode(new Object())
                .withParams(new FunctionParamBuilder().withName("param1").build())
                .withParams(new FunctionParamBuilder().withName("param2").build())
                .withReturnType(FunctionType.VOID)
                .withInferredReturnType(IntegerType.INSTANCE)
                .build();

        FunctionType functionType1 = functionBuilder1.build();
        assertEquals("testFunction", functionType1.getName());
        assertNotNull(functionType1.getSource());
        assertNotNull(functionType1.getParametersNode());
        assertNull(functionType1.getReturnType());
        assertEquals(IntegerType.INSTANCE, functionType1.getInferredReturnType());
        assertNull(functionType1.getTypeOfThis());
        assertNull(functionType1.getTemplateTypeName());
        assertFalse(functionType1.isConstructor());
        assertFalse(functionType1.isNativeType());
    }

    @Test
    public void testFunctionBuilderForConstructor() {
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
        assertFalse(functionType.isNativeType());

        FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
                .withName("testConstructor")
                .forConstructor()
                .build();

        FunctionType functionType1 = functionBuilder1.build();
        assertEquals("testConstructor", functionType1.getName());
        assertNull(functionType1.getSource());
        assertNull(functionType1.getParametersNode());
        assertNull(functionType1.getReturnType());
        assertNull(functionType1.getTypeOfThis());
        assertNull(functionType1.getTemplateTypeName());
        assertTrue(functionType1.isConstructor());
        assertFalse(functionType1.isNativeType());
    }

    @Test
    public void testFunctionBuilderForNativeType() {
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
        assertFalse(functionType.isNativeType());

        FunctionBuilder functionBuilder1 = new FunctionBuilder(registry)
                .withName("testFunction")
                .forNativeType()
                .build();

        FunctionType functionType1 = functionBuilder1.build();
        assertEquals("testFunction", functionType1.getName());
        assertNull(functionType1.getSource());
        assertNull(functionType1.getParametersNode());
        assertNull(functionType1.getReturnType());
        assertNull(functionType1.getTypeOfThis());
        assertNull(functionType1.getTemplateTypeName());
        assertFalse(functionType1.isConstructor());
        assertTrue(functionType1.isNativeType());
    }

    @Test
    public void testFunctionBuilderCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionType functionType = new FunctionType(registry, "testFunction", new Object(),
                new ArrowType(registry, null, IntegerType.INSTANCE, false),
                ObjectType.OBJECT, null, false, false);

        FunctionBuilder functionBuilder = new FunctionBuilder(registry);
        functionBuilder.copyFromOtherFunction(functionType);

        FunctionType functionType1 = functionBuilder.build();
        assertEquals("testFunction", functionType1.getName());
        assertNotNull(functionType1.getSource());
        assertNull(functionType1.getParametersNode());
        assertEquals(IntegerType.INSTANCE, functionType1.getReturnType());
        assertEquals(ObjectType.OBJECT, functionType1.getTypeOfThis());
        assertNull(functionType1.getTemplateTypeName());
        assertFalse(functionType1.isConstructor());
        assertFalse(functionType1.isNativeType());
    }
}