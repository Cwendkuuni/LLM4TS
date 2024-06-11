
package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
  public void testWithName() {
    FunctionBuilder functionBuilder = builder.withName("testFunction");
    assertEquals("testFunction", functionBuilder.build().getName());
  }

  @Test
  public void testWithSourceNode() {
    Node sourceNode = new Node(Node.SCRIPT);
    FunctionBuilder functionBuilder = builder.withSourceNode(sourceNode);
    assertEquals(sourceNode, functionBuilder.build().getSourceNode());
  }

  @Test
  public void testWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.addParam("param1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    paramBuilder.addParam("param2", registry.getNativeType(JSTypeNative.STRING_TYPE));
    Node parametersNode = paramBuilder.build();

    FunctionBuilder functionBuilder = builder.withParamsNode(parametersNode);
    assertEquals(parametersNode, functionBuilder.build().getParametersNode());
  }

  @Test
  public void testWithReturnType() {
    JSType returnType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
    FunctionBuilder functionBuilder = builder.withReturnType(returnType);
    assertEquals(returnType, functionBuilder.build().getReturnType());
  }

  @Test
  public void testWithInferredReturnType() {
    JSType returnType = registry.getNativeType(JSTypeNative.NULL_TYPE);
    FunctionBuilder functionBuilder = builder.withInferredReturnType(returnType);
    assertEquals(returnType, functionBuilder.build().getReturnType());
    assertTrue(functionBuilder.build().isInferredReturnType());
  }

  @Test
  public void testWithTypeOfThis() {
    ObjectType typeOfThis = registry.createObjectType("Test");
    FunctionBuilder functionBuilder = builder.withTypeOfThis(typeOfThis);
    assertEquals(typeOfThis, functionBuilder.build().getTypeOfThis());
  }

  @Test
  public void testWithTemplateName() {
    FunctionBuilder functionBuilder = builder.withTemplateName("Template");
    assertEquals("Template", functionBuilder.build().getTemplateTypeName());
  }

  @Test
  public void testForConstructor() {
    FunctionBuilder functionBuilder = builder.forConstructor();
    assertTrue(functionBuilder.build().isConstructor());
  }

  @Test
  public void testForNativeType() {
    FunctionBuilder functionBuilder = builder.forNativeType();
    assertTrue(functionBuilder.build().isNativeObjectType());
  }

  @Test
  public void testCopyFromOtherFunction() {
    FunctionType otherType = registry.createFunctionType(
        "testFunction", new Node(Node.SCRIPT),
        new ArrowType(registry, null, null, false),
        null, null, null, false, false);

    FunctionBuilder functionBuilder = builder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), functionBuilder.build().getName());
    assertEquals(otherType.getSourceNode(), functionBuilder.build().getSourceNode());
    assertEquals(otherType.getParametersNode(), functionBuilder.build().getParametersNode());
    assertEquals(otherType.getReturnType(), functionBuilder.build().getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionBuilder.build().getTypeOfThis());
    assertFalse(functionBuilder.build().isConstructor());
    assertFalse(functionBuilder.build().isNativeObjectType());
  }

  @Test
  public void testBuild() {
    FunctionType functionType = builder.build();
    assertNull(functionType.getName());
    assertNull(functionType.getSourceNode());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertFalse(functionType.isInferredReturnType());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}
