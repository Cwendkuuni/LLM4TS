package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    Node parametersNode = paramBuilder.build();

    FunctionBuilder result = builder.withParams(paramBuilder);

    assertSame(builder, result);
  }

  @Test
  public void testWithParamsNode() {
    Node parametersNode = new Node(Token.NAME);
    parametersNode.setString("param");

    FunctionBuilder result = builder.withParamsNode(parametersNode);

    assertSame(builder, result);
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

    FunctionBuilder result = builder.withReturnType(returnType);

    assertSame(builder, result);
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

    FunctionBuilder result = builder.withInferredReturnType(returnType);

    assertSame(builder, result);
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    FunctionBuilder result = builder.withTypeOfThis(typeOfThis);

    assertSame(builder, result);
  }

  @Test
  public void testWithTemplateName() {
    String templateName = "T";

    FunctionBuilder result = builder.withTemplateName(templateName);

    assertSame(builder, result);
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder result = builder.forConstructor();

    assertSame(builder, result);
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder result = builder.forNativeType();

    assertSame(builder, result);
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType(registry);
    otherType.setReferenceName("Function");
    otherType.setSource(new Node(Token.FUNCTION));
    otherType.setParametersNode(new Node(Token.NAME));
    otherType.setReturnType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    otherType.setTypeOfThis(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    otherType.setTemplateTypeName("T");
    otherType.setConstructor(true);
    otherType.setNativeObjectType(true);

    FunctionBuilder result = builder.copyFromOtherFunction(otherType);

    assertSame(builder, result);
  }

  @Test
  public void testBuild() {
    FunctionType result = builder.build();

    assertEquals(registry, result.getRegistry());
    assertNull(result.getReferenceName());
    assertNull(result.getSource());
    assertNotNull(result.getParametersNode());
    assertNull(result.getReturnType());
    assertNull(result.getTypeOfThis());
    assertNull(result.getTemplateTypeName());
    assertFalse(result.isConstructor());
    assertFalse(result.isNativeObjectType());
  }
}