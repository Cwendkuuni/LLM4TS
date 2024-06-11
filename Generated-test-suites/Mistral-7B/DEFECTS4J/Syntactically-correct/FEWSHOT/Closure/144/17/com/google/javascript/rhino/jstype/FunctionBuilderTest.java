package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

    @Test
    public void testFunctionBuilder() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.build();
        assertNull("Name should be null by default", functionType.getName());
        assertNull("Source node should be null by default", functionType.getSource());
        assertNull("Parameters node should be null by default", functionType.getParametersNode());
        assertNull("Return type should be null by default", functionType.getReturnType());
        assertNull("Type of this should be null by default", functionType.getTypeOfThis());
        assertNull("Template type name should be null by default", functionType.getTemplateTypeName());
        assertFalse("Inferred return type should be false by default", functionType.isInferredReturnType());
        assertFalse("Is constructor should be false by default", functionType.isConstructor());
        assertFalse("Is native type should be false by default", functionType.isNativeType());
    }

    @Test
    public void testSetName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        String name = "testFunction";
        FunctionType functionType = functionBuilder.withName(name).build();

        assertEquals(name, functionType.getName());
    }

    @Test
    public void testSetSourceNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        Node sourceNode = new Node();
        FunctionType functionType = functionBuilder.withSourceNode(sourceNode).build();

        assertEquals(sourceNode, functionType.getSource());
    }

    @Test
    public void testSetParametersNode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        Node parametersNode = new Node();
        FunctionType functionType = functionBuilder.withParamsNode(parametersNode).build();

        assertEquals(parametersNode, functionType.getParametersNode());
    }

    @Test
    public void testSetReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        JSType returnType = new IntType();
        FunctionType functionType = functionBuilder.withReturnType(returnType).build();

        assertEquals(returnType, functionType.getReturnType());
    }

    @Test
    public void testSetInferredReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        JSType returnType = new IntType();
        FunctionType functionType = functionBuilder.withReturnType(returnType)
                                                     .withInferredReturnType(returnType)
                                                     .build();

        assertTrue(functionType.isInferredReturnType());
    }

    @Test
    public void testSetTypeOfThis() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        ObjectType typeOfThis = new ObjectType("Object");
        FunctionType functionType = functionBuilder.withTypeOfThis(typeOfThis).build();

        assertEquals(typeOfThis, functionType.getTypeOfThis());
    }

    @Test
    public void testSetTemplateTypeName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        String templateTypeName = "TemplateType";
        FunctionType functionType = functionBuilder.withTemplateName(templateTypeName).build();

        assertEquals(templateTypeName, functionType.getTemplateTypeName());
    }

    @Test
    public void testSetConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.forConstructor().build();

        assertTrue(functionType.isConstructor());
    }

    @Test
    public void testSetNativeType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.forNativeType().build();

        assertTrue(functionType.isNativeType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetNameAfterReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.withReturnType(new IntType())
                                                     .withName("testFunction")
                                                     .build();

        // Should throw an exception as name should be set before return type
        fail("Should throw an exception");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetTypeOfThisAfterReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.withReturnType(new IntType())
                                                     .withTypeOfThis(new ObjectType("Object"))
                                                     .build();

        // Should throw an exception as typeOfThis should be set before return type
        fail("Should throw an exception");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetTemplateTypeNameAfterReturnType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        FunctionBuilder functionBuilder = new FunctionBuilder(registry);

        FunctionType functionType = functionBuilder.withReturnType(new IntType())
                                                     .withTemplateName("TemplateType")
                                                     .build();

        // Should throw an exception as templateTypeName should be set before return type
        fail("Should throw an exception");
    }

    @Test
    public void testCopyFromOtherFunction() {
        JSTypeRegistry registry = new JSTypeRegistry();

        String name1 = "testFunction1";
        String name2 = "testFunction2";

        FunctionBuilder functionBuilder1 = new FunctionBuilder(registry);
        FunctionType functionType1 = functionBuilder1.withName(name1).build();

        FunctionBuilder functionBuilder2 = new FunctionBuilder(registry);
        FunctionType functionType2 = functionBuilder2.copyFromOtherFunction(functionType1).build();

        assertEquals(name1, functionType2.getName());
        assertEquals(name1, functionType1.getName());
        assertEquals(functionType1.getSource(), functionType2.getSource());
        assertEquals(functionType1.getParametersNode(), functionType2.getParametersNode());
        assertEquals(functionType1.getReturnType(), functionType2.getReturnType());
        assertEquals(functionType1.getTypeOfThis(), functionType2.getTypeOfThis());
        assertEquals(functionType1.getTemplateTypeName(), functionType2.getTemplateTypeName());
        assertTrue(functionType1.isConstructor() == functionType2.isConstructor());
        assertTrue(functionType1.isNativeType() == functionType2.isNativeType());
    }
}