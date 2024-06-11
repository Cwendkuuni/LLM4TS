package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.FunctionParamBuilder;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testBuildWithNameAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    JSType returnType = mock(JSType.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, null, null, null, null, false, false);

    builder.withName(name);
    builder.withReturnType(returnType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, null, null, null, false, false);

    builder.withName(name);
    builder.withSourceNode(sourceNode);
    builder.withReturnType(returnType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), null, null, false, false);

    builder.withName(name);
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersReturnTypeAndInferredReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, true), null, null, false, false);

    builder.withName(name);
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);
    builder.withInferredReturnType(returnType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersReturnTypeTypeOfThisAndTemplateTypeName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "template";

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, false, false);

    builder.withName(name);
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);
    builder.withTypeOfThis(typeOfThis);
    builder.withTemplateName(templateTypeName);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersReturnTypeTypeOfThisAndTemplateTypeNameInferredReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "template";

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, true), typeOfThis, templateTypeName, false, false);

    builder.withName(name);
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);
    builder.withTypeOfThis(typeOfThis);
    builder.withTemplateName(templateTypeName);
    builder.withInferredReturnType(returnType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersReturnTypeTypeOfThisAndTemplateTypeNameConstructor() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "template";

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, true, false);

    builder.withName(name);
    builder.forConstructor();
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);
    builder.withTypeOfThis(typeOfThis);
    builder.withTemplateName(templateTypeName);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersReturnTypeTypeOfThisAndTemplateTypeNameNativeType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "template";

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, false, true);

    builder.withName(name);
    builder.forNativeType();
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);
    builder.withTypeOfThis(typeOfThis);
    builder.withTemplateName(templateTypeName);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithNameSourceParametersAndReturnType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);
    String name = "foo";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = mock(JSType.class);

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), null, null, false, false);

    builder.withName(name);
    builder.withSourceNode(sourceNode);
    builder.withParamsNode(parametersNode);
    builder.withReturnType(returnType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testBuildWithEmptyConstructor() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType expectedFunctionType = new FunctionType(registry, null, null, new ArrowType(registry, null, null, false), null, null, true, false);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testCopyFunctionType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    FunctionType originalFunctionType = mock(FunctionType.class);

    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.copyFromOtherFunction(originalFunctionType);

    String name = originalFunctionType.getReferenceName();
    Node sourceNode = originalFunctionType.getSource();
    Node parametersNode = originalFunctionType.getParametersNode();
    JSType returnType = originalFunctionType.getReturnType();
    ObjectType typeOfThis = originalFunctionType.getTypeOfThis();
    String templateTypeName = originalFunctionType.getTemplateTypeName();
    boolean isConstructor = originalFunctionType.isConstructor();
    boolean isNativeType = originalFunctionType.isNativeObjectType();

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode, new ArrowType(registry, parametersNode, returnType, false), typeOfThis, templateTypeName, isConstructor, isNativeType);

    FunctionType actualFunctionType = builder.build();

    assertSame(expectedFunctionType, actualFunctionType);
  }
}