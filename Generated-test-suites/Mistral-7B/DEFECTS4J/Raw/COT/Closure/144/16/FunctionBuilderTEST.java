import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeValue.isFunction;
import static com.google.javascript.rhino.NodeValue.isObject;
import static com.google.javascript.rhino.NodeValue.isString;
import static com.google.javascript.rhino.NodeValue.isUndefined;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.ArrowType;

import org.junit.Test;
import org.junit.Before;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithName() {
    functionBuilder.withName("testFunction");
    FunctionType result = functionBuilder.build();
    assertEquals("testFunction", result.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = Node.newNode("function testFunction() {}");
    functionBuilder.withSourceNode(sourceNode);
    FunctionType result = functionBuilder.build();
    assertEquals(sourceNode, result.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(registry.stringType());
    Node parametersNode = paramBuilder.build();
    functionBuilder.withParams(paramBuilder);
    FunctionType result = functionBuilder.build();
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    Node parametersNode = Node.newNode("[\"param1\", Function(\"param2\")]");
    functionBuilder.withParamsNode(parametersNode);
    FunctionType result = functionBuilder.build();
    assertEquals(parametersNode, result.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = registry.numberType();
    functionBuilder.withReturnType(returnType);
    FunctionType result = functionBuilder.build();
    assertEquals(returnType, result.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = registry.numberType();
    functionBuilder.withInferredReturnType(returnType);
    FunctionType result = functionBuilder.build();
    assertEquals(returnType, result.getReturnType());
    assertTrue(result.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    ObjectType typeOfThis = (ObjectType) registry.objectType("Object");
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType result = functionBuilder.build();
    assertEquals(typeOfThis, result.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    functionBuilder.withTemplateName("TestFunctionTemplate");
    FunctionType result = functionBuilder.build();
    assertEquals("TestFunctionTemplate", result.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.forConstructor();
    FunctionType result = functionBuilder.build();
    assertTrue(result.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();
    FunctionType result = functionBuilder.build();
    assertTrue(result.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionType otherType = registry.types.getFunctionType("testFunction");
    functionBuilder.copyFromOtherFunction(otherType);
    FunctionType result = functionBuilder.build();
    assertEquals(otherType.getReferenceName(), result.getReferenceName());
    assertEquals(otherType.getSource(), result.getSource());
    assertEquals(otherType.getParametersNode(), result.getParametersNode());
    assertEquals(otherType.getReturnType(), result.getReturnType());
    assertEquals(otherType.getTypeOfThis(), result.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), result.getTemplateTypeName());
    assertEquals(otherType.isConstructor(), result.isConstructor());
    assertEquals(otherType.isNativeObjectType(), result.isNativeObjectType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullName() {
    functionBuilder.withName(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullSourceNode() {
    functionBuilder.withSourceNode(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullParametersNode() {
    functionBuilder.withParamsNode(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullReturnType() {
    functionBuilder.withReturnType(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullTypeOfThis() {
    functionBuilder.withTypeOfThis(null);
    functionBuilder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFunctionBuilderWithNullTemplateTypeName() {
    functionBuilder.withTemplateName(null);
    functionBuilder.build();
  }

  @Test
  public void testFunctionBuilderWithEmptyParameters() {
    functionBuilder.withParams(new FunctionParamBuilder());
    FunctionType result = functionBuilder.build();
    Node parametersNode = result.getParametersNode();
    assertTrue(isUndefined(parametersNode));
  }

  @Test
  public void testFunctionBuilderWithSingleParameter() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(registry.stringType());
    Node parametersNode = paramBuilder.build();
    functionBuilder.withParams(paramBuilder);
    FunctionType result = functionBuilder.build();
    JSType parameterType = result.getParametersNode().getFirstChild().getType();
    assertEquals(registry.stringType(), parameterType);
  }

  @Test
  public void testFunctionBuilderWithMultipleParameters() {
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder();
    paramBuilder1.withName("param1").withType(registry.stringType());
    Node paramNode1 = paramBuilder1.build();

    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder();
    paramBuilder2.withName("param2").withType(registry.numberType());
    Node paramNode2 = paramBuilder2.build();

    Node parametersNode = Node.newNode("[", paramNode1, ",", paramNode2, "]");

    functionBuilder.withParamsNode(parametersNode);
    FunctionType result = functionBuilder.build();
    assertEquals(parametersNode, result.getParametersNode());

    JSType paramType1 = result.getParametersNode().getFirstChild().getType();
    assertEquals(registry.stringType(), paramType1);

    JSType paramType2 = result.getParametersNode().getLastChild().getType();
    assertEquals(registry.numberType(), paramType2);
  }

  @Test
  public void testFunctionBuilderWithVoidReturnType() {
    functionBuilder.withReturnType(registry.voidType());
    FunctionType result = functionBuilder.build();
    JSType returnType = result.getReturnType();
    assertEquals(registry.voidType(), returnType);
  }

  @Test
  public void testFunctionBuilderWithFunctionReturnType() {
    JSType returnType = new ArrowType(registry, Node.newNode("[]"), registry.stringType());
    functionBuilder.withReturnType(returnType);
    FunctionType result = functionBuilder.build();
    JSType functionType = result.getReturnType();
    assertTrue(isFunction(functionType.getBaseType()));
    ArrowType arrowType = (ArrowType) functionType.getBaseType();
    JSType parameterType = arrowType.getParametersNode().getFirstChild().getType();
    assertEquals(registry.stringType(), parameterType);
  }

  @Test
  public void testFunctionBuilderWithObjectReturnType() {
    ObjectType objectType = (ObjectType) registry.objectType("TestObject");
    functionBuilder.withReturnType(objectType);
    FunctionType result = functionBuilder.build();
    JSType returnType = result.getReturnType();
    assertEquals(objectType, returnType);
  }

  @Test
  public void testFunctionBuilderWithInterfaceReturnType() {
    ObjectType interfaceType = (ObjectType) registry.objectType("TestInterface");
    functionBuilder.withReturnType(new InterfaceType(registry, interfaceType));
    FunctionType result = functionBuilder.build();
    JSType returnType = result.getReturnType();
    assertTrue(returnType instanceof InterfaceType);
    InterfaceType interfaceTypeResult = (InterfaceType) returnType;
    assertEquals(interfaceType, interfaceTypeResult.getInterface());
  }

  @Test
  public void testFunctionBuilderWithFunctionParamType() {
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(new ArrowType(registry, Node.newNode("[]"), registry.stringType()));
    Node paramNode = paramBuilder.build();
    functionBuilder.withParams(paramBuilder);
    FunctionType result = functionBuilder.build();
    JSType paramType = result.getParametersNode().getFirstChild().getType();
    assertTrue(paramType instanceof ArrowType);
    ArrowType arrowType = (ArrowType) paramType;
    JSType parameterType = arrowType.getParametersNode().getFirstChild().getType();
    assertEquals(registry.stringType(), parameterType);
  }

  @Test
  public void testFunctionBuilderWithFunctionReturnTypeAndFunctionParamType() {
    JSType returnType = new ArrowType(registry, Node.newNode("[]"), registry.stringType());
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(new ArrowType(registry, Node.newNode("[]"), registry.numberType()));
    Node paramNode = paramBuilder.build();
    functionBuilder.withParams(paramBuilder);
    functionBuilder.withReturnType(returnType);
    FunctionType result = functionBuilder.build();
    JSType functionType = result.getReturnType();
    assertTrue(isFunction(functionType.getBaseType()));
    ArrowType arrowType = (ArrowType) functionType.getBaseType();
    JSType returnTypeResult = arrowType.getReturnType();
    assertEquals(registry.stringType(), returnTypeResult);

    JSType paramType = result.getParametersNode().getFirstChild().getType();
    assertTrue(paramType instanceof ArrowType);
    ArrowType paramArrowType = (ArrowType) paramType;
    JSType parameterType = paramArrowType.getParametersNode().getFirstChild().getType();
    assertEquals(registry.numberType(), parameterType);
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    functionBuilder.withTemplateName("TestFunctionTemplate");
    FunctionType result = functionBuilder.build();
    assertEquals("TestFunctionTemplate", result.getTemplateTypeName());
  }
}