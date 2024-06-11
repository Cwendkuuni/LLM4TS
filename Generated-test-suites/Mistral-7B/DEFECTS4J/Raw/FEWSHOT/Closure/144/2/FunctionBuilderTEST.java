package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder functionParamBuilder;
  private Node parametersNode;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
  }

  @Test
  public void testWithName() {
    String name = "myFunction";
    FunctionBuilder builder = functionBuilder.withName(name);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getName());
    assertEquals(name, functionType.getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = mock(Node.class);
    FunctionBuilder builder = functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getSource());
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = mock(Node.class);
    FunctionBuilder builder = functionBuilder.withParamsNode(parametersNode);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getParametersNode());
    assertEquals(parametersNode, functionType.getParametersNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    FunctionBuilder builder = functionBuilder.withParams(functionParamBuilder);
    builder.withParams(new FunctionParamBuilder().withName("param1").build());
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getParametersNode());
    Node parametersNode = functionType.getParametersNode();
    assertEquals(1, parametersNode.getChildCount());
    Node firstChild = parametersNode.getChild(0);
    assertNotNull(firstChild.getProperties());
    assertEquals("param1", firstChild.getProperties().getName());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = Type.VOID;
    FunctionBuilder builder = functionBuilder.withReturnType(returnType);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = Type.VOID;
    FunctionBuilder builder = functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getReturnType());
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = mock(ObjectType.class);
    FunctionBuilder builder = functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getTypeOfThis());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    String templateTypeName = "TemplateType";
    FunctionBuilder builder = functionBuilder.withTemplateName(templateTypeName);
    FunctionType functionType = builder.build();
    assertNotNull(functionType.getTemplateTypeName());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder builder = functionBuilder.forConstructor();
    FunctionType functionType = builder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder builder = functionBuilder.forNativeType();
    FunctionType functionType = builder.build();
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    String name = "myFunction";
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    JSType returnType = Type.VOID;
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "TemplateType";
    FunctionType otherType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false), typeOfThis,
        templateTypeName, false, false);
    FunctionBuilder builder = functionBuilder.copyFromOtherFunction(otherType);
    FunctionType functionType = builder.build();
    assertEquals(name, functionType.getName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }
}