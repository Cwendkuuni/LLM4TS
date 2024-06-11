package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class FunctionBuilderTest {
    private FunctionBuilder functionBuilder;
    private JSTypeRegistry registry;
    private FunctionParamBuilder functionParamBuilder;
    private ObjectType objectType;

    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        functionBuilder = new FunctionBuilder(registry);
        functionParamBuilder = mock(FunctionParamBuilder.class);
        objectType = mock(ObjectType.class);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithName() {
        String name = "myFunction";
        functionBuilder.withName(name);
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getReferenceName()).isEqualTo(name);
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = mock(Node.class);
        functionBuilder.withSourceNode(sourceNode);
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getSource()).isEqualTo(sourceNode);
    }

    @Test
    public void testWithParams() {
        List<FunctionParamBuilder.FunctionParamType> functionParams = Lists.newArrayList(
                FunctionParamBuilder.FunctionParamType.create("param1", JSType.INT),
                FunctionParamBuilder.FunctionParamType.create("param2", JSType.STRING));
        functionParamBuilder.withNames(functionParams.toArray(new FunctionParamBuilder.FunctionParamType[0]));
        functionBuilder.withParams(functionParamBuilder);

        FunctionType functionType = functionBuilder.build();
        FunctionType expectedFunctionType = new FunctionType(registry, null, null,
                new ArrowType(registry, null, JSType.VOID, false),
                objectType, null, false, false);
        expectedFunctionType.setParameters(functionParams.toArray(new FunctionParamBuilder.FunctionParamType[0]));

        assertThat(functionType).isEqualTo(expectedFunctionType);
    }

    @Test
    public void testWithParamsNode() {
        Node parametersNode = mock(Node.class);
        functionBuilder.withParamsNode(parametersNode);
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getParametersNode()).isEqualTo(parametersNode);
    }

    @Test
    public void testWithReturnType() {
        JSType returnType = JSType.INT;
        functionBuilder.withReturnType(returnType);
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getReturnType()).isEqualTo(returnType);
    }

    @Test
    public void testWithInferredReturnType() {
        JSType returnType = JSType.INT;
        functionBuilder.withInferredReturnType(returnType);
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getReturnType()).isEqualTo(returnType);
        assertThat(functionType.isInferredReturnType()).isTrue();
    }

    @Test
    public void testWithTypeOfThis() {
        functionBuilder.withTypeOfThis(objectType);
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getTypeOfThis()).isEqualTo(objectType);
    }

    @Test
    public void testForConstructor() {
        functionBuilder.forConstructor();
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.isConstructor()).isTrue();
    }

    @Test
    public void testForNativeType() {
        functionBuilder.forNativeType();
        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.isNativeType()).isTrue();
    }

    @Test
    public void testCopyFromOtherFunction() {
        String name = "myFunction";
        Node sourceNode = mock(Node.class);
        FunctionParamBuilder.FunctionParamType[] functionParams = new FunctionParamBuilder.FunctionParamType[] {
                FunctionParamBuilder.FunctionParamType.create("param1", JSType.INT),
                FunctionParamBuilder.FunctionParamType.create("param2", JSType.STRING)
        };
        FunctionType otherFunctionType = new FunctionType(registry, name, sourceNode,
                new ArrowType(registry, null, JSType.VOID, false),
                objectType, null, true, false);
        otherFunctionType.setParameters(functionParams);

        functionBuilder.copyFromOtherFunction(otherFunctionType);

        FunctionType functionType = functionBuilder.build();
        assertThat(functionType.getReferenceName()).isEqualTo(name);
        assertThat(functionType.getSource()).isEqualTo(sourceNode);
        assertThat(functionType.getParametersNode()).isNull();
        assertThat(functionType.getReturnType()).isNull();
        assertThat(functionType.getTypeOfThis()).isEqualTo(objectType);
        assertThat(functionType.getTemplateTypeName()).isNull();
        assertThat(functionType.isConstructor()).isTrue();
        assertThat(functionType.isNativeType()).isTrue();
        assertThat(functionType.getParameters()).isEqualTo(functionParams);
    }
}