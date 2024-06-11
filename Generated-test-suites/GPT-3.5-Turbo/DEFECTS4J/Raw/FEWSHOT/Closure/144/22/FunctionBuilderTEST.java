package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Property;
import com.google.javascript.rhino.jstype.TypeI;

public class FunctionBuilderTest {
    private FunctionBuilder functionBuilder;
    private JSTypeRegistry registry;
    private Node sourceNode;
    private Node parametersNode;
    private JSType returnType;
    private ObjectType typeOfThis;
    private String templateTypeName;
    private boolean inferredReturnType;
    private boolean isConstructor;
    private boolean isNativeType;

    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        sourceNode = mock(Node.class);
        parametersNode = mock(Node.class);
        returnType = mock(JSType.class);
        typeOfThis = mock(ObjectType.class);
        templateTypeName = "templateTypeName";
        functionBuilder = new FunctionBuilder(registry);
    }

    @Test
    public void testWithName() {
        functionBuilder.withName("name");
        assertEquals("name", functionBuilder.name);
    }

    @Test
    public void testWithSourceNode() {
        functionBuilder.withSourceNode(sourceNode);
        assertEquals(sourceNode, functionBuilder.sourceNode);
    }
    
    @Test
    public void testWithParams() {
        FunctionParamBuilder params = mock(FunctionParamBuilder.class);
        when(params.build()).thenReturn(parametersNode);
        
        functionBuilder.withParams(params);
        assertEquals(parametersNode, functionBuilder.parametersNode);
    }
    
    @Test
    public void testWithParamsNode() {
        functionBuilder.withParamsNode(parametersNode);
        assertEquals(parametersNode, functionBuilder.parametersNode);
    }
    
    @Test
    public void testWithReturnType() {
        functionBuilder.withReturnType(returnType);
        assertEquals(returnType, functionBuilder.returnType);
    }
    
    @Test
    public void testWithInferredReturnType() {
        functionBuilder.withInferredReturnType(returnType);
        assertEquals(returnType, functionBuilder.returnType);
        assertEquals(true, functionBuilder.inferredReturnType);
    }
    
    @Test
    public void testWithTypeOfThis() {
        functionBuilder.withTypeOfThis(typeOfThis);
        assertEquals(typeOfThis, functionBuilder.typeOfThis);
    }
    
    @Test
    public void testWithTemplateName() {
        functionBuilder.withTemplateName(templateTypeName);
        assertEquals(templateTypeName, functionBuilder.templateTypeName);
    }
    
    @Test
    public void testForConstructor() {
        functionBuilder.forConstructor();
        assertEquals(true, functionBuilder.isConstructor);
    }
    
    @Test
    public void testForNativeType() {
        functionBuilder.forNativeType();
        assertEquals(true, functionBuilder.isNativeType);
    }
    
    @Test
    public void testCopyFromOtherFunction() {
        FunctionType otherType = mock(FunctionType.class);
        when(otherType.getReferenceName()).thenReturn("name");
        when(otherType.getSource()).thenReturn(sourceNode);
        when(otherType.getParametersNode()).thenReturn(parametersNode);
        when(otherType.getReturnType()).thenReturn(returnType);
        when(otherType.getTypeOfThis()).thenReturn(typeOfThis);
        when(otherType.getTemplateTypeName()).thenReturn(templateTypeName);
        when(otherType.isConstructor()).thenReturn(isConstructor);
        when(otherType.isNativeObjectType()).thenReturn(isNativeType);
        
        functionBuilder.copyFromOtherFunction(otherType);
        assertEquals("name", functionBuilder.name);
        assertEquals(sourceNode, functionBuilder.sourceNode);
        assertEquals(parametersNode, functionBuilder.parametersNode);
        assertEquals(returnType, functionBuilder.returnType);
        assertEquals(typeOfThis, functionBuilder.typeOfThis);
        assertEquals(templateTypeName, functionBuilder.templateTypeName);
        assertEquals(isConstructor, functionBuilder.isConstructor);
        assertEquals(isNativeType, functionBuilder.isNativeType);
    }
    
    @Test
    public void testBuild() {
        functionBuilder.name = "name";
        functionBuilder.sourceNode = sourceNode;
        functionBuilder.parametersNode = parametersNode;
        functionBuilder.returnType = returnType;
        functionBuilder.typeOfThis = typeOfThis;
        functionBuilder.templateTypeName = templateTypeName;
        functionBuilder.isConstructor = isConstructor;
        functionBuilder.isNativeType = isNativeType;
        
        FunctionType result = functionBuilder.build();
        assertEquals(registry, result.getRegistry());
        assertEquals("name", result.getReferenceName());
        assertEquals(sourceNode, result.getSource());
        assertEquals(parametersNode, result.getParametersNode());
        assertEquals(returnType, result.getReturnType());
        assertEquals(typeOfThis, result.getTypeOfThis());
        assertEquals(templateTypeName, result.getTemplateTypeName());
        assertEquals(isConstructor, result.isConstructor());
        assertEquals(isNativeType, result.isNativeObjectType());
    }
}