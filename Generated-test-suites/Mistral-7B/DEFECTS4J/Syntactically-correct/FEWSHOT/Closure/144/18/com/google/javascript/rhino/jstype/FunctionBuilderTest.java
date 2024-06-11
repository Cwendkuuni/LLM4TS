package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FunctionBuilderTest {

  @Mock
  private JSTypeRegistry registry;

  @InjectMocks
  private FunctionBuilder functionBuilder;

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, null, null, null, false, false);
    when(registry.createFunctionType(name, null, null, null, null, null, false, false)).thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder.withName(name).withReturnType(returnType).build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(null), eq(null), eq(null), eq(null), eq(false), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndSourceNode() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    Node sourceNode = mock(Node.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, null, null, null, false, false);
    when(registry.createFunctionType(name, sourceNode, null, null, null, null, false, false)).thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder.withName(name).withReturnType(returnType).withSourceNode(sourceNode).build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(sourceNode), eq(null), eq(null), eq(null), eq(null), eq(false), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndParams() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);

    Node parametersNode = paramBuilder.build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), null, null, false, false);
    when(registry.createFunctionType(name, null, parametersNode, returnType, null, null, false, false)).thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder)
        .build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(parametersNode), eq(returnType), eq(null), eq(null), eq(false), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndParamsAndTypeOfThis() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);

    Node parametersNode = paramBuilder.build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, null, false, false);
    when(registry.createFunctionType(name, null, parametersNode, returnType, null, null, false, false))
        .thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder)
        .withTypeOfThis(typeOfThis)
        .build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(parametersNode), eq(returnType), eq(typeOfThis), eq(null), eq(false), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndParamsAndTypeOfThisAndTemplateTypeName() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "TemplateTypeName";

    Node parametersNode = paramBuilder.build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, false, false);
    when(registry.createFunctionType(name, null, parametersNode, returnType, null, templateTypeName, false, false))
        .thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(parametersNode), eq(returnType), eq(typeOfThis), eq(templateTypeName), eq(false), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndParamsAndInferredReturnType() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);

    Node parametersNode = paramBuilder.build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, true), null, null, false, false);
    when(registry.createFunctionType(name, null, parametersNode, null, null, null, true, false))
        .thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder)
        .withInferredReturnType()
        .build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(parametersNode), eq(null), eq(null), eq(null), eq(true), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndParamsAndInferredReturnTypeAndTypeOfThis() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);

    Node parametersNode = paramBuilder.build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, true), typeOfThis, null, false, false);
    when(registry.createFunctionType(name, null, parametersNode, null, null, null, true, false))
        .thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder)
        .withTypeOfThis(typeOfThis)
        .withInferredReturnType()
        .build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(parametersNode), eq(null), eq(typeOfThis), eq(null), eq(true), eq(false));
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndParamsAndInferredReturnTypeAndTypeOfThisAndTemplateTypeName() {
    String name = "myFunction";
    JSType returnType = mock(JSType.class);
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "TemplateTypeName";

    Node parametersNode = paramBuilder.build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, new ArrowType(registry, parametersNode, returnType, true), typeOfThis, templateTypeName, false, false);
    when(registry.createFunctionType(name, null, parametersNode, null, null, templateTypeName, true, false))
        .thenReturn(expectedFunctionType);

    FunctionType actualFunctionType = functionBuilder
        .withName(name)
        .withReturnType(returnType)
        .withParams(paramBuilder)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .withInferredReturnType()
        .build();

    assertEquals(expectedFunctionType, actualFunctionType);
    verify(registry, times(1)).createFunctionType(eq(name), eq(null), eq(parametersNode), eq(null), eq(typeOfThis), eq(templateTypeName), eq(true), eq(false));
  }

  @Test
  public void testFunctionBuilderCopyConstructor() {
    String name = "myFunction";
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionType otherFunctionType = new FunctionType(registry, name, null, null, null, null, false, false);

    when(registry.createFunctionType(name, null, null, null, null, null, false, false))
        .thenReturn(otherFunctionType);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    FunctionType functionType = functionBuilder.copyFromOtherFunction(otherFunctionType).build();

    assertEquals(otherFunctionType, functionType);
  }
}